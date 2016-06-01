/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 *
 */
@Controller
public class ErrorCodeController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {

        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
       
        if (requestUri == null) {
            requestUri = request.getRequestURL() + "";
        }

        ModelAndView mv = new ModelAndView("error/exception");
        int statusCode = response.getStatus();
        if (statusCode == 404) {
            mv.setViewName("error/404");
        }

        mv.addObject("title", "Error");
        mv.addObject("statusCode", statusCode);
        mv.addObject("uri", requestUri);

        return mv;
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }

}
