package dev.abstratium.abstrasst.agents;

import org.jboss.logging.Logger;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.agent.AgentRequest;
import dev.langchain4j.agentic.agent.AgentResponse;
import dev.langchain4j.agentic.declarative.AfterAgentInvocation;
import dev.langchain4j.agentic.declarative.BeforeAgentInvocation;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CfoAgent2 {

    Logger log = Logger.getLogger(CfoAgent2.class.getName());

    @Agent
    public String run(@UserMessage @V("topic") String topic);

    @BeforeAgentInvocation
    static void beforeAgentInvocation(AgentRequest agentRequest) {
        log.info("Before agent invocation: " + agentRequest);
    }

    @AfterAgentInvocation
    static void afterAgentInvocation(AgentResponse agentResponse) {
        log.info("After agent invocation: " + agentResponse);
    }

}
