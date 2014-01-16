/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

/**
 *
 * @author mario
 */
public class SiteResourceHandler extends ResourceHttpRequestHandler {

    private final static String THUMBNAILS = "/thumbnails/";

    @Override
    protected Resource getResource(HttpServletRequest request) {
        SiteService coreService = Containers.get().findObject(SiteService.class);
        Site site = coreService.getSite(request);
        if (site == null) {
            site = coreService.getMainSite();
        }

        if (site == null) {
            throw new SiteNotFoundException("Cannot load resources. Site Not found for " + request.getServerName());
        }

        String dir = site.getResourcesLocation();

        String uri = request.getPathInfo();
        uri = uri.substring("resources/".length());
        File file = new File(dir + uri);
        if (ImageUtil.isImage(file) && isThumbnail(request)) {
            file = createOrLoadThumbnail(file, uri, request);
        }

        return new FileSystemResource(file);
    }

    private boolean isThumbnail(HttpServletRequest request) {
        return request.getPathInfo().contains(THUMBNAILS);
    }

    private File createOrLoadThumbnail(File file, String uri, HttpServletRequest request) {

        String fileName = file.getName();
        String baseUri = file.getParentFile().getParent();

        String w = getParam(request, "w", "200");
        String h = getParam(request, "h", "200");
        String subfolder = w + "x" + h;
        File realThumbImg = new File(baseUri + "/" + subfolder + "/" + fileName);
        if (!realThumbImg.exists()) {
            File realImg = new File(baseUri, fileName);
            if (realImg.exists()) {
                ImageUtil.resizeJPEGImage(realImg, realThumbImg, Integer.parseInt(w), Integer.parseInt(h));
            }
        }
        return realThumbImg;

    }

    public String getParam(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        if (value == null || value.trim().isEmpty()) {
            value = defaultValue;
        }
        return value;
    }
}
