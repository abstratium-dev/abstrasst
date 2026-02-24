package dev.abstratium.abstrasst.service;

import java.util.function.Supplier;

import dev.langchain4j.memory.chat.ChatMemoryProvider;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyAiMemorySupplier implements Supplier<ChatMemoryProvider> {

    @Override
    public ChatMemoryProvider get() {
        return new MyAiMemory();
    }

}
