package com.satoaki.vertretungsplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class addFaecher extends Fragment {

    final String TAG = "main addFach";
    View v;
    ViewGroup mContainerView;
    ViewGroup getMaddFachViewFake;
    AutoCompleteTextView inp_Fach;
    TextView imp_Id;
    Button cb_leistung;
    ViewGroup maddFachView;
    EditText fake1;
    Button fake2;
    boolean LeistungsKurs = false;
    boolean toggle = true;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_faecher, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle("Fächer verwalten");
        fake1 = (EditText)v.findViewById(R.id.addFach_fake1);
        fake2 = (Button)v.findViewById(R.id.addFach_fake2);
        maddFachView = (ViewGroup) v.findViewById(R.id.addFriendContainer);
        getMaddFachViewFake = (ViewGroup) v.findViewById(R.id.addFriendContainerFake);
        mContainerView = (ViewGroup) v.findViewById(R.id.addFach_container);
        imp_Id = (TextView) v.findViewById(R.id.addFach_inp_num);
        cb_leistung = (Button) v.findViewById(R.id.addFach_leistung);
        if (Integer.parseInt(S.p.get(0).KlasseId) < 66) {
            imp_Id.setVisibility(View.GONE);
            cb_leistung.setVisibility(View.GONE);
            fake1.setVisibility(View.GONE);
            fake2.setVisibility(View.GONE);
        }
        getMaddFachViewFake.setVisibility(View.GONE);
        maddFachView.animate().translationYBy(-500).setDuration(1);
        inp_Fach = (AutoCompleteTextView) v.findViewById(R.id.addFach_input);
        keyListener onkl = new keyListener();
        inp_Fach.setOnKeyListener(onkl);
        imp_Id.setOnKeyListener(onkl);
        inp_Fach.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, S.VaidFaecherName));
        cb_leistung.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                toggleLeistung();
            }
        });
        maddFachView.setVisibility(View.VISIBLE);
        return v;
    }

    boolean iconplus = true;
    public void onPrepareOptionsMenu(Menu menu) {
        final MenuItem btnAddFach = menu.findItem(R.id.actionBar_addFach);
        btnAddFach.setVisible(true);

        btnAddFach.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                toggle();
                if(iconplus) {
                    iconplus = false;
                    btnAddFach.setIcon(R.drawable.ic_haken);
                }else {
                    checkFach();
                    iconplus = true;
                    btnAddFach.setIcon(R.drawable.ic_add);
                }
                return false;
            }
        });
    }

    private void checkFach(){
        boolean found = false;
        for(int b=0;b<S.VaidFaecherName.length;b++)
            if ((imp_Id.getText().length()>0&&!found||Integer.parseInt(S.p.get(0).KlasseId)<66) && S.VaidFaecherName[b].toLowerCase().equals(inp_Fach.getText().toString().toLowerCase())) {
                found = true;
                View kb = getActivity().getCurrentFocus();
                if (kb != null) {
                }
                v.findViewById(R.id.addFach_empty).setVisibility(View.INVISIBLE);
                S.p.get(0).addFach(codiereKlasse(b,S.ValidFaecherKuerzel[b].toString(),imp_Id.getText().toString()));
                if(imp_Id.getText().length()>0)
                    inp_Fach.setText(inp_Fach.getText()+"-"+imp_Id.getText());
                if(LeistungsKurs)
                    inp_Fach.setText("(LK) "+inp_Fach.getText());
                addItem();
            }
        if (!found){
            Toast.makeText(getContext(),"das Fach existiert nicht!",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onKey: Invalid Fach");
        }
    }


    private void toggleLeistung(){
        if(LeistungsKurs){
            LeistungsKurs = false;
            cb_leistung.setText(" 2-Stündig");
        }else{
            LeistungsKurs = true;
            cb_leistung.setText(" 4-Stündig");
        }
    }

    private void toggle() {
        if (toggle){
            toggle=false;
            maddFachView.animate().translationY(0).setDuration(300);
            imp_Id.setFocusableInTouchMode(true);
            inp_Fach.setFocusableInTouchMode(true);
            getMaddFachViewFake.setVisibility(View.VISIBLE);
        }else{
            toggle=true;
            maddFachView.animate().translationYBy(-500).setDuration(400);
            imp_Id.setFocusableInTouchMode(false);
            inp_Fach.setFocusableInTouchMode(false);
            getMaddFachViewFake.setVisibility(View.GONE);
            imp_Id.setText("");
        }
    }

    private void addItem() {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.addfaecher_kachel, mContainerView, false);
        ((TextView) newView.findViewById(R.id.addFach_listFach)).setText(inp_Fach.getText());

        newView.findViewById(R.id.addFach_btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContainerView.removeView(newView);
                if (mContainerView.getChildCount() == 0) {
                    v.findViewById(R.id.addFach_empty).setVisibility(View.VISIBLE);
                }
            }
        });
        mContainerView.addView(newView, 0);
        inp_Fach.setText("");
        imp_Id.setText("");

    }

    class keyListener implements View.OnKeyListener {
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            inp_Fach.showDropDown();
            if(KeyEvent.ACTION_DOWN==keyEvent.getAction()&&(i==KeyEvent.KEYCODE_NUMPAD_ENTER||i==KeyEvent.KEYCODE_ENTER))
                checkFach();
            return false;
        }
    }
    private String codiereKlasse(int id,String Fach, String Id) {
        StringBuilder KlasseCodiert = new StringBuilder();
        if (Integer.parseInt(S.p.get(0).KlasseId) < 66)
            KlasseCodiert.append(S.ValidFaecherKuerzel[id]);
        else {
            if(id <= S.unbearbeitetLassen){
                KlasseCodiert.append(Fach);
            }else {
                if (LeistungsKurs)
                    KlasseCodiert.append(Fach.toUpperCase());
                else
                    KlasseCodiert.append(Fach.toLowerCase());
            }
            KlasseCodiert.append("-"+Id);
        }
        return KlasseCodiert.toString();
    }
}

