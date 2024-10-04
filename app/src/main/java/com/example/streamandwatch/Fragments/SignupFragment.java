package com.example.streamandwatch.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streamandwatch.Activities.MainActivity;
import com.example.streamandwatch.Activities.WelcomeActivity;
import com.example.streamandwatch.R;
import com.example.streamandwatch.Room.UserDataRoom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEdt, passEdt, nameEdt, surnameEdt, phoneEdt;

    private Button signupBtn;
    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailEdt = view.findViewById(R.id.emailText);
        passEdt = view.findViewById(R.id.passwordText);
        signupBtn = view.findViewById(R.id.loginBtn);

        nameEdt = view.findViewById(R.id.nameText);
        surnameEdt = view.findViewById(R.id.surnameText);
        phoneEdt = view.findViewById(R.id.phoneText);

        TextView textView = view.findViewById(R.id.login);
        String login = "Login";
        SpannableString ss = new SpannableString(login);
        ClickableSpan login2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View v) {
                openLogin(v);
            }
        };
        ss.setSpan(login2, 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        signupBtn = (Button) view.findViewById(R.id.signupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(emailEdt.getText().toString(), passEdt.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //openLogin(v);
                                    createUserRoom(v);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText( getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                // [END sign_up_with_email]
                }
        });
    }


    public void createUserRoom(View v) {

        UserDataRoom newUser = new UserDataRoom();
        newUser.setName(nameEdt.getText().toString());
        newUser.setSurname(surnameEdt.getText().toString());
        newUser.setPhone(phoneEdt.getText().toString());
        newUser.setEmail(emailEdt.getText().toString());

        WelcomeActivity.database.userDao().insertUser(newUser);

        openLogin(v);
        }

    public void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password);
        // [END create_user_with_email]
    }


    private boolean isEmailOk(String email){
        boolean result = EmailValidator.getInstance().isValid(email);
        if(!result)
            emailEdt.setError(getString(R.string.EmailWrong));
        else
            emailEdt.setError(null);
        return result;
    }

    private boolean isPasswordOk(String password){   //TODO sistemare le condizioni e mettere messaggio d'errore in caso e' sbagliata
        if (password.isEmpty()) {
            passEdt.setError(getString(R.string.PasswordWrong));
            return false;
        } else {
            passEdt.setError(null);
            return true;
        }
    }
    private boolean isNameOk(String name) {
        if (name.isEmpty() || !isAlpha(name)) {
            //nameTextInputLayout.setError(getString(R.string.Name_Surname_Wrong));
            return false;
        } else {
            //nameTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean isSurnameOk(String surname) {
        if (surname.isEmpty() || !isAlpha(surname)) {
            //surnameTextInputLayout.setError(getString(R.string.Name_Surname_Wrong));
            return false;
        } else {
            //surnameTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean isPhoneNumberOk(String phone){ //TODO cercare se esiste un validator
        if (phone.isEmpty() || phone.length() != 10) {
            //phoneTextInputLayout.setError(getString(R.string.PhoneNumberWrong));
            return false;
        } else {
            //phoneTextInputLayout.setError(null);
            return true;
        }
    }

    private boolean isAlpha(String string){
        char[] array = string.toCharArray();
        for (char c: array) {
            if(!Character.isLetter(c))
                return false;
        }
        return true;
    }

    public void openLogin(View v){
        Navigation.findNavController(v).navigate(R.id.action_signupFragment_to_loginFragment);
    }
}