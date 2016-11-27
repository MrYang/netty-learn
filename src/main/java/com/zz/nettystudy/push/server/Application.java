package com.zz.nettystudy.push.server;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.zz.nettystudy.push");
        applicationContext.registerShutdownHook();
    }
}
