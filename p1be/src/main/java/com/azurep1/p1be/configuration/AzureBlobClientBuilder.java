package com.azurep1.p1be.configuration;

import com.azure.storage.blob.BlobClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile(Profiles.AZURE)
@Configuration
public class AzureBlobClientBuilder {

    @Value("${app.storage.azure-connection-string}")
    private String connectionString;

    @Bean
    public BlobClientBuilder getClient() {
        BlobClientBuilder client = new BlobClientBuilder();
        client.connectionString(connectionString);
        return client;
    }
}
