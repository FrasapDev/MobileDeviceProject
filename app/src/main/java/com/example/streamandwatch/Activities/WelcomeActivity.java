package com.example.streamandwatch.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;

import com.example.streamandwatch.R;
import com.example.streamandwatch.Room.UserDatabase;

public class WelcomeActivity extends AppCompatActivity {

    public static UserDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        database = Room.databaseBuilder(this, UserDatabase.class, "my-database")
                .allowMainThreadQueries()
                .build();
    }
}