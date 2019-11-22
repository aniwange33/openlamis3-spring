package org.fhi360.lamis.config;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.format.Formatter;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class MariaDBDatasourceConfiguration {
    private final ApplicationProperties properties;
    private static DB db;
    private static final String KEY = "U2FsdGVkX1+PUTUO1EVRoEV+fsEXWerotpN+PfLWietoC+oCHyWwuhoPe8uXXB42Szq1LXG4KRiXV/c+vh+QzkxcNzV6IqA8PoHLzE2g7lwsFWmYDfOWX0WqStjk8RQz";
    private static final String KEY_PASSWORD = "696f54aae255e6256a8533fa5dba2e863570af30d3811d549a522724aae648305770da5b5a536e73c9b448f162a2fb3f640d27137495df06da06e9096c5cbe3ff57fe2b42830826e71912d94d9f339416476d9662f51718dc9c3c39ce054274fe024e99cefa7a9392cb7a601ed3d4ff9ecd4614a6f9826addc291ad27295f1fa";

    public MariaDBDatasourceConfiguration(ApplicationProperties properties) {
        this.properties = properties;
    }


    @Bean
    public HikariDataSource dataSource() throws ManagedProcessException, IOException {
        DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
        File keyFile = File.createTempFile(RandomStringUtils.randomAlphabetic(4),
                RandomStringUtils.randomAlphabetic(4));
        File keyPasswordFile = File.createTempFile(RandomStringUtils.randomAlphabetic(4),
                RandomStringUtils.randomAlphabetic(4));
        byte[] bytes = Base64.getDecoder().decode(KEY);
        Files.write(keyFile.toPath(), bytes);
        try (FileWriter writer = new FileWriter(keyPasswordFile)) {
            writer.write(KEY_PASSWORD);
        }
        configBuilder.setPort(3307);
        configBuilder.addArg("--plugin_load_add=file_key_management");
        configBuilder.addArg(String.format("--file-key-management-filename=%s",
                keyFile.getAbsolutePath()));
        configBuilder.addArg(String.format("--file_key_management_filekey=FILE:%s",
                keyPasswordFile.getAbsolutePath()));
        configBuilder.addArg("--file-key-management-encryption-algorithm=AES_CBC");
        configBuilder.addArg("--innodb-encrypt-tables");
        configBuilder.addArg("--innodb-encrypt-log");
        configBuilder.addArg("--innodb_encryption_threads=4");
        configBuilder.addArg("--lower-case-table-names=1");
        configBuilder.addArg("--skip-grant-tables");
        configBuilder.setDataDir(String.format("%s/database/data", properties.getDatabasePath())); // just an example
        configBuilder.setBaseDir(String.format("%s/mariadb", properties.getDatabasePath()));
        configBuilder.setDeletingTemporaryBaseAndDataDirsOnShutdown(false);
        configBuilder.setSecurityDisabled(false);

        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**");
        for (Resource resource : resources) {
            if (resource != null && resource.getFilename() != null &&
                    resource.getFilename().endsWith("file_key_management.dll")) {
                FileUtils.copyURLToFile(resource.getURL(),
                        new File(configBuilder.getBaseDir() + "/lib/plugin/file_key_management.dll"));
            }
            if (resource != null && resource.getFilename() != null &&
                    resource.getFilename().endsWith(".exe")) {
                FileUtils.copyURLToFile(resource.getURL(),
                        new File(configBuilder.getBaseDir() + "/bin/" + resource.getFilename()));
            }
        }
        db = DB.newEmbeddedDB(configBuilder.build());
        db.start();
        sleep(5000);
        //String password = UUID.randomUUID().toString();
        String password = "password";
        String updatePassword = "update mysql.user set password = password('%s') where user = 'root'";
        db.run(String.format(updatePassword, password),
                "root", "");
        sleep(2000);
        db.createDB("lamis", "root", password);
        db.stop();
        configBuilder._getArgs().remove("--skip-grant-tables");
        db = DB.newEmbeddedDB(configBuilder.build());
        db.start();
        sleep(5000);
        db.run(String.format("grant all on across.* to 'across'@'localhost' identified by '%s'", password), "root", password);
        db.run(String.format("grant all on across1.* to 'across'@'localhost' identified by '%s'", password), "root", password);
        sleep(2000);
        db.run("GRANT RELOAD, PROCESS, LOCK TABLES, REPLICATION CLIENT ON *.* TO 'mbackup'@'localhost' identified by 'backup'", "root", password);
        sleep(2000);
        db.run("GRANT PROXY ON ''@'' TO 'root'@'localhost' WITH GRANT OPTION", "root", password);
        sleep(2000);
        db.run("GRANT PROXY ON ''@'' TO 'root'@'127.0.0.1' WITH GRANT OPTION", "root", password);
        FileUtils.deleteQuietly(keyFile);
        FileUtils.deleteQuietly(keyPasswordFile);
        String url = String.format("jdbc:mysql://localhost:%s/lamis?tinyInt1isBit=false&serverTimezone=UTC", configBuilder.getPort());

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(url)
                .driverClassName("com.mysql.jdbc.Driver")
                .username("root")
                .password(password)
                .build();
    }

    public void backup() throws ManagedProcessException {
        db.run("", "root", "");

    }

    @Bean
    public Formatter<LocalDate> localDateFormatter() {
        return new Formatter<LocalDate>() {
            @Override
            public LocalDate parse(String text, Locale locale) throws ParseException {
                return LocalDate.parse(text, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
            }

            @Override
            public String print(LocalDate object, Locale locale) {
                return DateTimeFormatter.ofPattern("MM/dd/yyyy").format(object);
            }
        };
    }

    private void sleep(long seconds) {
        try {
            TimeUnit.MILLISECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
