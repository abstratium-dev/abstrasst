package dev.abstratium.abstrasst.agents;

import org.jboss.logging.Logger;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.agent.AgentRequest;
import dev.langchain4j.agentic.agent.AgentResponse;
import dev.langchain4j.agentic.declarative.AfterAgentInvocation;
import dev.langchain4j.agentic.declarative.BeforeAgentInvocation;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CeoAgent {

    Logger log = Logger.getLogger(CeoAgent.class.getName());

    @SystemMessage("""
        You are the CEO of a small company that develops software and simple hardware products.
        You are asked to give your input to the current topic and must comment on the topic from a business perspective.
        Your strategic goals are:
        - Ensure that the company goals are met
        - Ensure that the company is profitable
        - Ensure that the company is sustainable
        """)
    @UserMessage("""
        {topic}
        """)
    @Agent(
        name = "Chief Executive Officer",
        description = "This agent gives business input to the current topic",
        outputKey = "ceoInput"
    )
    public String run(@V("topic") String topic);

    @BeforeAgentInvocation
    static void beforeAgentInvocation(AgentRequest agentRequest) {
        log.info("Before agent invocation: " + agentRequest);
    }

    @AfterAgentInvocation
    static void afterAgentInvocation(AgentResponse agentResponse) {
        log.info("After agent invocation: " + agentResponse);
    }

}
