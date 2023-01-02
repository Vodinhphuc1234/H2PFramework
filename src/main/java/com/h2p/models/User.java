package com.h2p.models;

import com.h2p.annotations.*;

/**
 * Created by vodinhphuc on 27/12/2022
 */
@Table(name = "user")
public class User {
    @Column(name = "user_id")
    @ID(auto = true)
    int id;
    @Column(name = "user_name")
    String name;
    @Column(name = "age")
    int age;
    @Nullable
    @Column(name = "class")
    @ManyToOne(referTo="class_id")
    Class aClass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", aClass=" + aClass +
                '}';
    }

    public static void main(String[] args) {

    }
}


