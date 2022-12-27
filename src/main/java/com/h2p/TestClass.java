package com.h2p;

/**
 * Created by vodinhphuc on 27/12/2022
 */
@MyAnnotation(value = "Table")
public class TestClass {
     String name;
     double age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }
}
