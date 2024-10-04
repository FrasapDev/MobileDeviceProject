package com.example.streamandwatch.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserDataRoom.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static UserDatabase INSTANCE;

    public static UserDatabase getUsersDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "users")
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
