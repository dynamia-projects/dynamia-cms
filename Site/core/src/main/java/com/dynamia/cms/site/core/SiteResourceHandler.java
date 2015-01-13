/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.services.SiteService;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.io.ImageUtil;

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

        Path dir = DynamiaCMS.getSitesResourceLocation(site);

        Path resource = Paths.get(request.getPathInfo());
        resource = resource.subpath(1, resource.getNameCount());

        File file = dir.resolve(resource).toFile();
        if (ImageUtil.isImage(file)) {
            
            if (isThumbnail(request)) {
                file = createOrLoadThumbnail(file, resource.toString(), request);
            }
            
            if(!file.exists()){
                file = dir.resolve("images/nophoto.jpg").toFile();
            }
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
