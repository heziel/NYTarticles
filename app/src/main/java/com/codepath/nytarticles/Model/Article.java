package com.codepath.nytarticles.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hezi Eliyahu on 20/10/2016.
 */

public class Article {

    private final static String NYT_URL = "http://nytimes.com/";

    private String webUrl;
    private String headLine;
    private String thumbNail;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    // Article constructor
    public Article(JSONObject jsonObject){
        try{
            this.webUrl = jsonObject.getString("web_url");
            this.headLine = jsonObject.getJSONObject("headline").getString("main");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if ( multimedia.length() > 0 ){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = NYT_URL + multimediaJson.getString("url");
            }
            else {
                this.thumbNail = "";
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    /*
    * this Method is for parsing all the list of articles
    */
    public static ArrayList<Article> fromJsonArray( JSONArray jsonArray){

        ArrayList<Article> results = new ArrayList<>();

        for ( int i = 0  ; i < jsonArray.length(); i++ ) {

           try {
               results.add(new Article(jsonArray.getJSONObject(i)));
           }catch (JSONException e){
               e.printStackTrace();
           }

        }

        return results;
    }
}
