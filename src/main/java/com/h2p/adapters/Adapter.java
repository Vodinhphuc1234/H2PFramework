package com.h2p.adapters;

import com.h2p.annotations.Column;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class Adapter<T> {
    private final Class<T> tClass;
    private final TableMapper<T> tableMapper;

    public Adapter(Class<T> tClass) {
        this.tClass = tClass;
        tableMapper = new TableMapper<>(tClass);
    }

    public T toObject(ResultSet rs) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException, SQLException {
        Constructor<T> ctor = tClass.getConstructor();
        T object = ctor.newInstance();
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Column.class) == null) continue;
            field.setAccessible(true);
            field.set(object, rs.getObject(field.getAnnotation(Column.class).name()));
        }
        return object;
    }

    public List<Object> toExecuteParams(T object, boolean excludeKey) throws IllegalAccessException {
        List<Field> fields = tableMapper.getFields(excludeKey);
        List<Object> list = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            list.add(field.get(object));
        }
        return list;
    }

    public Object getId(T object) throws IllegalAccessException {
        Field field = tableMapper.getKeyField();
        field.setAccessible(true);
        return field.get(object);
    }

}
