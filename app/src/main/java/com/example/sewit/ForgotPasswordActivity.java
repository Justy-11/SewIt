package com.example.sewit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText emailToResetPass;
    Button resetPassBtn;
    FirebaseAuth fAuth;
    boolean valid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailToResetPass = findViewById(R.id.emailToResetPassword);
        resetPassBtn = findViewById(R.id.resetPasswordBtn);

        fAuth = FirebaseAuth.getInstance();

        resetPassBtn.setOnClickListener(v -> {
            checkValidity();
            if(valid){
                fAuth.sendPasswordResetEmail(emailToResetPass.getText().toString()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(ForgotPasswordActivity.this,"Check your email to reset your password", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(ForgotPasswordActivity.this,"Try again, Something wrong happened", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public boolean checkValidity(){

        if(emailToResetPass.getText().toString().isEmpty()){
            emailToResetPass.setError("Email is required");
            emailToResetPass.requestFocus();
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailToResetPass.getText().toString()).matches()){
            emailToResetPass.setError("Please provide valid email");
            emailToResetPass.requestFocus();
            valid = false;
        }

        else{
            valid = true;
        }
        return valid;
    }
}