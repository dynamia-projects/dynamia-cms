package tools.dynamia.cms.admin.ui.ext

import org.zkoss.zul.A
import org.zkoss.zul.Label
import org.zkoss.zul.Vlayout
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.Site
import tools.dynamia.integration.sterotypes.Provider
import tools.dynamia.io.FileInfo
import tools.dynamia.modules.filemanager.FileManagerPreviewExtension

@Provider
class ViewResourceLinkPreviewExtension implements FileManagerPreviewExtension {

	@Override
    Object getView(FileInfo selectedFile) {
		if(selectedFile.directory){
			return null
        }
		

		Site site = SiteContext.get().current
        Vlayout layout = new Vlayout()
        layout.appendChild(new Label("URL"))
        layout.sclass = "container"
        layout.width = "100%"

        A absolute = new A(CMSUtil.getResourceURL(site, selectedFile.file))
        absolute.href = absolute.label
        absolute.target = "_blank"
        absolute.style = "word-wrap: break-word"
        absolute.tooltiptext = "Open in new tab"
        layout.appendChild(absolute)

        layout.appendChild(new Label("Relative Path"))
        A relative = new A(CMSUtil.getResourceRelativePath(site, selectedFile.file))
        relative.href = absolute.href
        relative.target = "_blank"
        relative.style = "word-wrap: break-word"
        relative.tooltiptext = "Open in new tab"
        layout.appendChild(relative)

        return layout

    }

}
