/*
 * Copyright (C) 2009 - 2019 Dynamia Soluciones IT S.A.S - NIT 900302344-1
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
