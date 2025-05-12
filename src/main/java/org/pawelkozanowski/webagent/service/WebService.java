package org.pawelkozanowski.webagent.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class WebService {
    private void makeApiRequest() {
        RestClient restClient = RestClient.create();

    }
}
