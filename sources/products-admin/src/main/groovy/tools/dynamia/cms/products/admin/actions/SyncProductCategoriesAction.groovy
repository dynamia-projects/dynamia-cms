package tools.dynamia.cms.products.admin.actions

import org.springframework.beans.factory.annotation.Autowired
import tools.dynamia.actions.InstallAction
import tools.dynamia.cms.products.domain.ProductsSiteConfig
import tools.dynamia.cms.products.services.ProductsSyncService
import tools.dynamia.crud.AbstractCrudAction
import tools.dynamia.crud.CrudActionEvent
import tools.dynamia.ui.UIMessages

@InstallAction
class SyncProductCategoriesAction extends AbstractCrudAction {

    @Autowired
    private ProductsSyncService service

    SyncProductCategoriesAction() {
        name = "Sync Cats"
        applicableClass = ProductsSiteConfig
    }

    @Override
    void actionPerformed(CrudActionEvent evt) {
        def config = evt.data as ProductsSiteConfig
        if (config) {
            def categories = service.synchronizeCategories(config)
            service.syncCategoriesDetails(config, categories)
            UIMessages.showMessage("Categories synced successfully")
        }
    }
}
