package dev.abstratium.abstrasst.boundary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import dev.abstratium.abstrasst.service.MyAiImageService;
import dev.abstratium.abstrasst.service.MyAiMailService;
import dev.abstratium.abstrasst.service.MyAiService;
import dev.langchain4j.data.image.Image;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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
        return myAiMailService.sendEmail(recipient, subject, modifiedBody);
    }

    @GET
    @Path("/memory/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public String whateverYouAsk(
        @PathParam("userId") String userId, 
        @QueryParam("dynamicSystemMessage") String dynamicSystemMessage, 
        @QueryParam("question") String question) {
        
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


}
