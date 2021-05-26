package com.example.sisonkebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
LOGIN ACTIVITY
*/


public class Login extends AppCompatActivity {
   //GLOBALS
    private EditText Email;
    private EditText Password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnCreateAccount);
        this.Email = findViewById(R.id.txtEmail);
        this.Password = findViewById(R.id.password);
    }


    //Validation messages if fields empty or password too short..
    public void processLogin(View v) {
        if(hasBlankInputs()){
            Toast.makeText(this,"Please enter a username " +
                    "and a password. ", Toast.LENGTH_SHORT).show();
        } else {
            if(!isValidEmail()){
                Toast.makeText(this,"Please enter a " +
                        "valid email address", Toast.LENGTH_SHORT).show();
            }else if (!isValidPassword()){
                Toast.makeText(this,"Password is too " +
                        "short. Min 5 Characters Required", Toast.LENGTH_SHORT).show();
            }


            //IF TRUE Login into DB
            else {
                DatabaseHelper db = new DatabaseHelper(this);
                if(db.isRegistered(Email.getText().toString())){
                    UserDetails userDetails = db.ValidateLogin(Email.getText().toString(),
                            Password.getText().toString());
                    try{
                        if(userDetails != null && userDetails.getPASSWORD().equals
                                (Password.getText().toString())){
                            Toast.makeText(this,"logged in! Welcome",
                                    Toast.LENGTH_SHORT).show();
                            Intent login = new Intent(this,
                                    MainActivity.class );
                            login.putExtra("email", userDetails.getEMAIL());
                            Email.setText("");
                            Password.setText("");
                            startActivity(login);
                        } }catch(Exception ex) {
                        Toast.makeText(this,"Error: Incorrect password.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this,"Invalid data inputted. Does not exist",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//-------------------VALIDATION OF FIELD---------------------------------------------------//
    //Check if the fields are empty
    public boolean hasBlankInputs() {
        return Email.getText().toString().isEmpty() ||
                Password.getText().toString().isEmpty();
    }

//Validate the email
    public boolean isValidEmail(){
        return Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches();
    }

    //Check if password is valid
    public boolean isValidPassword(){
        return Password.getText().toString().length() > 4;
    }
//----------------END OF VALIDATION OF FIELDS-----------------------------------------//


    //take use to registration page if reg text clicked
    public void processRegistration(View view) {
        Intent register = new Intent(this, Register.class);
        startActivity(register);
    }
}