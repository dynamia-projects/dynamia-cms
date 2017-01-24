/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.shoppingcart.services;

import java.util.List;

import javax.annotation.PostConstruct;

import tools.dynamia.cms.site.core.domain.Site;
import tools.dynamia.cms.site.payment.domain.ManualPayment;
import tools.dynamia.cms.site.payment.domain.enums.PaymentTransactionStatus;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCart;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingCartItem;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingOrder;
import tools.dynamia.cms.site.shoppingcart.domain.ShoppingSiteConfig;
import tools.dynamia.cms.site.users.domain.User;
import tools.dynamia.domain.ValidationError;
import tools.dynamia.domain.query.QueryParameters;
import tools.dynamia.web.util.HttpRemotingServiceClient;
import toosl.dynamia.cms.site.shoppingcart.api.ManualPaymentSender;
import toosl.dynamia.cms.site.shoppingcart.api.ShoppingOrderSender;
import toosl.dynamia.cms.site.shoppingcart.dto.ShoppingOrderDTO;

/**
 *
 * @author Mario Serrano Leones
 */
public interface ShoppingCartService {

    public ShoppingCartItem getItem(Site site, String code);

	public abstract ShoppingSiteConfig getConfiguration(Site site);

	public ShoppingOrder createOrder(ShoppingCart shoppingCart, ShoppingSiteConfig config);

	public abstract void cancelOrder(ShoppingOrder order);

	public abstract void saveOrder(ShoppingOrder order);

	public abstract void notifyOrderCompleted(ShoppingOrder order);

	public abstract void shipOrder(ShoppingOrder shoppingOrder);

	public abstract void notifyOrderShipped(ShoppingOrder order);

	public List<ShoppingOrder> getOrders(User current);

	void sendOrder(ShoppingOrder order);

	void sendAllOrders();

	void sendAllPayments();

	public List<ManualPayment> getManualPayments(User user);

}
