package org.pawelkozanowski.webagent.controller;

import lombok.RequiredArgsConstructor;
import org.pawelkozanowski.webagent.service.WebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class MainController {

    @GetMapping("/version")
    public String GetApplicationVersion() {
        return "1.0.0";
    }

}
