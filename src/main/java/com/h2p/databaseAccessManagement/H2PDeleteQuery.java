package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;
import com.h2p.annotations.OneToMany;
import com.h2p.annotations.OneToOneChild;
import com.h2p.annotations.OneToOneParent;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class H2PDeleteQuery<T> extends IH2PWritingQuery<T> {

    public H2PDeleteQuery(Class<T> tClass, IAdapter adapter) {
        super(tClass, adapter);
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
        String SQLQuery = String.format("DELETE FROM %s WHERE %s ", tableName, idColumnsStr);
        PreparedStatement preparedStatement = SQLConnectionManager.getInstance().getConn().prepareStatement(SQLQuery);

        List<Field> oneToManyColumnFields = adapter.getTableMapper().getOneToManyColumnFields(tClass);
        for (Field field : oneToManyColumnFields) {
            OneToMany oneToMany = field.getAnnotation(OneToMany.class);
            // get type
            ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
            Class<?> clazz = (Class<?>) stringListType.getActualTypeArguments()[0];

            String joinTableName = adapter.getTableMapper().getTableName(clazz);
            String foreignKey = oneToMany.foreignKey();
            String referredColumn = oneToMany.referred();
            Object referredValue = adapter.getValueByColumnName(object, referredColumn, tClass);

            PreparedStatement updateStatement;
            String updateQuery;
            if (!oneToMany.nullable()) {
                updateQuery = String.format(" DELETE FROM %s WHERE %s.%s = ? ", joinTableName, joinTableName, foreignKey);
            } else {
                updateQuery = String.format(" UPDATE %s SET %s.%s = null WHERE %s.%s = ? ",
                        joinTableName, joinTableName, foreignKey,
                        joinTableName, foreignKey);
            }
            updateStatement = SQLConnectionManager.getInstance().getConn().prepareStatement(updateQuery);
            updateStatement.setObject(1, referredValue);
            updateStatement.executeUpdate();
        }

        List<Field> oneToOneChildColumnFields = adapter.getTableMapper().getOneToOneParentColumnFields(tClass);
        for (Field field : oneToOneChildColumnFields) {
            field.setAccessible(true);
            OneToOneParent oneToOneParent = field.getAnnotation(OneToOneParent.class);
            String joinTableName = adapter.getTableMapper().getTableName(field.getType());
            String foreignKey = oneToOneParent.foreignKey();
            String referred = oneToOneParent.referred();
            Object referToValue = adapter.getValueByColumnName(object,referred, tClass );

            PreparedStatement updateStatement;
            String updateQuery;
            updateQuery = String.format(" DELETE FROM %s WHERE %s.%s = ? ", joinTableName, joinTableName, foreignKey);
            updateStatement = SQLConnectionManager.getInstance().getConn().prepareStatement(updateQuery);
            updateStatement.setObject(1, referToValue);
            updateStatement.executeUpdate();
        }
        // params
        int i = 1;
        for (Object param : idParams) {
            preparedStatement.setObject(i++, param);
        }
        return preparedStatement;
    }
}
