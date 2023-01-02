package com.h2p.models;

import com.h2p.annotations.Column;
import com.h2p.annotations.ID;
import com.h2p.annotations.OneToOneHoldKey;
import com.h2p.annotations.Table;

/**
 * Created by vodinhphuc on 29/12/2022
 */
@Table(name = "learning_book")
public class LearningBook {
    @ID(auto = true)
    @Column(name = "learning_book_id")
    int learningBookId;
    @Column(name = "learning_book_name")
    String learningBookName;

    @Column(name = "learning_book_id")
    @OneToOneHoldKey(referTo = "class_id")
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

    @Override
    public String toString() {
        return "LearningBook{" +
                "learningBookId=" + learningBookId +
                ", learningBookName='" + learningBookName + '\'' +
                ", aClass=" + aClass +
                '}';
    }
}
