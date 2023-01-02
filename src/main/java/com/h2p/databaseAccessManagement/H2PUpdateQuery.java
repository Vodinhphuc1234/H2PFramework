package com.h2p.databaseAccessManagement;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class H2PUpdateQuery<T> extends IH2PUpsertdelQuery<T> {

    public H2PUpdateQuery(Class<T> tClass) {
        super(tClass);
    }
    @Override
    protected PreparedStatement getStatement(String tableName, List<String> columnNames, List<Object> columnParams, List<String> idColumnNames, List<Object> idParams) throws SQLException {
        List<String> cloneColumnNames = new ArrayList<>(List.copyOf(columnNames));
        cloneColumnNames.removeAll(idColumnNames);
        String columnNameStr = "";
        for (String col : cloneColumnNames) {
            if (columnNameStr.isEmpty()) {
                columnNameStr = columnNameStr.concat(String.format("%s=?", col));
            } else {
                columnNameStr = columnNameStr.concat(String.format(",%s=?", col));
            }
        }
        String idColumnsStr = "";
        for (String col : idColumnNames) {
            if (idColumnsStr.isEmpty()) {
                idColumnsStr = idColumnsStr.concat(String.format("%s=?", col));
            } else {
                idColumnsStr = idColumnsStr.concat(String.format(",%s=?", col));
            }
        }

        String SQLQuery = String.format("UPDATE %s SET %s WHERE %s ", tableName, columnNameStr, idColumnsStr);
        PreparedStatement preparedStatement = this.sqlConnectionManager.getConn().prepareStatement(SQLQuery);
        // params set
        int i = 1;
        List<Object> cloneColumnParams = new ArrayList<>(columnParams);
        cloneColumnParams.removeAll(idParams);
        for (Object param : cloneColumnParams) {
            preparedStatement.setObject(i++, param);
        }
        // id in where
        for (Object param : idParams) {
            preparedStatement.setObject(i++, param);
        }
        return preparedStatement;
    }
}
