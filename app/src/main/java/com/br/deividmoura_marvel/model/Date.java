
package com.br.deividmoura_marvel.model;

import com.google.gson.annotations.Expose;

public class Date {

    @Expose
    private java.util.Date date;
    @Expose
    private String type;

    public Date() {
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
