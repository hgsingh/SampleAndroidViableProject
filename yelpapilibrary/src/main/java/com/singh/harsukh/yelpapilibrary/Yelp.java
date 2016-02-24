package com.singh.harsukh.yelpapilibrary;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

public class Yelp {

    private OAuthService service;
    private Token accessToken;

    /**
     * Setup the Yelp API OAuth credentials.
     * <p>
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param clientKey    Client key
     * @param clientSecret client secret
     * @param token        Token
     * @param tokenSecret  Token secret
     */
    public Yelp(String clientKey, String clientSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpOAuth.class).apiKey(clientKey).apiSecret(clientSecret).build();
        this.accessToken = new Token(token, tokenSecret);
    }


    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @return JSON string response
     */
    public String search(String term, double latitude, double longitude) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }

    /**
     * Search with term string location.
     *
     * @param term Search term
     * @return JSON string response
     */
    public String search(String term, String location) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("location", location);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        return response.getBody();
    }
}
