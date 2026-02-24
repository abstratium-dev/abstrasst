package dev.abstratium.abstrasst.service;

import dev.langchain4j.model.output.TokenUsage;
import dev.langchain4j.model.chat.listener.*;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MyChatModelListener implements ChatModelListener {

    private static final Logger log = Logger.getLogger(MyChatModelListener.class);
    private static final String NAME = "NAME";

    @Override
    public void onRequest(ChatModelRequestContext context) {
        // You can log the start of the request here if needed
        context.attributes().put(NAME, "JOSH");
    }

    @Override
    public void onResponse(ChatModelResponseContext context) {
        TokenUsage usage = context.chatResponse().tokenUsage();
        String model = context.chatResponse().modelName();
        
        // This is where you calculate costs
        int inputTokens = usage.inputTokenCount();
        int outputTokens = usage.outputTokenCount();
        int totalTokens = usage.totalTokenCount();

        log.infof("PRICING: Model: %s | Input: %s | Output: %s | Total: %s", 
                  model, inputTokens, outputTokens, totalTokens);
        
        // Logic to save to your database/session goes here
        log.infof("User ID: %s, Total Tokens: %s", context.attributes().get(NAME), totalTokens);
    }

    @Override
    public void onError(ChatModelErrorContext context) {
        log.error("Error during LLM exchange", context.error());
    }
}