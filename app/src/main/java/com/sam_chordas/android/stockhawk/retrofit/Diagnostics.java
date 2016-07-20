package com.sam_chordas.android.stockhawk.retrofit;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.Url;

/**
 * Created by Ayush on 20-07-2016.
 */
public class Diagnostics {
    private List<Url> url = new ArrayList<Url>();
    private String publiclyCallable;
    private List<Cache> cache = new ArrayList<Cache>();
    private List<Query_> query = new ArrayList<Query_>();
    private Javascript javascript;
    private String userTime;
    private String serviceTime;
    private String buildVersion;

    public String getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
    }

    public List<Cache> getCache() {
        return cache;
    }

    public void setCache(List<Cache> cache) {
        this.cache = cache;
    }

    public Javascript getJavascript() {
        return javascript;
    }

    public void setJavascript(Javascript javascript) {
        this.javascript = javascript;
    }

    public String getPubliclyCallable() {
        return publiclyCallable;
    }

    public void setPubliclyCallable(String publiclyCallable) {
        this.publiclyCallable = publiclyCallable;
    }

    public List<Query_> getQuery() {
        return query;
    }

    public void setQuery(List<Query_> query) {
        this.query = query;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public List<Url> getUrl() {
        return url;
    }

    public void setUrl(List<Url> url) {
        this.url = url;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }
}
