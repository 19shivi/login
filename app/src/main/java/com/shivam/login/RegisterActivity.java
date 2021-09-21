package com.shivam.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {


    private TextInputEditText name,email,password;
    private  MaterialButton register,login;
    private FirebaseAuth firebaseAuth;
    private  TextView error;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.button_register);
        login=findViewById(R.id.button_login);
        error=findViewById(R.id.error);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable username,passward,name_text;
                username = email.getText();
                 passward = password.getText();


                if (!isValid(username.toString())) {
                    error.setText("Enter a valid E-mail Id");
                } else if (passward.length() < 8) {
                    error.setText("Passward must have atleast 8 characters");
                }
                else if(name.getText().length()<2 || name.getText().toString().matches("\\d"))
                {
                    error.setText("Enter a correct Name");
                }
                else {
                    firebaseAuth = FirebaseAuth.getInstance();
                    progressBar = new ProgressDialog(v.getContext());
                    progressBar.setMessage("Signing Up");
                    progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.show();

                    firebaseAuth.createUserWithEmailAndPassword(username.toString(), passward.toString())
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {


                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                try {
                                                    throw task.getException();
                                                }
                                                // if user enters wrong email.
                                                catch (FirebaseAuthWeakPasswordException weakPassword) {

                                                    progressBar.dismiss();
                                                    error.setText("Weak Passward");
                                                    // TODO: take your actions!
                                                }
                                                // if user enters wrong password.
                                                catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                                    progressBar.dismiss();
                                                    error.setText("Invalid E-mail ");

                                                    // TODO: Take your action
                                                } catch (FirebaseAuthUserCollisionException existEmail) {

                                                } catch (Exception e) {
                                                    progressBar.dismiss();
                                                    error.setText("E-mail already Exist");
                                                }
                                            } else {

                                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(name.getText().toString())
                                                        .build();
                                                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.dismiss();
                                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });

                                            }
                                        }
                                    }
                            );
                }

            }
        });

    }









    static boolean isValid(String email) {
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        return email.matches(regex);
    }
}