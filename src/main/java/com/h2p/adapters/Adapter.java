package com.h2p.adapters;

import com.h2p.annotations.Column;
import com.h2p.annotations.ManyToOne;
import com.h2p.annotations.OneToOneChild;
import com.h2p.annotations.OneToOneParent;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Adapter<T> {
    private final Class<T> tClass;
    private final TableMapper<T> tableMapper;

    public Adapter(Class<T> tClass) {
        this.tClass = tClass;
        tableMapper = new TableMapper<>(tClass);
    }

    public T toObject(ResultSet rs) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor<T> ctor = tClass.getConstructor();
        T object = ctor.newInstance();
        List<Field> fields = tableMapper.getFields();
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

    public List<Object> toExecuteParams(T object) throws IllegalAccessException {
        List<Field> fields = tableMapper.getFields();
        List<Object> list = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(object);
            if ((field.getAnnotation(ManyToOne.class) != null
                    || field.getAnnotation(OneToOneChild.class) != null
                    || field.getAnnotation(OneToOneParent.class) != null)
                    && value != null) {
                TableMapper<?> tempTableMapper = new TableMapper<>(field.getType());
                String referTo = field.getAnnotation(ManyToOne.class).referTo();
                Field valueField = tempTableMapper.getFieldByColumnName(referTo);
                valueField.setAccessible(true);
                value = valueField.get(value);
            }
            list.add(value);
        }
        return list;
    }

    public List<Object> getId(T object) throws IllegalAccessException {
        List<Field> fields = new ArrayList<>(tableMapper.getIdMap().keySet());
        List<Object> idValues = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            idValues.add(field.get(object));
        }
        return idValues;
    }

    public Object getValueByColumnName(T object, String columnName) throws IllegalAccessException {
        Field field = tableMapper.getFieldByColumnName(columnName);
        field.setAccessible(true);
        return field.get(object);
    }
}
