package com.example.cheerio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheerio.models.User_model;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_Page extends AppCompatActivity {

    EditText user_name,user_phone,user_email,user_password;
    Button btn_signup;
    TextView txt_login;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        btn_signup = findViewById(R.id.btn_signup);
        txt_login = findViewById(R.id.txt_login);

        //setting onclick listeners
        btn_signup.setOnClickListener(signup_click);
        txt_login.setOnClickListener(login_click);
    }

    View.OnClickListener signup_click = v -> {
      //signup button is clicked
        User_model user_model = new User_model();

        if (user_name.getText().toString().equals("") ||
                user_password.getText().toString().equals("") ||
                user_email.getText().toString().equals("") ||
                user_phone.getText().toString().equals("")){
            btn_signup.setError("Please fill all the fields");
        }
        else {
            user_model.setName(user_name.getText().toString().trim());
            user_model.setEmail(user_email.getText().toString().trim());
            user_model.setPhone(user_phone.getText().toString().trim());
            user_model.setPassword(user_password.getText().toString().trim());

            firebaseAuth.createUserWithEmailAndPassword(user_model.getEmail(),user_model.getPassword())
                    .addOnFailureListener(e -> {
                        Snackbar.make(v,""+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                    })
                    .addOnSuccessListener(authResult -> {
                        if (authResult.getUser() != null)
                        {
                            user_model.setUid(authResult.getUser().getUid());
                            firestore.collection("Users")
                                    .document(authResult.getUser().getUid())
                                    .set(user_model)
                                    .addOnFailureListener(e -> {
                                        Snackbar.make(v,""+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                                    })
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register_Page.this,Home_Activity.class));
                                        finish();
                                    });
                        }

                    });
        }
    };

    View.OnClickListener login_click = v -> {
        //login button is clicked
        finish();
    };
}