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

import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.cms.core.services.SiteService
import tools.dynamia.integration.Containers
import tools.dynamia.io.IOUtils
import tools.dynamia.io.ImageUtil

import javax.servlet.http.HttpServletRequest
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 * @author Mario Serrano Leones
 */
class SiteResourceHandler extends ResourceHttpRequestHandler {

    private final static String THUMBNAILS = "/thumbnails/"

    @Override
    protected Resource getResource(HttpServletRequest request) {

        SiteService coreService = Containers.get().findObject(SiteService.class)
        Site site = coreService.getSite(request)
        if (site == null) {
            site = coreService.mainSite
        }

        if (site == null) {
            throw new SiteNotFoundException("Cannot load resources. Site Not found for " + request.serverName)
        }

        String resourceURI = request.requestURI
        if (resourceURI == null) {
            resourceURI = request.requestURI
        }

        Path dir = resolveResourceDirectory(site)
        Path resource = Paths.get(resourceURI)

        resource = resource.subpath(1, resource.nameCount)
        if (isPrivateResource(resource)) {
            return null
        }
        if (Files.isDirectory(dir.resolve(resource))) {
            resource = resource.resolve("index.html")
        }

        File file = dir.resolve(resource).toFile()


        if (ImageUtil.isImage(file) || file.name.endsWith(".jpeg")) {

            if (isThumbnail(request)) {
                file = createOrLoadThumbnail(file, resource.toString(), request)
            }

            if (!file.exists()) {
                file = dir.resolve("images/nophoto.jpg").toFile()
            }
        }

        if (!file.exists()) {
            return null
        }

        return new FileSystemResource(file)
    }

    private boolean isPrivateResource(Path resource) {
        for (String folder : (DynamiaCMS.privateLocations)) {

            if (resource.toString().startsWith(folder)) {
                return true
            }
        }

        return false
    }

    protected Path resolveResourceDirectory(Site site) {
        return DynamiaCMS.getSitesResourceLocation(site)
    }

    private boolean isThumbnail(HttpServletRequest request) {
        return request.requestURI.contains(THUMBNAILS)
    }

    private File createOrLoadThumbnail(File file, String uri, HttpServletRequest request) {

        String fileName = file.name
        String baseUri = file.parentFile.parent

        String w = getParam(request, "w", "200")
        String h = getParam(request, "h", "200")
        String subfolder = w + "x" + h
        File realThumbImg = new File(baseUri + "/" + subfolder + "/" + fileName)
        if (!realThumbImg.exists()) {
            File realImg = new File(baseUri, fileName)
            if (realImg.exists()) {
                String format = IOUtils.getFileExtension(realImg)
                ImageUtil.resizeImage(realImg, realThumbImg, format, Integer.parseInt(w), Integer.parseInt(h))
            }
        }
        if (realThumbImg.exists()) {
            return realThumbImg
        } else {
            return file
        }

    }

    String getParam(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name)
        if (value == null || value.trim().empty) {
            value = defaultValue
        }
        return value
    }
}
