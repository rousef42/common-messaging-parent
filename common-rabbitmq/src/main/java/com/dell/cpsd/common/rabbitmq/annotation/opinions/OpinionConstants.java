/**
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.rabbitmq.annotation.opinions;

/**
 * <p>
 * &copy; 2016 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 * </p>
 *
 * @since SINCE-TBD
 */
public class OpinionConstants
{
    public static final String UNDEFINED = "";

    public static boolean isDefined(String value)
    {
        return !UNDEFINED.equals(value);
    }
}
