/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.message;

import java.util.Date;

/**
 * This class is a container for message properties published with the message.
 * 
 * <p>
 * <p/>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * <p/>
 *
 * @version 1.0
 * 
 * @since   SINCE-TBD
 */
public class DefaultMessageProperties implements MessagePropertiesContainer
{
    /*
     * The message timestamp
     */
    private Date timestamp;
    
    /*
     * The message correlation identifier
     */
    private String correlationId;
    
    /*
     * The reply to destination.
     */
    private String replyTo;
   
    
    /**
     * DefaultMessageProperties constructor
     * 
     * @since   1.0
     */
    public DefaultMessageProperties()
    {
        super();
    }
    
    
    /**
     * DefaultMessageProperties constructor
     * 
     * @param   timestamp       The message timestamp
     * 
     * @since   1.0
     */
    public DefaultMessageProperties(final Date timestamp) 
    {
        super();
        
        this.timestamp = timestamp;
    }
    
    
    /**
     * DefaultMessageProperties constructor
     * 
     * @param   timestamp       The message timestamp
     * @param   correlationId   The message correlation id
     * 
     * @since   1.0
     */
    public DefaultMessageProperties(final Date timestamp, 
            final String correlationId) 
    {
        super();
        
        this.timestamp = timestamp;
        this.correlationId = correlationId;
    }
    
    
    /**
     * DefaultMessageProperties constructor
     * 
     * @param   timestamp       The message timestamp
     * @param   correlationId   The message correlation id
     * @param   replyTo         The message reply to destination.
     * 
     * @since   1.0
     */
    public DefaultMessageProperties(final Date timestamp, 
            final String correlationId, final String replyTo) 
    {
        super();
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.replyTo = replyTo;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Date getTimestamp() 
    {
        return timestamp;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimestamp(Date timestamp) 
    {
        this.timestamp = timestamp;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getCorrelationId() 
    {
        return correlationId;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setCorrelationId(String correlationId) 
    {
        this.correlationId = correlationId;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getReplyTo() 
    {
        return replyTo;
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setReplyTo(String replyTo) 
    {
        this.replyTo = replyTo;
    }
}
