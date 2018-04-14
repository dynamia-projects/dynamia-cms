package tools.dynamia.cms.admin.core.zk.ext

import org.zkoss.zul.A
import org.zkoss.zul.Label
import org.zkoss.zul.Vlayout
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManagerPreviewExtension

@Provider
class ViewResourceLinkPreviewExtension implements FileManagerPreviewExtension {

	@Override
    Object getView(FileInfo selectedFile) {
		if(selectedFile.isDirectory()){
			return null
        }
		

		Site site = SiteContext.get().getCurrent()
        Vlayout layout = new Vlayout()
        layout.appendChild(new Label("URL"))
        layout.setSclass("container")
        layout.setWidth("100%")

        A absolute = new A(CMSUtil.getResourceURL(site, selectedFile.getFile()))
        absolute.setHref(absolute.getLabel())
        absolute.setTarget("_blank")
        absolute.setStyle("word-wrap: break-word")
        absolute.setTooltiptext("Open in new tab")
        layout.appendChild(absolute)

        layout.appendChild(new Label("Relative Path"))
        A relative = new A(CMSUtil.getResourceRelativePath(site, selectedFile.getFile()))
        relative.setHref(absolute.getHref())
        relative.setTarget("_blank")
        relative.setStyle("word-wrap: break-word")
        relative.setTooltiptext("Open in new tab")
        layout.appendChild(relative)

        return layout

    }

}
