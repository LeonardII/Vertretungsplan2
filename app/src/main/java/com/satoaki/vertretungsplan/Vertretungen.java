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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_vertretungen, container, false);
        TextView tv = (TextView)v.findViewById(R.id.VertretungenTitle);
        getActivity().setTitle("Vertretungen");
        S.p.get(0).UpdateEvents();
        new Thread(new Loading(tv)).start();
        return v;
    }

}
class Loading implements Runnable{
    TextView tv;

    public Loading(TextView tv){
        this.tv = tv;
    }

    @Override
    public void run() {
        while(!S.p.get(0).u2date){}
        if(!S.hasInternet)
            tv.setText("Kein Internet verfuegbar");
        else {
            StringBuilder sb = new StringBuilder();
            for (Event e : S.p.get(0).event) {
                sb.append(e.mergeEvent());
                sb.append("\n");
            }
            Log.i("final", sb.toString());
        }
    }
}
