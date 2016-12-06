package com.moviles.marioandres.myaplicationmoviles;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private EditText txt_Email, txtPass, txtemails;
    private Button btn_registro, btn_ingreso;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_Email = (EditText) findViewById(R.id.txtEmail);
        txtPass = (EditText) findViewById(R.id.txtPassword);
        btn_ingreso = (Button) findViewById(R.id.btnEntrar);
        btn_registro = (Button) findViewById(R.id.btnRegistroLogin);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("MainActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("MainActivity", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btn_registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MainActivity.this, Registrarse.class));
                finish();

            }
        });


        btn_ingreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(txt_Email.getText().toString().trim(), txtPass.getText().toString().trim())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Iniciar Sesion", "signInWithEmail:onComplete:" + task.isSuccessful());

                                if (!task.isSuccessful()) {
                                    if(task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")){
                                        System.out.println("--------Usuario no Existe-----");
                                        Toast.makeText(MainActivity.this, "El Usuario No Existe", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, Registrarse.class));

                                    }

                                }else{

                                    Log.w("Inicio de Sesion tarea", "signInWithEmail", task.getException());

                                    // Write a message to the database
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference referenciaCargos = database.getReference("usuarios");

                                    //myRef.setValue("Hello, World!");

                                    referenciaCargos.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String EMAIL = (String) dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("email").getValue();
                                            String CARGO = (String) dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("cargo").getValue();

                                            System.out.println("---------- Email " + EMAIL);
                                            System.out.println("---------- CARGO " + CARGO);

                                            if(EMAIL.equals(null) && CARGO.equals(null)){

                                                System.out.println("---- USU NULL Y CARGO NULL");
                                                Toast.makeText(MainActivity.this, "Usuario No Existe Registrese por favor", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(MainActivity.this, Registrarse.class));

                                            }else {

                                                System.out.println("ADMINISTRADOR----- " + dataSnapshot.getChildrenCount());
                                                if(mAuth.getCurrentUser().isEmailVerified()){

                                                    startActivity(new Intent(MainActivity.this, Inicio.class));
                                                    System.out.println("---- nueva activity iniciada----");
                                                }else{
                                                    Toast.makeText(MainActivity.this, "Debe Validar Email", Toast.LENGTH_SHORT).show();

                                                    System.out.println("-----debe validar su email-----");
                                                }

                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });


                                }

                                // ...
                            }
                        });

            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
