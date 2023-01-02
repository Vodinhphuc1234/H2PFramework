package com.h2p.databaseAccessManagement;

import com.h2p.adapters.IAdapter;
import com.h2p.adapters.SQLAdapter;
import com.h2p.builders.select.SelectQuery;
import com.h2p.databaseConnections.SQLConnectionManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class H2PSelectQuery<T> extends IH2PReadingQuery<T> {
    public H2PSelectQuery(Class<T> tClass, IAdapter adapter) {
        super(tClass, adapter);
    }
    @Override
    public List<T> select(boolean deep) {
        List<T> ts = new ArrayList<>();
        try {
            SelectQuery selectQuery = selectBuilder.build();
            String SQLNoneRelation = selectQuery.toQuery(adapter.getTableMapper().getTableName(tClass), "");
            Statement statement = SQLConnectionManager.getInstance().getConn().createStatement();
            ResultSet rs = statement.executeQuery(SQLNoneRelation);
            while (rs.next()) {
                ts.add((T) adapter.toObject(rs, tClass));
            }
            // handle relation if you need
            if (deep) {
                String leftJoinQuery = adapter.getTableMapper().getManyToOneLeftJoinString(tClass)
                        + adapter.getTableMapper().getOneToManyLeftJoinString(tClass)
                        + adapter.getTableMapper().getOneToOneChildLeftJoinString(tClass)
                        + adapter.getTableMapper().getOneToOneParentLeftJoinString(tClass);
                String SQLRelation = selectQuery.toQuery(adapter.getTableMapper().getTableName(tClass), leftJoinQuery );
                ResultSet deepRs = statement.executeQuery(SQLRelation);

                Map<Field /*table field*/, Map<Object/*main table id*/, List<Object> /*join table value*/>> mapOneToMany = new HashMap<>();
                Map<Field /*table field*/, Map<Object/*main table id*/, Object /*join table value*/>> mapOneToOneAndManyToOne = new HashMap<>();
                while (deepRs.next()) {
                    T object = (T) adapter.toObject(deepRs, tClass);
                    List<Field> manyToOneColumnAndOneToOneFields = adapter.getTableMapper().getManyToOneColumnFields(tClass);
                    manyToOneColumnAndOneToOneFields.addAll(adapter.getTableMapper().getOneToOneChildColumnFields(tClass));
                    manyToOneColumnAndOneToOneFields.addAll(adapter.getTableMapper().getOneToOneParentColumnFields(tClass));
                    for (Field field : manyToOneColumnAndOneToOneFields) {
                        field.setAccessible(true);
                        Object singleObject = adapter.toObject(deepRs, field.getType());
                        Map<Object, Object> joinValueById = mapOneToOneAndManyToOne.get(field);
                        if (joinValueById == null) {
                            joinValueById = new HashMap<>();
                        }
                        joinValueById.put(adapter.getId(object, tClass), singleObject);
                        mapOneToOneAndManyToOne.put(field, joinValueById);
                    }
                    List<Field> oneToManyColumnFields = adapter.getTableMapper().getOneToManyColumnFields(tClass);
                    for (Field field : oneToManyColumnFields) {
                        field.setAccessible(true);
                        // get type
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> clazz = (Class<?>) stringListType.getActualTypeArguments()[0];

                        Object singleObject = adapter.toObject(deepRs, clazz);

                        Map<Object, List<Object>> joinValuesById = mapOneToMany.get(field);
                        if (joinValuesById == null) {
                            joinValuesById = new HashMap<>();
                        }
                        List<Object> values = joinValuesById.get(adapter.getId(object, tClass));
                        if (values == null) {
                            values = new ArrayList<>();
                        }
                        if (singleObject != null){
                            values.add(singleObject);
                        }
                        joinValuesById.put(adapter.getId(object, tClass), values);
                        mapOneToMany.put(field, joinValuesById);
                    }
                }
                for (T t : ts) {
                    Object key = adapter.getId(t, tClass);
                    for (Map.Entry<Field, Map<Object, Object>> entry : mapOneToOneAndManyToOne.entrySet()) {
                        Field field = entry.getKey();
                        for (Map.Entry<Object, Object> entry1 : entry.getValue().entrySet()) {
                            if (key.equals(entry1.getKey())) {
                                field.setAccessible(true);
                                field.set(t, entry1.getValue());
                            }
                        }
                    }
                    for (Map.Entry<Field, Map<Object, List<Object>>> entry : mapOneToMany.entrySet()) {
                        Field field = entry.getKey();
                        for (Map.Entry<Object, List<Object>> entry1 : entry.getValue().entrySet()) {
                            if (key.equals(entry1.getKey())) {
                                field.setAccessible(true);
                                field.set(t, entry1.getValue());
                            }
                        }
                    }
                }
            }
        } catch (NoSuchMethodException
                 | IllegalAccessException
                 | InstantiationException
                 | InvocationTargetException
                 | SQLException e) {
            e.printStackTrace();
        } finally {
            selectBuilder.reset();
        }


        return ts;
    }
}
