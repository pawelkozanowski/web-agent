package org.pawelkozanowski.webagent.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Slf4j
@Service
public class WebService {

    private final static String TARGET_URL = "https://xyz.ag3nts.org/";

    public String getAgentsPageContent() {
        RestClient restClient = RestClient.create();

        String pageContent = restClient.get().uri(TARGET_URL).retrieve().body(String.class);
        log.info(pageContent);
        return pageContent;

    }
}
