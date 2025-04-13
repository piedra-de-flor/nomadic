package com.example.Triple_clone.configuration;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

@Component
public class TomcatConfigLogger implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers((Connector connector) -> {
            ProtocolHandler handler = connector.getProtocolHandler();
            if (handler instanceof AbstractProtocol<?> protocol) {
                System.out.println("==================================");
                System.out.println("Tomcat maxThreads: " + protocol.getMaxThreads());
                System.out.println("Tomcat minSpareThreads: " + protocol.getMinSpareThreads());
                System.out.println("Tomcat acceptCount: " + protocol.getAcceptCount());
                System.out.println("==================================");
            } else {
                System.out.println("ProtocolHandler is not instance of AbstractProtocol");
            }
        });
    }
}
