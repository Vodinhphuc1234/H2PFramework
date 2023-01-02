package com.h2p.queries;

import java.util.List;

/**
 * Created by vodinhphuc on 30/12/2022
 */
public class UpdateQueryHandler<T> extends IQueryHandler<T> {
    public UpdateQueryHandler(Class<T> tClass) {
        super( tClass);
    }
    @Override
    List<T> handleQuery(boolean deep) {
        return null;
    }
}
