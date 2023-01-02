package com.h2p.models;

import com.h2p.annotations.*;

@Table(name = "learning_book")
public class LearningBook {
    @ID(auto = true)
    @Column(name = "learning_book_id")
    Integer learningBookId;
    @Column(name = "learning_book_name")
    String learningBookName;

    @OneToOneParent(referred="class_id", foreignKey="learning_book_id")
    Class aClass;

    public int getLearningBookId() {
        return learningBookId;
    }

    public void setLearningBookId(int learningBookId) {
        this.learningBookId = learningBookId;
    }

    public String getLearningBookName() {
        return learningBookName;
    }

    public void setLearningBookName(String learningBookName) {
        this.learningBookName = learningBookName;
    }

    public Class getaClass() {
        return aClass;
    }

    public void setaClass(Class aClass) {
        this.aClass = aClass;
    }

    @Override
    public String toString() {
        return "LearningBook{" +
                "learningBookId=" + learningBookId +
                ", learningBookName='" + learningBookName + '\'' +
                ", aClass=" + aClass +
                '}';
    }
}
