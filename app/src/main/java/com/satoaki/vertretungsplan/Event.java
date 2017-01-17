package com.satoaki.vertretungsplan;

/**
 * Created by satoa on 17.01.2017.
 */

public class Event {
    String Stunden="",Vertreter="",Raum="",Fach="",Art="",Tag="";

    public void setArt(String art) {
        Art = art;
    }
    public void setFach(String fach) {
        Fach = fach;
    }
    public void setRaum(String raum) {
        Raum = raum;
    }
    public void setStunden(String stunden) {
        Stunden = stunden;
    }
    public void setVertreter(String vertreter) {
        Vertreter = vertreter;
    }
    public void setTag(String tag) {
        Tag = tag;
    }
}
