package com.wooteco.nolto.auth.domain;

import org.springframework.beans.factory.annotation.Value;

public class SocialOAuthInfoTest {

    @Value("${oauth.github.client.id}")
    public static String githubClient_id;

    @Value("${oauth.github.client.secret}")
    public static String githubClient_secret;

    @Value("${oauth.github.scope}")
    public static String  githubScope;

    @Value("${oauth.github.redirect-uri}")
    public static String githubRedirect_uri;

    @Value("${oauth.google.client.id}")
    public static String googleClient_id;

    @Value("${oauth.google.client.secret}")
    public static String googleClient_secret;

    @Value("${oauth.google.scope}")
    public static String googleScope;

    @Value("${oauth.google.redirect-uri}")
    public static String googleRedirect_uri;

    public static final String googleResponse_type = "code";
    public static final String googleGrant_type = "authorization_code";
}
