package com.gtappdevelopers.firmadocus;

import android.os.Parcel;


/**
 * Created by Jose on 15/05/2018. ;)
 */

public class DocuementosClass {


    private long IDDP;
    private String Fecha;
    private String Archivo;
    private String Paciente;
    private String Nif;
    private String Ruta;

    public DocuementosClass() {


    }

    public DocuementosClass(Parcel in) {

    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public long getIDDP() {
        return IDDP;
    }

    public void setIDDP(long IDDP) {
        this.IDDP = IDDP;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getArchivo() {
        return Archivo;
    }

    public void setArchivo(String archivo) {
        Archivo = archivo;
    }

    public String getPaciente() {
        return Paciente;
    }

    public void setPaciente(String paciente) {
        Paciente = paciente;
    }

    public String getNif() {
        return Nif;
    }

    public void setNif(String nif) {
        Nif = nif;
    }
}
