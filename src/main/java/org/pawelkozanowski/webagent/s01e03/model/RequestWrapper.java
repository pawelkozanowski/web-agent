package org.pawelkozanowski.webagent.s01e03.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestWrapper {
    private String apikey;
    private String description;
    private String copyright;
    @JsonProperty("test-data")
    private List<TestData> testData;
}
