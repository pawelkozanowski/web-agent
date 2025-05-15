package org.pawelkozanowski.webagent.s01e03.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestData {
    private String question;
    private Integer answer;
    private TestQuestion test;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TestQuestion {
    private String q;
    private String a;
}
