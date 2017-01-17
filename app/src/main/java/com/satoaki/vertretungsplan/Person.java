package com.satoaki.vertretungsplan;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by satoa on 15.01.2017.
 */

public class Person {
    String Name;
    String Klasse;
    String KlasseId;
    ArrayList<String> Faecher;

    public Person(String name,String klasse){
        Name = name;Klasse = klasse;
        KlasseId = S.getKlassenId(Klasse);
        Log.i(TAG, "Person: "+Name+" "+Klasse+" "+KlasseId+" erfolgreich erstellt");
    }
    void setFaecher(String faecher){

    }
    void addFach(String Fach){
        Faecher.add(Fach);
        Log.i(TAG, "addFach: Sucessfully added: "+Fach);
    }

    static final String TAG = "main Person";
}
