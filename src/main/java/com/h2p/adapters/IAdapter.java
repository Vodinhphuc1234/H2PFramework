package com.h2p.adapters;

import com.h2p.mappers.ITableMapper;
import com.h2p.mappers.TableMapper;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.util.List;

/**
 * Created by vodinhphuc on 02/01/2023
 */
public interface IAdapter {
    Object toObject(ResultSet rs, Class<?> tClass) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
    List<Object> toExecuteParams(Object object, Class<?> tClass) throws IllegalAccessException;
    List<Object> getId(Object object, Class<?> tClass) throws IllegalAccessException;
    ITableMapper getTableMapper();
    Object getValueByColumnName(Object object, String columnName, Class<?> tClass) throws IllegalAccessException;
}
