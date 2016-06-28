/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.site.core;

import java.util.Map;
import java.util.Optional;
import tools.dynamia.cms.site.core.api.TypeExtension;
import tools.dynamia.integration.Containers;

/**
 *
 * @author Dynamia Soluciones IT SAS and the original author or authors
 */
public interface StringParser extends TypeExtension {

    public String parse(String template, Map<String, Object> model);

    /**
     * Find and return StringParse by id, if not parse found Mustache is used as
     * default
     *
     * @param id
     * @return
     */
    public static StringParser get(String id) {
        if (id != null) {
            Optional<StringParser> parser = Containers.get().findObjects(StringParser.class)
                    .stream().filter(p -> p.getId().equals(id))
                    .findFirst();

            if (parser.isPresent()) {
                return parser.get();
            }
        }
        return Containers.get().findObject(MustacheStringParser.class);
    }

}
