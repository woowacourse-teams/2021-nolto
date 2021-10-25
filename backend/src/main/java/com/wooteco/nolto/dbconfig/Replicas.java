package com.wooteco.nolto.dbconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@ConfigurationProperties(prefix = "spring.datasource.hikari.replica")
public class Replicas {

    List<Replica> replicaList = new ArrayList<>();

    public <D extends DataSource> Map<String, DataSource> createDataSources(Class<D> type) {
        return this.replicaList.stream()
                .collect(Collectors.toMap(Replica::getName, replica -> replica.createDataSource(type)));
    }

    public List<Replica> getReplicaList() {
        return replicaList;
    }

    public static class Replica {
        private String name;
        private String username;
        private String password;
        private String jdbcUrl;

        public <D extends DataSource> DataSource createDataSource(Class<D> type) {
            return DataSourceBuilder.create()
                    .type(type)
                    .url(this.getJdbcUrl())
                    .username(this.getUsername())
                    .password(this.getPassword())
                    .build();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getJdbcUrl() {
            return jdbcUrl;
        }

        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }
    }
}
