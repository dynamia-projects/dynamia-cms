package tools.dynamia.cms.site.products.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import tools.dynamia.cms.site.core.domain.SiteBaseEntity
import tools.dynamia.cms.site.users.domain.User

import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Created by mario on 12/07/2017.
 */
@Entity
@Table(name = "prd_reviews", uniqueConstraints = @UniqueConstraint(columnNames = ["product_id", "user_id"]))
class ProductReview extends SiteBaseEntity {

    @OneToOne
    @NotNull
    User user

    @ManyToOne
    @NotNull
    @JsonIgnore
    Product product


    @Column(length = 1000)
    String comment
    @Min(1L)
    @Max(5L)
    int stars = 5

    boolean verified
    String document
    boolean incomplete

    
    @Override
    String toString() {
        return String.format("%s review %s with %s stars and say %s", user, product, stars, comment)
    }
}
