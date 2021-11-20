package com.azurep1.p1be

import com.azurep1.p1be.configuration.AzureBlobClientBuilder
import com.azurep1.p1be.limitation.domain.LimitationRepository
import com.azurep1.p1be.report.application.AzureReportReportArchiveStorage
import com.azurep1.p1be.report.application.ReportArchiveStorage
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.flywaydb.core.Flyway
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.web.servlet.MockMvc
import org.springframework.transaction.support.TransactionTemplate
import spock.lang.Specification

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ComponentScan
@EnableAutoConfiguration
@AutoConfigureMockMvc
@SpringBootTest
class BaseIntegrationSpec extends Specification {

    @SpringBean
    Clock clock = Clock.fixed(
            LocalDateTime.parse("2020-01-10T00:00:00").atOffset(ZoneOffset.UTC).toInstant(),
            ZoneId.of("UTC")
    )

    @SpringBean
    ReportArchiveStorage archiveStorage = Mock(ReportArchiveStorage)

    @Autowired
    LimitationRepository limitationRepository

    @Autowired
    TransactionTemplate transactionTemplate

    @Autowired
    MockMvc mvc

    @Autowired
    Flyway flyway

    static getObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
    }

    def cleanup() {
        flyway.clean()
        flyway.migrate()
    }

}
