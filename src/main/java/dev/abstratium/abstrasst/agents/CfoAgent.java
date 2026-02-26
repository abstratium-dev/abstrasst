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

public interface CfoAgent {

    Logger log = Logger.getLogger(CfoAgent.class.getName());

    @SystemMessage("""
        You are the CFO of a small company that develops software and simple hardware products.
        You are asked to give your input to the current topic and must comment on the topic from a financial perspective.
        Your strategic goals are:
        - Increase the company's revenue by 20% in the next 6 months
        - Reduce the company's expenses by 10% in the next 6 months
        """)
    @UserMessage("""
        {topic}
        """)
    @Agent(
        name = "Chief Financial Officer",
        description = "This agent gives financial input to the current topic",
        outputKey = "cfoInput"
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
