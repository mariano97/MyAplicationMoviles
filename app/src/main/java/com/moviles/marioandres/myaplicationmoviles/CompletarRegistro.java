package com.moviles.marioandres.myaplicationmoviles;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CompletarRegistro extends AppCompatActivity {

    private EditText txtNombre, txtapellidos, txttipodocumento, txtnumerodocumento, txtnumerotelefono,
              txtEstadoPersonal, txtcargopersonal, txtbuscardoc;
    private Button btn_tiposDocumentos,  btn_ingresarelpersonal, btn_EstadosdelPersonal, btn_cargoPerfil, btn_buscarpersonal;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    CompletarelRegistro cregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_registro);

        txtNombre = (EditText) findViewById(R.id.txtNombrePersonal);
        txtapellidos = (EditText) findViewById(R.id.txtApellidos);
        txttipodocumento = (EditText) findViewById(R.id.txtTipoDocumento);
        txtnumerodocumento = (EditText) findViewById(R.id.txtnumeroDocumento);
        txtnumerotelefono = (EditText) findViewById(R.id.txtTelefono);
        txtEstadoPersonal = (EditText) findViewById(R.id.txtEstado);
        txtcargopersonal = (EditText) findViewById(R.id.txtcargo);
        txtbuscardoc = (EditText) findViewById(R.id.txtdocBuscarPersonal);

        btn_buscarpersonal = (Button) findViewById(R.id.btnBucarDocPersonal);
        btn_cargoPerfil = (Button) findViewById(R.id.btncargo);
        btn_ingresarelpersonal = (Button) findViewById(R.id.btnIngresarRegistro);
        btn_tiposDocumentos = (Button) findViewById(R.id.btntipodocumento);
        btn_EstadosdelPersonal = (Button) findViewById(R.id.btnEstados);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("CompletarRegistro", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("CompletarRegistro", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        btn_buscarpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mibuscardoc = database.getReference("usuario_personal");

                mibuscardoc.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.hasChild(txtbuscardoc.getText().toString().trim())){


                            dataSnapshot.hasChild(txtbuscardoc.getText().toString().trim());

                            cregistro = dataSnapshot.child(txtbuscardoc.getText().toString().trim()).getValue(CompletarelRegistro.class);

                            txtNombre.setText(cregistro.getNombre());
                            txttipodocumento.setText(cregistro.getTipodocPersonal());
                            txtcargopersonal.setText(cregistro.getCargo());
                            txtnumerotelefono.setText(cregistro.getTelefono());
                            txtEstadoPersonal.setText(cregistro.getEstadopersonal());
                            txtapellidos.setText(cregistro.getApellidos());
                            txtnumerodocumento.setText(cregistro.getNumerodocPersonal());

                        }else {

                            Toast.makeText(CompletarRegistro.this, "el usuario No existe", Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });


        btn_EstadosdelPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerForContextMenu(btn_EstadosdelPersonal);
                openContextMenu(view);

            }
        });

        btn_tiposDocumentos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerForContextMenu(btn_tiposDocumentos);
                openContextMenu(view);

            }
        });

        btn_cargoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerForContextMenu(btn_cargoPerfil);
                openContextMenu(view);

            }
        });

        btn_ingresarelpersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(txtnumerodocumento.getText().toString().trim().equals("") && txtNombre.getText().toString().trim().equals("") &&
                        txtapellidos.getText().toString().trim().equals("") ){
                    Toast.makeText(CompletarRegistro.this, "Profavor Llene los Campos", Toast.LENGTH_SHORT).show();
                    System.out.println("------NO SE LLENARON LOS CAMPOS ----");
                }else{

                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ReferencianuevoPersonal = database.getReference("personal").child(txtnumerodocumento.getText().toString().trim());

                    cregistro = new CompletarelRegistro();
                    cregistro.setNombre(txtNombre.getText().toString().trim());
                    cregistro.setApellidos(txtapellidos.getText().toString().trim());
                    cregistro.setTipodocPersonal(txttipodocumento.getText().toString().trim());
                    cregistro.setNumerodocPersonal(txtnumerodocumento.getText().toString().trim());
                    cregistro.setTelefono(txtnumerotelefono.getText().toString().trim());
                    cregistro.setEstadopersonal(txtEstadoPersonal.getText().toString().trim());
                    cregistro.setCargo(txtcargopersonal.getText().toString().trim());

                    ReferencianuevoPersonal.setValue(cregistro);

                    txtNombre.setText("");
                    txtapellidos.setText("");
                    txtnumerodocumento.setText("");
                    txtEstadoPersonal.setText("");
                    txtnumerotelefono.setText("");
                    txttipodocumento.setText("");
                    txtcargopersonal.setText("");

                    System.out.println("-----TODO VA BIEN, ACTIVITY INICIADA---");
                    Toast.makeText(CompletarRegistro.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();

                    cregistro = null;


                }

            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menus, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menus, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menunuevoregistro, menus);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.cedula :
                txttipodocumento.setText("Cedula de Ciudadania");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.cedulaExtranjera :
                txttipodocumento.setText("Cedula de Extranjeria");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.permisotipodoc :
                txttipodocumento.setText("Permiso Trabajo");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.soltero :
                txtEstadoPersonal.setText("soltero");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.casado :
                txtEstadoPersonal.setText("casado");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;


            case R.id.viudo :
                txtEstadoPersonal.setText("viudo");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.fallecido :
                txtEstadoPersonal.setText("fallecido");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.estudiante :
                txtcargopersonal.setText("estudiante");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.profesional :
                txtcargopersonal.setText("profesional");
                Toast.makeText(CompletarRegistro.this, "Opcion Seleccionada", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

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
