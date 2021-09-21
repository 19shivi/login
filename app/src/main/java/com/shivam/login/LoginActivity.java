package com.shivam.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText email,password;
    private  MaterialButton register,login;
    private FirebaseAuth firebaseAuth;
    private  TextView error;
    private ProgressDialog progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.button_register);
        login=findViewById(R.id.button_login);
        error=findViewById(R.id.error);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Editable username,passward;

                username = email.getText();
               passward = password.getText();

                if (!isValid(username.toString())) {
                    error.setText("Enter a valid Email");
                } else if (passward.length() < 8) {
                    error.setText("Passward Must have atleast 8 characters");
                } else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    progressBar = new ProgressDialog(view.getContext());
                    progressBar.setMessage("Signing In");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(username.toString(), passward.toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressBar.dismiss();
                                try {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidCredentialsException e) {

                                    error.setText("Bad credentials");
                                    //TextView textView=(TextView)findViewById(R.id.forgot);
                                    // textView.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    error.setText("Invalid Details");
                                    e.printStackTrace();
                                }


                            } else {
                                progressBar.dismiss();
                                if(!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                    error.setText("Email not verified");
                                    FirebaseAuth.getInstance().signOut();
                                }
                                else {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    });

                }

            }
        });


    }



    static boolean isValid(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regex);
    }



}