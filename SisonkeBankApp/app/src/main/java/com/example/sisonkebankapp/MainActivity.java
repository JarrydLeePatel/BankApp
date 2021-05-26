package com.example.sisonkebankapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public UserDetails userDetails;
    private TextView welcome;
    private Button balance;
    private Button transfer;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent p = getIntent();
        String email = p.getExtras().getString("email");
        welcome = findViewById(R.id.txtLoginWelcome);
        DatabaseHelper db = new DatabaseHelper(this);
        userDetails = db.getUserDetails(email);

        welcome.setText(String.format("%s %s", welcome.getText(), userDetails.getNAME()));
        balance = findViewById(R.id.btnAccountBalance);
        transfer = findViewById(R.id.btnAccountTransfer);
        logout = findViewById(R.id.btnLogout);

        balance.setOnClickListener(this);
        transfer.setOnClickListener(this);
        logout.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnAccountBalance:
                Intent balances = new Intent(this, Balance.class);
                balances.putExtra("email", userDetails.getEMAIL());
                startActivity(balances);
                break;
            case R.id.btnAccountTransfer:
                Intent transfers = new Intent(this, Transfer.class);
                transfers.putExtra("email", userDetails.getEMAIL());
                startActivity(transfers);
                break;
            case R.id.btnLogout:
                Intent logout = new Intent(this, Login.class);
                logout.putExtra("finish", true);
                logout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                finish();
                Toast.makeText(this, "logged out successfully .",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}