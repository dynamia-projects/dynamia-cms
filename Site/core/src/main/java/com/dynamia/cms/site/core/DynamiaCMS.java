/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.tools.commons.logger.LoggingService;
import com.dynamia.tools.commons.logger.SLF4JLoggingService;
import com.dynamia.tools.domain.query.ApplicationParameters;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author mario
 */
public class DynamiaCMS {

    private static final LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class);

    private static final String PREFIX = "CMSConfig_";

    static final String HOMEVAR = PREFIX + "HomePath";
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
            path = ApplicationParameters.get().getValue(HOMEVAR);
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
