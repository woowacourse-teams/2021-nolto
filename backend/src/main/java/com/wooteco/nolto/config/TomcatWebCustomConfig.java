package com.wooteco.nolto.config;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatWebCustomConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    private static final String SPECIAL_CHARACTER = "<>[\\]^`{|}";
    private static final String QUERY_PARAM_PROPERTY = "relaxedQueryChars";

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> connector.setProperty(QUERY_PARAM_PROPERTY, SPECIAL_CHARACTER));
    }
}
