/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.ext;

import com.dynamia.cms.site.core.CMSUtil;
import com.dynamia.cms.site.core.api.CMSExtension;
import com.dynamia.cms.site.core.api.SiteRequestInterceptorAdapter;
import com.dynamia.cms.site.core.domain.Site;
import com.dynamia.cms.site.core.domain.SiteParameter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author mario
 */
@CMSExtension
public class UtilsVarSiteInterceptor extends SiteRequestInterceptorAdapter {

    private CMSUtil util = new CMSUtil();

    @Override
    protected void afterRequest(Site site, ModelAndView modelAndView) {
        modelAndView.addObject("site", site);
        modelAndView.addObject("siteParams", createParams(site));
        modelAndView.addObject("cmsUtil", util);
    }

    private Object createParams(Site site) {
        Map<String, String> map = new HashMap<>();
        try {
            for (SiteParameter p : site.getParameters()) {
                map.put(p.getName(), p.getValue());
            }
        } catch (Exception e) {
        }
        return map;
    }

}
