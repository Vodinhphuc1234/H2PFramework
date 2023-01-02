package com.h2p.databaseAccessManagement;

import com.h2p.builders.select.SelectQuery;

import java.util.List;

public abstract class IH2PReadingQuery<T> extends IH2PQuery<T> {
    public SelectQuery.Builder selectBuilder = SelectQuery.newBuilder();
    public IH2PReadingQuery(Class<T> tClass) {
        super(tClass);
    }
    public abstract List<T> select(boolean deep);
}
