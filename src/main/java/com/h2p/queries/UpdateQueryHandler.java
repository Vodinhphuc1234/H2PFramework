package com.h2p.queries;

import java.util.List;

public class UpdateQueryHandler<T> extends IQueryHandler<T> {
    public UpdateQueryHandler(Class<T> tClass) {
        super( tClass);
    }
    @Override
    List<T> handleQuery(boolean deep) {
        return null;
    }
}
