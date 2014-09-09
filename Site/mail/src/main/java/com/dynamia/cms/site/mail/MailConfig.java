package com.dynamia.cms.site.mail;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {

	@Bean
	public VelocityEngine velocityEngine() {
		VelocityEngine velocityEngine = new VelocityEngine();
		return velocityEngine;
	}

}
