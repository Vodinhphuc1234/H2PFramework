package com.h2p.mappers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by vodinhphuc on 02/01/2023
 */
public interface ITableMapper {
    List<String> getColumns(Class<?> tClass);

    Field getFieldByColumnName(Class<?> tClass, String columnName);

    public List<Field> getFields(Class<?> tClass);

    String getTableName(Class<?> tClass);

    Map<Field, String> getIdMap(Class<?> tClass);

    List<Field> getOneToManyColumnFields(Class<?> tClass);

    List<Field> getManyToOneColumnFields(Class<?> tClass);

    List<Field> getOneToOneParentColumnFields(Class<?> tClass);

    List<Field> getOneToOneChildColumnFields(Class<?> tClass);

    public String getManyToOneLeftJoinString(Class<?> tClass);

    String getOneToManyLeftJoinString(Class<?> tClass);

    String getOneToOneParentLeftJoinString(Class<?> tClass);

    String getOneToOneChildLeftJoinString(Class<?> tClass);
    boolean checkAutoId (Class<?> tClass);

    Object createInstance(Class<?> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
