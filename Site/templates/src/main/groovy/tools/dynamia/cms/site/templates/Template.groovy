/* 
 * Copyright 2016 Dynamia Soluciones IT SAS and the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tools.dynamia.cms.site.templates

import java.nio.file.Path

/**
 *
 * @author Mario Serrano Leones
 */
public class Template implements Serializable {

	private String name;
	private String directoryName;
	private String description;
	private String author;
	private String date;
	private String version;
	private List<String> positions;

	private Template() {
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return author;
	}

	public String getDate() {
		return date;
	}

	public String getVersion() {
		return version;
	}

	public List<String> getPositions() {
		return positions;
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public static Template build(Properties prop, Path directory) {
		Template t = new Template();
		t.name = prop.getProperty("name");
		t.author = prop.getProperty("author");
		t.date = prop.getProperty("date");
		t.version = prop.getProperty("version");
		t.description = prop.getProperty("description");
		t.directoryName = directory.getFileName().toString();

		String pos = prop.getProperty("positions");
		if (pos != null && !pos.isEmpty()) {
			String[] positions = pos.split(",");
			if (positions != null) {
				Arrays.sort(positions);
				List<String> positionsList = new ArrayList<>();
				for (String position : positions) {
					positionsList.add(position.trim());
				}
				t.positions = Collections.unmodifiableList(positionsList);
			}
		}

		return t;
	}

	@Override
	public String toString() {
		return name;
	}

}
