package com.wooteco.nolto.dbconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    public static final String DATASOURCE_MASTER_KEY = "master";
    public static final String DATASOURCE_SLAVE_KEY = "slave";

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            log.info("use slave datasource");
            return DATASOURCE_SLAVE_KEY;
        }
        log.info("use master datasource");
        return DATASOURCE_MASTER_KEY;
    }
}
