package org.pawelkozanowski.webagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class CourseTaskController {

    @GetMapping("/s01e01")
    public String GetApplicationVersion() {
        return "Response for task S01E01";
    }
}
