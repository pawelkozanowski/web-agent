package org.pawelkozanowski.webagent.service;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Slf4j
@Service
public class WebService {

    private final static String TARGET_URL = "https://xyz.ag3nts.org/";
    private final static String PROMPT = "Answer to the below question. Give a strict, short one word answer: ";

    public String getAgentsPageContent() {
        RestClient restClient = RestClient.create();

        String pageContent = restClient.get().uri(TARGET_URL).retrieve().body(String.class);
        log.info(pageContent);


        WebDriver driver = new ChromeDriver();
        driver.get(TARGET_URL);
        String rawQuestion = driver.findElement(By.id("human-question")).getText();

        String question = rawQuestion.replace("Question:\n", "");
        log.info("Pytanie ze strony: " + question);

        OpenAIClient client = OpenAIOkHttpClient.fromEnv();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(PROMPT + question)
                .model(ChatModel.GPT_4O_MINI)
                .build();

        ChatCompletion chatCompletion = client.chat()
                .completions()
                .create(params);

        String chatResponse = chatCompletion.choices()
                .stream()
                .flatMap(choice -> choice.message().content().stream()).findFirst().orElse("No answer from AI chat");

        log.info("Odpowiedź AI: " + chatResponse);
        driver.quit();
        return chatResponse;

    }

    protected static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        return options;
    }
}
