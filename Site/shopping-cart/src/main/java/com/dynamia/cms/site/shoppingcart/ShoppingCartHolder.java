/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.shoppingcart;

import com.dynamia.cms.site.shoppingcart.domains.ShoppingCart;
import com.dynamia.cms.site.users.UserHolder;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.sterotypes.Component;
import java.io.Serializable;
import org.springframework.context.annotation.Scope;

/**
 *
 * @author mario_2
 */
@Component
@Scope("session")
public class ShoppingCartHolder implements Serializable {

    private ShoppingCart current;

    public static ShoppingCartHolder get() {
        return Containers.get().findObject(ShoppingCartHolder.class);
    }

    public ShoppingCart getCurrent() {
        if (current == null) {
            current = new ShoppingCart();

            if (UserHolder.get().isAuthenticated()) {
                current.setUser(UserHolder.get().getCurrent());
            }
        }
        return current;
    }

}
