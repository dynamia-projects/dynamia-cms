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
package tools.dynamia.cms.site.pages.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import tools.dynamia.cms.site.core.Aliasable;
import tools.dynamia.cms.site.core.api.SiteAware;
import tools.dynamia.cms.site.core.domain.Content;
import tools.dynamia.cms.site.core.domain.ContentAuthor;
import tools.dynamia.domain.contraints.NotEmpty;

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_pages")
public class Page extends Content implements SiteAware, Aliasable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1980497517680583352L;
	@NotEmpty(message = "Enter page title")
	private String title;
	@Column(length = 1000)
	private String subtitle;
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String content;
	@Column(length = 2000)
	private String summary;
	private String imageURL;
	@OneToOne
	private PageCategory category;
	private String layout;
	private String type = "default";

	@Column(name = "pageAlias")
	private String alias;
	@OneToOne
	@NotNull(message = "Select content author")
	private ContentAuthor author;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date();
	@Temporal(TemporalType.DATE)
	private Date startDate = new Date();
	@Temporal(TemporalType.DATE)
	private Date endDate;
	private boolean published = true;
	private boolean neverExpire = true;
	@Column(length = 2000)
	private String tags;
	private String icon;
	@OneToMany(mappedBy = "page", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PageParameter> parameters = new ArrayList<>();
	private boolean showTitle = true;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	private String templateEngine;

	private String styleClass;

	public String getTemplateEngine() {
		return templateEngine;
	}

	public void setTemplateEngine(String templateEngine) {
		this.templateEngine = templateEngine;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean isNeverExpire() {
		return neverExpire;
	}

	public void setNeverExpire(boolean neverExpire) {
		this.neverExpire = neverExpire;
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	public String getType() {
		if (type == null) {

			type = "default";
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ContentAuthor getAuthor() {
		return author;
	}

	public void setAuthor(ContentAuthor author) {
		this.author = author;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {

		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public PageCategory getCategory() {
		return category;
	}

	public void setCategory(PageCategory category) {
		this.category = category;
	}

	public List<PageParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<PageParameter> parameters) {
		this.parameters = parameters;
	}

	public PageParameter getParam(String name) {
		if (parameters != null) {
			for (PageParameter pageParameter : parameters) {
				if (pageParameter.getName().equals(name)) {
					return pageParameter;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return getTitle();
	}

	public Date getPublishedDate() {
		if (startDate != null && startDate.after(creationDate)) {
			return startDate;
		} else {
			return creationDate;
		}
	}

	public boolean hasImage() {
		return imageURL != null && !imageURL.isEmpty();
	}

	@Override
	public String aliasSource() {
		return getTitle();
	}

}
