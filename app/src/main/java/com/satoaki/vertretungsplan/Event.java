package com.satoaki.vertretungsplan;

import android.util.Log;

/**
 * Created by satoa on 17.01.2017.
 */

public class Event {
    String Stunden = "", Vertreter = "", Raum = "", Fach = "", Art = "", Tag = "";

    public void setArt(String art) {
        Art = art;
        Log.i(TAG, "setArt: "+art);
    }

    public void setFach(String fach) {
        Fach = fach;
        Log.i(TAG, "setFach: "+fach);
    }

    public void setRaum(String raum) {
        Raum = raum;
        Log.i(TAG, "setRaum: "+raum);
    }

    public void setStunden(String stunden) {
        Stunden = stunden;
        Log.i(TAG, "setStunden: "+stunden);
    }

    public void setVertreter(String vertreter) {
        Vertreter = vertreter;
        Log.i(TAG, "setVertreter: "+vertreter);
    }

    public void setTag(String tag) {
        Tag = tag;
        Log.i(TAG, "setTag: "+tag);
    }
    final String TAG = "main Event ";
}
