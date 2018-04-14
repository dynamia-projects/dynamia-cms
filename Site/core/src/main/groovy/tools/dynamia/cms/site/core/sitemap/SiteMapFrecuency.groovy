package tools.dynamia.cms.site.core.sitemap;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum SiteMapFrecuency {
	always, hourly, daily, weekly, monthly, yearly, never;

}
