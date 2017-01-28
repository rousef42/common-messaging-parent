/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.persistence.utils;

import com.dell.cpsd.common.rabbitmq.utils.MessageLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.Assert.fail;

/**
 * H2 database helper for tests.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.2
 */
public class H2Util
{
    private static final Logger LOGGER           = LoggerFactory.getLogger(MessageLoader.class);
    private static final String Q_INTEGRITY_OFF  = "SET REFERENTIAL_INTEGRITY FALSE;";
    private static final String Q_INTEGRITY_ON   = "SET REFERENTIAL_INTEGRITY TRUE;";
    private static final String Q_SHOW_TABLES    = "SHOW TABLES;";
    private static final String Q_TRUNCATE_START = "DELETE FROM ";

    private final EntityManager em;

    public H2Util(final EntityManager em)
    {
        if (em == null)
        {
            fail("EntityManager is null");
        }
        this.em = em;
    }

    public void cleanDb()
    {
        LOGGER.info("Cleaning all tables");
        em.getTransaction().begin();
        em.createNativeQuery(Q_INTEGRITY_OFF).executeUpdate();
        List tableList = em.createNativeQuery(Q_SHOW_TABLES).getResultList();
        for (final Object row : tableList)
        {
            Object[] rowArray = (Object[]) row;
            String tableName = (String) rowArray[0];
            //String schema = (String) rowArray[1];
            String sqlTruncate = Q_TRUNCATE_START + tableName + ";";
            LOGGER.info(sqlTruncate);
            em.createNativeQuery(sqlTruncate).executeUpdate();
        }
        em.createNativeQuery(Q_INTEGRITY_ON).executeUpdate();
        em.getTransaction().commit();
    }
}
