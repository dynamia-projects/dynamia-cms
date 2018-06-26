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
