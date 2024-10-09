package com.gaei.api.clientes.srvapiclientes.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CLIENTES")
public class Cliente {

    @Id
    private String idTx;

    @NotBlank
    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @NotBlank
    
    @Column(name = "numero_documento", unique = true)
    private String numeroDocumento;

    @NotBlank
    @Column(name = "primer_nombre")
    private String primerNombre;

    @Column(name = "segundo_nombre")
    private String segundoNombre;

    @NotBlank
    @Column(name = "primer_apellido")
    private String primerApellido;

    @Column(name = "segundo_apellido") 
    private String segundoApellido;

    @NotNull
    private Long telefono;
    @NotNull
    @NotBlank
    @Email
    @Column(name = "corre_electronico")
    private String correoElectronico;

    public String getIdTx() {
        return idTx;
    }

    public void setIdTx(String idTx) {
        this.idTx = idTx;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getPrimerNombre() {
        return primerNombre;
    }

    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre() {
        return segundoNombre;
    }

    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public String toString() {
        return "Cliente [idTx=" + idTx + ", tipoDocumento=" + tipoDocumento + ", numeroDocumento=" + numeroDocumento
                + ", primerNombre=" + primerNombre + ", segundoNombre=" + segundoNombre + ", primerApellido="
                + primerApellido + ", segundoApellido=" + segundoApellido + ", telefono=" + telefono
                + ", correoElectronico=" + correoElectronico + "]";
    }

    

}
