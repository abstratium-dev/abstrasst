package dev.abstratium.abstrasst.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyAiMemory implements ChatMemoryProvider {

    private final ConcurrentMap<String, ChatMemory> memories = new ConcurrentHashMap<>();

    @Override
    public ChatMemory get(Object memoryId) {
        // or see https://docs.quarkiverse.io/quarkus-langchain4j/dev/guide-semantic-compression.html
        return memories.computeIfAbsent(memoryId.toString(), id -> MessageWindowChatMemory.builder().maxMessages(10).build());
    }

}
