package com.h2p.mappers;
import com.h2p.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TableMapper implements ITableMapper {
    public TableMapper() {

    }

    @Override
    public List<String> getColumns(Class<?> tClass) {
        List<String> list = Arrays.stream(tClass.getDeclaredFields()).map(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null) return column.name();
            return null;
        }).collect(Collectors.toList());
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return list;
    }

    @Override
    public Field getFieldByColumnName(Class<?> tClass, String columnName) {
        Optional<Field> field = Arrays.stream(tClass.getDeclaredFields()).filter(f -> {
            Column column = f.getAnnotation(Column.class);
            return column != null && Objects.equals(column.name(), columnName);
        }).findFirst();
        return field.orElse(null);
    }

    @Override
    public List<Field> getFields(Class<?> tClass) {
        return Stream.of(tClass.getDeclaredFields()).filter(field -> {
            Column column = field.getAnnotation(Column.class);
            return column != null;
        }).collect(Collectors.toList());
    }

    @Override
    public String getTableName(Class<?> tClass) {
        Table tableAnnotation = (Table) tClass.getAnnotation(Table.class);
        if (tableAnnotation.name() == null || tableAnnotation.name().isBlank() || tableAnnotation.name().isEmpty()) {
            return tClass.getSimpleName();
        }
        return tableAnnotation.name();
    }

    @Override
    public Map<Field, String> getIdMap(Class<?> tClass) {
        Map<Field, String> map = new LinkedHashMap<>();
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(ID.class) != null).collect(Collectors.toList());
        for (Field field: fields){
            map.put(field, field.getAnnotation(Column.class).name());
        }
        return map;
    }

    @Override
    public List<Field> getOneToManyColumnFields(Class<?> tClass) {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToMany annotation = field.getAnnotation(OneToMany.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }
    @Override
    public List<Field> getManyToOneColumnFields(Class<?> tClass) {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            ManyToOne annotation = field.getAnnotation(ManyToOne.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }

    @Override
    public List<Field> getOneToOneParentColumnFields(Class<?> tClass) {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToOneParent annotation = field.getAnnotation(OneToOneParent.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }

    @Override
    public List<Field> getOneToOneChildColumnFields(Class<?> tClass) {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToOneChild annotation = field.getAnnotation(OneToOneChild.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }

    @Override
    public String getManyToOneLeftJoinString(Class<?> tClass) {
        List<Field> fields = getManyToOneColumnFields(tClass);
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName(tClass);
        if (fields != null && fields.size() > 0) {
            leftJoinStrs = fields.stream().map(field -> {
                ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
                Column column = field.getAnnotation(Column.class);
                String foreignKey = column.name();
                String referTo = manyToOne.referTo();
                // get join table name
                String joinTableName = field.getType().getAnnotation(Table.class).name();
                return String.format(" LEFT JOIN %s ON %s = %s ",
                        joinTableName,
                        String.format("%s.%s", joinTableName, referTo),
                        String.format("%s.%s", tableName, foreignKey)
                );
            }).collect(Collectors.toList());
        }
        return String.join(",", leftJoinStrs);
    }

    @Override
    public String getOneToManyLeftJoinString(Class<?> tClass) {
        List<Field> fields = getOneToManyColumnFields(tClass);
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName(tClass);
        if (fields != null && fields.size() > 0) {
            leftJoinStrs = fields.stream().map(field -> {
                OneToMany oneToMany = field.getAnnotation(OneToMany.class);
                String foreignKey = oneToMany.foreignKey();
                String referred = oneToMany.referred();
                // get join table name
                ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
                Class<?> clazz = (Class<?>) stringListType.getActualTypeArguments()[0];

                String joinTableName = clazz.getAnnotation(Table.class).name();
                return String.format(" LEFT JOIN %s ON %s = %s ",
                        joinTableName,
                        String.format("%s.%s", joinTableName, foreignKey),
                        String.format("%s.%s", tableName, referred)
                );
            }).collect(Collectors.toList());
        }
        return String.join(",", leftJoinStrs);
    }

    @Override
    public String getOneToOneParentLeftJoinString(Class<?> tClass) {
        List<Field> fields = getOneToOneParentColumnFields(tClass);
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName(tClass);
        if (fields != null && fields.size() > 0) {
            leftJoinStrs = fields.stream().map(field -> {
                OneToOneParent oneToOneParent = field.getAnnotation(OneToOneParent.class);
                String foreignKey = oneToOneParent.foreignKey();
                String referred = oneToOneParent.referred();
                // get join table name
                String joinTableName = field.getType().getAnnotation(Table.class).name();
                return String.format(" LEFT JOIN %s ON %s = %s ",
                        joinTableName,
                        String.format(" %s.%s ", tableName, foreignKey),
                        String.format(" %s.%s ", joinTableName, referred)
                );
            }).collect(Collectors.toList());
        }
        return String.join(",", leftJoinStrs);
    }

    @Override
    public String getOneToOneChildLeftJoinString(Class<?> tClass) {
        List<Field> fields = getOneToOneChildColumnFields(tClass);
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName(tClass);
        if (fields != null && fields.size() > 0) {
            leftJoinStrs = fields.stream().map(field -> {
                OneToOneChild oneToOneChild = field.getAnnotation(OneToOneChild.class);
                String foreignKey = oneToOneChild.foreignKey();
                String referTo = oneToOneChild.referTo();
                // get join table name
                String joinTableName = field.getType().getAnnotation(Table.class).name();
                return String.format(" LEFT JOIN %s ON %s = %s ",
                        joinTableName,
                        String.format(" %s.%s ", joinTableName, foreignKey),
                        String.format(" %s.%s ", tableName, referTo)
                );
            }).collect(Collectors.toList());
        }
        return String.join(",", leftJoinStrs);
    }

    @Override
    public Object createInstance(Class<?> tClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> ctor = tClass.getConstructor();
        return ctor.newInstance();
    }
}
