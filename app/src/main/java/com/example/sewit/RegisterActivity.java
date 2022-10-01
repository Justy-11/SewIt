package com.example.sewit;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText userName,email,password,phone;
    Button registerBtn;
    TextView goToLogin;
    CheckBox isTailorBox, isCustomerBox;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userName = findViewById(R.id.inputUserName);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        phone = findViewById(R.id.inputPhoneNo);
        registerBtn = findViewById(R.id.registerBTN);
        goToLogin = findViewById(R.id.alreadyHaveAccTV);
        isTailorBox = findViewById(R.id.checkBoxTailor);
        isCustomerBox = findViewById(R.id.checkBoxCustomer);

        //checkbox logic
        isCustomerBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                isTailorBox.setChecked(false);
            }
        });

        isTailorBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){
                isCustomerBox.setChecked(false);
            }
        });

        registerBtn.setOnClickListener(v -> {
            checkValidity();

            if(valid){

                //01/10/2022
                fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(task -> {

                    if(task.isSuccessful()){

                        assert fAuth.getCurrentUser() != null;
                        fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {

                            if(task1.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Account created successfully, check your email to verify your account", Toast.LENGTH_LONG).show();
                                DocumentReference df = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
                                Map<String,Object> userInfo = new HashMap<>();
                                userInfo.put("UserName",userName.getText().toString());
                                userInfo.put("Email",email.getText().toString());
                                userInfo.put("PhoneNumber",phone.getText().toString());

                                // Tailor or Customer
                                if(isTailorBox.isChecked()){
                                    userInfo.put("isTailor","1");
                                    // for tailor rating
                                    int rating = 0;
                                    userInfo.put("rating",rating);
                                }
                                if(isCustomerBox.isChecked()){
                                    userInfo.put("isCustomer","1");
                                }

                                df.set(userInfo);
                                //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                            }else{
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                    }

                });

                /*//TODO: check username uniqueness added on 12/7/2022 - change this (not working when there are no Users collection)
                fStore.collection("Users").whereEqualTo("UserName",userName.getText().toString())
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if(queryDocumentSnapshots.size() > 0){
                        userName.setError("Username is already in use, please choose another username");
                        userName.requestFocus();
                    }
                    // if no usernames matching, then create account
                    if(queryDocumentSnapshots.size() == 0){
                        fAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(task -> {

                            if(task.isSuccessful()){

                                assert fAuth.getCurrentUser() != null;
                                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {

                                    if(task1.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this,"Account created successfully, check your email to verify your account", Toast.LENGTH_LONG).show();
                                        DocumentReference df = fStore.collection("Users").document(fAuth.getCurrentUser().getUid());
                                        Map<String,Object> userInfo = new HashMap<>();
                                        userInfo.put("UserName",userName.getText().toString());
                                        userInfo.put("Email",email.getText().toString());
                                        userInfo.put("PhoneNumber",phone.getText().toString());

                                        // Tailor or Customer
                                        if(isTailorBox.isChecked()){
                                            userInfo.put("isTailor","1");
                                            // for tailor rating
                                            int rating = 0;
                                            userInfo.put("rating",rating);
                                        }
                                        if(isCustomerBox.isChecked()){
                                            userInfo.put("isCustomer","1");
                                        }

                                        df.set(userInfo);
                                        //startActivity(new Intent(RegisterActivity.this,LoginActivity.class));

                                    }else{
                                        Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else{
                                Toast.makeText(RegisterActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                            }

                        });
                    }
                });*/

            }

        });

        goToLogin.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),LoginActivity.class)));


    }

    public boolean checkValidity(){
        if(userName.getText().toString().isEmpty()){
            userName.setError("Username is required");
            userName.requestFocus();
            valid = false;
        }

        if(email.getText().toString().isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            valid = false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please provide valid email");
            email.requestFocus();
            valid = false;
        }

        if(password.getText().toString().isEmpty()){
            password.setError("password is required");
            password.requestFocus();
            valid = false;
        }

        if(password.getText().toString().length() < 6){
            password.setError("Min password length should be 6 characters");
            password.requestFocus();
            valid = false;
        }

        if(phone.getText().toString().isEmpty()){
            phone.setError("phone number is required");
            phone.requestFocus();
            valid = false;
        }

        if(!Patterns.PHONE.matcher(phone.getText().toString()).matches()){
            phone.setError("Please provide valid phone number");
            phone.requestFocus();
            valid = false;
        }

        if(!(isTailorBox.isChecked() || isCustomerBox.isChecked())){
            Toast.makeText(RegisterActivity.this,"Select the account type", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }

    //Password visibility
    public void ShowHidePass(View view) {

        if(view.getId()==R.id.show_pass_btn){
            if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility_off);
                //Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.ic_visibility);
                //Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }


}
