package dev.abstratium.abstrasst.agents;

import org.jboss.logging.Logger;

import dev.abstratium.abstrasst.service.MyAiTools;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.agent.AgentRequest;
import dev.langchain4j.agentic.agent.AgentResponse;
import dev.langchain4j.agentic.declarative.AfterAgentInvocation;
import dev.langchain4j.agentic.declarative.BeforeAgentInvocation;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.ToolBox;

public interface AddingAgent {

    Logger log = Logger.getLogger(AddingAgent.class.getName());
/*
    @SystemMessage("""
        You are an adding agent.
        """)
    @UserMessage("""
        Add the following numbers: `{number1}` and `{number2}`
        """)
    @Agent(
        name = "Adding Agent",
        description = "This agent calculates the sum of two numbers",
        outputKey = "sum"
    )
    @ToolBox({
        MyAiTools.class
    })
    public int run(int number1, int number2);

    @BeforeAgentInvocation
    static void beforeAgentInvocation(AgentRequest agentRequest) {
        log.info("Before agent invocation: " + agentRequest);
    }

    @AfterAgentInvocation
    static void afterAgentInvocation(AgentResponse agentResponse) {
        log.info("After agent invocation: " + agentResponse);
    }
*/
}
