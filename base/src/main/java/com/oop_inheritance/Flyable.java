package com.oop_inheritance;

import java.sql.DataTruncation;

public interface Flyable {

    void fly();

    default int minDistance(){
        return 10;
    }

    default void startFly(String name, int distance) {
        System.out.println(name + " начинает полет на дистанцию " + distance + " метров");
    }
}
