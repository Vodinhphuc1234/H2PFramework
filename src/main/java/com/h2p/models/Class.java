package com.h2p.models;

import com.h2p.annotations.Column;
import com.h2p.annotations.ID;
import com.h2p.annotations.OneToMany;
import com.h2p.annotations.Table;

import java.util.List;

/**
 * Created by vodinhphuc on 29/12/2022
 */
@Table(name = "class")
public class Class {
    @Column(name = "class_id")
    @ID(auto = true)
    int classId;
    @Column(name = "class_name")
    String className;
    @Column(name = "class_code")
    String classCode;
    @OneToMany( referred = "class_id", foreignKey = "class")
    List<User> users;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Class{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                ", users=" + users +
                '}';
    }
}
