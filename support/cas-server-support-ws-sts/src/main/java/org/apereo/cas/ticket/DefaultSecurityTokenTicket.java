package org.apereo.cas.ticket;

import lombok.val;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.cxf.ws.security.tokenstore.SecurityToken;
import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.util.EncodingUtils;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.NoArgsConstructor;

/**
 * This is {@link DefaultSecurityTokenTicket}.
 *
 * @author Misagh Moayyed
 * @since 5.1.0
 */
@Entity
@Table(name = "SECURITYTOKENTICKET")
@DiscriminatorColumn(name = "TYPE")
@DiscriminatorValue(SecurityTokenTicket.PREFIX)
@Slf4j
@NoArgsConstructor
public class DefaultSecurityTokenTicket extends AbstractTicket implements SecurityTokenTicket {

    private static final long serialVersionUID = 3940671352560102114L;

    @ManyToOne(targetEntity = TicketGrantingTicketImpl.class)
    private TicketGrantingTicket ticketGrantingTicket;

    @Column(name = "SECURITY_TOKEN")
    private String securityToken;

    public DefaultSecurityTokenTicket(final String id, final TicketGrantingTicket ticketGrantingTicket, final ExpirationPolicy expirationPolicy, final String securityToken) {
        super(id, expirationPolicy);
        this.ticketGrantingTicket = ticketGrantingTicket;
        this.securityToken = securityToken;
    }

    @Override
    public TicketGrantingTicket getTicketGrantingTicket() {
        return this.ticketGrantingTicket;
    }

    @Override
    public Authentication getAuthentication() {
        return getTicketGrantingTicket().getAuthentication();
    }

    @Override
    public String getPrefix() {
        return SecurityTokenTicket.PREFIX;
    }

    @Override
    public SecurityToken getSecurityToken() {
        val securityTokenBin = EncodingUtils.decodeBase64(this.securityToken);
        return SerializationUtils.deserialize(securityTokenBin);
    }
}
