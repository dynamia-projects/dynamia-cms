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

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 *
 * @author Mario Serrano Leones
 */
@Component
class VelocityStringParser implements StringParser {

    private VelocityEngine velocityEngine

    @Autowired
    VelocityStringParser(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine
    }

    @Override
    String parse(String template, Map<String, Object> templateModel) {

        StringWriter writer = new StringWriter()
        VelocityContext context = new VelocityContext()

        if (templateModel != null) {
            for (Map.Entry<String, Object> entry : templateModel.entrySet()) {
                context.put(entry.key, entry.value)
            }
        }

        velocityEngine.evaluate(context, writer, "log", template)
        writer.flush()

        return writer.toString()
    }

    @Override
    String getId() {
        return "velocity"
    }

    @Override
    String getName() {
        return "Velocity"
    }

    @Override
    String getDescription() {
        return "Use this if you need advanced template with conditionals and such"
    }

}
