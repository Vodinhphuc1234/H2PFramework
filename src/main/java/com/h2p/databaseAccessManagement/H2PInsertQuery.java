package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;
import com.h2p.databaseConnections.SQLConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class H2PInsertQuery<T> extends IH2PWritingQuery<T> {

    public H2PInsertQuery(Class<T> tClass, IAdapter adapter) {
        super(tClass, adapter);
    }
    @Override
    protected PreparedStatement getStatement(String tableName, List<String> columnNames, List<Object> columnParams, List<String> idColumnNames, List<Object> idParams)
            throws SQLException {
        List<String> cloneColumns = new ArrayList<>(columnNames);
        List<Object> cloneParams = new ArrayList<>(columnParams);
        if (this.adapter.getTableMapper().checkAutoId(tClass)) {
            cloneColumns.removeAll(idColumnNames);
            cloneParams.removeAll(idParams);
        }

        List<String> questions = new ArrayList<>(Collections.nCopies(cloneColumns.size(), "?"));
        String questionStr = String.join(", ", questions);
        String columnNameStr = String.join(",", cloneColumns);
        String SQLQuery = String.format("INSERT INTO %s (%s) VALUES(%s)", tableName, columnNameStr, questionStr);

        System.out.println(SQLQuery);
        PreparedStatement preparedStatement = SQLConnectionManager.getInstance().getConn().prepareStatement(SQLQuery);
        int i = 1;
        for (Object param : cloneParams) {
            preparedStatement.setObject(i++, param);
        }
        return preparedStatement;
    }
}
