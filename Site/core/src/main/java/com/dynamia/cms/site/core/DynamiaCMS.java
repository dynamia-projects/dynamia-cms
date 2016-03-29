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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.dynamia.cms.site.core.domain.Site;

import tools.dynamia.commons.logger.LoggingService;
import tools.dynamia.commons.logger.SLF4JLoggingService;
import tools.dynamia.domain.query.ApplicationParameters;

/**
 *
 * @author Mario Serrano Leones
 */
public class DynamiaCMS {

    private static final LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class);

    private static final String PREFIX = "CMSConfig_";

    public static final String CFG_HOMEVAR = PREFIX + "HOMEPATH";
    public static final String CFG_DEFAULT_TEMPLATE = PREFIX + "DEFAULTTEMPLATE";
    public static final String CFG_SUPER_ADMIN_SITE = PREFIX + "SUPERADMINSITE";

    private static final String TEMPLATES = "templates";
    private static final String VIEWS = "views";
    private static final String MODULES = "modules";
    private static final String SITES = "sites";
    private static String path;

    public static Path getHomePath() {
        Path home = Paths.get(homePath());
        createDirectoryIfNotExists(home);
        return home;
    }

    public static Path getSitesResourceLocation() {
        Path path = Paths.get(homePath(), SITES);
        createDirectoryIfNotExists(path);
        return path;
    }

    public static Path getSitesResourceLocation(Site site) {
        Path path = Paths.get(homePath(), SITES, site.getKey());

        createDirectoryIfNotExists(path);
        return path;
    }

    public static Path getModulesLocation() {
        Path path = Paths.get(homePath(), MODULES);
        createDirectoryIfNotExists(path);
        return path;
    }

    public static Path getTemplatesLocation() {
        Path path = Paths.get(homePath(), TEMPLATES);
        createDirectoryIfNotExists(path);
        return path;
    }

    public static Path getViewLocation() {
        Path path = Paths.get(homePath(), VIEWS);
        createDirectoryIfNotExists(path);
        return path;
    }

    private static String homePath() {
        if (path == null) {
            path = ApplicationParameters.get().getValue(CFG_HOMEVAR);
        }
        return path;
    }

    private static void createDirectoryIfNotExists(Path path) {
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ex) {
                logger.error("Error creating DynamiaCMS path " + path, ex);
            }
        }
    }

    public static String[] getRelativeLocations() {
        return new String[]{VIEWS, MODULES};
    }

    public static void reloadHomePath() {
        path = null;
    }

}
