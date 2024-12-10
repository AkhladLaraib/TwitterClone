package com.example.twitterclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("EtgkvjXgQMpCQK2He8o1y5kOoXjLXDWh0s5ELT30")
                // if defined
                .clientKey("RMLTYJPzHMHMVP81WSGaQfjTDmAOAhDN89iOPHzR")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}