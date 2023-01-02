package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IH2PWritingQuery<T> extends IH2PQuery<T> {
    T object;
    public IH2PWritingQuery(Class<T> tClass, IAdapter adapter) {
        super(tClass, adapter);
    }
    protected abstract PreparedStatement getStatement(String tableName,
                                                      List<String> columnNames,
                                                      List<Object> columnParams,
                                                      List<String> idColumnNames,
                                                      List<Object> idParams) throws SQLException, IllegalAccessException;
    public int execute (T object) {
        try{
            Map<Field, String> idMap = adapter.getTableMapper().getIdMap(tClass);
            List<String> columns = adapter.getTableMapper().getColumns(tClass);
            List<String> idColumns = new ArrayList<>(idMap.values());
            List<Object> params = adapter.toExecuteParams(object, tClass);
            List<Object> idParams = adapter.getId(object, tClass);
            String tableName = adapter.getTableMapper().getTableName(tClass);
            this.object = object;
            PreparedStatement statement = getStatement(tableName, columns, params, idColumns, idParams);
            return statement.executeUpdate();
        } catch (IllegalAccessException | SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
}
