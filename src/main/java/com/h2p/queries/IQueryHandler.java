package com.h2p.queries;

import com.h2p.adapters.Adapter;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public abstract class IQueryHandler<T> {
    final SQLConnectionManager sqlConnectionManager;
    final TableMapper<T> tableMapper;
    final Adapter<T> adapter;
    Class<T> tClass;
    public IQueryHandler(Class<T> tClass) {
        this.tableMapper = new TableMapper<>(tClass);
        this.adapter = new Adapter<>(tClass);
        this.tClass = tClass;
        sqlConnectionManager = SQLConnectionManager.getInstance();
    }
    abstract List<T> handleQuery(boolean deep) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
    public List<T> execute(boolean deep) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        sqlConnectionManager.open();
        List<T> res = handleQuery(deep);
        sqlConnectionManager.close();
        return res;
    }

    // dung bridge, de xay dung cay query
}
