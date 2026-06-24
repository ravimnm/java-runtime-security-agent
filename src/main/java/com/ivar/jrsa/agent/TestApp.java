package com.ivar.jrsa.agent;

public class TestApp {

    public void launchProcess() throws Exception {
        Runtime.getRuntime().exec("notepad.exe");
    }

    public static void main(String[] args) throws Exception {
        TestApp app = new TestApp();
        app.launchProcess();
    }
}