package com.example.streamandwatch.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.streamandwatch.Activities.WelcomeActivity;
import com.example.streamandwatch.R;
import com.example.streamandwatch.Room.UserDataRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_account extends Fragment {

    private Button logoutBtn;

    private TextView emailEdt, nameEdt, surnameEdt, phoneEdt;
    String email;
    public fragment_account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_account.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_account newInstance(String param1, String param2) {
        fragment_account fragment = new fragment_account();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
        }

        List<UserDataRoom> lista = WelcomeActivity.database.userDao().getAllUsers();

        for (UserDataRoom x : lista) {
           if(email.compareTo(x.getEmail()) == 0) {
                nameEdt = view.findViewById(R.id.textViewname);
                nameEdt.setText("Name: " + x.getName());
                surnameEdt = view.findViewById(R.id.textViewSurname);
                surnameEdt.setText("Surname: " + x.getSurname());
                phoneEdt = view.findViewById(R.id.textViewPhone);
                phoneEdt.setText("Phone: " + x.getPhone());
            }
        }


        emailEdt = view.findViewById(R.id.textViewEmailAccount);
        emailEdt.setText("Email: " + email);

        logoutBtn = view.findViewById(R.id.loginBtn);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Navigation.findNavController(v).navigate(R.id.action_fragment_account_to_welcomeActivity);
            }
        });
    }
}