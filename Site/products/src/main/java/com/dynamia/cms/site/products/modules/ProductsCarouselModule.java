package com.dynamia.cms.site.products.modules;

import com.dynamia.cms.site.core.api.AbstractModule;
import com.dynamia.cms.site.core.api.CMSModule;
import com.dynamia.cms.site.core.api.ModuleContext;
import com.dynamia.cms.site.core.domain.ModuleInstance;
import com.dynamia.cms.site.products.domain.Product;
import com.dynamia.cms.site.products.services.ProductsService;
import com.dynamia.tools.commons.collect.PagedList;
import com.dynamia.tools.domain.query.BooleanOp;
import com.dynamia.tools.domain.query.QueryConditions;
import com.dynamia.tools.domain.query.QueryParameters;
import com.dynamia.tools.domain.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mario on 18/11/2014.
 */
@CMSModule
public class ProductsCarouselModule extends AbstractModule {

    @Autowired
    private ProductsService service;

    @Autowired
    private CrudService crudService;

    public ProductsCarouselModule() {
        super("products_carousel", "Products Carousel", "products/modules/carousel");
        setDescription("Show a products list carousel");
        putMetadata("author", "Mario Serrano Leones");
        putMetadata("version", "1.0");
        putMetadata("created at", "18-11-2014");
        putMetadata("columns", "4");
        putMetadata("type", "featured");

    }

    @Override
    public void init(ModuleContext context) {
        ProductCarouselType type = ProductCarouselType.valueOf(getParameter(context, "type").toUpperCase());
        int columns = Integer.parseInt(getParameter(context, "columns"));
        String category = getParameter(context, "category");


        List<Product> products = new ArrayList<>();
        QueryParameters params = QueryParameters.with("site", context.getSite())
                .add("active", true)
                .paginate(columns * 4);

        switch (type) {
            case ALL:
                products = service.getSpecialProducts(context.getSite());
                break;
            case FEATURED:
                params.add("featured", true);
                break;
            case NEW:
                params.add("newproduct", true);
                break;
            case SALE:
                params.add("sale", true);
                break;
            case MOST_VIEWED:
                params.orderBy("views", false);
                break;
        }

        if (category != null && !category.isEmpty()) {
            try {
                Long categoryId = new Long(category);
                QueryParameters catParams = QueryParameters.with("category.id",QueryConditions.eq(categoryId, BooleanOp.OR))
                        .add("category.parent.id",QueryConditions.eq(categoryId,BooleanOp.OR));
                params.addGroup(catParams,BooleanOp.AND);
            } catch (NumberFormatException nf){

            }
        }

        if (products.isEmpty()) {
            products = crudService.find(Product.class, params);
        }

        if (products instanceof PagedList) {
            PagedList<Product> pagedList = (PagedList<Product>) products;
            products = pagedList.getDataSource().getPageData();
        }

        ModuleInstance mod = context.getModuleInstance();
        mod.addObject("products", products);
        mod.addObject("columns",columns);
    }

    private String getParameter(ModuleContext context, String name) {
        String param = context.getModuleInstance().getParameter(name);
        if (param == null || param.trim().isEmpty()) {
            Object metadata = getMetadata().get(name);
            if (metadata != null) {
                param = metadata.toString();
            }
        }

        if (param != null) {
            param = param.trim();
        }

        return param;
    }
}
