package com.singh.harsukh.yelpapilibrary;

import org.scribe.builder.api.DefaultApi10a;
import org.scribe.extractors.RequestTokenExtractor;
import org.scribe.model.Token;


public class YelpOAuth extends DefaultApi10a {

    //these methods need to be implemented to use OAuth
    /**
     * Service provider for "2-legged" OAuth10a for Yelp API (version 2).
     */
    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token token) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }
}
