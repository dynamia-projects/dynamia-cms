package tools.dynamia.cms.payment.domain

import tools.dynamia.commons.StringUtils
import tools.dynamia.domain.Reference
import tools.dynamia.domain.SimpleEntity
import tools.dynamia.domain.contraints.NotEmpty

import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "pay_gateway_accounts")
class PaymentGatewayAccount extends SimpleEntity {

    @NotEmpty
    String name
    boolean enabled = true
    String source
    @Reference("PaymentGateway")
    @NotNull
    String gatewayId
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    List<PaymentGatewayConfig> configuration = new ArrayList<>()
    String uuid = StringUtils.randomString()


    void addConfig(PaymentGatewayConfig config) {
        configuration << config
        config.source = source
        config.account = this
    }

    Map<String, String> getConfigurationMap() {
        def map = new HashMap<String,String>()
        configuration.each { map[it.name] = it.value }
        return map
    }

    @Override
    String toString() {
        return "$name ($gatewayId)"
    }
}
