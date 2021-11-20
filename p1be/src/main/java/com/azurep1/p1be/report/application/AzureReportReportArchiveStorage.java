package com.azurep1.p1be.report.application;

import com.azure.storage.blob.BlobClientBuilder;
import com.azurep1.p1be.configuration.Profiles;
import com.azurep1.p1be.report.domain.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Profile(Profiles.AZURE)
@Service
@RequiredArgsConstructor
public class AzureReportReportArchiveStorage implements ReportArchiveStorage {

    @Value("${app.archivization.container-name}")
    private String containerName;

    private final ObjectMapper objectMapper;
    private final BlobClientBuilder blobClientBuilder;
    private final Clock clock;

    @SneakyThrows
    public void archive(List<Report> toArchive) {
        var blobName = LocalDateTime.now(clock).toString();
        var payload = objectMapper.writeValueAsBytes(toArchive);
        var inputStream = new ByteArrayInputStream(payload);
        blobClientBuilder
                .containerName(containerName)
                .blobName(blobName)
                .buildClient()
                .upload(inputStream, payload.length);
        log.info(String.format("Archived %s reports", toArchive.size()));
    }

}
