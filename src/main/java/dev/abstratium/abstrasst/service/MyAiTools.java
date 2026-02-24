package dev.abstratium.abstrasst.service;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.guardrails.ToolInputGuardrails;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrails;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyAiTools {

    @Tool("Send an email")
    @ToolInputGuardrails({
            EmailFormatValidator.class
    })
    @ToolOutputGuardrails({
            EmailOutputValidator.class
    })
    String sendEmail(@V("recipient") String recipient, @V("subject") String subject, @V("body") String body) {
        System.out.println("Sending email to " + recipient + " with subject " + subject + " and body " + body);
        return "Email sent successfully";
    }
    
}
