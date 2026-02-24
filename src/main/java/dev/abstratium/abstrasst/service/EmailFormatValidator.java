package dev.abstratium.abstrasst.service;

import org.jboss.logging.Logger;

import io.quarkiverse.langchain4j.guardrails.ToolInputGuardrail;
import io.quarkiverse.langchain4j.guardrails.ToolInputGuardrailRequest;
import io.quarkiverse.langchain4j.guardrails.ToolInputGuardrailResult;

public class EmailFormatValidator implements ToolInputGuardrail {

    private static Logger log = Logger.getLogger(EmailFormatValidator.class);

    public ToolInputGuardrailResult validate(ToolInputGuardrailRequest request) {
        log.info("Validating email format with request args as json: " + request.argumentsAsJson());
        // TODO: Implement email format validation logic
        return ToolInputGuardrailResult.success();
    }

}
