/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.dynamia.cms.core

import tools.dynamia.integration.Containers

/**
 *
 * @author Dynamia Soluciones IT SAS and the original author or authors
 */
interface StringParser extends tools.dynamia.cms.core.api.TypeExtension {

    String parse(String template, Map<String, Object> model)


}

abstract class StringParsers {
    /**
     * Find and return StringParse by id, if not parse found Mustache is used as
     * default
     *
     * @param id
     * @return
     */
    static StringParser get(String id) {
        if (id != null) {
            Optional<StringParser> parser = Containers.get().findObjects(StringParser.class)
                    .stream().filter { p -> (p.id == id) }
                    .findFirst()

            if (parser.present) {
                return parser.get()
            }
        }
        return Containers.get().findObject(MustacheStringParser.class)
    }
}
