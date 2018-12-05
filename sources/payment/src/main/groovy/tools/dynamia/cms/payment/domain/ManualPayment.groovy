/*
 *  Copyright (c) 2018 Dynamia Soluciones IT SAS and the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
