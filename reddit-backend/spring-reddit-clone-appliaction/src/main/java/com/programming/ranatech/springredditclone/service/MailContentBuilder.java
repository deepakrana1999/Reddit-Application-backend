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

    public String activationMailBuilder(String username, String activationUrl) {

        Context context = new Context();

        context.setVariables(Map.of(
                "username", username,
                "activationUrl", activationUrl
                 ));

        return templateEngine.process("activationMailTemplate", context);
    }
	
    public String commentMailBuilder(String postUsername, String currentUsername,String postUrl) {
    	Context context = new Context();
    	
    	context.setVariables(Map.of(
    			"postUsername", postUsername,
    			"currentUserName",currentUsername,
    			"postUrl", postUrl
    			));
    	return templateEngine.process("commentMailTemplate", context);
    }
}
