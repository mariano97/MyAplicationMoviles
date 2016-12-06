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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registrarse extends AppCompatActivity {


    private EditText txtnombre, txtapellidos, txtemail, txtconfirmaremail, txtpassword, txtconfirmarpassword;
    private Button  btn_registrar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ModelRegistro usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        txtnombre = (EditText) findViewById(R.id.txtnombres);
        txtapellidos = (EditText) findViewById(R.id.txtapellidos);
        txtemail = (EditText) findViewById(R.id.txtemail);
        txtconfirmaremail = (EditText) findViewById(R.id.txtconfirmaremail);
        txtpassword = (EditText) findViewById(R.id.txtpassword);
        txtconfirmarpassword = (EditText) findViewById(R.id.txtconfirmarpass);

        btn_registrar = (Button) findViewById(R.id.btnresgistrar);


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Registrarse", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Registrarse", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(txtemail.getText().toString().trim().equals(txtconfirmaremail.getText().toString().trim())
                        && txtpassword.getText().toString().trim().equals(txtconfirmarpassword.getText().toString().trim())){

                    mAuth.createUserWithEmailAndPassword(txtemail.getText().toString().trim(), txtpassword.getText().toString().trim())
                            .addOnCompleteListener(Registrarse.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(!task.isSuccessful()){

                                        Toast.makeText(Registrarse.this, "El Registro ha Fallado: " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                    else{

                                        Toast.makeText(Registrarse.this, "Debe Validar el Email en su Correo", Toast.LENGTH_SHORT).show();
                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(Registrarse.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(!task.isSuccessful()){
                                                    Toast.makeText(Registrarse.this, "Error al Enviar Email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                                }else {
                                                    Toast.makeText(Registrarse.this, "Se ha Enviado  Email", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(Registrarse.this, CompletarRegistro.class));

                                                }
                                            }
                                        });

                                        UserProfileChangeRequest nuevousuario = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(txtnombre.getText().toString().trim()).build();
                                        mAuth.getCurrentUser().updateProfile(nuevousuario);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference ReferenciaUsuarios = database.getReference("usuarios").child(mAuth.getCurrentUser().getUid());

                                        // myRef.setValue("Hello, World!");

                                        usuario = new ModelRegistro();
                                        usuario.setNombres(txtnombre.getText().toString().trim());
                                        usuario.setApellidos(txtapellidos.getText().toString().trim());
                                        usuario.setEmail(txtemail.getText().toString().trim());

                                        ReferenciaUsuarios.setValue(usuario);

                                        txtemail.setText("");
                                        txtpassword.setText("");
                                        txtconfirmaremail.setText("");
                                        txtapellidos.setText("");
                                        txtnombre.setText("");



                                    }

                                }
                            });
                }else{
                    System.out.println("------ debe confirmar email y password");
                    Toast.makeText(Registrarse.this, "Comfirme Email y Password", Toast.LENGTH_SHORT).show();
                }


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
