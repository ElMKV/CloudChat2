package com.example.cloudchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static int MAX_MESSAGE_LENGHT = 100;

    FirebaseDatabase database = FirebaseDatabase.getInstance();//получает ссылку на бд
    DatabaseReference myRef = database.getReference("messages"); //получает ссылку на бд в блоке

    EditText mEditTextMessage;
    Button mSendButon;
    RecyclerView mMessageRecycler;

    ArrayList<String> messages = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSendButon = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mMessageRecycler = findViewById(R.id.message_recycler);

        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        final DataAdapter dataAdapter = new DataAdapter(this, messages);

        mMessageRecycler.setAdapter(dataAdapter );

        mSendButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String msg = mEditTextMessage.getText().toString();

                if (msg.equals(""))
                {

                    Toast.makeText(getApplicationContext(), "Введите сообщение!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length()== MAX_MESSAGE_LENGHT)
                {
                    Toast.makeText(getApplicationContext(), "Слишком длинное сообщение!",Toast.LENGTH_SHORT).show();
                    return;

                }
                myRef.push().setValue(msg);
                mEditTextMessage.setText("");

            }
        });


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessageRecycler. smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













    }
}
