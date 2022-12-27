package com.h2p.mappers;

import com.h2p.annotations.Column;
import com.h2p.annotations.ID;
import com.h2p.annotations.Table;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    public List<String> getColumns(boolean excludeKey) {
        List<String> list = Arrays.stream(tClass.getDeclaredFields()).map(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null) return column.name();
            return null;
        }).collect(Collectors.toList());
        list = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        if (excludeKey) {
            list = list.stream().filter(str -> !Objects.equals(str, getId())).collect(Collectors.toList());
        }
        return list;
    }

    public List<Field> getFields(boolean excludeKey) {
        List<Field> fields = Stream.of(tClass.getDeclaredFields()).filter(field -> {
            Column column = field.getAnnotation(Column.class);
            return column != null;
        }).collect(Collectors.toList());

        if (excludeKey) {
            fields = fields.stream().filter(field -> {
                Column column = field.getAnnotation(Column.class);
                return column!= null && !column.name().equals(getId());
            }).collect(Collectors.toList());
        }
        return fields;
    }

    public String getTableName() {
        Table tableAnnotation = tClass.getAnnotation(Table.class);
        if (tableAnnotation.name() == null || tableAnnotation.name().isBlank() || tableAnnotation.name().isEmpty()) {
            tClass.getSimpleName();
        }
        return tableAnnotation.name();
    }

    public String getId() {
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(ID.class) != null).collect(Collectors.toList());
        Column column = fields.get(0).getAnnotation(Column.class);
        if (column.name() == null || column.name().isEmpty() || column.name().isBlank()) return "id";
        return column.name();
    }

    public Field getKeyField() {
        List<Field> fields = Arrays.stream(tClass.getDeclaredFields()).filter(f -> f.getAnnotation(ID.class) != null).collect(Collectors.toList());
        return fields.get(0);
    }
}
