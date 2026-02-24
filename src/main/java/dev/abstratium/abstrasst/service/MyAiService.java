package dev.abstratium.abstrasst.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(
    // curl https://api.openai.com/v1/models -H "Authorization: Bearer $OPENAI_API_KEY"
    modelName = "gpt-4.1-nano",

    chatMemoryProviderSupplier = MyAiMemorySupplier.class
)
@ApplicationScoped
public interface MyAiService {
    
    @SystemMessage("You are a professional poet")
    @UserMessage("""
                Write a poem about {topic}.
                The poem should be {lines} lines long.
            """)
    Multi<String> writeAPoem(String topic, int lines);

    @SystemMessage("{dynamicSystemMessage}")
    String whateverYouAskWithMemory(@MemoryId String userId, @V("dynamicSystemMessage") String dynamicSystemMessage, @UserMessage String userMessage);

    @SystemMessage("{dynamicSystemMessage}")
    String whateverYouAsk(@MemoryId String userId, @V("dynamicSystemMessage") String dynamicSystemMessage, @UserMessage String userMessage);

}