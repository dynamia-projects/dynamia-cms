/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core.thymeleaf;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;

/**
 *
 * @author mario
 */
public class FormatTextProcessor extends AbstractTextChildModifierAttrProcessor {

    public FormatTextProcessor() {
        super("formatText");
    }

    @Override
    protected String getText(Arguments arguments, Element element, String attributeName) {
        final Configuration configuration = arguments.getConfiguration();

        /*
         * Obtain the attribute value
         */
        final String attributeValue = element.getAttributeValue(attributeName);

        /*
         * Obtain the Thymeleaf Standard Expression parser
         */
        return "";
    }

    @Override
    public int getPrecedence() {
        return 10000;
    }

}
