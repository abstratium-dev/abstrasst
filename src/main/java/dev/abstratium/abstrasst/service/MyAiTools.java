package dev.abstratium.abstrasst.service;

import dev.langchain4j.agent.tool.ReturnBehavior;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.guardrails.ToolInputGuardrails;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrails;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MyAiTools {

    @Tool(
        name = "EmailSender", 
        value = "Send an email to the given recipient, with the given subject and body",
        returnBehavior = ReturnBehavior.IMMEDIATE // no point sending "email sent successfully" back to the llm
    )
    @ToolInputGuardrails({
            EmailFormatValidator.class
    })
    @ToolOutputGuardrails({
            EmailOutputValidator.class
    })
    @RunOnVirtualThread
    String sendEmail(@V("recipient") String recipient, @V("subject") String subject, @V("body") String body) {
        System.out.println("Sending email to " + recipient + " with subject " + subject + " and body " + body);
        return "Email sent successfully";
    }
    
    @Tool(
        name = "Adder", 
        value = {
            "Add two numbers",
            "number1 is the first number to add",
            "number2 is the second number to add",
            "return the sum of the two numbers"
        }
    )
    public int add(int number1, int number2) {
        return number1 + number2;
    }
}
