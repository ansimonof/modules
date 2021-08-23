package org.myorg.modules;

import org.apache.catalina.startup.Tomcat;

public class WebStarter {

    public static void main(String[] args) {
        try {
            Tomcat tomcat = new Tomcat();
            tomcat.setPort(8070);
            tomcat.setHostname("localhost");
            tomcat.getHost().setAppBase(".");
            tomcat.addWebapp("", ".");
            tomcat.start();
            tomcat.getServer().await();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}