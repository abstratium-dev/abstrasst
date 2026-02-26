package dev.abstratium.abstrasst.workflows;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import dev.abstratium.abstrasst.agents.CeoAgent2;
import dev.abstratium.abstrasst.agents.CfoAgent2;
import dev.abstratium.abstrasst.agents.CtoAgent2;
import dev.abstratium.abstrasst.service.MyChatModelListener;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProgrammaticWorkflow {

    public String run(String topic) {

        // TODO memory
        // TODO context?

        ChatModel model = OpenAiChatModel.builder()
            .apiKey(System.getenv("OPENAI_API_KEY"))
            .modelName("gpt-4.1-nano")
            .maxTokens(300)
            .listeners(List.of(new MyChatModelListener(UUID.randomUUID().toString())))
            .build();

        var cfo = AgenticServices.agentBuilder(CfoAgent2.class)
                            .chatModel(model)
                            .systemMessageProvider((memoryId) -> {
                                return """
                                        You are the CFO of a small company that develops software and simple hardware products.
                                        You are asked to give your input to the current topic and must comment on the topic from a financial perspective.
                                        Your strategic goals are:
                                        - Increase the company's revenue by 20% in the next 6 months
                                        - Reduce the company's expenses by 10% in the next 6 months
                                        """.stripIndent();
                            })
                            .outputKey("cfoInput")
                            .name("Chief Financial Officer")
                            .description("This agent gives financial input to the current topic")
                            .build();
        var cto = AgenticServices.agentBuilder(CtoAgent2.class)
                            .chatModel(model)
                            .systemMessageProvider((memoryId) -> {
                                return """
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
                                        """.stripIndent();
                            })
                            .outputKey("ctoInput")
                            .name("Chief Technical Officer")
                            .description("This agent gives technical input to the current topic")
                            .build();

        var ceo = AgenticServices.agentBuilder(CeoAgent2.class)
                            .chatModel(model)
                            .systemMessageProvider((memoryId) -> {
                                return """
                                        You are the CEO of a small company that develops software and simple hardware products.
                                        You are asked to give your input to the current topic and must comment on the topic from a business perspective.
                                        Your strategic goals are:
                                        - Ensure that the company goals are met
                                        - Ensure that the company is profitable
                                        - Ensure that the company is sustainable
                                        """.stripIndent();
                            })
                            .outputKey("ceoInput")
                            .name("Chief Executive Officer")
                            .description("This agent gives business input to the current topic")
                            .build();

        var parallelAgentBuilder = AgenticServices.parallelBuilder();
        Object[] meetingMembers = {cfo, cto, ceo};
        parallelAgentBuilder.subAgents(meetingMembers);
        parallelAgentBuilder.outputKey("meetingMemberInputs");
        parallelAgentBuilder.name("Meeting Member Inputs Agent");
        parallelAgentBuilder.description("This agent asks all the meeting members for their input to the current topic");
        parallelAgentBuilder.output((scope) -> {
            return "Topic: " + scope.readState("topic") + "\n" + "CFO Input: " + scope.readState("cfoInput") + "\n" + "CTO Input: " + scope.readState("ctoInput") + "\n" + "CEO Input: " + scope.readState("ceoInput");
        });
        var parallelAgent = parallelAgentBuilder.build();

        var result = parallelAgent.invokeWithAgenticScope(Map.of("topic", topic, "sessionId", UUID.randomUUID().toString()));

        return result.result();
    }
}
