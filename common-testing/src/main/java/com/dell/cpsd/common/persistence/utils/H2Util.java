/*
 * &copy; 2017 VCE Company, LLC. All rights reserved.
 * VCE Confidential/Proprietary Information
 */

package com.dell.cpsd.common.persistence.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * H2 database helper for tests.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries. All Rights Reserved.
 * </p>
 *
 * @since 1.2
 */
public class H2Util extends DbTestUtil
{
    private static final Logger LOGGER          = LoggerFactory.getLogger(H2Util.class);
    private static final String Q_INTEGRITY_OFF = "SET REFERENTIAL_INTEGRITY FALSE;";
    private static final String Q_INTEGRITY_ON  = "SET REFERENTIAL_INTEGRITY TRUE;";
    private static final String Q_SHOW_TABLES   = "SHOW TABLES;";
    private static final String Q_TRUNCATE      = "DELETE FROM \"%s\";";

    public H2Util(final EntityManager em)
    {
        super(em);
    }

    @Override
    public void cleanDb()
    {
        LOGGER.info("Cleaning all tables");
        em.getTransaction().begin();
        LOGGER.info(Q_INTEGRITY_OFF);
        em.createNativeQuery(Q_INTEGRITY_OFF).executeUpdate();
        List tableList = em.createNativeQuery(Q_SHOW_TABLES).getResultList();
        for (final Object row : tableList)
        {
            Object[] rowArray = (Object[]) row;
            String tableName = (String) rowArray[0];
            //String schema = (String) rowArray[1];
            String sqlTruncate = String.format(Q_TRUNCATE, tableName);
            LOGGER.info(sqlTruncate);
            em.createNativeQuery(sqlTruncate).executeUpdate();
        }
        LOGGER.info(Q_INTEGRITY_ON);
        em.createNativeQuery(Q_INTEGRITY_ON).executeUpdate();
        em.getTransaction().commit();
    }
}
