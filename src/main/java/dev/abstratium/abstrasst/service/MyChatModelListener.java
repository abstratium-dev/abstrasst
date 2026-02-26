package dev.abstratium.abstrasst.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.logging.Logger;

import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;

public class MyChatModelListener implements ChatModelListener {

    private static final Logger log = Logger.getLogger(MyChatModelListener.class);
    private final String sessionId;

    public MyChatModelListener(String sessionId) {
        this.sessionId = sessionId;
    }

    public static record Usage(String model, int inputTokens, int inputCachedTokens, int outputTokens) {}

    // sessionId -> model -> usage
    static Map<String, Map<String, Usage>> usageBySessionIdAndModel = new ConcurrentHashMap<>();

    @Override
    public void onRequest(ChatModelRequestContext context) {
        // You can log the start of the request here if needed
    }

    @Override
    public void onResponse(ChatModelResponseContext context) {
        TokenUsage usage = context.chatResponse().tokenUsage();
        String model = context.chatResponse().modelName();
        
        // This is where you calculate costs
        int inputTokens = usage.inputTokenCount();
        int cachedInputTokens = 0; // not currently supported in langchain4j - ok, our pricing will be pessimistic (more than it actually costs)
        int outputTokens = usage.outputTokenCount();

        // update or create usage
        var totalUsageForSession = usageBySessionIdAndModel.computeIfAbsent(sessionId, k -> new ConcurrentHashMap<>());

        var modelUsage = totalUsageForSession.computeIfAbsent(model, k -> new Usage(model, 0, 0, 0));

        modelUsage = new Usage(model, modelUsage.inputTokens + inputTokens, modelUsage.inputCachedTokens + cachedInputTokens, modelUsage.outputTokens + outputTokens);

        totalUsageForSession.put(model, modelUsage);

        // calculate running costs for each model
        var costOfLastCall = getCost(model, inputTokens, cachedInputTokens, outputTokens);
        log.info(sessionId + " - Cost of last call for model " + model + ": " + costOfLastCall);
    
        BigDecimal totalCost = BigDecimal.ZERO;
        for (var mu : totalUsageForSession.values()) {
            totalCost = totalCost.add(getCost(mu.model, mu.inputTokens, mu.inputCachedTokens, mu.outputTokens));
        }
        log.info(sessionId + " - Total cost for session: " + totalCost);
    }

    // https://developers.openai.com/api/docs/pricing
    private BigDecimal getCost(String model, int inputTokens, int cachedInputTokens, int outputTokens) {
        if(model.startsWith("gpt-4.1-nano")) { // gpt-4.1-nano-2025-04-14
            // 0.40 usd per million inputs, 0.10 usd per million cached inputs, 1.60 usd per million outputs
            var v = BigDecimal.valueOf(inputTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.40));
            v = v.add(BigDecimal.valueOf(cachedInputTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(0.10)));
            v = v.add(BigDecimal.valueOf(outputTokens).divide(BigDecimal.valueOf(1_000_000), 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(1.60)));
            return v;
        } else {
            throw new IllegalArgumentException("Unknown model: " + model);
        }
    }

    @Override
    public void onError(ChatModelErrorContext context) {
        log.error("Error during LLM exchange", context.error());
    }
}