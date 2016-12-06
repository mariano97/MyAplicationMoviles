package com.moviles.marioandres.myaplicationmoviles;

/**
 * Created by MarioAndres on 5/12/2016.
 */

public class ModelRegistro {

    private String nombres;
    private String Apellidos;
    private String Email;
    private String cargo;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
}
