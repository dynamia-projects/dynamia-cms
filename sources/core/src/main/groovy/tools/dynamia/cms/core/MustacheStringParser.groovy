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
