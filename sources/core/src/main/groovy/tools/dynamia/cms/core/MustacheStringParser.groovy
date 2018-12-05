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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.core

import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Mario Serrano Leones
 */
@Component("mustacheStringParser")
class MustacheStringParser implements StringParser {

    private MustacheFactory factory

    @Autowired
    MustacheStringParser(MustacheFactory factory) {
        this.factory = factory
    }

    @Override
    String parse(String template, Map<String, Object> templateModel) {

        StringWriter writer = new StringWriter()

        Mustache mustache = factory.compile(new StringReader(template), "template")
        mustache.execute(writer, templateModel)
        writer.flush()

        return writer.toString()
    }

    @Override
    String getId() {
        return "mustache"
    }

    @Override
    String getName() {
        return "Mustache"
    }

    @Override
    String getDescription() {
        return "useful for simple templates"
    }

}
