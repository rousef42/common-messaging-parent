/**
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.context;

/**
 * <p>
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class RequestReplyKey
{
    private Class request;
    private Class reply;

    public RequestReplyKey(Class request, Class reply)
    {
        this.request = request;
        this.reply = reply;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        RequestReplyKey that = (RequestReplyKey) o;

        if (request != null ? !request.equals(that.request) : that.request != null)
        {
            return false;
        }
        return reply != null ? reply.equals(that.reply) : that.reply == null;

    }

    @Override
    public int hashCode()
    {
        int result = request != null ? request.hashCode() : 0;
        result = 31 * result + (reply != null ? reply.hashCode() : 0);
        return result;
    }
}
