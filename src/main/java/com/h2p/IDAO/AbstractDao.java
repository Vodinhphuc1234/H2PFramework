package com.h2p.IDAO;

import com.h2p.adapters.IAdapter;
import com.h2p.adapters.SQLAdapter;
import com.h2p.mappers.TableMapper;

import java.util.List;

/**
 * Created by vodinhphuc on 02/01/2023
 */
public abstract class AbstractDao <T> {
    final IAdapter adapter;

    protected AbstractDao() {
        TableMapper tableMapper = new TableMapper();
        this.adapter = new SQLAdapter(tableMapper);
    }

    public T findById (Object... ids){
        return null;
    }
    public List<T> findAll(){
        return null;
    }

    public int save(T object){
        return 0;
    }
    public int delete(T object){
        return 0;
    }
}
