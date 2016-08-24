package tools.dynamia.cms.site.templates;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.metainfo.PageDefinitions;
import org.zkoss.zk.ui.sys.RequestInfo;

import tools.dynamia.commons.StringUtils;
import tools.dynamia.integration.sterotypes.Provider;
import tools.dynamia.zk.ui.PageDefinitionLoader;

@Provider
public class SiteZKPageDefinitionLoader implements PageDefinitionLoader {

	private Map<String, PageDefinition> cache = new HashMap<>();

	@Override
	public PageDefinition getPageDefinition(RequestInfo ri, String path) {
		HttpServletRequest request = (HttpServletRequest) ri.getNativeRequest();
		String realPath = (String) request.getAttribute("realPath");

		if (realPath == null) {
			realPath = request.getParameter("realPath");
		}

		Boolean cacheable = (Boolean) request.getAttribute("cacheable");

		PageDefinition page = cache.get(realPath);

		if (cacheable != Boolean.TRUE) {
			page = null;
		}

		if (page == null) {
			try {
				Reader reader = new InputStreamReader(new FileInputStream(realPath));
				String ext = StringUtils.getFilenameExtension(realPath);

				page = PageDefinitions.getPageDefinitionDirectly(ri.getWebApp(), ri.getLocator(), reader, ext);

				cache.put(realPath, page);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return page;
	}

}
