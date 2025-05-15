package org.pawelkozanowski.webagent.s01e03.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerWrapper {
    private String task = "JSON";
    private String apikey;
    private RequestWrapper answer;
}
