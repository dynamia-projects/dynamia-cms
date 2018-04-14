package tools.dynamia.cms.site.users.actions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.servlet.ModelAndView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.actions.ActionEvent
import tools.dynamia.cms.site.core.actions.SiteAction
import tools.dynamia.cms.site.core.api.CMSAction
import tools.dynamia.cms.site.users.api.UserDTO
import tools.dynamia.cms.site.users.services.UserService
import tools.dynamia.domain.services.CrudService

@CMSAction
class LoadExternalUserAction implements SiteAction {

    @Autowired
    private UserService service

    @Autowired
    private CrudService crudService


    @Override
    String getName() {
        return "loadExternalUser"
    }

    @Override
    void actionPerformed(ActionEvent evt) {

        ModelAndView mv = new ModelAndView("users/external")
        String id = evt.request.getParameter("id")


        if (id != null && !id.empty) {


            UserDTO dto = service.loadExternalUser(evt.site, id)
            if (dto != null) {
                mv.addObject("externalUser", dto)
                evt.request.session.setAttribute("externalUser", dto)
                CMSUtil.addSuccessMessage("Por favor verifique los datos antes de continuar", mv)
            } else {
                CMSUtil.addErrorMessage("No se encontro cliente con identificacion: " + id, mv)
            }

        } else {
            CMSUtil.addErrorMessage("Ingrese identificacion de cliente", mv)
        }

    }
}
