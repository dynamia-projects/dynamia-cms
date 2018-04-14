package tools.dynamia.cms.site.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.domain.SiteSimpleEntity
import tools.dynamia.cms.site.products.dto.StoreContactDTO
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "prd_store_contacts")
public class StoreContact extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 858677057279005993L;

	@ManyToOne
	@JsonIgnore
	private Store store;
	private String name;
	private String lastName;
	private String image;
	private String department;
	private String jobTitle;
	private ContactInfo contactInfo = new ContactInfo();
	private Long externalRef;

	public Long getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(Long externalRef) {
		this.externalRef = externalRef;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public void sync(StoreContactDTO dto) {

		BeanUtils.setupBean(this, dto);
		BeanUtils.setupBean(this.getContactInfo(), dto);
	}

}
