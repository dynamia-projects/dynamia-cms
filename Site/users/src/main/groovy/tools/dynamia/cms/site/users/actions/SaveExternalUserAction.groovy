package tools.dynamia.cms.site.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.api.UserDTO
import tools.dynamia.cms.site.users.services.UserService

@CMSAction
public class SaveExternalUserAction implements SiteAction {

    @Autowired
    private UserService service;


    @Override
    public String getName() {
        return "saveExternalUser";
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        ModelAndView mv = new ModelAndView();
        CMSUtil.redirectHome(mv);


        UserDTO dto = (UserDTO) evt.getRequest().getSession().getAttribute("externalUser");
        if (dto != null) {

        }
    }
}
