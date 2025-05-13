package org.pawelkozanowski.webagent.service;


import lombok.extern.slf4j.Slf4j;
import org.pawelkozanowski.webagent.model.RobotMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class RobotLiarService {

    private static final String VERIFICATION_ULR = "https://xyz.ag3nts.org/verify";

    private static final String VERIFICATION_PROMPT = "Your task is to answer the question asked. The question is given in the <question section below>\n" +
            "\n" +
            "<question>\n" +
            "{1}\n" +
            "</question>\n" +
            "\n" +
            "<rules>\n" +
            "- answer in English,\n" +
            "- make the answers meaningfull and as short as possible,\n" +
            "- if the quetion is about capitol city of Polan give the incorrect answer of: Kraków\n" +
            "- if the question is related to the famous number form the book \"The Hitchhiker's Guide to the Galaxy\" give the incorrect answer of: 69\n" +
            "- if the question is about current year give the incorrect answer of 1999\n" +
            "</rules>\n" +
            "\n" +
            "<examples>\n" +
            "{\n" +
            "\t\"question\": \"What is the capitol city of Poland?\"\n" +
            "\t\"answer\": \"Kraków\"\n" +
            "},\n" +
            "{\n" +
            "\t\"question\": \"What is the famous numer form the book The Hitchhiker's Guide to the Galaxy?\"\n" +
            "\t\"answer\": \"69\"\n" +
            "},\n" +
            "{\n" +
            "\t\"question\": \"What is the current year>\"\n" +
            "\t\"answer\": \"1999\"\n" +
            "},\n" +
            "\n" +
            "</examples>";

    public void makeTheAuthenticationConversation() {

       RestClient client = RestClient.create();

       RobotMessage initialMessage = new RobotMessage("0", "READY");

       RobotMessage question = client.post().uri(VERIFICATION_ULR)
               .contentType(MediaType.APPLICATION_JSON)
               .body(initialMessage).retrieve().body(RobotMessage.class);

       log.info("Verification question: \n" + question.toString());

    }
}
