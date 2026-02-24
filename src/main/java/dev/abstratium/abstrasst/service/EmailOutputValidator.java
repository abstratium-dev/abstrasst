package dev.abstratium.abstrasst.service;

import org.jboss.logging.Logger;

import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrail;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailRequest;
import io.quarkiverse.langchain4j.guardrails.ToolOutputGuardrailResult;

public class EmailOutputValidator implements ToolOutputGuardrail {

    private static Logger log = Logger.getLogger(EmailOutputValidator.class);

    @Override
    public ToolOutputGuardrailResult validate(ToolOutputGuardrailRequest request) {
        log.info("Validating email output with request args as json: " + request.argumentsAsJson());
        // TODO: Implement validation logic
        return ToolOutputGuardrailResult.success();
    }

}
