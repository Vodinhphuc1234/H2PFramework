package com.h2p;

import com.h2p.databaseAccessManagement.H2PInsertQuery;
import com.h2p.databaseAccessManagement.H2PSelectQuery;
import com.h2p.databaseAccessManagement.H2PUpdateQuery;
import com.h2p.databaseAccessManagement.IH2PUpsertdelQuery;
import com.h2p.databaseConnections.SQLConnectionManager;
import com.h2p.models.User;

import java.sql.SQLException;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class Application {

    public static void main(String[] args) throws SQLException, IllegalAccessException {
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
//        ObjectDAM<User> userObjectDAM = new ObjectDAM<>(User.class);
//        User user = new User();
//        user.setAge(10);
//        user.setName("Phuc test insert");
//        System.out.println(userObjectDAM.insert(user));
//        ObjectDAM roomObjectDAM = new ObjectDAM<>(LearningBook.class);
//        System.out.println(roomObjectDAM.select(false));
//        LearningBook room = new LearningBook();
//        room.setLearningBookName("Math");
//
//        roomObjectDAM.detete(1);
        User user = new User();
        user.setName("Vo Dinh Phuc Test Test Test");
        user.setAge(21);
        user.setId(4);
        user.setaClass(null);
        SQLConnectionManager.getInstance().open();
        IH2PUpsertdelQuery<User> upsertdelQuery = new H2PUpdateQuery<>(User.class);
//        List<User> users = userIntegerObjectDAM.select(true,
//                SelectQuery.newBuilder()
//                .where().logic("user_id>4").and().logic("age>22").done()
//                .build()
//        );
//        List<LearningBook> list = userIntegerObjectDAM.select(true, SelectQuery.newBuilder().build());
        System.out.println(upsertdelQuery.execute(user));
//            List<User> users = userIntegerObjectDAM.select(true, SelectQuery.newBuilder().build());
//        System.out.println(users.get(5));
        SQLConnectionManager.getInstance().close();
//        ObjectDAM<Class, Integer> classIntegerObjectDAM = new ObjectDAM<>(Class.class);
//        List<Class> classes = classIntegerObjectDAM.select(true, SelectQuery.newBuilder().build());
//
//        User user = users.get(0);
//        user.setaClass(classes.get(1));
//        userIntegerObjectDAM.update(user, true);
//        SQLConnectionManager.getInstance().open();

//        System.out.println(new HashSet<>(users));
//        SelectQuery selectQuery = SelectQuery
//                .newBuilder()
//                    .where()
//                    .logic("A>B")
//                    .and()
//                    .logic("C>D")
//                    .or()
//                    .logic("E>F")
//                .done()
//                    .groupBy()
//                    .columns("A,B,C")
//                    .having()
//                    .logic("H>K")
//                .done()
//                .done()
//                .build();
//        System.out.println(selectQuery.toQuery());

    }
}
