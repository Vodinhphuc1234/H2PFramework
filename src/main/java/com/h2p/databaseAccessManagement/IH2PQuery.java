package com.h2p.databaseAccessManagement;

import com.h2p.adapters.Adapter;
import com.h2p.mappers.TableMapper;

public abstract class IH2PQuery<T> {
    final TableMapper<T> tableMapper;
    final Adapter<T> adapter;
    final Class<T> tClass;

    public IH2PQuery(Class<T> tClass) {
        this.tClass = tClass;
        tableMapper = new TableMapper<>(tClass);
        adapter = new Adapter<>(tClass);
    }
}
