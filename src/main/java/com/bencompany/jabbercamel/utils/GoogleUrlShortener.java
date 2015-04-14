package com.bencompany.jabbercamel.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

 
@Component
@PropertySource({ "classpath:localhost.properties" })
public class GoogleUrlShortener {
	static Logger logger = Logger.getLogger(GoogleUrlShortener.class);
	
	@Autowired
	private Environment env;
	
	@Value("${apikey.google}")
	public String googleAPIKey;
	
	@Value("${xmpp.server") 
	public String xmppServer;
	
    public String shorten(String longUrl) {
    	logger.info("Shortening " + longUrl);
        if (longUrl == null) {
            return longUrl;
        }
        
        try {
        	// set up HTTP Request
            URL url = new URL("https://www.googleapis.com/urlshortener/v1/url?key=" + googleAPIKey );
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("{'longUrl':'" +longUrl + "'}");
            
            writer.close();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = rd.readLine()) != null) {
                sb.append(line + '\n');
            }
            String json = sb.toString();
            return json.substring(json.indexOf("http"), json.indexOf("\"", json.indexOf("http")));
        } catch (Exception e) {
        	logger.error(e.getMessage());
        	return "Sorry, can't get a link for you right now!";
        }
    }
 
}