package com.h2p.databaseAccessManagement;

import com.h2p.adapters.Adapter;
import com.h2p.builders.select.SelectQuery;

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

public class H2PSelectQuery<T> extends IH2PSelectQuery<T> {
    public H2PSelectQuery(Class<T> tClass) {
        super(tClass);
    }
    public SelectQuery.Builder selectBuilder = SelectQuery.newBuilder();
    @Override
    public List<T> select(boolean deep) {
        List<T> ts = new ArrayList<>();
        try {
            SelectQuery selectQuery = selectBuilder.build();
            String SQLNoneRelation = selectQuery.toQuery(tableMapper.getTableName(), "");
            Statement statement = sqlConnectionManager.getConn().createStatement();
            ResultSet rs = statement.executeQuery(SQLNoneRelation);
            while (rs.next()) {
                ts.add(adapter.toObject(rs));
            }
            // handle relation if you need
            if (deep) {
                String subQuery = tableMapper.getManyToOneLeftJoinString()
                        + tableMapper.getOneToManyLeftJoinString()
                        + tableMapper.getOneToOneChildLeftJoinString()
                        + tableMapper.getOneToOneParentLeftJoinString();
                String SQLRelation = selectQuery.toQuery(tableMapper.getTableName(), subQuery );
                ResultSet deepRs = statement.executeQuery(SQLRelation);

                Map<Field /*table field*/, Map<Object/*main table id*/, List<Object> /*join table value*/>> mapOneToMany = new HashMap<>();
                Map<Field /*table field*/, Map<Object/*main table id*/, Object /*join table value*/>> mapOneToOneAndManyToOne = new HashMap<>();
                while (deepRs.next()) {
                    T object = adapter.toObject(deepRs);
                    List<Field> manyToOneColumnAndOneToOneFields = tableMapper.getManyToOneColumnFields();
                    manyToOneColumnAndOneToOneFields.addAll(tableMapper.getOneToOneChildColumnFields());
                    manyToOneColumnAndOneToOneFields.addAll(tableMapper.getOneToOneParentColumnFields());
                    for (Field field : manyToOneColumnAndOneToOneFields) {
                        field.setAccessible(true);
                        Adapter<?> tempAdapter = new Adapter<>((Class<Object>) field.getType());
                        Object singleObject = tempAdapter.toObject(deepRs);
                        Map<Object, Object> joinValueById = mapOneToOneAndManyToOne.get(field);
                        if (joinValueById == null) {
                            joinValueById = new HashMap<>();
                        }
                        joinValueById.put(adapter.getId(object), singleObject);
                        mapOneToOneAndManyToOne.put(field, joinValueById);
                    }
                    List<Field> oneToManyColumnFields = tableMapper.getOneToManyColumnFields();
                    for (Field field : oneToManyColumnFields) {
                        field.setAccessible(true);
                        // get type
                        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                        Class<?> clazz = (Class<?>) stringListType.getActualTypeArguments()[0];

                        Adapter<?> tempAdapter = new Adapter<>((Class<Object>) clazz);
                        Object singleObject = tempAdapter.toObject(deepRs);

                        Map<Object, List<Object>> joinValuesById = mapOneToMany.get(field);
                        if (joinValuesById == null) {
                            joinValuesById = new HashMap<>();
                        }
                        List<Object> values = joinValuesById.get(adapter.getId(object));
                        if (values == null) {
                            values = new ArrayList<>();
                        }
                        if (singleObject != null){
                            values.add(singleObject);
                        }
                        joinValuesById.put(adapter.getId(object), values);
                        mapOneToMany.put(field, joinValuesById);
                    }
                }
                for (T t : ts) {
                    Object key = adapter.getId(t);
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
