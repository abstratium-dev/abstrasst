package dev.abstratium.abstrasst.boundary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

import dev.abstratium.abstrasst.service.MyAiImageService;
import dev.abstratium.abstrasst.service.MyAiMailService;
import dev.abstratium.abstrasst.service.MyAiService;
import dev.abstratium.abstrasst.service.SessionId;
import dev.abstratium.abstrasst.workflows.MyWorkflow;
import dev.abstratium.abstrasst.workflows.ProgrammaticWorkflow;
import dev.langchain4j.data.image.Image;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/my-ai")
public class MyAiResource {

    @Inject
    MyAiService myAiService;

    @Inject
    MyAiImageService myAiImageService;

    @Inject
    MyAiMailService myAiMailService;

    @Inject
    MyWorkflow myWorkflow;

    @Inject
    ProgrammaticWorkflow programmaticWorkflow;

    @Inject
    SessionId sessionId;

    @GET
    @Path("/poem")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<String> writeAPoem(@QueryParam("topic") String topic) {
        return myAiService.writeAPoem(topic, 4);
    }
    
    @GET
    @Path("/email")
    @Produces(MediaType.APPLICATION_JSON)
    public String sendEmail(@QueryParam("recipient") String recipient, @QueryParam("subject") String subject, @QueryParam("body") String body) {
        String modifiedBody = myAiService.whateverYouAsk("001", "You are a professional clown and turn everything into a joke. Rewrite this email to be funnier.", body);
        var r = myAiMailService.sendEmail(recipient, subject, modifiedBody);

        int inputTokenCount = r.tokenUsage().inputTokenCount();
        int outputTokenCount = r.tokenUsage().outputTokenCount();
        System.out.println("Input token count: " + inputTokenCount);
        System.out.println("Output token count: " + outputTokenCount);

        return r.content();
    }

    @GET
    @Path("/memory/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String whateverYouAsk(
        @PathParam("userId") String userId, 
        @QueryParam("dynamicSystemMessage") String dynamicSystemMessage, 
        @QueryParam("question") String question) {

        sessionId.setId(userId);
        
        return myAiService.whateverYouAskWithMemory(userId, dynamicSystemMessage, question);
    }    

    @GET
    @Path("/process-image")
    @Produces(MediaType.APPLICATION_JSON)
    public String processImage() throws IOException {
        File file = new File(System.getProperty("user.home") + "/Downloads/1749725546671.jpeg");
        var image = Image.builder()
            .base64Data(encodeFileToBase64(file))
            .mimeType("image/jpeg")
            .build();

        String s = myAiImageService.process(image);
        return s;
    }

    private static String encodeFileToBase64(File file) throws IOException {
        var content = Files.readAllBytes(file.toPath());
        return Base64.getEncoder().encodeToString(content);
    }

    @POST
    @Path("/workflow")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String runWorkflow() {
        return myWorkflow.run(UUID.randomUUID().toString(), """
            Board Meeting 2026-02-26

            # AGENDA ITEM
            Financial Results of 2025

            ## Financial Data
            Profit: 1000
            Expenses: 500
            Revenue: 1500
            EBITDA: 500
            EBIT: 500
            Taxes: 10
            Depreciation: 5
            Interest: 2
            Result: 500

            ## Topic of discussion
            This year we only made income from our software consulting services. We have no hardware products to sell, although we have 
            some in the pipeline. Our core goal is to make money from products (software and hardware), the consulting
            is just an extra revenue stream. We should discuss the next steps for the company.

            """.stripIndent());
    }

    @POST
    @Path("/workflow-programmatic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String runWorkflowProgrammatic() {
        return programmaticWorkflow.run("""
            Board Meeting 2026-02-26

            # AGENDA ITEM
            Financial Results of 2025

            ## Financial Data
            Profit: 1000
            Expenses: 500
            Revenue: 1500
            EBITDA: 500
            EBIT: 500
            Taxes: 10
            Depreciation: 5
            Interest: 2
            Result: 500

            ## Topic of discussion
            This year we only made income from our software consulting services. We have no hardware products to sell, although we have 
            some in the pipeline. Our core goal is to make money from products (software and hardware), the consulting
            is just an extra revenue stream. We should discuss the next steps for the company.

            """.stripIndent());
    }


}
