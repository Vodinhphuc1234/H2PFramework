package com.h2p.databaseAccessManagement;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class IH2PUpsertdelQuery<T> extends IH2PQuery<T> {
    T object;
    public IH2PUpsertdelQuery(Class<T> tClass) {
        super(tClass);
    }
    protected abstract PreparedStatement getStatement(String tableName,
                                                      List<String> columnNames,
                                                      List<Object> columnParams,
                                                      List<String> idColumnNames,
                                                      List<Object> idParams) throws SQLException, IllegalAccessException;
    public int execute (T object) {
        try{
            Map<Field, String> idMap = tableMapper.getIdMap();
            List<String> columns = tableMapper.getColumns();
            List<String> idColumns = new ArrayList<>(idMap.values());
            List<Object> params = adapter.toExecuteParams(object);
            List<Object> idParams = adapter.getId(object);
            String tableName = tableMapper.getTableName();
            this.object = object;
            PreparedStatement statement = getStatement(tableName, columns, params, idColumns, idParams);
            return statement.executeUpdate();
        } catch (IllegalAccessException | SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
}
