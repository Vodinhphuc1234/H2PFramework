package com.h2p.IDAO;

import com.h2p.adapters.IAdapter;
import com.h2p.adapters.SQLAdapter;
import com.h2p.databaseAccessManagement.*;
import com.h2p.mappers.TableMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vodinhphuc on 02/01/2023
 */
public abstract class AbstractDao<T> {
    final IAdapter adapter;
    final Class<T> tClass;
    final IH2PReadingQuery<T> readingQuery;
    final IH2PWritingQuery<T> insertQuery;
    final IH2PWritingQuery<T> updateQuery;
    final IH2PWritingQuery<T> deleteQuery;

    protected AbstractDao(Class<T> tClass) {
        TableMapper tableMapper = new TableMapper();
        this.adapter = new SQLAdapter(tableMapper);
        this.tClass = tClass;
        this.readingQuery = new H2PSelectQuery<>(tClass, this.adapter);
        this.insertQuery = new H2PInsertQuery<>(tClass, this.adapter);
        this.updateQuery = new H2PUpdateQuery<>(tClass, this.adapter);
        this.deleteQuery = new H2PDeleteQuery<>(tClass, this.adapter);
    }

    public T findByIds(Object... ids) throws Exception {
        List<String> idColumns = new ArrayList<>(adapter.getTableMapper().getIdMap(tClass).values());
        if (ids.length != idColumns.size()) {
            Exception e =  new Exception(String.format("Size of params (%d) and size of id columns (%d) are not matched", ids.length, idColumns.size()));
            e.printStackTrace();
            return null;
        }
        int size = ids.length;
        for (int i = 0; i < size; i++) {
            if (i == 0)
                readingQuery.selectBuilder.where().logic(String.format("%s=%s", idColumns.get(i), ids[i]));
            else readingQuery.selectBuilder.where().and().logic(String.format("%s=%s", idColumns.get(i), ids[i]));
        }
        readingQuery.selectBuilder.where().done();
        return readingQuery.select(true).stream().findFirst().orElse(null);
    }

    public List<T> findAll() {
        return readingQuery.select(true);
    }

    public int save(T object) throws Exception {
        List<Object> idValues = new ArrayList<>(adapter.getId(object, tClass));
        T current = findByIds(idValues.toArray());
        if (current == null){
            return insertQuery.execute(object);
        } else{
            return updateQuery.execute(object);
        }
    }

    public int delete(T object) {
       return deleteQuery.execute(object);
    }
}
