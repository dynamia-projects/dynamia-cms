package tools.dynamia.cms.site.products;

import tools.dynamia.cms.site.products.domain.Product;
import tools.dynamia.cms.site.products.domain.ProductCategory;
import tools.dynamia.commons.collect.HashSetMultiMap;
import tools.dynamia.commons.collect.MultiMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProductCategoryGroup implements Serializable {

    private ProductCategory category;
    private List<Product> products;

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public static List<ProductCategoryGroup> groupByCategory(List<Product> products) {
        List<ProductCategoryGroup> groups = new ArrayList<>();
        if (products != null) {
            MultiMap<ProductCategory, Product> maps = new HashSetMultiMap<>();
            products.forEach(p -> maps.put(p.getCategory(), p));


            maps.forEach((k, v) -> {
                ProductCategoryGroup grp = new ProductCategoryGroup();
                grp.setCategory(k);
                grp.setProducts(new ArrayList<>(v));
                groups.add(grp);
            });

            groups.sort(Comparator.comparing(g -> g.getCategory().getName()));
        }
        return groups;
    }


}
