package com.kkumar.chatterbox;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class contactsFragment extends Fragment {
    public contactsFragment(){
        //empty constructor
    }
    ArrayList<friend> dost;
    ListView contacts;
    contactsAdapter adapter;
    ContactList_Handler ch;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view= inflater.inflate(R.layout.fragment_contacts,container,false);

        contacts=(ListView)view.findViewById(R.id.contacts);
        ch=new ContactList_Handler(getActivity());
        adapter=new contactsAdapter(getActivity(),getData());
        contacts.setAdapter(adapter);
        ((BaseAdapter)contacts.getAdapter()).notifyDataSetChanged();
        return view;

    }

    public ArrayList<friend> getData()
    {
        dost=new ArrayList<>();
        Cursor cc=ch.databaseToString();
        while(cc.moveToNext()) {
            friend f = new friend();
            f.setName(cc.getString(1));
            f.setPhone(cc.getString(2));
            dost.add(f);
        }
        return dost;
    }
}