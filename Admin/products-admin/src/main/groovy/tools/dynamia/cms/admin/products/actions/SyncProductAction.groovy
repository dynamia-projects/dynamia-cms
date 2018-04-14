package tools.dynamia.cms.admin.products.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.site.core.SiteContext
import tools.dynamia.cms.site.products.api.ProductsDatasource
import tools.dynamia.cms.site.products.domain.Product
import tools.dynamia.cms.site.products.domain.ProductsSiteConfig
import tools.dynamia.cms.site.products.dto.ProductDTO
import tools.dynamia.cms.site.products.services.ProductsService
import tools.dynamia.cms.site.products.services.ProductsSyncService
import tools.dynamia.commons.ApplicableClass
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.crud.CrudState
import tools.dynamia.ui.MessageType
import tools.dynamia.ui.UIMessages

@InstallAction
public class SyncProductAction extends AbstractCrudAction {

    @Autowired
    private ProductsService service;

    @Autowired
    private ProductsSyncService syncService;

    public SyncProductAction() {
        setName("Sync");
        setImage("refresh");
    }

    @Override
    public void actionPerformed(CrudActionEvent evt) {

        Product product = (Product) evt.getData();
        if (product != null) {

            if (product.getExternalRef() == null) {
                UIMessages.showMessage("Cannot sync product, external reference not found", MessageType.ERROR);
                return;
            }

            ProductsSiteConfig cfg = service.getSiteConfig(SiteContext.get().getCurrent());
            if (cfg != null) {
                ProductsDatasource datasource = syncService.getDatasource(cfg);
                if (datasource != null) {
                    ProductDTO remoteProduct = datasource.getProduct(product.getExternalRef(), cfg.getParametersAsMap());
                    if (remoteProduct != null) {
                        String stage = "";
                        try {
                            stage = "general info";
                            syncService.synchronizeProduct(cfg, remoteProduct);
                            stage = "details";
                            syncService.syncProductDetails(cfg, remoteProduct);
                            stage = "stock Details";
                            syncService.syncProductStockDetails(cfg, remoteProduct);
                            stage = "image download";
                            syncService.downloadProductImages(cfg, remoteProduct);
                            UIMessages.showMessage(product + " sync successfully");

                        } catch (Exception e) {
                            UIMessages.showMessage("Error syncing product: " + stage + ": " + e.getMessage(), MessageType.ERROR);
                            e.printStackTrace();
                        }
                    } else {
                        UIMessages.showMessage("No remove product found", MessageType.WARNING);
                    }
                } else {
                    UIMessages.showMessage("Products Datasource not configured", MessageType.WARNING);
                }
            } else {
                UIMessages.showMessage("Site products configuration not found", MessageType.WARNING);
            }

        } else {
            UIMessages.showMessage("Select product first", MessageType.WARNING);
        }
    }

    @Override
    public ApplicableClass[] getApplicableClasses() {
        return ApplicableClass.get(Product.class);
    }

    @Override
    public CrudState[] getApplicableStates() {
        return CrudState.get(CrudState.READ);
    }
}
