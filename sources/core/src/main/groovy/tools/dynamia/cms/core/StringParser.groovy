/*
 * Copyright (C) 2009 - 2020 Dynamia Soluciones IT S.A.S - NIT 900302344-1
 * Colombia - South America
 * All Rights Reserved.
 *
 * DynamiaCMS is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License (LGPL v3) as
 * published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamiaCMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamiaCMS.  If not, see <https://www.gnu.org/licenses/>.
 *
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
