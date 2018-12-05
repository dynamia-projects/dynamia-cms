/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package tools.dynamia.cms.admin.ui

import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import tools.dynamia.cms.core.SiteContext
import tools.dynamia.cms.core.domain.ModuleInstance
import tools.dynamia.cms.pages.domain.Page
import tools.dynamia.cms.payment.api.PaymentTransactionStatus
import tools.dynamia.cms.products.domain.Product
import tools.dynamia.cms.products.domain.ProductCategory
import tools.dynamia.cms.products.services.ProductsService
import tools.dynamia.cms.shopping.admin.actions.ViewOrderDetailsAction
import tools.dynamia.cms.shoppingcart.domain.ShoppingOrder
import tools.dynamia.cms.users.domain.User
import tools.dynamia.cms.users.domain.UserLog
import tools.dynamia.commons.DateTimeUtils
import tools.dynamia.domain.query.QueryConditions
import tools.dynamia.domain.query.QueryParameters
import tools.dynamia.domain.services.AbstractService
import tools.dynamia.domain.util.LabelValue
import tools.dynamia.domain.util.QueryBuilder
import tools.dynamia.integration.Containers
import tools.dynamia.zk.addons.chartjs.CategoryChartjsData
import tools.dynamia.zk.navigation.ZKNavigationManager

/**
 * Simple VisualModel for admin dashboard
 * @author Mario Serrano Leones
 */
class AdminDashboardVM extends AbstractService {

    private ProductsService productsService = Containers.get().findObject(ProductsService)

    long modulesCount
    long pagesCount
    long usersCount
    long usersCountLastMonth

    long lastWeekShoppings
    long todayShoppings
    long productsCount
    long categoriesCount

    Date lastStoreUpdate
    List<Product> newProducts
    List<User> newUsers

    List<ShoppingOrder> newShoppingrOrders
    CategoryChartjsData lastMonthLogins

    final Date sinceLastWeek = DateTimeUtils.addDays(new Date(), -8)
    final Date sinceLastMonth = DateTimeUtils.addDays(new Date(), -30)

    @Init
    def init() {
        modulesCount = crudService().count(ModuleInstance, QueryParameters.with("enabled", true))
        pagesCount = crudService().count(Page, QueryParameters.with("published", true))
        loadUsersData()
        loadShoppingData()
        loadProductsData()

    }

    private void loadProductsData() {
        try {
            lastStoreUpdate = productsService.getSiteConfig(SiteContext.get().current).lastSync
        } catch (Exception e) {
        }
        productsCount = crudService().count(Product, QueryParameters.with("active", true))
        categoriesCount = crudService().count(ProductCategory, QueryParameters.with("active", true))
        newProducts = crudService().find(Product, QueryParameters.with("active", true)
                .add("creationDate", QueryConditions.geqt(sinceLastMonth))
                .add("site", SiteContext.get().current)
                .orderBy("creationDate", false))
    }

    private void loadUsersData() {
        usersCount = crudService().count(User, QueryParameters.with("enabled", true))
        newUsers = crudService().find(User, QueryParameters.with("creationDate", QueryConditions.geqt(sinceLastMonth))
                .add("enabled", false)
                .add("site", SiteContext.get().current)
                .orderBy("id", false))

        usersCountLastMonth = crudService().executeProjection(Long, "select count(l.id) from UserLog l where l.event = 'Login' and l.creationDate >= :date group by l.event", QueryParameters.with("date", sinceLastMonth))

        lastMonthLogins = new CategoryChartjsData()
        def query = QueryBuilder.select("DATE_FORMAT(l.creationDate,'%d/%m'),count(l.id)")
                .from(UserLog, "l").where("creationDate", QueryConditions.geqt(sinceLastMonth))
                .and("event", QueryConditions.eq("login"))
                .and("site", QueryConditions.eq(SiteContext.get().current))
                .groupBy("l.creationDate")
                .orderBy("l.creationDate")
                .resultType(LabelValue)


        def result = crudService().executeQuery(query)
        result.each {
            def data = it as LabelValue
            lastMonthLogins.add(data.label, data.value as Long)
        }
    }

    private void loadShoppingData() {

        lastWeekShoppings = crudService().count(ShoppingOrder,
                QueryParameters.with("creationDate", QueryConditions.geqt(sinceLastWeek))
                        .add("transaction.status", QueryConditions.in(PaymentTransactionStatus.COMPLETED, PaymentTransactionStatus.PROCESSING))
                        .add("site", SiteContext.get().current))

        todayShoppings = crudService().count(ShoppingOrder,
                QueryParameters.with("creationDate", new Date())
                        .add("transaction.status", QueryConditions.in(PaymentTransactionStatus.COMPLETED, PaymentTransactionStatus.PROCESSING))
                        .add("site", SiteContext.get().current))

        newShoppingrOrders = crudService().find(ShoppingOrder,
                QueryParameters.with("creationDate", QueryConditions.geqt(sinceLastMonth))
                        .add("transaction.status", QueryConditions.in(PaymentTransactionStatus.COMPLETED, PaymentTransactionStatus.PROCESSING))
                        .add("site", SiteContext.get().current)
                        .orderBy("id", false))
    }


    @Command
    def viewOrder(@BindingParam("order") ShoppingOrder order) {
        if (order != null) {
            order = crudService().reload(order)
            Containers.get().findObject(ViewOrderDetailsAction).view(order)
        }
    }

    static void show() {
        ZKNavigationManager.instance.navigateTo("content/dashboard")
    }

}
