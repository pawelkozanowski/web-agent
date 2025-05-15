package org.pawelkozanowski.webagent.s01e03;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.pawelkozanowski.webagent.s01e03.model.RequestWrapper;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class S01e03Service {


public void correctCalibrations() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("s01e03_data.txt").getFile());
        RequestWrapper payload = mapper.readValue(file, RequestWrapper.class);

        log.info(payload.toString());
    }
}
