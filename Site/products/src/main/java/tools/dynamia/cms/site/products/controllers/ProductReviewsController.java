package tools.dynamia.cms.site.products.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.pages.PageNotFoundException;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductReview;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by mario on 12/07/2017.
 */
@Controller
@RequestMapping("/store/products/")
public class ProductReviewsController {


    private final SiteService siteService;

    private final ProductsService service;

    private final CrudService crudService;

    @Autowired
    public ProductReviewsController(SiteService siteService, ProductsService service, CrudService crudService) {
        this.siteService = siteService;
        this.service = service;
        this.crudService = crudService;
    }


    @RequestMapping(value = "/{id}/reviews/create", method = RequestMethod.POST)
    public ModelAndView saveAddress(@PathVariable Long id, @RequestParam String comment, @RequestParam int star,
                                    HttpServletRequest request, RedirectAttributes redirectAttributes) {


        Product product = crudService.find(Product.class, id);

        if (product == null || !product.getSite().equals(siteService.getSite(request))) {
            throw new PageNotFoundException("Producto no encontrado");
        }


        ModelAndView mv = new ModelAndView();

        String redirect = request.getParameter("currentURI");

        if (redirect == null || redirect.isEmpty()) {
            redirect = "/store/products/" + id;
        }

        mv.setView(new RedirectView(redirect, true, true, false));


        try {
            service.saveReview(product, comment, star);
            CMSUtil.addSuccessMessage("Gracias por escribir una reseña de este producto", redirectAttributes);
        } catch (ValidationError e) {
            CMSUtil.addErrorMessage(e.getMessage(), redirectAttributes);
        }


        return mv;
    }

    @GetMapping("/{id}/reviews")
    public List<ProductReview> getReviews(@PathVariable Long id, @RequestParam(required = false, defaultValue = "30") int max, HttpServletRequest request){
        Product product = crudService.find(Product.class, id);

        if (product == null || !product.getSite().equals(siteService.getSite(request))) {
            throw new PageNotFoundException("Producto no encontrado");
        }



        return service.getTopReviews(product,max);
    }
}
