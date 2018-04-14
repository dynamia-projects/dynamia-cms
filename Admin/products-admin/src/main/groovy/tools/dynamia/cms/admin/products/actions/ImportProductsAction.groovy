package tools.dynamia.cms.admin.products.actions

import org.apache.poi.ss.usermodel.Row
import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.ActionRenderer
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.admin.importer.ImportExcelAction
import tools.dynamia.cms.admin.importer.ImportUtils
import tools.dynamia.cms.admin.importer.ui.Importer
import tools.dynamia.cms.site.core.DynamiaCMS
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.core.domain.Site
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductBrand
import tools.dynamia.cms.site.products.domain.ProductCategory
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.commons.StringUtils
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.CrudService
import tools.dynamia.integration.ProgressMonitor
import tools.dynamia.zk.actions.ToolbarbuttonActionRenderer

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@InstallAction
class ImportProductsAction extends AbstractCrudAction {

    @Autowired
    private CrudService crudService

    ImportProductsAction() {
        setName("Import from Excel")
        setImage("export-xlsx")
    }

    @Override
    ActionRenderer getRenderer() {
        return new ToolbarbuttonActionRenderer(true)
    }

    @Override
    ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Product.class)
    }

    @Override
    CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ)
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        Importer importer = new Importer()
        importer.addAction(new ParseExcelAction())
        importer.addColumn("sku")
        importer.addColumn("reference")
        importer.addColumn("name")
        importer.addColumn("brand")
        importer.addColumn("category", "parentCategory")
        importer.addColumn("subcategory", "subcategory")
        importer.addColumn("status")
        importer.addColumn("cost")
        importer.addColumn("price")
        importer.addColumn("taxPercent")
        importer.addColumn("taxName")
        importer.addColumn("taxIncluded")
        importer.addColumn("stock")
        importer.addColumn("description")
        importer.addColumn("image")
        importer.addColumn("image2")
        importer.addColumn("image3")
        importer.addColumn("image4")
        importer.addColumn("externalRef")
        importer.show("Import Products from Excel")

    }

    class ParseExcelAction extends ImportExcelAction<Product> {

        @Override
        List<Product> importFromExcel(InputStream excelFile, ProgressMonitor monitor) throws Exception {
            Site site = SiteContext.get().getCurrent()
            return ImportUtils.importExcel(Product.class, excelFile, monitor, {
                System.out.println("Importing product - Row " + row.getRowNum())
                String sku = ImportUtils.getCellValue(row, 0)
                String ref = ImportUtils.getCellValue(row, 1)
                String name = ImportUtils.getCellValue(row, 2)
                Long externalRef = getExternalRef(row)

                Product p = findProduct(site, sku, ref, name, externalRef)
                if (p == null) {
                    p = new Product()
                }
                ImportUtils.tryToParse(row, p, "sku", "reference", "name", null, null, null, "status", "cost", "price",
                        "taxPercent", "taxName", "taxIncluded", "stock", "description", null, null, null, null,
                        "externalRef")

                if (!isValid(sku)) {
                    sku = System.currentTimeMillis() + ""
                }

                p.setSku(sku)
                p.setReference(ref)

                if (p.getDescription() != null && p.getDescription().length() > 1000) {
                    p.setLongDescription(p.getDescription())
                    p.setDescription(p.getDescription().substring(0, 999))
                }

                try {
                    p.setCost(new BigDecimal(ImportUtils.getCellValueObject(row, 7).toString()))
                } catch (Exception e) {
                }
                try {
                    Object priceValue = ImportUtils.getCellValueObject(row, 8)
                    if (priceValue != null) {
                        p.setPrice(new BigDecimal(priceValue.toString()))
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }

                try {
                    Object stock = ImportUtils.getCellValueObject(row, 12)
                    if (isValid(stock) && stock instanceof Number) {
                        p.setStock(((Number) stock).longValue())
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

                String brandName = ImportUtils.getCellValue(row, 3)
                String categoryName = ImportUtils.getCellValue(row, 4)
                String subcategoryName = ImportUtils.getCellValue(row, 5)
                try {

                    p.setBrand(getBrand(site, brandName))
                } catch (Exception e1) {
                    System.err.println("Error loading brand")
                }
                p.setCategory(getCategory(site, categoryName, subcategoryName))

                int[] imagesCols = [14, 15, 16, 17]
                for (int i = 0; i < imagesCols.length; i++) {
                    int col = imagesCols[i]
                    String imageURL = ImportUtils.getCellValue(row, col)
                    try {
                        String imageName = downloadImage(site, imageURL)
                        switch (i) {
                            case 0:
                                p.setImage(imageName)
                                break
                            case 1:
                                p.setImage2(imageName)
                                break
                            case 2:
                                p.setImage3(imageName)
                                break
                            case 3:
                                p.setImage4(imageName)
                                break

                            default:
                                break
                        }
                    } catch (Exception e) {
                        System.err.println("Cannot download image " + i + e.getMessage())
                    }
                }

                if (p.getCategory() == null) {
                    p = null
                }

                return p
            })

        }

        private ProductCategory getCategory(Site site, String categoryName, String subcategoryName) {
            QueryParameters params = new QueryParameters()
            params.setAutocreateSearcheableStrings(false)
            if (isValid(categoryName) && isValid(subcategoryName)) {
                params.add("name", subcategoryName)
                params.add("parent.name", categoryName)
            } else if (isValid(categoryName)) {
                params.add("name", categoryName)
            }
            if (!params.isEmpty()) {
                params.add("site", site)
                ProductCategory category = crudService.findSingle(ProductCategory.class, params)

                if (category == null) {
                    if (isValid(categoryName) && isValid(subcategoryName)) {
                        category = new ProductCategory()
                        category.setName(subcategoryName)
                        category.setSite(site)

                        ProductCategory parent = findCategory(site, categoryName)
                        category.setParent(parent)

                        crudService.create(category)
                    } else if (isValid(categoryName)) {
                        category = findCategory(site, categoryName)
                    }
                }

                return category
            }

            return null
        }

        private ProductCategory findCategory(Site site, String categoryName) {
            ProductCategory cat = crudService.findSingle(ProductCategory.class, "name", categoryName)
            if (cat == null) {
                cat = new ProductCategory()
                cat.setName(categoryName)
                cat.setSite(site)
                crudService.create(cat)
            }
            return cat
        }

        private ProductBrand getBrand(Site site, String brandName) {
            if (isValid(brandName)) {
                ProductBrand brand = crudService.findSingle(ProductBrand.class,
                        QueryParameters.with("site", site).add("name", QueryConditions.eq(brandName)))

                if (brand == null) {
                    brand = new ProductBrand()
                    brand.setName(brandName)
                    brand.setSite(site)
                    crudService.create(brand)
                }
                return brand
            } else {
                return null
            }

        }

        private Product findProduct(Site site, String sku, String ref, String name, Long externalRef) {
            QueryParameters params = new QueryParameters()
            params.setAutocreateSearcheableStrings(false)
            if (isValid(sku)) {
                params.add("sku", sku)
            }
            if (isValid(ref)) {
                params.add("reference", ref)
            }

            if (isValid(name)) {
                params.add("name", name)
            }

            if (isValid(externalRef)) {
                params.add("externalRef", externalRef)
            }

            if (!params.isEmpty()) {
                params.add("site", site)
                return crudService.findSingle(Product.class, params)
            }

            return null
        }

        private boolean isValid(Object value) {
            return value != null && !value.toString().trim().isEmpty()
        }

        private Long getExternalRef(Row row) {
            try {
                return Long.valueOf(ImportUtils.getCellValue(row, 18))
            } catch (NumberFormatException e) {
                return null
            }
        }

        String downloadImage(Site site, String imageURL) throws Exception {

            String localFolder = DynamiaCMS.getSitesResourceLocation(site)
                    .resolve("products" + File.separator + "images").toString()

            if (imageURL == null || imageURL.isEmpty()) {
                throw new IllegalArgumentException("No image URL to download")
            }

            final String imageName = System.currentTimeMillis() + "." + StringUtils.getFilenameExtension(imageURL)
            final URL url = new URL(imageURL)
            final Path folder = Paths.get(localFolder)
            final Path localFile = folder.resolve(imageName)

            if (Files.notExists(folder)) {
                Files.createDirectories(folder)
            }

            try {
                url.openStream().withCloseable {
                    Files.copy(it, localFile, StandardCopyOption.REPLACE_EXISTING)
                }

            } catch (Exception ex) {

            }

            return imageName

        }

    }
}
