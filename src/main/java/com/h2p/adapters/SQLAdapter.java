package com.h2p.adapters;

import com.h2p.annotations.Column;
import com.h2p.annotations.ManyToOne;
import com.h2p.annotations.OneToOneChild;
import com.h2p.annotations.OneToOneParent;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLAdapter implements IAdapter {
    public TableMapper tableMapper;
    public SQLAdapter(TableMapper tableMapper) {
        this.tableMapper = tableMapper;
    }

    @Override
    public Object toObject(ResultSet rs, Class<?> tClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Object object = tableMapper.createInstance(tClass);
        List<Field> fields = tableMapper.getFields(tClass);
        for (Field field : fields) {
            if (field.getAnnotation(OneToOneChild.class) != null
                    || field.getAnnotation(OneToOneParent.class) != null
                    || field.getAnnotation(ManyToOne.class) != null)
                continue;
            if (field.getAnnotation(Column.class) == null) continue;
            field.setAccessible(true);
            Object setObject;
            try {
                setObject = rs.getObject(field.getAnnotation(Column.class).name());
            } catch (SQLException e) {
                setObject = null;
            }
            field.set(object, setObject);
        }
        for (Field field : fields){
            field.setAccessible(true);
            Object value = field.get(object);
            if (value != null){
                return object;
            }
        }
        return null;
    }

    @Override
    public List<Object> toExecuteParams(Object object, Class<?> tClass) throws IllegalAccessException {
        List<Field> fields = tableMapper.getFields(tClass);
        List<Object> list = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);
            if ((field.getAnnotation(ManyToOne.class) != null
                    || field.getAnnotation(OneToOneChild.class) != null
                    || field.getAnnotation(OneToOneParent.class) != null)
                    && value != null) {
                String referTo = field.getAnnotation(ManyToOne.class).referTo();
                Field valueField = tableMapper.getFieldByColumnName(field.getType(), referTo);
                valueField.setAccessible(true);
                value = valueField.get(value);
            }
            list.add(value);
        }
        return list;
    }

    @Override
    public List<Object> getId(Object object, Class<?> tClass) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>(tableMapper.getIdMap(tClass).keySet());
        List<Object> idValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            idValues.add(field.get(object));
        }
        return idValues;
    }

    @Override
    public TableMapper getTableMapper() {
        return this.tableMapper;
    }

    @Override
    public Object getValueByColumnName(Object object, String columnName, Class<?> tClass) throws IllegalAccessException {
        Field field = tableMapper.getFieldByColumnName(tClass, columnName);
        field.setAccessible(true);
        return field.get(object);
    }
}
