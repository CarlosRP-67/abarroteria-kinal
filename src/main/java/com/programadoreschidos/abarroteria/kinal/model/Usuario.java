package main.java.com.programadoreschidos.abarroteria.kinal.model;

public class Usuario {
    private String idUsuarios;
    private String nombre;
    private String apellido;
    private String email;
    private String contrasenaHash;
    private int id_roles;
    
    //constructor vacio
    public Usuario(){
}

    public Usuario(String idUsuarios, String nombre, String apellido, String email, String contrasenaHash, int id_roles) {
        this.idUsuarios = idUsuarios;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.contrasenaHash = contrasenaHash;
        this.id_roles = id_roles;
    }

    //metodos getter and setter
    public String getIdUsuarios() {
        return idUsuarios;
    }

    public void setIdUsuarios(String idUsuarios) {
        this.idUsuarios = idUsuarios;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public int getId_roles() {
        return id_roles;
    }

    public void setId_roles(int id_roles) {
        this.id_roles = id_roles;
    }
    
   
}
