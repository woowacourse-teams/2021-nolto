package com.wooteco.nolto.dbconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    public static final String DATASOURCE_SOURCE_KEY = "source";

    private ReplicaDataSourceNames replicaDataSourceNames;

    public void setReplicaDataSourceNames(List<String> names) {
        this.replicaDataSourceNames = new ReplicaDataSourceNames(names);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            String nextReplicaDataSourceName = replicaDataSourceNames.getNextName();
            log.info("Using Replica DB Name : {}", nextReplicaDataSourceName);
            return nextReplicaDataSourceName;
        }
        log.info("Using Source DB");
        return DATASOURCE_SOURCE_KEY;
    }

    public static class ReplicaDataSourceNames {

        private final List<String> values;
        private int counter = 0;

        public ReplicaDataSourceNames(List<String> values) {
            this.values = values;
        }

        public String getNextName() {
            if (counter == values.size()) {
                counter = 0;
            }
            return values.get(counter++);
        }
    }
}
