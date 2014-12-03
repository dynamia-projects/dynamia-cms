/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dynamia.cms.site.pages.domain;

import com.dynamia.cms.site.core.api.SiteAware;
import com.dynamia.cms.site.core.domain.Content;
import com.dynamia.cms.site.core.domain.ContentAuthor;
import com.dynamia.tools.domain.contraints.NotEmpty;

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

/**
 *
 * @author mario
 */
@Entity
@Table(name = "pg_pages")
public class Page extends Content implements SiteAware {

	@NotEmpty(message = "Enter page title")
	private String title;
	@Column(length = 1000)
	private String subtitle;
	@Basic(fetch = FetchType.LAZY)
	@Lob
	private String content;
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
	private Date creationDate;
	@Temporal(TemporalType.DATE)
	private Date startDate;
	@Temporal(TemporalType.DATE)
	private Date endDate;
	private boolean published;
	private String tags;
	private String icon;
	@OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PageParameter> parameters = new ArrayList<>();

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

}
