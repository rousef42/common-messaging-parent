/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.persistence.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;

import static org.junit.Assert.fail;

/**
 * Database helper for tests.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.2
 */
public class DbTestUtil
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DbTestUtil.class);

    protected final EntityManager em;

    public DbTestUtil(final EntityManager em)
    {
        if (em == null)
        {
            fail("EntityManager is null");
        }
        this.em = em;
    }

    /**
     * Remove content of all user's tables data.
     * Does not delete table definitions.
     */
    public void cleanDb()
    {
        LOGGER.warn("This implementataion is not removing any data.");
    }

    /**
     * Factory.
     */
    public static DbTestUtil create(final EntityManager em, String dbType)
    {
        if ("h2".equalsIgnoreCase(dbType))
        {
            return new H2Util(em);
        }
        else if ("POSTGRESQL".equalsIgnoreCase(dbType))
        {
            return new PostgresUtil(em);
        }
        else
        {
            LOGGER.error("Unknown DB type: " + dbType);
            return new DbTestUtil(em);
        }
    }
}
