//package com.h2p.queries;
//
//import com.h2p.builders.select.SelectQuery;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by vodinhphuc on 30/12/2022
// */
//public class SelectQueryHandler<T, K> extends IQueryHandler<T, K> {
//    public SelectQueryHandler(Class<T> tClass) {
//        super(tClass);
//    }
//    @Override
//    List<T> handleQuery(boolean deep) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        Statement statement = sqlConnectionManager.getConn().createStatement();
//        ResultSet rs = statement.executeQuery(String.format("select * from %s", tableMapper.getTableName()));
//        List<T> ts = new ArrayList<>();
//        while (rs.next()) {
//            ts.add(adapter.toObject(rs));
//        }
//        if (deep) {
//            Map<Field, List<Object>> mapOneToManyValue = this.queryAllOneToMany();
//            Map<Field, Object> mapManyToOneValue = this.queryAllManyToOne();
//            Map<Field, Object> mapOneToOneHoldkey = this.queryAllOneToOneHoldKey();
//
//            for (T t: ts){
//                for (Map.Entry<Field, List<Object>> entry: mapOneToManyValue.entrySet()){
//                    entry.getKey().setAccessible(true);
//                    entry.getKey().set(t, entry.getValue());
//                }
//
//                for (Map.Entry<Field, Object> entry: mapManyToOneValue.entrySet()){
//                    entry.getKey().setAccessible(true);
//                    entry.getKey().set(t, entry.getValue());
//                }
//                for (Map.Entry<Field, Object> entry: mapOneToOneHoldkey.entrySet()){
//                    entry.getKey().setAccessible(true);
//                    entry.getKey().set(t, entry.getValue());
//                }
//            }
//        }
//        return ts;
//    }
//}
