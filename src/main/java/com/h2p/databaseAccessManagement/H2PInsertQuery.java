package com.h2p.databaseAccessManagement;

import com.h2p.databaseConnections.SQLConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class H2PInsertQuery<T> extends IH2PWritingQuery<T> {

    public H2PInsertQuery(Class<T> tClass) {
        super(tClass);
    }
    @Override
    protected PreparedStatement getStatement(String tableName, List<String> columnNames, List<Object> columnParams, List<String> idColumnNames, List<Object> idParams)
            throws SQLException {
        List<String> questions = new ArrayList<>(Collections.nCopies(columnParams.size(), "?"));
        String questionStr = String.join(", ", questions);

        String columnNameStr = String.join(",", columnNames);
        String SQLQuery = String.format("INSERT INTO %s (%s) VALUES(%s)", tableName, columnNameStr, questionStr);

        PreparedStatement preparedStatement = SQLConnectionManager.getInstance().getConn().prepareStatement(SQLQuery);
        int i = 1;
        for (Object param : columnParams) {
            preparedStatement.setObject(i++, param);
        }
        return preparedStatement;
    }
}
