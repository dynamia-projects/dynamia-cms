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
import tools.dynamia.cms.site.products.dto.ProductDTO;
import tools.dynamia.cms.site.products.dto.ProductsReviewResponse;
import tools.dynamia.cms.site.products.services.ProductsService;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.services.CrudService;
import tools.dynamia.integration.sterotypes.Controller;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    public ModelAndView checkExternalReviews(@PathVariable String requestUuid, HttpServletRequest requet) {

        ModelAndView mv = new ModelAndView("products/reviews/external");

        Site site = siteService.getSite(requet);

        if (site == null) {
            CMSUtil.redirectHome(mv);
            return mv;
        }
        ProductsSiteConfig config = service.getSiteConfig(site);

        ProductsReviewResponse response = service.requestExternalReviews(config, requestUuid);

        if (response.isAccepted()) {
            User user = service.findUserForReview(site, response);
            mv.addObject("user", user);

            List<ProductReview> reviews = new ArrayList<>();

            if (response.getProducts() != null) {
                for (ProductDTO dto : response.getProducts()) {
                    Product product = service.getProduct(site, dto);
                    if (product != null) {
                        ProductReview review = service.getUserReview(product, user);

                        if (review == null) {
                            review = new ProductReview();
                            review.setSite(site);
                            review.setUser(user);
                            review.setProduct(product);
                            review.setDocument(response.getDocument());
                            review.setComment(".");
                            review.setIncomplete(true);
                            review = crudService.create(review);
                        }

                        if (review.isIncomplete()) {
                            reviews.add(review);
                        }
                    }

                }
            }

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

        return mv;
    }

    @PostMapping("/reviews/create")
    public ModelAndView saveMultipleReviews(@ModelAttribute("reviewsForm") ProductsReviewForm form,
                                            BindingResult result, HttpServletRequest request) {

        Site site = siteService.getSite(request);

        ModelAndView mv = new ModelAndView("products/reviews/external");

        if (form != null && form.getReviews() != null) {
            for (ProductReview review : form.getReviews()) {
                try {
                    System.out.println("Saving review Product ID: " + review.getProduct().getId());
                    review.setSite(site);
                    if (review.getComment() == null || review.getComment().isEmpty()
                            || review.getComment().equals(".")) {
                        review.setIncomplete(true);
                        review.setComment(".");
                        review.setVerified(false);
                    } else {
                        review.setVerified(true);
                        review.setIncomplete(false);
                        review.setCreationDate(new Date());
                    }
                    crudService.save(review);
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

        return mv;
    }
}
