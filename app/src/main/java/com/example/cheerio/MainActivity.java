package com.example.cheerio;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheerio.common.Common;
import com.example.cheerio.models.User_model;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.FirebaseInstallations;

public class MainActivity extends AppCompatActivity {

    EditText user_email, user_password;
    TextView txt_forgot_password, txt_register;
    Button btn_login;
    ImageButton img_google;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    User_model user_model = new User_model();

    private static final String TAG = "GoogleSignIn";
    private static final int RC_SIGN_IN = 9001;

    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instantiate firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        txt_forgot_password = findViewById(R.id.txt_forgot_password);
        txt_register = findViewById(R.id.txt_register);
        btn_login = findViewById(R.id.btn_login);
        img_google = findViewById(R.id.img_google);

        //setting onclick listeners
        txt_forgot_password.setOnClickListener(forgot_password_click);
        txt_register.setOnClickListener(register_clicked);
        btn_login.setOnClickListener(login_click);
        img_google.setOnClickListener(google_click);

        //Google sign configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);


    }

    View.OnClickListener forgot_password_click = v -> {
        //when forgot password is clicked
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.forgot_password_bottom,
                (LinearLayout) findViewById(R.id.forgotPasswordContainer));
        bottomSheetView.findViewById(R.id.btn_forgot_password).setOnClickListener(v1 -> {
            EditText forgot_email = bottomSheetView.findViewById(R.id.edt_forgot_email);
            firebaseAuth.sendPasswordResetEmail(forgot_email.getText().toString().trim())
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Password reset link sent", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.hide();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Issue in sending reset link. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    };

    View.OnClickListener register_clicked = v -> {
        //when register is clicked
        startActivity(new Intent(MainActivity.this, Register_Page.class));
    };

    View.OnClickListener login_click = v -> {
        //when login button is clicked
        firebaseAuth.signInWithEmailAndPassword(user_email.getText().toString().trim(), user_password.getText().toString().trim())
                .addOnFailureListener(e -> {
                    Snackbar.make(v, "" + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                })
                .addOnSuccessListener(authResult -> {
                    firestore.collection("Users")
                            .document(authResult.getUser().getUid())
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Common.user_model = documentSnapshot.toObject(User_model.class);
                                startActivity(new Intent(MainActivity.this, Home_Activity.class));
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Snackbar.make(v,""+e.getMessage(),Snackbar.LENGTH_SHORT).show();
                            });

                });
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful() && task.getResult().getUser() != null) {
                        // Sign in success, update UI with the signed-in user's information

                        user_model.setName(task.getResult().getUser().getDisplayName());
                        user_model.setEmail(task.getResult().getUser().getEmail());
                        user_model.setPhone(task.getResult().getUser().getPhoneNumber());
                        user_model.setUid(task.getResult().getUser().getUid());
                        user_model.setImage(task.getResult().getUser().getPhotoUrl().toString());

                        firestore.collection("Users")
                                .document(user_model.getUid())
                                .set(user_model)
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                                .addOnSuccessListener(unused -> {
                                    Common.user_model = user_model;
                                    startActivity(new Intent(MainActivity.this, Home_Activity.class));
                                    finish();
                                });


                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());

                    }
                });
    }

    View.OnClickListener google_click = v -> {
        //when login with google is clicked
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    };


}