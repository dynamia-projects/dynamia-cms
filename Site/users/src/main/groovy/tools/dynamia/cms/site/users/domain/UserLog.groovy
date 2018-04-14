package tools.dynamia.cms.site.users.domain

import tools.dynamia.cms.site.core.domain.SiteBaseEntity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "usr_logs")
class UserLog extends SiteBaseEntity {

    @OneToOne
    @NotNull
    private User user
    @Column(length = 2000)
    private String event

    UserLog() {
    }

    UserLog(User user, String event) {
        this.user = user
        this.event = event
        site = user.site
    }

    User getUser() {
        return user
    }

    void setUser(User user) {
        this.user = user
    }

    String getEvent() {
        return event
    }

    void setEvent(String event) {
        this.event = event
    }
}
