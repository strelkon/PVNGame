package com.pvn;

/**
 * Created by Nike on 12.02.17.
 */
public class Strategy {
    public Strategy(){

    }

    public Strategy(String id) {
        this.id = id;
    }

    private String id;
    private String shortDescription;
    private String longDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }
}
