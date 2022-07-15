package com.example.sewit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    TextView goToRegister,forgotPassword;
    EditText email,password;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        loginBtn = findViewById(R.id.loginBTN);
        goToRegister = findViewById(R.id.DonNotHvTV);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        forgotPassword = findViewById(R.id.forgotPasswordTV);

        loginBtn.setOnClickListener(v -> {
            checkValidity();
            if(valid){
                fAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnSuccessListener(authResult -> {

                    /* TODO: email verification - this should be uncommented after testing is done
                    if(fAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_SHORT).show();
                        checkUserAccessLevel(authResult.getUser().getUid());
                    }else{
                        Toast.makeText(LoginActivity.this,"Please verify your email", Toast.LENGTH_SHORT).show();
                    }

                    */

                    //TODO - this should be deleted after testing is done
                    Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_SHORT).show();
                    checkUserAccessLevel(authResult.getUser().getUid());

                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this,"Login failed, Please check your credentials", Toast.LENGTH_SHORT).show());

            }

        });

        goToRegister.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),RegisterActivity.class)));

        //reset password
        forgotPassword.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),ForgotPasswordActivity.class)));
    }

    private void checkUserAccessLevel(String uid){
        DocumentReference df = fStore.collection("Users").document(uid);
        //extract data from document
        df.get().addOnSuccessListener(documentSnapshot -> {
            Log.d("TAG","onSuccess: " + documentSnapshot.getData());

            if(documentSnapshot.getString("isTailor") != null){
                // user is tailor
                startActivity(new Intent(getApplicationContext(),TailorActivity.class));
                finish();
            }

            if(documentSnapshot.getString("isCustomer") != null){
                // user is customer
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    public boolean checkValidity(){

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
            password.setError("Min length is 6 characters");
            password.requestFocus();
            valid = false;
        }
        else{
            valid = true;
        }
        return valid;
    }

    //Password visibility
    public void ShowHidePassLogin(View view) {

        if(view.getId()==R.id.show_pass_btn_login){
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

    //if user already logged in, log in on start of app
    @Override
    protected void onStart() {
        super.onStart();

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        //TODO - this should be deleted after testing is done
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.getString("isTailor") != null){
                    startActivity(new Intent(getApplicationContext(),TailorActivity.class));
                    finish();
                }
                if(documentSnapshot.getString("isCustomer") != null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(e -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            });
        }

        /* TODO: email verification - this should be uncommented after testing is done
        if(FirebaseAuth.getInstance().getCurrentUser() != null && fAuth.getCurrentUser().isEmailVerified()){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.getString("isTailor") != null){
                    startActivity(new Intent(getApplicationContext(),TailorActivity.class));
                    finish();
                }
                if(documentSnapshot.getString("isCustomer") != null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(e -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            });
        }

         */

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
