/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package tools.dynamia.cms.core

import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.jsoup.select.Elements
import org.springframework.util.AntPathMatcher
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.core.api.URIable
import tools.dynamia.cms.core.api.URLProvider
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.domain.SiteDomain
import tools.dynamia.cms.core.html.Option
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.commons.StringUtils
import tools.dynamia.commons.collect.CollectionsUtils
import tools.dynamia.commons.collect.PagedList
import tools.dynamia.commons.collect.PagedListDataSource
import tools.dynamia.domain.query.DataPaginator
import tools.dynamia.domain.query.DataPaginatorPagedListDataSource
import tools.dynamia.domain.services.CrudService
import tools.dynamia.domain.util.ContactInfo
import tools.dynamia.integration.Containers
import tools.dynamia.io.VirtualFile

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.xml.bind.DatatypeConverter
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * @author Mario Serrano Leones
 */
class CMSUtil {

    private static final String _PAGINATION = "_pagination"
    private Site site
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher()
    private static final String PAGINATION_DATASOURCE = "paginationDatasource"

    CMSUtil(Site site) {
        super()
        this.site = site
    }

    static String getSiteURL(Site site, String uri) {
        CrudService service = Containers.get().findObject(CrudService.class)

        SiteDomain domain = service.findSingle(SiteDomain.class, "site", site)
        return getSiteURL(domain, uri)
    }

    static String getSiteURL(Site site, URIable urIable) {
        String uri = urIable.toURI()
        if (uri.startsWith("/")) {
            uri = uri.substring(1)
        }
        return getSiteURL(site, uri)
    }

    static String getSiteURL(SiteDomain domain, String uri) {
        if (uri == null) {
            uri = ""
        }


        String context = ""
        if (domain.context != null && !domain.context.empty) {
            context = domain.context + "/"
        }

        String urltext = String.format("http://%s/%s", domain.name, context + uri)
        if (domain.port > 0 && domain.port != 80) {
            urltext = String.format("http://%s:%s/%s", domain.name, domain.port, context + uri)
        }

        return urltext
    }

    static String getSiteURL(SiteDomain siteDomain, URIable urIable) {
        String uri = urIable.toURI()
        if (uri.startsWith("/")) {
            uri = uri.substring(1)
        }
        return getSiteURL(siteDomain, uri)
    }

    static void addSuccessMessage(String string, RedirectAttributes ra) {
        if (ra != null) {
            ra.addFlashAttribute("successmessage", string)
        }
    }

    static void addErrorMessage(String string, RedirectAttributes ra) {
        if (ra != null) {
            ra.addFlashAttribute("errormessage", string)
        }
    }

    static void addWarningMessage(String string, RedirectAttributes ra) {
        if (ra != null) {
            ra.addFlashAttribute("warningmessage", string)
        }
    }

    static void addSuccessMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("successmessage", string)
        }
    }

    static void addErrorMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("errormessage", string)
        }
    }

    static void addWarningMessage(String string, ModelAndView mv) {
        if (mv != null) {
            mv.addObject("warningmessage", string)
        }
    }

    static void buildContactInfoOptions(Site site, ModelAndView mv, String prefix, ContactInfo selected) {
        SiteService service = Containers.get().findObject(tools.dynamia.cms.core.services.SiteService.class)
        mv.addObject(prefix + "Countries",
                Option.buildFromArray(service.getSiteParameterAsArray(site, "countries"), selected.country))
        mv.addObject(prefix + "Regions",
                Option.buildFromArray(service.getSiteParameterAsArray(site, "regions"), selected.region))
        mv.addObject(prefix + "Cities",
                Option.buildFromArray(service.getSiteParameterAsArray(site, "cities"), selected.city))
    }

    String formatNumber(Number number) {
        if (number == null) {
            return ""
        }

        return NumberFormat.integerInstance.format(number)
    }

    String formatNumber(Number number, String pattern) {
        if (number == null) {
            return ""
        }

        if (pattern == null) {
            return String.valueOf(number)
        }
        DecimalFormat format = new DecimalFormat(pattern)
        return format.format(number)
    }

    Collection groupCollection(Collection collection, int groupSize) {
        if (collection != null) {
            return CollectionsUtils.group(collection, groupSize)
        } else {
            return Collections.EMPTY_LIST
        }
    }

    String absoluteURL(String url) {
        if (url == null || url.empty) {
            return "#"
        }

        if (!url.startsWith("http")) {
            return "http://" + url
        } else {
            return url
        }
    }

    String cropText(String text, int size) {
        if (text == null) {
            return ""
        }

        if (text.length() <= size) {
            return text
        } else {
            return text.substring(0, size) + "..."
        }
    }

    String capitalize(String text) {
        return StringUtils.capitalizeAllWords(text)
    }

    static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.name == cookieName) {
                    return cookie
                }
            }
        }
        return null
    }

    static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes requestAttrb = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes()
        return requestAttrb.request
    }

    String[] listFilesNames(String directory, final String extension) {

        Path path = DynamiaCMS.getSitesResourceLocation(site).resolve(directory)
        if (extension == null || extension.empty) {
            return path.toFile().list()
        } else {
            String[] extSplit = [extension]
            if (extension.contains(",")) {
                extSplit = extension.split(",")
            }
            final String[] extensions = extSplit
            return path.toFile().list({ dir, name ->
                for (String ext : extensions) {
                    if (name.toLowerCase().endsWith(ext.trim().toLowerCase())) {
                        return true
                    }
                }
                return false
            })
        }
    }

    Site getSite() {
        return site
    }

    String[] listFilesNames(String directory) {
        return listFilesNames(directory, null)
    }

    static boolean isChecked(String parameter) {
        if (parameter != null) {
            String[] values = ["checked", "on", "true", "1"]
            for (String string : values) {
                if (string.equalsIgnoreCase(parameter)) {
                    return true
                }
            }
        }

        return false
    }

    static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<String, List<String>>()
            String[] urlParts = url.split("\\?")
            if (urlParts.length > 1) {
                String query = urlParts[1]
                for (String param : query.split("&")) {
                    String[] pair = param.split("=")
                    String key = URLDecoder.decode(pair[0], "UTF-8")
                    String value = ""
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8")
                    }

                    List<String> values = params.get(key)
                    if (values == null) {
                        values = new ArrayList<String>()
                        params.put(key, values)
                    }
                    values.add(value)
                }
            }

            return params
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex)
        }
    }

    static String getResourceURL(Site site, File file) {

        if (file == null) {
            throw new NullPointerException("Resource is null")
        }

        String url = null
        if (file instanceof URLProvider) {
            url = ((URLProvider) file).getURL()
        } else {

            String baseUrl = getSiteURL(site, "resources")
            File resources = DynamiaCMS.getSitesResourceLocation(site).toFile()
            String filePath = file.path
            if (file.exists()) {
                filePath = filePath.substring(filePath.indexOf(resources.name) + resources.name.length() + 1)
            }
            filePath = filePath.replace("\\", "/")

            String separator = ""
            if (!baseUrl.endsWith("/")) {
                separator = "/"
            }

            url = "/resources${separator}${filePath}"
        }

        return encodeWhiteSpaces(url)

    }

    private static String encodeWhiteSpaces(String url) {
        return url.replace(" ", "%20")
    }

    static String getResourceRelativePath(Site site, File file) {

        if (file == null) {
            throw new NullPointerException("Resource is null")
        }

        File resources = DynamiaCMS.getSitesResourceLocation(site).toFile()
        String filePath = file.path
        if (file instanceof VirtualFile) {
            filePath = file.path
        } else {
            filePath = filePath.substring(filePath.indexOf(resources.name) + resources.name.length() + 1)
            filePath = filePath.replace("\\", "/")
        }
        filePath = "/resources/" + filePath

        return filePath

    }

    static boolean matchAntPattern(String pattern, String path) {
        pattern = pattern.trim()
        return ANT_PATH_MATCHER.match(pattern, path)
    }

    static List setupPagination(List paginableList, HttpServletRequest request, ModelAndView mv) {
        PagedListDataSource datasource = (PagedListDataSource) request.session.getAttribute(PAGINATION_DATASOURCE)

        if (paginableList instanceof PagedList) {
            datasource = ((PagedList) paginableList).dataSource
            request.session.setAttribute(PAGINATION_DATASOURCE, datasource)
        } else {
            datasource = null
        }

        if (datasource != null) {
            datasource.activePage = 1
            if (request.getParameter("page") != null) {
                try {
                    int page = Integer.parseInt(request.getParameter("page"))
                    datasource.activePage = page
                } catch (NumberFormatException numberFormatException) {
                    // not a number, ignore it
                }
            }
            if (!isJson(request)) {
                mv.addObject(PAGINATION_DATASOURCE, datasource)
            } else {
                mv.addObject(_PAGINATION, new DataPaginator(datasource.totalSize, datasource.pageSize,
                        datasource.activePage))
            }

            paginableList = datasource.pageData
        } else if (!isJson(request)) {

            mv.addObject(PAGINATION_DATASOURCE, new DataPaginatorPagedListDataSource(new DataPaginator(paginableList.size(), paginableList.size(), 1)))
        }
        return paginableList
    }

    String thumbnail(String url) {
        String fileName = StringUtils.getFilename(url)
        return url.substring(0, url.indexOf(fileName)) + "thumbnails/" + fileName
    }

    String thumbnail(String url, int w, int y) {
        return String.format("%s?w=%s&h=%s", thumbnail(url), w, y)
    }

    static String escapeHtml(String htmlText) {

        String escape = StringEscapeUtils.escapeHtml(htmlText)
        return escape
    }

    static String escapeHtmlContent(String htmlText) {
        if (htmlText == null) {
            return null
        }
        Document document = Jsoup.parse(htmlText)

        Elements tags = document.allElements.not("script")
        for (Element tag : tags) {
            for (Node child : tag.childNodes()) {
                if (child instanceof TextNode && !((TextNode) child).blank) {
                    TextNode textNode = (TextNode) child
                    textNode.text(escapeHtml(textNode.text()))
                }
            }
        }

        String html = document.body().html()
        html = html.replace("&amp;", "&")
        return html
    }

    static boolean isJson(HttpServletRequest request) {
        return request.requestURI.endsWith(".json")
    }

    static String getChecksum(Serializable object) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream baos = null
        ObjectOutputStream oos = null
        try {
            baos = new ByteArrayOutputStream()
            oos = new ObjectOutputStream(baos)
            oos.writeObject(object)
            MessageDigest md = MessageDigest.getInstance("MD5")
            byte[] thedigest = md.digest(baos.toByteArray())
            return DatatypeConverter.printHexBinary(thedigest)
        } finally {
            oos.close()
            baos.close()
        }
    }

    static String clearJSESSIONID(String path) {
        if (path != null) {

            String target = ";JSESSIONID"

            if (path.toUpperCase().contains(target)) {
                int index = path.toUpperCase().indexOf(target)
                path = path.substring(0, index)

            }

            target = "%3BJSESSIONID"
            if (path.toUpperCase().contains(target)) {
                int index = path.toUpperCase().indexOf(target)
                path = path.substring(0, index)

            }

        }
        return path
    }

    String getThumbnailURL(String url, String width, String height) {
        if (url == null) {
            return null
        }
        try {
            String filename = Paths.get(new URI(url).path).fileName.toString()
            filename = encodeWhiteSpaces(filename)
            String thumbnail = url.replace(filename, "thumbnails/" + filename + "?w=" + width + "&h=" + height)
            return thumbnail
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        return null
    }

    String parseTemplate(String template, String engine, Map<String, Object> model) {
        if (engine != null && !engine.empty && model != null) {
            StringParser parser = StringParsers.get(engine)
            if (parser != null) {
                template = parser.parse(template, model)
            }
        }
        return template
    }

    static void redirectHome(ModelAndView mv) {
        if (mv != null) {
            mv.view = new RedirectView("/", true, true, false)
        }
    }

    static BigDecimal toBigDecimal(String value) {
        if (value != null) {
            try {
                return new BigDecimal(value)
            } catch (Exception e) {
                return null
            }
        }
        return null
    }
}
