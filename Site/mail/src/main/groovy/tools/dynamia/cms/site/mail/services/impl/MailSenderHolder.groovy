package tools.dynamia.cms.site.mail.services.impl

import org.springframework.mail.MailSender

/**
 * Created by mario on 13/07/2017.
 */
class MailSenderHolder {

    private long timestamp
    private MailSender sender

    MailSenderHolder(long timestamp, MailSender sender) {
        this.timestamp = timestamp
        this.sender = sender
    }

    MailSender getSender() {
        return sender
    }

    long getTimestamp() {
        return timestamp
    }

    boolean isOld(long newtimestamp) {
        return newtimestamp > timestamp
    }

}
