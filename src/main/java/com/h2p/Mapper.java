package com.h2p;

import java.util.Arrays;

/**
 * Created by vodinhphuc on 27/12/2022
 */
public class Mapper<T> {
    private final Class<T> clazz;
    public Mapper(Class<T> clazz) {
        this.clazz = clazz;
    }
    public void getInformation() {
        System.out.println(Arrays.toString(clazz.getDeclaredFields()));
    }
}
