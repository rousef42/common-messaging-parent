/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.common.persistence.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Postgres database helper for tests.
 * It is not working in a standard symphony database, as users doesn't have permission to disable triggers.
 * For manual Integration Tests only, where database user have all permissions.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * </p>
 *
 * @since 1.2
 */
public class PostgresUtil extends DbTestUtil
{
    private static final Logger LOGGER             = LoggerFactory.getLogger(PostgresUtil.class);
    private static final String Q_SHOW_TABLES      = "SELECT tablename, schemaname FROM pg_tables WHERE schemaname=current_schema;";
    private static final String Q_TRUNCATE         = "DELETE FROM \"%s\" CASCADE;";
    private static final String Q_DISABLE_TRIGGERS = "ALTER TABLE \"%s\" DISABLE TRIGGER USER";
    private static final String Q_ENABLE_TRIGGERS  = "ALTER TABLE \"%s\" ENABLE TRIGGER USER";

    public PostgresUtil(final EntityManager em)
    {
        super(em);
    }

    @Override
    public void cleanDb()
    {
        LOGGER.info("Cleaning all symphony tables");
        em.getTransaction().begin();
        List tableList = em.createNativeQuery(Q_SHOW_TABLES).getResultList();
        List<String> tableNames = new ArrayList<>();
        for (final Object row : tableList)
        {
            Object[] rowArray = (Object[]) row;
            String tableName = (String) rowArray[0];
            String schema = (String) rowArray[1];
            //tableName = schema + "." + tableName;
            tableNames.add(tableName);
        }

        for (final String tableName : tableNames)
        {
            String sqlDisableTriggers = String.format(Q_DISABLE_TRIGGERS, tableName);
            LOGGER.info(sqlDisableTriggers);
            em.createNativeQuery(sqlDisableTriggers).executeUpdate();
        }
        for (final String tableName : tableNames)
        {
            String sqlTruncate = String.format(Q_TRUNCATE, tableName);
            LOGGER.info(sqlTruncate);
            em.createNativeQuery(sqlTruncate).executeUpdate();
        }
        for (final String tableName : tableNames)
        {
            String sqlEnableTriggers = String.format(Q_ENABLE_TRIGGERS, tableName);
            LOGGER.info(sqlEnableTriggers);
            em.createNativeQuery(sqlEnableTriggers).executeUpdate();
        }
        em.getTransaction().commit();
    }
}
