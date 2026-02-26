package dev.abstratium.abstrasst.workflows;

import org.jboss.logging.Logger;

import dev.abstratium.abstrasst.agents.MeetingMemberInputsAgent;
import dev.langchain4j.agentic.agent.AgentRequest;
import dev.langchain4j.agentic.agent.AgentResponse;
import dev.langchain4j.agentic.declarative.AfterAgentInvocation;
import dev.langchain4j.agentic.declarative.BeforeAgentInvocation;
import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.service.V;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface MyWorkflow {

    Logger log = Logger.getLogger(MyWorkflow.class.getName());

    @ParallelAgent(
        name = "Workflow that holds a meeting",
        description = "This workflow holds a meeting and asks all the members for their input to the current topic",
        subAgents = {
            MeetingMemberInputsAgent.class
        }
    )
    public String run(@V("SESSION_ID") String sessionId, @V("topic") String topic);

    @Output
    static String output(@V("meetingMemberInputs") String meetingMemberInputs) {
        return meetingMemberInputs;
    }

    @BeforeAgentInvocation
    static void beforeAgentInvocation(AgentRequest agentRequest) {
        log.info("Before agent invocation: " + agentRequest);
    }

    @AfterAgentInvocation
    static void afterAgentInvocation(AgentResponse agentResponse) {
        log.info("After agent invocation: " + agentResponse);
    }

}
