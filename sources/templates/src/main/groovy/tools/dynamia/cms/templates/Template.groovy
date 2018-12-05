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
package tools.dynamia.cms.templates

import java.nio.file.Path

/**
 *
 * @author Mario Serrano Leones
 */
class Template implements Serializable {

	String name
    String directoryName
    String description
    String author
    String date
    String version
    List<String> positions

    private Template() {
	}



    static Template build(Properties prop, Path directory) {
		Template t = new Template()
        t.name = prop.getProperty("name")
        t.author = prop.getProperty("author")
        t.date = prop.getProperty("date")
        t.version = prop.getProperty("version")
        t.description = prop.getProperty("description")
        t.directoryName = directory.fileName.toString()

        String pos = prop.getProperty("positions")
        if (pos != null && !pos.empty) {
			String[] positions = pos.split(",")
            if (positions != null) {
				Arrays.sort(positions)
                List<String> positionsList = new ArrayList<>()
                for (String position : positions) {
					positionsList.add(position.trim())
                }
				t.positions = Collections.unmodifiableList(positionsList)
            }
		}

		return t
    }

	@Override
    String toString() {
		return name
    }

}
