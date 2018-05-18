package tools.dynamia.cms.core.sitemap

import javax.xml.bind.annotation.XmlEnum

@XmlEnum
enum SiteMapFrecuency {
	always, hourly, daily, weekly, monthly, yearly, never

}
