package com.example.streamandwatch.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertUser(UserDataRoom user);

    @Delete
    void deleteUser(UserDataRoom user);

    @Query("SELECT * FROM users")
    List<UserDataRoom> getAllUsers();

    @Query("DELETE FROM users")
    void deleteAllUsers();

}
