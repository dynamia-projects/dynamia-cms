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
package tools.dynamia.cms.pages.domain

import tools.dynamia.cms.core.Aliasable
import tools.dynamia.cms.core.api.SiteAware
import tools.dynamia.cms.core.domain.Content
import tools.dynamia.cms.core.domain.ContentAuthor
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
                if (pageParameter.name.equals(name)) {
                    return pageParameter
                }
            }
        }
        return null
    }

    @Override
    String toString() {
        if (title == null) {
            return "empty page"
        }
        return title
    }

    Date getPublishedDate() {
        if (startDate != null && startDate.after(creationDate)) {
            return startDate
        } else {
            return creationDate
        }
    }

    boolean hasImage() {
        return imageURL != null && !imageURL.empty
    }

    @Override
    String aliasSource() {
        return title
    }

}
