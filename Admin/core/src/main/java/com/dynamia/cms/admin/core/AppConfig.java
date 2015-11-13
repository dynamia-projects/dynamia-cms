/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import libraries.hibernate.App;
import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 *
 * @author mario
 */
@Configuration
@EnableScheduling
@EnableAsync
public class AppConfig {

	@Bean
	public LoggingService cmsLoggingService() {
		return new SLF4JLoggingService(AppConfig.class);
	}

}
