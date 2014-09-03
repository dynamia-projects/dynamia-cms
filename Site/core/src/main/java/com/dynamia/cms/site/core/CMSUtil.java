/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.commons.CollectionsUtils;
import com.dynamia.tools.commons.StringUtils;
import com.dynamia.tools.integration.Containers;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author mario
 */
public class CMSUtil {

	private Site site;

	public CMSUtil(Site site) {
		super();
		this.site = site;
	}

	public static void addSuccessMessage(String string, RedirectAttributes ra) {
		if (ra != null) {
			ra.addFlashAttribute("successmessage", string);
		}
	}

	public static void addErrorMessage(String string, RedirectAttributes ra) {
		if (ra != null) {
			ra.addFlashAttribute("errormessage", string);
		}
	}

	public static void addWarningMessage(String string, RedirectAttributes ra) {
		if (ra != null) {
			ra.addFlashAttribute("warningmessage", string);
		}
	}

	public String formatNumber(Number number) {
		if (number == null) {
			return "";
		}

		return NumberFormat.getIntegerInstance().format(number);
	}

	public String formatNumber(Number number, String pattern) {
		if (number == null) {
			return "";
		}

		if (pattern == null) {
			return String.valueOf(number);
		}
		DecimalFormat format = new DecimalFormat(pattern);
		return format.format(number);
	}

	public Collection groupCollection(Collection collection, int groupSize) {
		if (collection != null) {
			return CollectionsUtils.group(collection, groupSize);
		} else {
			return Collections.EMPTY_LIST;
		}
	}

	public String absoluteURL(String url) {
		if (url == null || url.isEmpty()) {
			return "#";
		}

		if (!url.startsWith("http")) {
			return "http://" + url;
		} else {
			return url;
		}
	}

	public String cropText(String text, int size) {
		if (text == null) {
			return "";
		}

		if (text.length() <= size) {
			return text;
		} else {
			return text.substring(0, size) + "...";
		}
	}

	public String capitalize(String text) {
		return StringUtils.capitalizeAllWords(text);
	}

	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes requestAttrb = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return requestAttrb.getRequest();
	}

	public String[] listFilesNames(String directory, final String extension) {
		SiteService service = Containers.get().findObject(SiteService.class);
		HttpServletRequest request = getCurrentRequest();
		Site site = service.getSite(request);
		Path path = DynamiaCMS.getSitesResourceLocation(site).resolve(directory);
		if (extension == null) {
			return path.toFile().list();
		} else {
			return path.toFile().list(new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(extension.toLowerCase());

				}
			});
		}
	}

	public Site getSite() {
		return site;
	}

	public String[] listFilesNames(String directory) {
		return listFilesNames(directory, null);
	}
}
