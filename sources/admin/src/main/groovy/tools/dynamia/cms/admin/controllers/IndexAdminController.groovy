package tools.dynamia.cms.admin.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.annotation.Secured
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.View
import org.springframework.web.servlet.view.InternalResourceView
import tools.dynamia.app.ApplicationInfo
import tools.dynamia.zk.navigation.ZKNavigationManager

@Controller
class IndexAdminController {

    @Autowired
    private ApplicationInfo applicationInfo

    IndexAdminController() {
        println "Starting CMS Admin Controller"
    }

    @GetMapping("/cms-admin")
    @Secured(["ROLE_ADMIN", "ROLE_EDITOR"])
    ModelAndView cmsAdmin() {
        ZKNavigationManager.instance.autoSyncClientURL = false
        def mv = new ModelAndView()
        mv.view = new InternalResourceView("/zkau/web/templates/${applicationInfo.template.toLowerCase()}/views/index.zul")
        mv.addObject("contextPath", "/cms-admin")
        return mv
    }
}
