package com.ivar.jrsa.agent;

public class VictimApp {

    public void sayHello() {
        System.out.println("Hello from VictimApp");
    }

    public static void main(String[] args) {

        VictimApp app = new VictimApp();

        app.sayHello();
    }
}