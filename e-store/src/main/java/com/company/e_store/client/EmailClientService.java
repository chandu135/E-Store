package com.company.e_store.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailClientService {

    private final WebClient webClient;

    @Value("${email.service.name}")
    private String emailServiceName;

    public EmailClientService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String sendWelcomeEmail(String to, String name) {
        return webClient.post()
                .uri("http://" + emailServiceName + "/api/email/send-template?to={to}&name={name}", to, name)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String sendTextEmail(String to, String subject, String text) {
        return webClient.post()
                .uri("http://" + emailServiceName + "/api/email/send-text?to={to}&subject={subject}&text={text}", to, subject, text)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
