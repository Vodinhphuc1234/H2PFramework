package com.h2p.databaseAccessManagement;

import com.h2p.adapters.Adapter;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.mappers.TableMapper;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public abstract class IH2PQuery<T> {
    final SQLConnectionManager sqlConnectionManager;
    final TableMapper<T> tableMapper;
    final Adapter<T> adapter;
    final Class<T> tClass;

    public IH2PQuery(Class<T> tClass) {
        this.tClass = tClass;
        sqlConnectionManager = SQLConnectionManager.getInstance();
        tableMapper = new TableMapper<>(tClass);
        adapter = new Adapter<>(tClass);
    }
}
