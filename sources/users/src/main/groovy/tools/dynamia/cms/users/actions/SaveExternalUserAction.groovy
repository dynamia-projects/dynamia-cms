package tools.dynamia.cms.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.core.CMSUtil
import tools.dynamia.cms.core.actions.ActionEvent
import tools.dynamia.cms.core.actions.SiteAction
import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.api.UserDTO
import tools.dynamia.cms.users.services.UserService

@CMSAction
class SaveExternalUserAction implements SiteAction {

    @Autowired
    private UserService service


    @Override
    String getName() {
        return "saveExternalUser"
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        ModelAndView mv = new ModelAndView()
        CMSUtil.redirectHome(mv)


        UserDTO dto = (UserDTO) evt.request.session.getAttribute("externalUser")
        if (dto != null) {

        }
    }
}
