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
package tools.dynamia.cms.site.core

import tools.dynamia.cms.site.core.domain.Site
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

	static Path getSitesResourceLocation(Site site) {
		Path path = Paths.get(homePath(), SITES, site.key)

		createDirectoryIfNotExists(path)
		return path
	}

	static Path getSitesStaticResourceLocation(Site site) {
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
				logger.error("Error creating DynamiaCMS path " + path, ex)
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
