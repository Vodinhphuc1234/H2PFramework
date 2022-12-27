package com.h2p;

import com.h2p.databaseAccessManagement.ObjectDAM;
import com.h2p.models.User;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class Application {
    static final String DB_URL = "jdbc:mysql://localhost/designPattern";
    static final String USER = "root";
    static final String PASS = "Dinhphuc2009.";
    static final String QUERY = "SELECT * FROM Persons";
    public static void main(String[] args) throws SQLException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
//        Class<TestClass> aClass = TestClass.class;
//        for (Field field: aClass.getDeclaredFields() ) {
//            System.out.println(field.getGenericType().getTypeName());
//        }

//        Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
//        Statement stmt = connection.createStatement();
//
//        ResultSet rs = stmt.executeQuery(QUERY);
//
//        while (rs.next()) {
//            System.out.println(rs.getInt("PersonID"));
//            System.out.println(rs.getString("LastName"));
//            System.out.println(rs.getString("FirstName"));
//            System.out.println(rs.getString("Address"));
//            System.out.println(rs.getString("City"));
//        }
//
//        Mapper<TestClass> mapper = new Mapper<>(TestClass.class);
//        mapper.getInformation();

//        System.out.println(TestClass.class.getSimpleName());
        ObjectDAM<User> userObjectDAM = new ObjectDAM<>(User.class);
        User user = new User();
        user.setId(1);
        user.setAge(10);
        user.setName("Phuc test insert");
        System.out.println(userObjectDAM.update(user));
    }
}
