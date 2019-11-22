/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fhi360.lamis.config;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.platform.DatabaseNamesConstants;
import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.db.platform.JdbcDatabasePlatformFactory;
import org.jumpmind.db.platform.generic.GenericJdbcDatabasePlatform;
import org.jumpmind.db.sql.LogSqlBuilder;
import org.jumpmind.db.sql.SqlTemplateSettings;
import org.jumpmind.properties.TypedProperties;
import org.jumpmind.security.SecurityServiceFactory;
import org.jumpmind.symmetric.AbstractSymmetricEngine;
import org.jumpmind.symmetric.ISymmetricEngine;
import org.jumpmind.symmetric.ITypedPropertiesFactory;
import org.jumpmind.symmetric.common.Constants;
import org.jumpmind.symmetric.common.ParameterConstants;
import org.jumpmind.symmetric.db.ISymmetricDialect;
import org.jumpmind.symmetric.db.JdbcSymmetricDialectFactory;
import org.jumpmind.symmetric.io.stage.BatchStagingManager;
import org.jumpmind.symmetric.io.stage.IStagingManager;
import org.jumpmind.symmetric.job.IJobManager;
import org.jumpmind.symmetric.job.JobManager;
import org.jumpmind.symmetric.service.*;
import org.jumpmind.symmetric.service.impl.ClientExtensionService;
import org.jumpmind.symmetric.service.impl.MonitorService;
import org.jumpmind.symmetric.statistic.IStatisticManager;
import org.jumpmind.symmetric.statistic.StatisticManager;
import org.jumpmind.symmetric.util.LogSummaryAppenderUtils;
import org.jumpmind.symmetric.util.SnapshotUtil;
import org.jumpmind.symmetric.util.SymmetricUtils;
import org.jumpmind.symmetric.util.TypedPropertiesFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//@Component
public final class ClientSymmetricEngine extends AbstractSymmetricEngine {

    private static Logger LOG = LoggerFactory.getLogger(ClientSymmetricEngine.class);

    private static final String PROPERTIES_FILE = "client.properties";

    private static final String DEPLOYMENT_TYPE_CLIENT = "client";

    private static final String PROPERTIES_FACTORY_CLASS_NAME = "properties.factory.class.name";

    private Properties properties = System.getProperties();

    private DataSource dataSource;

    private IMonitorService monitorService;

    private IDatabasePlatform databasePlatform;

    private IDatabasePlatform targetPlatform;

    private ISymmetricDialect dialect;

    private JdbcTemplate jdbcTemplate;

    public ClientSymmetricEngine(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        super(false);
        setDeploymentType(DEPLOYMENT_TYPE_CLIENT);
        setDeploymentSubTypeByProperties(properties);
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.init();
    }

    private void setDeploymentSubTypeByProperties(Properties properties) {
        if (properties != null) {
            String loadOnly = properties.getProperty(ParameterConstants.NODE_LOAD_ONLY);
            setDeploymentSubType(loadOnly != null && loadOnly.equals("true") ? Constants.DEPLOYMENT_SUB_TYPE_LOAD_ONLY : null);
        }
    }

    @Override
    protected void init() {

        try {
            LogSummaryAppenderUtils.registerLogSummaryAppender();

            if (getSecurityServiceType().equals(SecurityServiceFactory.SecurityServiceType.CLIENT)) {
                SymmetricUtils.logNotices();
            }
            super.init();

            this.monitorService = new MonitorService(parameterService, symmetricDialect, nodeService, extensionService,
                    clusterService, contextService);
            checkLoadOnly();
        } catch (RuntimeException ex) {
            destroy();
            throw ex;
        }
    }

    public IDatabasePlatform createDatabasePlatform(TypedProperties properties, DataSource dataSource) {
        boolean delimitedIdentifierMode = properties.is(
                ParameterConstants.DB_DELIMITED_IDENTIFIER_MODE, true);
        boolean caseSensitive = !properties.is(ParameterConstants.DB_METADATA_IGNORE_CASE, true);

        //dataSource = BasicDataSourceFactory.create(properties, SecurityServiceFactory.create(SecurityServiceType.CLIENT, properties));

        databasePlatform = JdbcDatabasePlatformFactory.createNewPlatformInstance(dataSource,
                createSqlTemplateSettings(properties), delimitedIdentifierMode, caseSensitive, false);
        return databasePlatform;
    }

    private static SqlTemplateSettings createSqlTemplateSettings(TypedProperties properties) {
        SqlTemplateSettings settings = new SqlTemplateSettings();
        settings.setFetchSize(properties.getInt(ParameterConstants.DB_FETCH_SIZE, 1000));
        settings.setQueryTimeout(properties.getInt(ParameterConstants.DB_QUERY_TIMEOUT_SECS, 300));
        settings.setBatchSize(properties.getInt(ParameterConstants.JDBC_EXECUTE_BATCH_SIZE, 100));
        settings.setBatchBulkLoaderSize(properties.getInt(ParameterConstants.JDBC_EXECUTE_BULK_BATCH_SIZE, 25));
        settings.setOverrideIsolationLevel(properties.getInt(ParameterConstants.JDBC_ISOLATION_LEVEL, -1));
        settings.setReadStringsAsBytes(properties.is(ParameterConstants.JDBC_READ_STRINGS_AS_BYTES, false));
        settings.setTreatBinaryAsLob(properties.is(ParameterConstants.TREAT_BINARY_AS_LOB_ENABLED, true));
        settings.setRightTrimCharValues(properties.is(ParameterConstants.RIGHT_TRIM_CHAR_VALUES, false));
        settings.setAllowUpdatesWithResults(properties.is(ParameterConstants.ALLOW_UPDATES_WITH_RESULTS, false));

        LogSqlBuilder logSqlBuilder = new LogSqlBuilder();
        logSqlBuilder.setLogSlowSqlThresholdMillis(properties.getInt(ParameterConstants.LOG_SLOW_SQL_THRESHOLD_MILLIS, 20000));
        logSqlBuilder.setLogSqlParametersInline(properties.is(ParameterConstants.LOG_SQL_PARAMETERS_INLINE, true));
        settings.setLogSqlBuilder(logSqlBuilder);

        if (settings.getOverrideIsolationLevel() >= 0) {
            log.info("Overriding isolation level to " + settings.getOverrideIsolationLevel());
        }
        return settings;
    }

    @Override
    public IExtensionService createExtensionService() {
        return new ClientExtensionService(this, null);
    }

    @Override
    public IJobManager createJobManager() {
        return new JobManager(this);
    }

    @Override
    public IStagingManager createStagingManager() {
        String directory = parameterService.getString(ParameterConstants.STAGING_DIR);
        if (StringUtils.isBlank(directory)) {
            directory = parameterService.getTempDirectory();
        }
        String stagingManagerClassName = parameterService.getString(ParameterConstants.STAGING_MANAGER_CLASS);
        if (stagingManagerClassName != null) {
            try {
                Constructor<?> cons = Class.forName(stagingManagerClassName).getConstructor(ISymmetricEngine.class, String.class);
                return (IStagingManager) cons.newInstance(this, directory);
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }

        return new BatchStagingManager(this, directory);
    }

    @Override
    public IStatisticManager createStatisticManager() {
        String statisticManagerClassName = parameterService.getString(ParameterConstants.STATISTIC_MANAGER_CLASS);
        if (statisticManagerClassName != null) {
            try {
                Constructor<?> cons = Class.forName(statisticManagerClassName).getConstructor(IParameterService.class,
                        INodeService.class, IConfigurationService.class, IStatisticService.class, IClusterService.class);
                return (IStatisticManager) cons.newInstance(parameterService, nodeService,
                        configurationService, statisticService, clusterService);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return new StatisticManager(parameterService, nodeService,
                configurationService, statisticService, clusterService);
    }

    @Override
    protected ISymmetricDialect createSymmetricDialect() {
        ISymmetricDialect dialect = new JdbcSymmetricDialectFactory(parameterService, platform).create();
        dialect.setTargetPlatform(targetPlatform);
        return dialect;
    }

    @Override
    public ITypedPropertiesFactory createTypedPropertiesFactory() {
        return createTypedPropertiesFactory(
                new File(this.getClass().getClassLoader().getResource(PROPERTIES_FILE).getFile()), properties);
    }

    @Override
    protected IDatabasePlatform createDatabasePlatform(TypedProperties properties) {
        return createDatabasePlatform(properties, dataSource);
    }

    @Override
    protected SecurityServiceFactory.SecurityServiceType getSecurityServiceType() {
        return SecurityServiceFactory.SecurityServiceType.CLIENT;
    }

    private static ITypedPropertiesFactory createTypedPropertiesFactory(File propFile, Properties prop) {
        String propFactoryClassName = System.getProperties().getProperty(PROPERTIES_FACTORY_CLASS_NAME);
        ITypedPropertiesFactory factory;
        if (propFactoryClassName != null) {
            try {
                factory = (ITypedPropertiesFactory) Class.forName(propFactoryClassName).newInstance();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
        } else {
            factory = new TypedPropertiesFactory();
        }
        factory.init(propFile, prop);
        return factory;
    }

    @Override
    public List<File> listSnapshots() {
        File snapshotsDir = SnapshotUtil.getSnapshotDirectory(this);
        List<File> files = new ArrayList<>(FileUtils.listFiles(snapshotsDir, new String[]{"zip"}, false));
        files.sort((o1, o2) -> -o1.compareTo(o2));
        return files;
    }

    private void checkLoadOnly() {
        TypedProperties properties = new TypedProperties();
        if (parameterService.is(ParameterConstants.NODE_LOAD_ONLY, false)) {
            String[] sqlTemplateProperties = new String[]{
                    ParameterConstants.DB_FETCH_SIZE,
                    ParameterConstants.DB_QUERY_TIMEOUT_SECS,
                    ParameterConstants.JDBC_EXECUTE_BATCH_SIZE,
                    ParameterConstants.JDBC_ISOLATION_LEVEL,
                    ParameterConstants.JDBC_READ_STRINGS_AS_BYTES,
                    ParameterConstants.TREAT_BINARY_AS_LOB_ENABLED,
                    ParameterConstants.LOG_SLOW_SQL_THRESHOLD_MILLIS,
                    ParameterConstants.LOG_SQL_PARAMETERS_INLINE
            };
            for (String prop : sqlTemplateProperties) {
                properties.put(prop, parameterService.getString(ParameterConstants.LOAD_ONLY_PROPERTY_PREFIX + prop));
            }

            String[] kafkaProperties = new String[]{
                    ParameterConstants.KAFKA_PRODUCER,
                    ParameterConstants.KAFKA_MESSAGE_BY,
                    ParameterConstants.KAFKA_TOPIC_BY,
                    ParameterConstants.KAFKA_FORMAT
            };

            for (String prop : kafkaProperties) {
                properties.put(prop, parameterService.getString(prop));
            }

            targetPlatform = createDatabasePlatform(properties, null);
            DataSource loadDataSource = targetPlatform.getDataSource();
            if (targetPlatform instanceof GenericJdbcDatabasePlatform) {
                if (targetPlatform.getName() == null || targetPlatform.getName().equals(DatabaseNamesConstants.GENERIC)) {
                    String name = null;
                    try {
                        String[] nameVersion = JdbcDatabasePlatformFactory.determineDatabaseNameVersionSubprotocol(loadDataSource);
                        name = (String.format("%s%s", nameVersion[0], nameVersion[1]).toLowerCase());
                    } catch (Exception e) {
                        log.info("Unable to determine database name and version, " + e.getMessage());
                    }
                    if (name == null) {
                        name = DatabaseNamesConstants.GENERIC;
                    }
                    ((GenericJdbcDatabasePlatform) targetPlatform).setName(name);
                }
                targetPlatform.getDatabaseInfo().setNotNullColumnsSupported(parameterService.is(ParameterConstants.LOAD_ONLY_PROPERTY_PREFIX + ParameterConstants.CREATE_TABLE_NOT_NULL_COLUMNS, true));
            }
            getSymmetricDialect().setTargetPlatform(targetPlatform);
        } else {
            getSymmetricDialect().setTargetPlatform(getSymmetricDialect().getPlatform());
        }
    }

    @Override
    public File snapshot() {
        return SnapshotUtil.createSnapshot(this);
    }

    @Override
    public ISymmetricDialect getSymmetricDialect() {
        if (dialect == null) {
            dialect = createSymmetricDialect();
            dialect.setTargetPlatform(platform);
        }
        return dialect;
    }

    @Override
    public IMonitorService getMonitorService() {
        return monitorService;
    }

    @PostConstruct
    public void startEngine() {
        Runnable task = this::start;
        new Thread(task).start();
    }

}
