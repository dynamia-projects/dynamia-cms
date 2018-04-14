package tools.dynamia.cms.site.products.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import tools.dynamia.cms.site.core.CMSUtil
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.core.services.SiteService
import tools.dynamia.cms.site.pages.PageNotFoundException
import tools.dynamia.cms.site.products.ProductsReviewForm
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductReview
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.dto.ProductsReviewResponse
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.users.UserHolder
import tools.dynamia.cms.site.users.domain.User
import tools.dynamia.domain.ValidationError
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.sterotypes.Controller

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by mario on 12/07/2017.
 */
@Controller
@RequestMapping("/store/products/")
class ProductReviewsController {

    private final SiteService siteService

    private final ProductsService service

    private final CrudService crudService

    @Autowired
    ProductReviewsController(SiteService siteService, ProductsService service, CrudService crudService) {
        this.siteService = siteService
        this.service = service
        this.crudService = crudService
    }

    @RequestMapping(value = "/{id}/reviews/create", method = RequestMethod.POST)
    ModelAndView saveAddress(@PathVariable Long id, @RequestParam String comment, @RequestParam int star,
                             HttpServletRequest request, RedirectAttributes redirectAttributes) {

        Product product = crudService.find(Product.class, id)

        if (product == null || !product.site.equals(siteService.getSite(request))) {
            throw new PageNotFoundException("Producto no encontrado")
        }

        ModelAndView mv = new ModelAndView()

        String redirect = request.getParameter("currentURI")

        if (redirect == null || redirect.empty) {
            redirect = "/store/products/" + id
        }

        mv.view = new RedirectView(redirect, true, true, false)

        try {
            service.saveReview(product, comment, star)
            try {
                service.computeProductStars(product)
            } catch (Exception e) {
                e.printStackTrace()
            }

            CMSUtil.addSuccessMessage("Gracias por escribir una reseña de este producto", redirectAttributes)
        } catch (ValidationError e) {
            CMSUtil.addErrorMessage(e.message, redirectAttributes)
        }

        return mv
    }

    @GetMapping("/{id}/reviews")
    List<ProductReview> getReviews(@PathVariable Long id,
                                   @RequestParam(required = false, defaultValue = "30") int max, HttpServletRequest request) {
        Product product = crudService.find(Product.class, id)

        if (product == null || !product.site.equals(siteService.getSite(request))) {
            throw new PageNotFoundException("Producto no encontrado")
        }

        return service.getTopReviews(product, max)
    }

    @GetMapping("/reviews/{requestUuid}")
    ModelAndView checkExternalReviews(
            @PathVariable String requestUuid, HttpServletRequest request, HttpServletResponse httpResponse) {

        ModelAndView mv = new ModelAndView("products/reviews/external")

        Site site = siteService.getSite(request)

        if (site == null) {
            CMSUtil.redirectHome(mv)
            return mv
        }
        ProductsSiteConfig config = service.getSiteConfig(site)

        ProductsReviewResponse response = service.requestExternalReviews(config, requestUuid)

        if (response.accepted) {
            User user = service.findUserForReview(site, response)
            mv.addObject("user", user)

            List<ProductReview> reviews = service.getExternalProductReviews(site, response, user)

            mv.addObject("reviewsForm", new ProductsReviewForm(reviews))
            mv.addObject("title", response.description)

            if (reviews.empty) {
                mv.addObject("message", "No hay reseñas pendientes")
                response = ProductsReviewResponse.rejected()
            }

        } else {
            mv.addObject("message", "No se encontraron reseñas de productos pendientes")
            response = ProductsReviewResponse.rejected()
        }

        mv.addObject("response", response)

        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
        httpResponse.setDateHeader("Expires", 0)
        return mv
    }


    @PostMapping("/reviews/create")
    ModelAndView saveMultipleReviews(@ModelAttribute("reviewsForm") ProductsReviewForm form,
                                     BindingResult result, HttpServletRequest request, HttpServletResponse httpResponse) {

        Site site = siteService.getSite(request)

        ModelAndView mv = new ModelAndView("products/reviews/external")

        if (form != null && form.reviews != null) {
            for (ProductReview review : (form.reviews)) {
                try {
                    System.out.println("Saving review Product ID: " + review.product.id)
                    review.site = site
                    if (review.comment == null || review.comment.empty) {
                        review.incomplete = true
                        review.verified = false
                    } else {
                        review.verified = true
                        review.incomplete = false
                        review.creationDate = new Date()
                    }
                    crudService.executeWithinTransaction { crudService.save(review) }
                    service.computeProductStars(review.product)
                    System.out.println("OK")
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
        }
        mv.addObject("response", ProductsReviewResponse.rejected())
        mv.addObject("message", "Gracias por enviar sus reseñas")
        httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
        httpResponse.setDateHeader("Expires", 0)
        return mv
    }

    @GetMapping("/reviews")
    ModelAndView reviews(HttpServletRequest request, HttpServletResponse httpResponse) {

        ModelAndView mv = new ModelAndView("products/reviews/external")

        Site site = siteService.getSite(request)

        if (site == null) {
            CMSUtil.redirectHome(mv)
            return mv
        }

        if (UserHolder.get().authenticated) {


            User user = UserHolder.get().current
            List<ProductReview> reviews = service.getIncompleteProductReviews(user)
            mv.addObject("user", user)
            mv.addObject("reviewsForm", new ProductsReviewForm(reviews))
            mv.addObject("title", reviews.empty ? "No tiene reseñas pendientes" : reviews.size() + " reseñas pendientes")

            mv.addObject("response", new ProductsReviewResponse(Collections.emptyList(), user.username, user.fullName))

            httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate")
            httpResponse.setDateHeader("Expires", 0)
        } else {
            mv.addObject("response", ProductsReviewResponse.rejected())
            mv.addObject("message", "Inicio sesion para cargar reseñas")
        }
        return mv
    }

}
