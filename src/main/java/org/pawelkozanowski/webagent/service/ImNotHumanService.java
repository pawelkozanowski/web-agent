package org.pawelkozanowski.webagent.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Slf4j
@Service
public class ImNotHumanService {

    private final static String TARGET_URL = "https://xyz.ag3nts.org/";
    private final static String USERNAME = "tester";
    private final static String PASSWORD = "574e112a";
    private final static String PROMPT_1_PART = "Answer to the below test question: \n<question> \n";
    private final static String PROMPT_2_PART= "\n</question>\nGive a strict, short answer in form of only one word or year in YYYY format or number.";

    @SneakyThrows
    public String getAgentsPageContent() throws Exception {
        RestClient restClient = RestClient.create();

        String pageContent = restClient.get().uri(TARGET_URL).retrieve().body(String.class);
        log.info(pageContent);


        WebDriver driver = new ChromeDriver();
        driver.get(TARGET_URL);
        String rawQuestion = driver.findElement(By.id("human-question")).getText();
        driver.quit();

        String question = rawQuestion.replace("Question:\n", "");
        log.info("Pytanie ze strony: " + question);

        String prompt = PROMPT_1_PART + question + PROMPT_2_PART;

        log.info("PROMTP: \n" + prompt);

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(PROMPT_1_PART + question + PROMPT_2_PART)
                .model(ChatModel.GPT_4O_MINI)
                .build();

        ChatCompletion chatCompletion = client.chat()
                .completions()
                .create(params);

        String chatResponse = chatCompletion.choices()
                .stream()
                .flatMap(choice -> choice.message().content().stream()).findFirst().orElse("No answer from AI chat");

        log.info("Odpowiedź AI: " + chatResponse);

        String requestBody =
                "username=" + USERNAME +"&password=" + PASSWORD +"&answer=" + chatResponse;

        String solution = restClient.post()
                .uri(TARGET_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        log.info(solution);

        return solution;
    }

    protected static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        return options;
    }
}
