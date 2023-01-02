package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;

public abstract class IH2PQuery<T> {
    final IAdapter adapter;
    final Class<T> tClass;

    public IH2PQuery(Class<T> tClass, IAdapter adapter) {
        this.tClass = tClass;
        this.adapter = adapter;
    }
}
