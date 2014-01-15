/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.thymeleaf;

import com.dynamia.tools.commons.BeanUtils;
import com.dynamia.tools.integration.Containers;
import com.dynamia.tools.integration.ObjectMatcher;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

/**
 *
 * @author mario
 */
public class DynamiaCMSDialect extends AbstractDialect {

    @Override
    public String getPrefix() {
        return "cms";
    }

    @Override
    public boolean isLenient() {
        return true;
    }

    @Override
    public Set<IProcessor> getProcessors() {
        Collection<IProcessor> result = Containers.get().findObjects(IProcessor.class, new ObjectMatcher<IProcessor>() {

            @Override
            public boolean match(IProcessor object) {
                return BeanUtils.isAnnotated(TemplateProccesor.class, object.getClass());
            }
        });

        Set<IProcessor> processors = new HashSet<>(result);
        return processors;
    }

}
