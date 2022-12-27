package com.h2p.databaseAccessManagement;

import com.h2p.adapters.Adapter;
import com.h2p.annotations.ID;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class ObjectDAM<T> {
    SQLConnectionManager sqlConnectionManager;
    TableMapper<T> tableMapper;
    Adapter<T> adapter;

    public ObjectDAM(Class<T> tClass) {
        sqlConnectionManager = SQLConnectionManager.getInstance();
        tableMapper = new TableMapper<>(tClass);
        adapter = new Adapter<>(tClass);
    }

    public List<T> select() throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        this.sqlConnectionManager.open();
        Statement statement = sqlConnectionManager.getConn().createStatement();
        ResultSet rs = statement.executeQuery(String.format("select * from %s", tableMapper.getTableName()));
        List<T> ts = new ArrayList<>();
        while (rs.next()) {
            ts.add(adapter.toObject(rs));
        }
        this.sqlConnectionManager.close();
        return ts;
    }

    public Object insert(T object) throws SQLException, IllegalAccessException {
        this.sqlConnectionManager.open();
        boolean excludeKey = tableMapper.getKeyField().getAnnotation(ID.class).auto();
        List<String> columns = tableMapper.getColumns(excludeKey);
        List<String> questions = new ArrayList<>(Collections.nCopies(columns.size(), "?"));
        List<Object> params = adapter.toExecuteParams(object, excludeKey);

        String tableName = tableMapper.getTableName();
        String columnNameStr = String.join(", ", columns);
        String questionStr = String.join(", ", questions);

        String SQLQuery = String.format("INSERT INTO %s (%s) VALUES(%s)", tableName, columnNameStr, questionStr);

        String[] returnId = {tableMapper.getId()};
        PreparedStatement preparedStatement = this.sqlConnectionManager.getConn().prepareStatement(SQLQuery, returnId);

        int i = 1;
        for (Object param : params) {
            preparedStatement.setObject(i++, param);
        }

        int effectRows = preparedStatement.executeUpdate();
        System.out.printf("%d rows effect%n", effectRows);

        Object res = null;
        try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
            if (rs.next()) {
                res = rs.getObject(1);
            }
        }
        this.sqlConnectionManager.close();
        return res;
    }

    public Object update(T object) throws SQLException, IllegalAccessException {
        this.sqlConnectionManager.open();
        List<String> columns = tableMapper.getColumns(true);
        List<Object> params = adapter.toExecuteParams(object, true);

        String tableName = tableMapper.getTableName();
        String columnNameStr = "";
        for (String col : columns) {
            if (columnNameStr.isEmpty()) {
                columnNameStr = columnNameStr.concat(String.format("%s=?", col));
            } else {
                columnNameStr = columnNameStr.concat(String.format(",%s=?", col));
            }
        }

        String SQLQuery = String.format("UPDATE %s SET %s WHERE %s=? ", tableName, columnNameStr, tableMapper.getId());

        PreparedStatement preparedStatement = this.sqlConnectionManager.getConn().prepareStatement(SQLQuery);

        // params set
        int i = 1;
        for (Object param : params) {
            preparedStatement.setObject(i++, param);
        }
        // id in where
        preparedStatement.setObject(i++, adapter.getId(object));
        int effectRows = preparedStatement.executeUpdate();

        this.sqlConnectionManager.close();

        System.out.printf("%d rows effect%n", effectRows);
        if (effectRows > 0) return adapter.getId(object);
        return null;
    }
}
