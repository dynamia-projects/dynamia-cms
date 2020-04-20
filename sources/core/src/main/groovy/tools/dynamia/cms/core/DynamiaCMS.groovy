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

import tools.dynamia.commons.logger.LoggingService
import tools.dynamia.commons.logger.SLF4JLoggingService
import tools.dynamia.domain.query.ApplicationParameters

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 *
 * @author Mario Serrano Leones
 */
class DynamiaCMS {

	private static final LoggingService logger = new SLF4JLoggingService(DynamiaCMS.class)

	private static final String PREFIX = "CMSConfig_"

	public static final String CFG_HOMEVAR = PREFIX + "HomePath"
	public static final String CFG_DEFAULT_TEMPLATE = PREFIX + "DefaultTemplate"
	public static final String CFG_SUPER_ADMIN_SITE = PREFIX + "SuperAdminSite"

	public static final String TEMPLATES = "templates"
	public static final String VIEWS = "views"
	public static final String MODULES = "modules"
	public static final String SITES = "sites"
	public static final String STATIC = "static"
	private static String path

	static Path getHomePath() {
		Path home = Paths.get(homePath())
		createDirectoryIfNotExists(home)
		return home
	}

	static Path getSitesResourceLocation() {
		Path path = Paths.get(homePath(), SITES)
		createDirectoryIfNotExists(path)
		return path
	}

	static Path getSitesResourceLocation(tools.dynamia.cms.core.domain.Site site) {
		Path path = Paths.get(homePath(), SITES, site.key)

		createDirectoryIfNotExists(path)
		return path
	}

	static Path getSitesStaticResourceLocation(tools.dynamia.cms.core.domain.Site site) {
		Path path = Paths.get(homePath(), SITES, site.key, STATIC)

		createDirectoryIfNotExists(path)
		return path
	}

	static Path getModulesLocation() {
		Path path = Paths.get(homePath(), MODULES)
		createDirectoryIfNotExists(path)
		return path
	}

	static Path getTemplatesLocation() {
		Path path = Paths.get(homePath(), TEMPLATES)
		createDirectoryIfNotExists(path)
		return path
	}

	static Path getViewLocation() {
		Path path = Paths.get(homePath(), VIEWS)
		createDirectoryIfNotExists(path)
		return path
	}

	private static String homePath() {
		if (path == null) {
			path = ApplicationParameters.get().getValue(CFG_HOMEVAR, System.getProperty("cms.home", "."))
		}
		return path
	}

	static void createDirectoryIfNotExists(Path path) {
		if (Files.notExists(path)) {
			try {
				Files.createDirectories(path)
			} catch (IOException ex) {
				logger.error("Error creating boot path " + path, ex)
			}
		}
	}

	static String[] getRelativeLocations() {
		return [ VIEWS, MODULES ]
	}

	static String[] getPrivateLocations() {
		return [ VIEWS, MODULES, TEMPLATES ]
	}

	static void reloadHomePath() {
		path = null
		initDefaultLocations()
	}

	static void initDefaultLocations() {
        homePath
        modulesLocation
        sitesResourceLocation
        templatesLocation
        viewLocation
	}

}
