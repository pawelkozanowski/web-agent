package org.pawelkozanowski.webagent.config;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public OpenAIClient getOpenAIClient() {
        return  OpenAIOkHttpClient.fromEnv();
    }
}
