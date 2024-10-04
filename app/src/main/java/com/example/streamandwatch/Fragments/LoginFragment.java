package com.example.streamandwatch.Fragments;

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

import com.example.streamandwatch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private EditText emailEdt, passEdt;

    private Button loginBtn;
    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        emailEdt = view.findViewById(R.id.emailText);
        passEdt = view.findViewById(R.id.passwordText);
        loginBtn = view.findViewById(R.id.loginBtn);

        TextView textView = view.findViewById(R.id.signup);
        String signUp = "Sign Up";
        SpannableString ss = new SpannableString(signUp);
        ClickableSpan signUp2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View v) {
                openSignUp(v);
            }
        };
        ss.setSpan(signUp2, 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmailOk(emailEdt.getText().toString()) && isPasswordOk(passEdt.getText().toString())) {
                    mAuth.signInWithEmailAndPassword(emailEdt.getText().toString(), passEdt.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            openMainPage(v);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText( getActivity(), "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // [END sign_in_with_email]
                }
            }
            });
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

    public void openSignUp(View v){
        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_signupFragment);
    }

    public void openMainPage(View v){
        Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_mainActivity);
    }
}