package com.h2p.models;

import com.h2p.annotations.*;

import java.util.List;

@Table(name = "class")
public class Class {
    @Column(name = "class_id")
    @ID(auto = true)
    Integer classId;
    @Column(name = "class_name")
    String className;
    @Column(name = "class_code")
    String classCode;
    @OneToMany( referred = "class_id", foreignKey = "class", nullable = true)
    List<User> users;
    @OneToOneChild(referTo="class_id", foreignKey="learning_book_id")
    LearningBook learningBook;

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

    public LearningBook getLearningBook() {
        return learningBook;
    }

    public void setLearningBook(LearningBook learningBook) {
        this.learningBook = learningBook;
    }

    @Override
    public String  toString() {
        return "Class{" +
                "classId=" + classId +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                ", users=" + users +
                ", learningBook=" + learningBook +
                '}';
    }
}
