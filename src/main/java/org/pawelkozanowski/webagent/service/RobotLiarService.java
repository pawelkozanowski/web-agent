package org.pawelkozanowski.webagent.service;


import com.openai.client.OpenAIClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pawelkozanowski.webagent.model.RobotMessage;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.awt.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotLiarService {

    private static final String VERIFICATION_ULR = "https://xyz.ag3nts.org/verify";

    private static final String VERIFICATION_PROMPT = "Your task is to answer the question asked. The question is given in the <question section below>\n" +
            "\n" +
            "<question>\n" +
            "%s\n" +
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
            "\n" +
            "\"What is the capitol city of Poland?\"\n" +
            "\"Kraków\"\n" +
            "\n" +
            "\n" +
            "\"What is the famous numer form the book The Hitchhiker's Guide to the Galaxy\n" +
            "\"69\"\n" +
            "\n" +
            "\"What is the current year>\"\n" +
            "\"1999\"\n" +
            "\n" +
            "}\n" +
            "</examples>";

    private final OpenAIClient openAIClient;

    public RobotMessage makeTheAuthenticationConversation() {

       RestClient client = RestClient.create();

       RobotMessage initialMessage = new RobotMessage("0", "READY");

       RobotMessage question = client.post().uri(VERIFICATION_ULR)
               .contentType(MediaType.APPLICATION_JSON)
               .body(initialMessage).retrieve().body(RobotMessage.class);

       log.info("Verification question: \n" + question.toString());
       log.info("Question PROMPT: \n" + String.format(VERIFICATION_PROMPT, question));

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(String.format(VERIFICATION_PROMPT, question))
                .model(ChatModel.GPT_4O_MINI)
                .build();

        ChatCompletion chatCompletion = openAIClient.chat()
                .completions()
                .create(params);

        String chatResponse = chatCompletion.choices()
                .stream()
                .flatMap(choice -> choice.message().content().stream()).findFirst().orElse("No answer found");

        log.info("AI answer: " + chatResponse);

       RobotMessage answer = new RobotMessage(question.msgID(), chatResponse);

       log.info("Answer message: \n" + answer.toString());

       RobotMessage verificationResult = client.post().uri(VERIFICATION_ULR)
               .contentType(MediaType.APPLICATION_JSON)
               .body(answer).retrieve().body(RobotMessage.class);

       log.info("Verification result: " + verificationResult.toString());

       return verificationResult;

    }
}
