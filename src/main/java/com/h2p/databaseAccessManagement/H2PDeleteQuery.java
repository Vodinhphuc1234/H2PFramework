package com.h2p.databaseAccessManagement;
import com.h2p.adapters.Adapter;
import com.h2p.annotations.Nullable;
import com.h2p.annotations.OneToMany;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by vodinhphuc on 01/01/2023
 */
public class H2PDeleteQuery<T> extends IH2PUpsertdelQuery<T>{

    public H2PDeleteQuery(Class<T> tClass) {
        super(tClass);
    }

    @Override
    protected PreparedStatement getStatement(String tableName, List<String> columnNames, List<Object> columnParams, List<String> idColumnNames, List<Object> idParams) throws SQLException, IllegalAccessException {
        String idColumnsStr = "";
        for (String col : idColumnNames) {
            if (idColumnsStr.isEmpty()) {
                idColumnsStr = idColumnsStr.concat(String.format("%s=?", col));
            } else {
                idColumnsStr = idColumnsStr.concat(String.format(",%s=?", col));
            }
        }
        String SQLQuery = String.format("DELETE FROM %s WHERE %s=? ", tableName, idColumnsStr);
        PreparedStatement preparedStatement = this.sqlConnectionManager.getConn().prepareStatement(SQLQuery);

        List<Field> oneToManyColumnFields = tableMapper.getOneToManyColumnFields();
        for (Field field: oneToManyColumnFields){
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            TableMapper tempTableMapper = new TableMapper<>(field.getType());
            String joinTableName = tempTableMapper.getTableName();
            String foreignKey = oneToMany.foreignKey();
            String referredColumn = oneToMany.referred();
            Object referredValue = adapter.getValueByColumnName(object, referredColumn);

            PreparedStatement updateStatement;
            String updateQuery;
            if (field.getAnnotation(Nullable.class) == null){
                updateQuery = String.format(" DELETE FROM %s WHERE %s.%s = ? ", joinTableName, joinTableName, foreignKey);
            } else{
                updateQuery = String.format(" UPDATE %s SET %s.%s = null WHERE %s.%s = ? ",
                        joinTableName, joinTableName, foreignKey,
                        joinTableName, foreignKey);
            }
            updateStatement = this.sqlConnectionManager.getConn().prepareStatement(updateQuery);
            updateStatement.setObject(1, referredValue);
        }
        // params
        int i = 1;
        for (Object param : idParams) {
            preparedStatement.setObject(i++, param);
        }
        return preparedStatement;
    }
}
