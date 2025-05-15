package org.pawelkozanowski.webagent.s01e03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pawelkozanowski.webagent.s01e03.model.AnswerWrapper;
import org.pawelkozanowski.webagent.s01e03.model.RequestWrapper;
import org.pawelkozanowski.webagent.s01e03.model.TestData;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Slf4j
@Service
@RequiredArgsConstructor
public class S01e03Service {

        private static final String URL = "https://c3ntrala.ag3nts.org/report";

        private static final String PROMPT = """
                Your task is to verify whether the answers to the questions and mathematical operations given in the question set given in the <questions> block are correct.
                If the answer is correct, leave it unchanged. 
                If the answer is incorrect, change its value to the correct one. 
                Leave the answer format exactly the same, changing only the value to the correct one.
                
                The question set has a format of JSON objects collection.
                Questions are placed in fields with the key "question" or "q" and answers in fields with the key "answer" or "a".
                
                <examples>
                USER:
                {
                	"question": "62 + 23",
                	"answer": 85
                },
                {
                	"question": "99 + 5",
                	"answer": 104
                },
                {
                	"question": "86 + 75",
                	"answer": 161
                }
                
                AI:
                {
                	"question": "62 + 23",
                	"answer": 85
                },
                {
                	"question": "99 + 5",
                	"answer": 104
                },
                {
                	"question": "86 + 75",
                	"answer": 161
                }
                
                USER:
                {
                	"question": "70 + 46",
                	"answer": 116
                },
                {
                	"question": "93 + 29",
                	"answer": 46
                },
                {
                	"question": "61 + 25",
                	"answer": 86
                }
                
                AI:
                {
                	"question": "70 + 46",
                	"answer": 116
                },
                {
                	"question": "93 + 29",
                	"answer": 122
                },
                {
                	"question": "61 + 25",
                	"answer": 86
                }
                
                USER:
                {
                	"question": "35 + 69",
                	"answer": 104
                },
                {
                	"question": "28 + 45",
                	"answer": 73
                },
                {
                	"question": "45 + 74",
                	"answer": 119,
                	"test": {
                		"q": "What is the capital city of France?",
                		"a": "???"
                	}
                }
                
                AI:
                {
                	"question": "35 + 69",
                	"answer": 10
                },
                {
                	"question": "28 + 45",
                	"answer": 73
                },
                {
                	"question": "45 + 74",
                	"answer": 119,
                	"test": {
                		"q": "What is the capital city of France?",
                		"a": Paris
                	}
                }
                
                <questions>
                %s
                </questions>
                
                In response, return the entire data set in the same structure. 
                Change only the values in the responses "answer" and "a" that are not correct. Do not add any extra characters, symbols or comments.
                """;

        private final OpenAIClient openAIClient;

public void correctCalibrations() throws Exception {

        String apiKey = System.getenv("API_KEY");

        RestClient client = RestClient.create();

        log.info("AI_Devs API Key: " + apiKey);

        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("s01e03_data.txt").getFile());
        RequestWrapper payload = mapper.readValue(file, RequestWrapper.class);
        int numberOfQuestions = payload.getTestData().size();
        int batchSize = 100;
        log.info("Number of calibration questions: " + numberOfQuestions);

        List<TestData> correctDataSet = new ArrayList<>();

        for (int i = 0; numberOfQuestions > correctDataSet.size(); i = i + batchSize) {

                List<TestData> sublist;
                if (numberOfQuestions - (i + batchSize) > 0) {
                        sublist = new ArrayList<>(payload.getTestData().subList(i, i+batchSize));
                } else {
                        sublist = new ArrayList<>(payload.getTestData().subList(i, (numberOfQuestions)));
                }
                String sublistTest = mapper.writeValueAsString(sublist);

                ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                        .addUserMessage(String.format(PROMPT, sublistTest))
                        .model(ChatModel.GPT_4O_MINI)
                        .temperature(0.0)
                        .build();

                ChatCompletion chatCompletion = openAIClient.chat()
                        .completions()
                        .create(params);

                String chatResponse = chatCompletion.choices()
                        .stream()
                        .flatMap(choice -> choice.message().content().stream()).findFirst().orElse("No answer found");

                log.info("Iteration " + ((i + 1)/100) + " chat response: \n----\n" + chatResponse + "\n----\n");

//                String clearedChatResponse = chatResponse.substring(7, chatResponse.length()-3);
                CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, TestData.class);
                List<TestData> answerData = mapper.readValue(chatResponse, collectionType);
                correctDataSet.addAll(answerData);
        }

        log.info("Number of answered questions: " + correctDataSet.size());
        payload.setApikey(apiKey);
        payload.setTestData(correctDataSet);

        AnswerWrapper answerPayload = AnswerWrapper.builder()
                .task("JSON")
                .apikey(apiKey)
                .answer(payload)
                .build();

        log.info(mapper.writeValueAsString(answerPayload));

        String solution = client.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .body(answerPayload)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        log.info(solution);

    }
}
