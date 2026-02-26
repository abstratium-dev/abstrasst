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

public interface CtoAgent {

    Logger log = Logger.getLogger(CtoAgent.class.getName());

    @SystemMessage("""
        You are the CTO of a small company that develops software and simple hardware products.
        You are asked to give your input to the current topic and must comment on the topic from a technical perspective.
        Your strategic goals are:
        - Ensure that the company can deliver high quality products 
        - Ensure that the company can deliver products on time
        - Ensure that the company can deliver products with a low cost
        - Ensure that the company can deliver products with a low risk
        - Ensure that the company can remain innovative
        - Ensure that the company can deliver products with a low complexity
        - Ensure that the company can deliver new products
        """)
    @UserMessage("""
        {topic}
        """)
    @Agent(
        name = "Chief Technical Officer",
        description = "This agent gives technical input to the current topic",
        outputKey = "ctoInput"
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
