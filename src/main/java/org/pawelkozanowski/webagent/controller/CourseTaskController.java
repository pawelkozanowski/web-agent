package org.pawelkozanowski.webagent.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.pawelkozanowski.webagent.model.RobotMessage;
import org.pawelkozanowski.webagent.s01e03.S01e03Service;
import org.pawelkozanowski.webagent.service.ImNotHumanService;
import org.pawelkozanowski.webagent.service.RobotLiarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CourseTaskController {

    private final ImNotHumanService webService;
    private final RobotLiarService robotService;
    private final S01e03Service s01e03Service;

    @GetMapping("/s01e01")
    @SneakyThrows
    public String answerAuthQuestion() {
        return webService.getAgentsPageContent();
    }

    @GetMapping("/s01e02")
    @SneakyThrows
    public RobotMessage makeAuthConversation() {
        return robotService.makeTheAuthenticationConversation();
    }

    @GetMapping("/s01e03")
    @SneakyThrows
    public void correctCalibrations() {
        s01e03Service.correctCalibrations();
    }
}
