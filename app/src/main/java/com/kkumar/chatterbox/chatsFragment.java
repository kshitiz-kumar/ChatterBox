package com.kkumar.chatterbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.kkumar.chatterbox.friendAdapter;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class chatsFragment extends Fragment {

    ArrayList<friend> chats;
    ListView people;
    friendAdapter adapter;
    ContactList_Handler ch;
    ImageButton addcontact;
    EditText takename;
    EditText takephone;
    LinearLayout adduserlayout;
    ImageButton dontadduser;
    ImageButton adduserinDB;


    public chatsFragment(){
        //empty constructor
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view= inflater.inflate(R.layout.fragment_chats,container,false);

        addcontact=(ImageButton)view.findViewById(R.id.adduser);
        takename=(EditText)view.findViewById(R.id.takename);
        takephone=(EditText)view.findViewById(R.id.takephone);
        dontadduser=(ImageButton)view.findViewById(R.id.dontadduser);
        adduserinDB=(ImageButton)view.findViewById(R.id.putuserinDB);
        adduserlayout=(LinearLayout)view.findViewById(R.id.adduserlayout);
        people=(ListView)view.findViewById(R.id.people);
        ch=new ContactList_Handler(getActivity());
        adapter=new friendAdapter(getActivity(),getData());
        people.setAdapter(adapter);

        people.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone=adapter.chats.get(position).getPhone();
                SharedPreferences pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("chatwith", phone);
                editor.commit();

                Intent intent =new Intent(getContext(),chatbox.class);
                startActivity(intent);
            }
        });
        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcontact.setVisibility(View.GONE);
                adduserlayout.setVisibility(View.VISIBLE);
            }
        });

        dontadduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcontact.setVisibility(View.VISIBLE);
                takename.setText("");
                takephone.setText("");
                adduserlayout.setVisibility(View.GONE);
            }
        });

        adduserinDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=takename.getText().toString().trim();
                String phone=takephone.getText().toString().trim();

                ch.addcontact(name,phone);

                addcontact.setVisibility(View.VISIBLE);
                takename.setText("");
                takephone.setText("");
                adduserlayout.setVisibility(View.GONE);
                ((BaseAdapter)people.getAdapter()).notifyDataSetChanged();
            }
        });
        return view;

    }

    public ArrayList<friend> getData()
    {
        chats=new ArrayList<>();
        Cursor cc=ch.databaseToString();
        while(cc.moveToNext()) {
            friend f = new friend();
            f.setName(cc.getString(1));
            f.setPhone(cc.getString(2));
            chats.add(f);
        }
        return chats;
    }

}
