package tools.dynamia.cms.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.core.domain.SiteSimpleEntity
import tools.dynamia.commons.BeanUtils
import tools.dynamia.domain.util.ContactInfo

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "prd_store_contacts")
class StoreContact extends SiteSimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 858677057279005993L

    @ManyToOne
	@JsonIgnore
	private Store store
    private String name
    private String lastName
    private String image
    private String department
    private String jobTitle
    private ContactInfo contactInfo = new ContactInfo()
    private Long externalRef

    Long getExternalRef() {
		return externalRef
    }

    void setExternalRef(Long externalRef) {
		this.externalRef = externalRef
    }

    String getImage() {
		return image
    }

    void setImage(String image) {
		this.image = image
    }

    Store getStore() {
		return store
    }

    void setStore(Store store) {
		this.store = store
    }

    String getName() {
		return name
    }

    void setName(String name) {
		this.name = name
    }

    String getLastName() {
		return lastName
    }

    void setLastName(String lastName) {
		this.lastName = lastName
    }

    String getDepartment() {
		return department
    }

    void setDepartment(String department) {
		this.department = department
    }

    String getJobTitle() {
		return jobTitle
    }

    void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle
    }

    ContactInfo getContactInfo() {
		return contactInfo
    }

    void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo
    }

    void sync(tools.dynamia.cms.products.dto.StoreContactDTO dto) {

		BeanUtils.setupBean(this, dto)
        BeanUtils.setupBean(this.contactInfo, dto)
    }

}
