package com.programming.ranatech.springredditclone.service;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailContentBuilder {
	
	private final TemplateEngine templateEngine;

    public String build(String username, String activationUrl) {

        Context context = new Context();

        context.setVariables(Map.of(
                "username", username,
                "activationUrl", activationUrl
        ));

        return templateEngine.process("mailTemplate", context);
    }
	
}
