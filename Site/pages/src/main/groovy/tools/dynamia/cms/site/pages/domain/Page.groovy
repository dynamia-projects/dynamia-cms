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
package tools.dynamia.cms.site.pages.domain

import tools.dynamia.cms.site.core.Aliasable
import tools.dynamia.cms.site.core.api.SiteAware
import tools.dynamia.cms.site.core.domain.Content
import tools.dynamia.cms.site.core.domain.ContentAuthor
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 * @author Mario Serrano Leones
 */
@Entity
@Table(name = "pg_pages")
class Page extends Content implements SiteAware, Aliasable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1980497517680583352L
    @NotEmpty(message = "Enter page title")
	private String title
    @Column(length = 1000)
	private String subtitle
    @Basic(fetch = FetchType.LAZY)
	@Lob
	private String content
    @Column(length = 2000)
	private String summary
    private String imageURL
    @OneToOne
	private PageCategory category
    private String layout
    private String type = "default"

    @Column(name = "pageAlias")
	private String alias
    @OneToOne
	@NotNull(message = "Select content author")
	private ContentAuthor author
    @Temporal(TemporalType.TIMESTAMP)
	private Date creationDate = new Date()
    @Temporal(TemporalType.DATE)
	private Date startDate = new Date()
    @Temporal(TemporalType.DATE)
	private Date endDate
    private boolean published = true
    private boolean neverExpire = true
    @Column(length = 2000)
	private String tags
    private String icon
    @OneToMany(mappedBy = "page", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PageParameter> parameters = new ArrayList<>()
    private boolean showTitle = true
    @Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate
    private String templateEngine

    private String styleClass

    String getTemplateEngine() {
		return templateEngine
    }

    void setTemplateEngine(String templateEngine) {
		this.templateEngine = templateEngine
    }

    String getStyleClass() {
		return styleClass
    }

    void setStyleClass(String styleClass) {
		this.styleClass = styleClass
    }

    Date getLastUpdate() {
		return lastUpdate
    }

    void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate
    }

    String getImageURL() {
		return imageURL
    }

    void setImageURL(String imageURL) {
		this.imageURL = imageURL
    }

    String getSummary() {
		return summary
    }

    void setSummary(String summary) {
		this.summary = summary
    }

    boolean isNeverExpire() {
		return neverExpire
    }

    void setNeverExpire(boolean neverExpire) {
		this.neverExpire = neverExpire
    }

    boolean isShowTitle() {
		return showTitle
    }

    void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle
    }

    String getType() {
		if (type == null) {

			type = "default"
        }
		return type
    }

    void setType(String type) {
		this.type = type
    }

    String getIcon() {
		return icon
    }

    void setIcon(String icon) {
		this.icon = icon
    }

    String getSubtitle() {
		return subtitle
    }

    void setSubtitle(String subtitle) {
		this.subtitle = subtitle
    }

    String getAlias() {
		return alias
    }

    void setAlias(String alias) {
		this.alias = alias
    }

    ContentAuthor getAuthor() {
		return author
    }

    void setAuthor(ContentAuthor author) {
		this.author = author
    }

    Date getCreationDate() {
		return creationDate
    }

    void setCreationDate(Date creationDate) {
		this.creationDate = creationDate
    }

    Date getStartDate() {
		return startDate
    }

    void setStartDate(Date startDate) {
		this.startDate = startDate
    }

    Date getEndDate() {
		return endDate
    }

    void setEndDate(Date endDate) {
		this.endDate = endDate
    }

    boolean isPublished() {
		return published
    }

    void setPublished(boolean published) {
		this.published = published
    }

    String getTags() {
		return tags
    }

    void setTags(String tags) {
		this.tags = tags
    }

    String getLayout() {
		return layout
    }

    void setLayout(String layout) {
		this.layout = layout
    }

    String getTitle() {
		return title
    }

    void setTitle(String title) {
		this.title = title
    }

    String getContent() {

		return content
    }

    void setContent(String content) {
		this.content = content
    }

    PageCategory getCategory() {
		return category
    }

    void setCategory(PageCategory category) {
		this.category = category
    }

    List<PageParameter> getParameters() {
		return parameters
    }

    void setParameters(List<PageParameter> parameters) {
		this.parameters = parameters
    }

    PageParameter getParam(String name) {
		if (parameters != null) {
			for (PageParameter pageParameter : parameters) {
				if (pageParameter.getName().equals(name)) {
					return pageParameter
                }
			}
		}
		return null
    }

	@Override
    String toString() {
		return getTitle()
    }

    Date getPublishedDate() {
		if (startDate != null && startDate.after(creationDate)) {
			return startDate
        } else {
			return creationDate
        }
	}

    boolean hasImage() {
		return imageURL != null && !imageURL.isEmpty()
    }

	@Override
    String aliasSource() {
		return getTitle()
    }

}
