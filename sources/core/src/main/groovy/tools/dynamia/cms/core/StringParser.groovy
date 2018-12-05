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

import tools.dynamia.cms.core.api.TypeExtension
import tools.dynamia.integration.Containers

/**
 *
 * @author Dynamia Soluciones IT SAS and the original author or authors
 */
interface StringParser extends TypeExtension {

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
