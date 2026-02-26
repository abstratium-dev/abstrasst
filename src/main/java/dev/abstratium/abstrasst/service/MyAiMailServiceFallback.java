package dev.abstratium.abstrasst.service;

import org.eclipse.microprofile.faulttolerance.ExecutionContext;
import org.eclipse.microprofile.faulttolerance.FallbackHandler;

import dev.langchain4j.service.Result;
import jakarta.enterprise.context.ApplicationScoped;

// do NOT use Result<String> here, it will cause a runtime error since quarkus thinks that the return types aren't equal
@ApplicationScoped
public class MyAiMailServiceFallback implements FallbackHandler<Result> {

    @Override
    public Result handle(ExecutionContext context) {
        Object[] params = context.getParameters();
        String recipient = (String) params[0];
        String subject = (String) params[1];
        String body = (String) params[2];

        Result.ResultBuilder<String> r = Result.builder();
        r.content("FALLBACK: Failed to send email to " + recipient + " with subject " + subject + " and body " + body);
        return r.build();
    }
}
