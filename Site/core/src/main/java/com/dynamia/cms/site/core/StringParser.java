/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.core;

import java.util.Map;

/**
 *
 * @author Dynamia Soluciones IT SAS and the original author or authors
 */
public interface StringParser {

    public String parse(String template, Map<String, Object> model);

}
