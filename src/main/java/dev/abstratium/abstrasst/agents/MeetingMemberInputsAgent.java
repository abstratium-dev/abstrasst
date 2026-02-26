package dev.abstratium.abstrasst.agents;

import org.jboss.logging.Logger;

import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.service.V;

public interface MeetingMemberInputsAgent {

    Logger log = Logger.getLogger(MeetingMemberInputsAgent.class.getName());

    @ParallelAgent(
        name = "Meeting Member Inputs Agent",
        description = "This agent asks all the meeting members for their input to the current topic",
        outputKey = "meetingMemberInputs",
        subAgents = {
            CfoAgent.class,
            CtoAgent.class,
            CeoAgent.class
        }
    )
    String run(@V("topic") String topic);

    @Output
    static String output(@V("topic") String topic, @V("cfoInput") String cfoInput, @V("ctoInput") String ctoInput, @V("ceoInput") String ceoInput) {
        return "Topic: " + topic + "\n" + "CFO Input: " + cfoInput + "\n" + "CTO Input: " + ctoInput + "\n" + "CEO Input: " + ceoInput;
    }


}
