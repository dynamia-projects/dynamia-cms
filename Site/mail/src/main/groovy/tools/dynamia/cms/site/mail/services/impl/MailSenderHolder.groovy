package tools.dynamia.cms.site.mail.services.impl;

import org.springframework.mail.MailSender;

/**
 * Created by mario on 13/07/2017.
 */
class MailSenderHolder {

    private long timestamp;
    private MailSender sender;

    public MailSenderHolder(long timestamp, MailSender sender) {
        this.timestamp = timestamp;
        this.sender = sender;
    }

    public MailSender getSender() {
        return sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isOld(long newtimestamp) {
        return newtimestamp > timestamp;
    }

}
