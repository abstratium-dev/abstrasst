package dev.abstratium.abstrasst.service;

import java.time.temporal.ChronoUnit;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.ToolBox;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(
    // tools = MyAiTools.class, this is an alternative to @ToolBox

    // curl https://api.openai.com/v1/models -H "Authorization: Bearer $OPENAI_API_KEY"
    modelName = "gpt-4.1-nano"
)
@ApplicationScoped
public interface MyAiMailService {

    @ToolBox({
        MyAiTools.class
    })
    @UserMessage("""
                Send an email to {recipient}
                with the subject {subject}
                and the body {body}.
            """)
    @Retry(delay = 100, jitter = 50, maxRetries = 2)
    @Timeout(value = 60, unit = ChronoUnit.SECONDS)
    @Fallback(MyAiMailServiceFallback.class)
    Result<String> sendEmail(@V("recipient") String recipient, @V("subject") String subject, @V("body") String body);

}