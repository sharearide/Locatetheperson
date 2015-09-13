package com.example.bunty.locatetheperson;

/**
 * Created by bunty on 3/28/2015.
 */
public class Person {
    String name;
    String time;
    String distance;


    public Person() {

    }

    public Person(String time, String distance, String name) {
        this.time = time;
        this.distance = distance;
        this.name = name;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
