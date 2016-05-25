/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.core;

import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Mario Serrano Leones
 */
@Component("mustacheStringParser")
public class MustacheStringParser implements StringParser {

    private MustacheFactory factory;

    @Autowired
    public MustacheStringParser(MustacheFactory factory) {
        this.factory = factory;
    }

    @Override
    public String parse(String template, Map<String, Object> templateModel) {

        StringWriter writer = new StringWriter();

        Mustache mustache = factory.compile(new StringReader(template), "template");
        mustache.execute(writer, templateModel);
        writer.flush();

        return writer.toString();
    }

}
