/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.admin.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.dynamia.cms.site.pages.services.PageService;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;

/**
 *
 * @author mario
 */
@Service
public class PageAutoPublisher {

	@Autowired
	private PageService service;

	private LoggingService logger = new SLF4JLoggingService(PageAutoPublisher.class);

	@Scheduled(fixedDelay = 10 * 60 * 60 * 1000)
	public void sync() {
		try {
			logger.info("Publishing pages for today");
			int r = service.publishPages();
			logger.info(r + " pages published ");
		} catch (Exception e) {
			logger.error("Error publishng pages", e);
		}

		try {
			logger.info("Unpublishing old pages");
			int r = service.unpublishPages();
			logger.info(r + " pages unpublished ");
		} catch (Exception e) {
			logger.error("Error unpublishng pages", e);
		}
	}

}
