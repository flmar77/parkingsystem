package com.parkit.parkingsystem.unittests.service;

import com.parkit.parkingsystem.service.DataBaseConfigService;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DataBaseConfigServiceTest {

    @Test
    public void should_logErrorAndReturnNull_whenWrongFilePath() {
        DataBaseConfigService dataBaseConfigService = new DataBaseConfigService("WrongFilePath");
        LogCaptor logCaptor = LogCaptor.forClass(DataBaseConfigService.class);

        assertThat(dataBaseConfigService.getDataBaseConfig()).isNull();
        assertThat(logCaptor.getErrorLogs()).size().isEqualTo(1);
    }
}
