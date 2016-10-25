package com.codepath.nytarticles.model;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private String beginDate =  "";
   // static String beginDateDisplay =  "";
    private String sortOrder = "";
    private List<String> newsDesks = new ArrayList<>();

    // Default constractor
    public Filter(){};

    public Filter(String beginDate, String sortOrder, List<String> newsDesks) {
        this.beginDate  = beginDate;
        this.sortOrder = sortOrder;
        this.newsDesks = newsDesks;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<String> getNewsDesks() {
        return newsDesks;
    }

    public void setNewsDesks(List<String> newsDesks) {
        this.newsDesks = newsDesks;
    }

}
