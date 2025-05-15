package org.pawelkozanowski.webagent.s01e03.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestData {
    private String question;
    private Integer answer;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private TestQuestion test;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TestQuestion {
    private String q;
    private String a;
}
