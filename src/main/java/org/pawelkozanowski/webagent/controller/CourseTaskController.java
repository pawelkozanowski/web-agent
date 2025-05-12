package org.pawelkozanowski.webagent.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.pawelkozanowski.webagent.service.WebService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class CourseTaskController {

    private final WebService webService;

    @GetMapping("/s01e01")
    @SneakyThrows
    public String GetApplicationVersion() {
        return webService.getAgentsPageContent();
    }
}
