package tools.dynamia.cms.site.products.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import tools.dynamia.cms.site.core.CMSUtil;
import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.core.services.SiteService;
import tools.dynamia.cms.site.pages.PageNotFoundException;
import tools.dynamia.cms.site.products.ProductsReviewForm;
import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductReview;
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig;
import tools.dynamia.cms.site.products.dto.ProductsReviewResponse;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.users.UserHolder;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
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
            try {
                service.computeProductStars(product);
            } catch (Exception e) {
                e.printStackTrace();
            }

            CMSUtil.addSuccessMessage("Gracias por escribir una reseña de este producto", redirectAttributes);
        } catch (ValidationError e) {
            CMSUtil.addErrorMessage(e.getMessage(), redirectAttributes);
        }

        return mv;
    }

    @GetMapping("/{id}/reviews")
    public List<ProductReview> getReviews(@PathVariable Long id,
                                          @RequestParam(required = false, defaultValue = "30") int max, HttpServletRequest request) {
        Product product = crudService.find(Product.class, id);

        if (product == null || !product.getSite().equals(siteService.getSite(request))) {
            throw new PageNotFoundException("Producto no encontrado");
        }

        return service.getTopReviews(product, max);
    }

    @GetMapping("/reviews/{requestUuid}")
    public ModelAndView checkExternalReviews(@PathVariable String requestUuid, HttpServletRequest request, HttpServletResponse httpResponse) {

        ModelAndView mv = new ModelAndView("products/reviews/external");

        Site site = siteService.getSite(request);

        if (site == null) {
            CMSUtil.redirectHome(mv);
            return mv;
        }
        ProductsSiteConfig config = service.getSiteConfig(site);

        ProductsReviewResponse response = service.requestExternalReviews(config, requestUuid);

        if (response.isAccepted()) {
            User user = service.findUserForReview(site, response);
            mv.addObject("user", user);

            List<ProductReview> reviews = service.getExternalProductReviews(site, response, user);

            mv.addObject("reviewsForm", new ProductsReviewForm(reviews));
            mv.addObject("title", response.getDescription());

            if (reviews.isEmpty()) {
                mv.addObject("message", "No hay reseñas pendientes");
                response = ProductsReviewResponse.rejected();
            }

        } else {
            mv.addObject("message", "No se encontraron reseñas de productos pendientes");
            response = ProductsReviewResponse.rejected();
        }

        mv.addObject("response", response);

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setDateHeader("Expires", 0);
        return mv;
    }


    @PostMapping("/reviews/create")
    public ModelAndView saveMultipleReviews(@ModelAttribute("reviewsForm") ProductsReviewForm form,
                                            BindingResult result, HttpServletRequest request, HttpServletResponse httpResponse) {

        Site site = siteService.getSite(request);

        ModelAndView mv = new ModelAndView("products/reviews/external");

        if (form != null && form.getReviews() != null) {
            for (ProductReview review : form.getReviews()) {
                try {
                    System.out.println("Saving review Product ID: " + review.getProduct().getId());
                    review.setSite(site);
                    if (review.getComment() == null || review.getComment().isEmpty()) {
                        review.setIncomplete(true);
                        review.setVerified(false);
                    } else {
                        review.setVerified(true);
                        review.setIncomplete(false);
                        review.setCreationDate(new Date());
                    }
                    crudService.executeWithinTransaction(() -> crudService.save(review));
                    service.computeProductStars(review.getProduct());
                    System.out.println("OK");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        mv.addObject("response", ProductsReviewResponse.rejected());
        mv.addObject("message", "Gracias por enviar sus reseñas");
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpResponse.setDateHeader("Expires", 0);
        return mv;
    }

    @GetMapping("/reviews")
    public ModelAndView reviews(HttpServletRequest request, HttpServletResponse httpResponse) {

        ModelAndView mv = new ModelAndView("products/reviews/external");

        Site site = siteService.getSite(request);

        if (site == null) {
            CMSUtil.redirectHome(mv);
            return mv;
        }

        if (UserHolder.get().isAuthenticated()) {


            User user = UserHolder.get().getCurrent();
            List<ProductReview> reviews = service.getIncompleteProductReviews(user);
            mv.addObject("user", user);
            mv.addObject("reviewsForm", new ProductsReviewForm(reviews));
            mv.addObject("title", reviews.isEmpty() ? "No tiene reseñas pendientes" : reviews.size() + " reseñas pendientes");

            mv.addObject("response", new ProductsReviewResponse(Collections.emptyList(), user.getUsername(), user.getFullName()));

            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            httpResponse.setDateHeader("Expires", 0);
        } else {
            mv.addObject("response", ProductsReviewResponse.rejected());
            mv.addObject("message", "Inicio sesion para cargar reseñas");
        }
        return mv;
    }

}
