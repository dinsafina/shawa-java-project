package com.oop_inheritance;

public class Plane implements Flyable{

    @Override
    public void fly() {
        System.out.println("Самолет полетел");
    }
}
