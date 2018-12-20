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
