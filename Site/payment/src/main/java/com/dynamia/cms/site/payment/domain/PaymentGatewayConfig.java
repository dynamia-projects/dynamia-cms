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
package com.dynamia.cms.site.payment.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.dynamia.cms.site.payment.PaymentGateway;

import tools.dynamia.domain.SimpleEntity;

@Entity
@Table(name = "pay_gateway_config")
public class PaymentGatewayConfig extends SimpleEntity {

	@NotNull
	private String gatewayId;
	private String name;
	private String value;
	private String source;
	private String label;

	public PaymentGatewayConfig() {
		// TODO Auto-generated constructor stub
	}

	public PaymentGatewayConfig(PaymentGateway gateway, String name, String value) {
		this.gatewayId = gateway.getId();
		this.name = name;
		this.value = value;
	}

	public PaymentGatewayConfig(PaymentGateway gateway, String name, String value, String source) {
		this.gatewayId = gateway.getId();
		this.name = name;
		this.value = value;
		this.source = source;
	}

	public PaymentGatewayConfig(PaymentGateway gateway, String name, String value, String source, String label) {
		super();
		this.gatewayId = gateway.getId();
		this.name = name;
		this.value = value;
		this.source = source;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getGatewayId() {
		return gatewayId;
	}

	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
