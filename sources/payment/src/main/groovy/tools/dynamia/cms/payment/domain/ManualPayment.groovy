package tools.dynamia.cms.payment.domain

import tools.dynamia.cms.payment.api.Payment
import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.BaseEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(name = "pay_manuals")
@Entity
class ManualPayment extends BaseEntity implements Payment {

	/**
	 * 
	 */
	static final long serialVersionUID = 1369368571148885123L

	String uuid = StringUtils.randomString().toUpperCase()

	String number

	@NotNull(message = "Manual Payment source invalid")
	String source
	String registrator
	String registratorCode
	String registratorId

	@Column(length = 1000)
	String payer
	String payerCode
	String payerId
	@Column(length = 3000)
	String reference
	@Column(length = 3000)
	String reference2
	@Column(length = 3000)
	String reference3

	@NotNull(message = "Payment amount is required")
	BigDecimal amount
	String type

	@Column(length = 3000)
	String description

	boolean sended
	String errorCode
	String errorMessage
	String externalRef

}
