package com.example.sisonkebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;
/*
* CLASS TO REGISTER USER:
*
* */
    public class Register extends AppCompatActivity {
    private EditText Name;
    private EditText Surname;
    private EditText Email;
    private EditText Password;
    private EditText Mobile;
    private RadioGroup Gender;
    private String selectedGender;

    //Main on create//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Variables
        this.Name = findViewById(R.id.txtName);
        this.Surname = findViewById(R.id.txtSurname);
        this.Email = findViewById(R.id.txtEmail);
        this.Mobile = findViewById(R.id.txtMobile);
        this.Gender = findViewById(R.id.genderOptions);
        this.Password = findViewById(R.id.password);
   }
//-------------------------Validaiton of fields--------------------------//

//Check is name is valid:
    public boolean isNameValid(String name){
        return !name.matches(getString(R.string.NameValidator));
    }
//If field is empty
    private boolean hasBlankInputs() {
        return Name.getText().toString().isEmpty() ||
                Surname.getText().toString().isEmpty() ||
                Email.getText().toString().isEmpty() ||
                Mobile.getText().toString().isEmpty() ;
    }
//Check if email is valid.
    private boolean isValidEmail(){
        return Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches();
    }
//Check if mobile number is valid -regex
    private boolean isValidMobile() {
        String phone = Mobile.getText().toString();
        return Pattern.matches("\\d+", phone);
    }
//Check is password is valid
    private boolean isValidPassword(){
        return Password.getText().toString().length() > 4;
    }

    private void processGender() {
        int rbCheck = Gender.getCheckedRadioButtonId();
        switch (rbCheck){
            case R.id.rbMale:
                selectedGender = "Male";
                break;
            case R.id.rbFemale:
                selectedGender = "Female";
                break;
            default:
                selectedGender = "None";break;
        }
    }
//------------------------------------END OF VALIDATION------------------------------------//
//Registration
    public void processRegistration(View view) {
        if(hasBlankInputs()){
            Toast.makeText(this,"Error: There are fields that are empty!",
                    Toast.LENGTH_SHORT).show();
        }else {
            processGender();
            if(isNameValid(Name.getText().toString())) {
                Toast.makeText(this,"Error: Please enter a valid name!",
                        Toast.LENGTH_SHORT).show();
            }else if(isNameValid(Surname.getText().toString())) {
                Toast.makeText(this,"Error: Please enter a valid surname!"
                        , Toast.LENGTH_SHORT).show();
            }else if(!isValidEmail()){
                Toast.makeText(this,"Error: Please enter a valid email address!",
                        Toast.LENGTH_SHORT).show();
            }else if (!isValidPassword()){
                Toast.makeText(this,
                        "Error: Password is too short. It should " +
                                "at least be five characters" +
                                " long!",
                        Toast.LENGTH_SHORT).show();
            }else if(!isValidMobile()){
                Toast.makeText(this,"Error: " +
                        "Please enter a valid number!", Toast.LENGTH_SHORT).show();
            }else if(selectedGender.equals("None")){
                Toast.makeText(this,"Error: Please " +
                        "select a gender! ", Toast.LENGTH_SHORT).show();
            }else {
                DatabaseHelper db = new DatabaseHelper(this);
                if(!db.isRegistered(Email.getText().toString())){

                    UserDetails userDetails = new UserDetails();

                    //GET Details
                    userDetails.setNAME(Name.getText().toString());
                    userDetails.setSURNAME(Surname.getText().toString());
                    userDetails.setEMAIL(Email.getText().toString());
                    userDetails.setPASSWORD(Password.getText().toString());
                    userDetails.setMOBILE(Mobile.getText().toString());
                    userDetails.setGENDER(selectedGender);

                    //Bank amount set for new user
                    userDetails.setSAVINGS_BALANCE(5000.00);
                    userDetails.setCURRENT_BALANCE(1500.00);


                    db.addUser(userDetails);//add user to db


                    //Notification if true
                    Toast.makeText(this,"You successfully created an " +
                            "account.", Toast.LENGTH_SHORT).show();
                    Intent register = new Intent(this, Login.class);
                    startActivity(register);
                }else {  //Notification if false
                    Toast.makeText(this,"User does exist", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

//Take user to login page if registration is successfull.
public void processLogin(View view) {

        Intent register = new Intent(this, Login.class);
        startActivity(register);
    }
}