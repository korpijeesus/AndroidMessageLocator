package com.example.huzzah.messagelocator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GiveUserName extends AppCompatActivity {

    EditText loginName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_user_name);
        loginName = (EditText)findViewById(R.id.editText2);
    }

    public void userLogin( View view)
    {
        String loginText = loginName.getText().toString();
        if (loginText.matches(""))
        {
            //Herjaa jos ei ole nimeä annettu.
            Toast.makeText(this,"Please enter a name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Lähettää usernamen eteenpäin MainActivityyn
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("username", loginText);
            startActivity(i);
        }
    }
}
