package dev.abstratium.abstrasst.service;

import dev.langchain4j.data.image.Image;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService
@ApplicationScoped
public interface MyAiImageService {
    @UserMessage("""
            You take an image in and output the text extracted from the image.
            Translate it in English.
            """)
    String process(Image image);
    
}
