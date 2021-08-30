package org.myorg.modules;

import org.apache.catalina.startup.Tomcat;
import org.json.JSONObject;
import org.myorg.modules.util.ConfigUtils;


public class WebStarter {

    private static final String UUID = "org.myorg.module.web";

    public static void main(String[] args) {
        try {
            JSONObject webConfig = ConfigUtils.getJsonConfig(UUID);
            int port = webConfig.getInt("port");
            String host = webConfig.getString("host");

            Tomcat tomcat = new Tomcat();
            tomcat.setPort(port);
            tomcat.setHostname(host);
            tomcat.getHost().setAppBase(".");
            tomcat.addWebapp("", ".");

            // Настройка логов
           /* Host tomcatHost = tomcat.getHost();
            LogbackValve logbackValve = new LogbackValve();
            logbackValve.setFilename("logback-access.xml");
            tomcatHost.getPipeline().addValve(logbackValve);*/

            tomcat.start();
            tomcat.getServer().await();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}