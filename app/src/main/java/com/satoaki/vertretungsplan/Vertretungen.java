package com.satoaki.vertretungsplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Vertretungen extends Fragment {

    View v;
    TextView tv;
    ViewGroup Event_container;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_vertretungen, container, false);
        Event_container = (ViewGroup) v.findViewById(R.id.Vertretungen_EventContainer);
        getActivity().setTitle("Vertretungen");
        S.p.get(0).UpdateEvents();
        new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();
                while (!S.p.get(0).u2date) {
                }
                if (!S.hasInternet) {
                    Event e = new Event();
                    e.setFach("Kein Internet vorhanden!");
                    addItem(e);
                }else {
                    for (Event e : S.p.get(0).event) {
                        addItem(e);
                    }
                }
            }
        }).start();
        return v;
    }

    private void addItem(Event e) {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.vertretungen_kachel, Event_container, false);
        ((TextView) newView.findViewById(R.id.verkach_Fach)).setText(e.Fach);
        ((TextView) newView.findViewById(R.id.verkach_Art)).setText(e.Art);
        ((TextView) newView.findViewById(R.id.verkach_Lehrer)).setText(e.Vertreter);
        ((TextView) newView.findViewById(R.id.verkach_Raum)).setText(e.Raum);

        newView.findViewById(R.id.verkach_expand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("main Vertretungen", "expand...");
            }
        });
        Event_container.addView(newView, 0);
    }
}
