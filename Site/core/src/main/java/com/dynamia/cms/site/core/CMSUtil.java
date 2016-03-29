/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dynamia.cms.site.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dynamia.cms.site.core.api.URLProvider;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteDomain;
import com.dynamia.cms.site.core.html.Option;
import com.dynamia.cms.site.core.services.SiteService;

import tools.dynamia.commons.CollectionsUtils;
import tools.dynamia.commons.StringUtils;
import tools.dynamia.commons.collect.PagedList;
import tools.dynamia.commons.collect.PagedListDataSource;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.domain.util.ContactInfo;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Mario Serrano Leones
 */
public class CMSUtil {

    private Site site;
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    private static final String PAGINATION_DATASOURCE = "paginationDatasource";

    public CMSUtil(Site site) {
        super();
        this.site = site;
    }

    public static String getSiteURL(Site site, String uri) {
        CrudService service = Containers.get().findObject(CrudService.class);

        SiteDomain domain = service.findSingle(SiteDomain.class, "site", site);
        return getSiteURL(domain, uri);
    }

    public static String getSiteURL(SiteDomain domain, String uri) {
        if (uri == null) {
            uri = "";
        }

        String urltext = String.format("http://%s/%s", domain.getName(), uri);
        if (domain.getPort() > 0) {
            urltext = String.format("http://%s:%s/%s", domain.getName(), domain.getPort(), uri);
        }

        return urltext;
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

    public static void addSuccessMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("successmessage", string);
        }
    }

    public static void addErrorMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("errormessage", string);
        }
    }

    public static void addWarningMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("warningmessage", string);
        }
    }

    public static void buildContactInfoOptions(Site site, ModelAndView mv, String prefix, ContactInfo selected) {
        SiteService service = Containers.get().findObject(SiteService.class);
        mv.addObject(prefix + "Countries",
                Option.buildFromArray(service.getSiteParameterAsArray(site, "countries"), selected.getCountry()));
        mv.addObject(prefix + "Regions", Option.buildFromArray(service.getSiteParameterAsArray(site, "regions"), selected.getRegion()));
        mv.addObject(prefix + "Cities", Option.buildFromArray(service.getSiteParameterAsArray(site, "cities"), selected.getCity()));
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

    public static boolean isChecked(String parameter) {
        if (parameter != null) {
            String values[] = {"checked", "on", "true", "1"};
            for (String string : values) {
                if (string.equalsIgnoreCase(parameter)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.get(key);
                    if (values == null) {
                        values = new ArrayList<String>();
                        params.put(key, values);
                    }
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    public static String getResourceURL(Site site, File file) {

        if (file == null) {
            throw new NullPointerException("Resource is null");
        }

        String url = null;
        if (file instanceof URLProvider) {
            url = ((URLProvider) file).getURL();
        } else {

            String baseUrl = getSiteURL(site, "resources");
            File resources = DynamiaCMS.getSitesResourceLocation(site).toFile();
            String filePath = file.getPath();
            filePath = filePath.substring(filePath.indexOf(resources.getName()) + resources.getName().length() + 1);

            filePath = filePath.replace("\\", "/");

            String separator = "";
            if (!baseUrl.endsWith("/")) {
                separator = "/";
            }

            url = baseUrl + separator + filePath;
        }

        return url.replace(" ", "%20");

    }

    public static boolean matchAntPattern(String pattern, String path) {
        pattern = pattern.trim();
        return ANT_PATH_MATCHER.match(pattern, path);
    }

    public static List setupPagination(List paginableList, HttpServletRequest request, ModelAndView mv) {
        PagedListDataSource datasource = (PagedListDataSource) request.getSession().getAttribute(PAGINATION_DATASOURCE);

        if (paginableList instanceof PagedList) {
            datasource = ((PagedList) paginableList).getDataSource();
            request.getSession().setAttribute(PAGINATION_DATASOURCE, datasource);
        }

        if (datasource != null) {
            mv.addObject(PAGINATION_DATASOURCE, datasource);

            if (request.getParameter("page") != null) {
                try {
                    int page = Integer.parseInt(request.getParameter("page"));
                    datasource.setActivePage(page);
                } catch (NumberFormatException numberFormatException) {
                    // not a number, ignore it
                }
            }

            paginableList = datasource.getPageData();
        }
        return paginableList;
    }

    public String thumbnail(String url) {
        String fileName = StringUtils.getFilename(url);
        return url.substring(0, url.indexOf(fileName)) + "thumbnails/" + fileName;
    }

    public String thumbnail(String url, int w, int y) {
        return String.format("%s?w=%s&h=%s", thumbnail(url), w, y);
    }

}
