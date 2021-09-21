package com.shivam.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null)
        {
         Intent intent=new Intent(MainActivity.this,LoginActivity.class);
         startActivity(intent);
        }
        else
        {
            TextView textView=findViewById(R.id.text);
            textView.setText("Hello "+FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this,
                    "You have been signed out.",
                    Toast.LENGTH_LONG)
                    .show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            // Close activity
            finish();
        }
        return true;

    }
}