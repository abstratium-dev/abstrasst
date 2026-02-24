package dev.abstratium.abstrasst.service;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(
    tools = MyAiTools.class,

    // curl https://api.openai.com/v1/models -H "Authorization: Bearer $OPENAI_API_KEY"
    modelName = "gpt-4.1-nano"
)
@ApplicationScoped
public interface MyAiMailService {
    
    @UserMessage("""
                Send an email to {recipient}
                with the subject {subject}
                and the body {body}.
            """)
    String sendEmail(@V("recipient") String recipient, @V("subject") String subject, @V("body") String body);

}