package com.satoaki.vertretungsplan;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by satoa on 14.01.2017.
 */
//TODO: if Kursstufe Chi ->Chin
public class S {

    static ArrayList<Person> p;
    static ArrayList<String> vertretungen;
    static boolean hasInternet = false;

    static boolean init(){
        p = new ArrayList<>();
        vertretungen = new ArrayList<>();
        File Ordner =  new File(Environment.getExternalStorageDirectory(),"Vertretungsplan");
        setValidKlassen();
        Log.i(TAG, "Initializing Done");
        if(!Ordner.exists()) {
            //Ordner.mkdir();
            return true;
        }else{
            Update();
            return false;
        }
    }
    static void Update(){
        for(Person pp:p)
            pp.Faecher.clear();
        p.clear();
        try{
            File assets = new File(Ordner,"assets.txt");
            Log.i(TAG, String.valueOf(assets.exists()));
            BufferedReader bf = new BufferedReader(new FileReader(new File(Ordner, "assets.txt")));
            EinstellWerte = bf.readLine();
            p.add(new Person(bf.readLine(), bf.readLine()));
            p.get(0).setFaecher(bf.readLine());
        }catch (Exception e){
            Log.i(TAG, "ERROR: Something went wrong reading File");
            System.exit(0);
        }

    }
    static void Speichern(){

    }
    static String getKlassenId(String klasse){
        String klassenId = "";
        for (int i=0;i<ValidKlassen.length;i++)
            if(ValidKlassen[i].equals(klasse)) {
                if (i<10)
                    klassenId = "0";
                klassenId += String.valueOf(i);
            }
        if(klassenId=="")
            Log.i(TAG, "ERROR: Invalid Klass: "+klassenId);
        else
            Log.i(TAG, "Successfully Generated KlassenId: "+klassenId);
        return klassenId;
    }

    private static void setValidKlassen(){
        char[] kuerzel = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'k', 'l', 'm' };
        int[] anz = { 11, 10, 11, 11, 12, 10 };
        int k = 1;
        ValidKlassen[0]="";
        for (int i = 5; i < 11; i++) {
            for (int j = 0; j < anz[i - 5]; j++) {
                ValidKlassen[k] = (String.valueOf(i) + String.valueOf(kuerzel[j]));
                k++;
            }
        }
        ValidKlassen[k]="Ks-1";
        ValidKlassen[k+1]="Ks-2";
    }
    static String EinstellWerte = "001";
    static String[] VaidFaecherName = {"Spanisch","Italienisch","Evangelische Religion","Katholische Religion","Religion(Leistungskurs)","Deutsch","Englisch","Französisch","Latein","Chinesisch","Russisch",
            "Mathe","Ethik", "Informatik","Physik","Chemie","Biologie","Natur Phänomene","Naturwissenschaften und Technik",
            "Bildene Kunst","Gemeinschaftskunde","Geschichte","Erdkunde","Medien","Sport(Gemischt)","Sport(Weiblich)","Sport(Männlich)","Klassenlehrerin",
            "Literatur","Astronomie","Wirtschaft","Darstellende Geometrie","Psychologie","Seminarkurs","Kunstprofil intermedialer Kommunikation",};
    static String[] ValidFaecherKuerzel = {"Spa","Ita","evR","kR","Rel","D","E","F","L","Chi","rus",
            "M","Eth","inf","Ph","Ch","Bio","NP","NwT","Bk","Gk","G","Ek","Medien","SPO","SW","SM","KL",
            "lit","ast","wir","dg","psy","sem","KimKo"};
    static String[] ValidKlassen = new String[68];
    static String[] ValidArten  = { "Vertretung", "Raum", "Vtr. ohne Lehrer", "Entfall" };
    static File Ordner;
    static final int unbearbeitetLassen = 3;
    static final String TAG = "main Static";
}

