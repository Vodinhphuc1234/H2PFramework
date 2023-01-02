package com.h2p.databaseAccessManagement;

import java.util.List;

/**
 * Created by vodinhphuc on 01/01/2023
 */
public abstract class IH2PSelectQuery<T> extends IH2PQuery<T> {
    public IH2PSelectQuery(Class<T> tClass) {
        super(tClass);
    }
    public abstract List<T> select(boolean deep);
}
