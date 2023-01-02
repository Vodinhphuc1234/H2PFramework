package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;
import com.h2p.builders.select.SelectQuery;
import com.h2p.mappers.TableMapper;

import java.util.List;

public abstract class IH2PReadingQuery<T> extends IH2PQuery<T> {
    public SelectQuery.Builder selectBuilder = SelectQuery.newBuilder();
    public IH2PReadingQuery(Class<T> tClass, IAdapter adapter) {
        super(tClass, adapter);
    }
    public abstract List<T> select(boolean relation);
}
