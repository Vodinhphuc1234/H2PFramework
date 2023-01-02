package com.h2p.mappers;

import com.h2p.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class TableMapper<T> {
    private final Class<T> tClass;

    public TableMapper(Class<T> tClass) {
        this.tClass = tClass;
    }

    public List<String> getColumns() {
        List<String> list = Arrays.stream(tClass.getDeclaredFields()).map(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null) return column.name();
            return null;
        }).collect(Collectors.toList());
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        return list;
    }

    public Field getFieldByColumnName(String columnName) {
        Optional<Field> field = Arrays.stream(tClass.getDeclaredFields()).filter(f -> {
            Column column = f.getAnnotation(Column.class);
            return column != null && Objects.equals(column.name(), columnName);
        }).findFirst();
        return field.orElse(null);
    }

    public List<Field> getFields() {
        return Stream.of(tClass.getDeclaredFields()).filter(field -> {
            Column column = field.getAnnotation(Column.class);
            return column != null;
        }).collect(Collectors.toList());
    }
    public String getTableName() {
        Table tableAnnotation = tClass.getAnnotation(Table.class);
        if (tableAnnotation.name() == null || tableAnnotation.name().isBlank() || tableAnnotation.name().isEmpty()) {
            return tClass.getSimpleName();
        }
        return tableAnnotation.name();
    }

    public Map<Field, String> getIdMap() {
        Map<Field, String> map = new LinkedHashMap<>();
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(ID.class) != null).collect(Collectors.toList());
        for (Field field: fields){
            map.put(field, field.getAnnotation(Column.class).name());
        }
        return map;
    }
    public List<Field> getOneToManyColumnFields() {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToMany annotation = field.getAnnotation(OneToMany.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }
    public List<Field> getManyToOneColumnFields() {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            ManyToOne annotation = field.getAnnotation(ManyToOne.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }
    public List<Field> getOneToOneHoldKeyColumnFields() {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToOneHoldKey annotation = field.getAnnotation(OneToOneHoldKey.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }
    public String getManyToOneLeftJoinString() {
        List<Field> fields = getManyToOneColumnFields();
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName();
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
    public String getOneToManyLeftJoinString() {
        List<Field> fields = getOneToManyColumnFields();
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName();
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
    public String getOneToOneHoldKeyLeftJoinString() {
        List<Field> fields = getOneToOneHoldKeyColumnFields();
        List<String> leftJoinStrs = new ArrayList<>();
        String tableName = getTableName();
        if (fields != null && fields.size() > 0) {
            leftJoinStrs = fields.stream().map(field -> {
                OneToOneHoldKey oneToOneHoldKey = field.getAnnotation(OneToOneHoldKey.class);
                Column column = field.getAnnotation(Column.class);
                String foreignKey = column.name();
                String referTo = oneToOneHoldKey.referTo();
                // get join table name
                String joinTableName = field.getType().getAnnotation(Table.class).name();
                return String.format(" LEFT JOIN %s ON %s = %s ",
                        joinTableName,
                        String.format(" %s.%s ", joinTableName, referTo),
                        String.format(" %s.%s ", tableName, foreignKey)
                );
            }).collect(Collectors.toList());
        }
        return String.join(",", leftJoinStrs);
    }

    public List<Field> getOneToOneNoneKeyColumnFields() {
        List<Field> fields = List.of(tClass.getDeclaredFields());
        fields = fields.stream().filter(field -> {
            OneToOneNoneKey annotation = field.getAnnotation(OneToOneNoneKey.class);
            return annotation != null;
        }).collect(Collectors.toList());
        return fields;
    }
}
