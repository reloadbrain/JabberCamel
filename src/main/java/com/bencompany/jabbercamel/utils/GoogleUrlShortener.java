package com.bencompany.jabbercamel.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
 
@Component
public final class GoogleUrlShortener {
	static Logger logger = Logger.getLogger(GoogleUrlShortener.class);
	
    public static String shorten(String longUrl) {
    	logger.info("Shortening " + longUrl);
        if (longUrl == null) {
            return longUrl;
        }
        
        try {
            URL url = new URL("https://www.googleapis.com/urlshortener/v1/url");
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
            return json;
            //return json.substring(json.indexOf("http"), json.indexOf("\"", json.indexOf("http")));
        } catch (MalformedURLException e) {
        	return e.getMessage();
        } catch (IOException e) {
        	return e.getMessage();
        }
    }
 
}