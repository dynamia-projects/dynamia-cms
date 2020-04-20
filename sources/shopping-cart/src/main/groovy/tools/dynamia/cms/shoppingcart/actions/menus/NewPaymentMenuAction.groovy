/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package tools.dynamia.cms.shoppingcart.actions.menus

import tools.dynamia.cms.core.api.CMSAction
import tools.dynamia.cms.users.actions.UserMenuActionEnableable
import tools.dynamia.cms.users.api.UserProfile
import tools.dynamia.cms.users.domain.User

@CMSAction
class NewPaymentMenuAction implements UserMenuActionEnableable {

    @Override
    String getLabel() {
        return "Registrar Pago"
    }

    @Override
    String getDescription() {
        return "Registrar Pago Manual"
    }

    @Override
    String getIcon() {
        // TODO Auto-generated method stub
        return null
    }

    @Override
    int getOrder() {
        return 6
    }

    @Override
    String action() {
        return "shoppingcart/newpayment"
    }

    @Override
    boolean isEnabled(User currentUser) {

        return currentUser.site.corporateSite && currentUser.site.isParameter("manualPayments") && currentUser.profile == UserProfile.SELLER
    }

}
