package com.example.gpacalculator;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Materias implements Parcelable, Serializable {

    //default serialVersion id
    private static final long serialVersionUID = 1L;

    private String codigo;
    private String nombre;
    private int num_creditos;
    private String letra;

    public Materias() {
    }

    public Materias(String codigo, String nombre, int num_creditos, String letra) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.num_creditos = num_creditos;
        this.letra = letra;
    }

    protected Materias(Parcel in) {
        codigo = in.readString();
        nombre = in.readString();
        num_creditos = in.readInt();
        letra = in.readString();
    }


    public static final Creator<Materias> CREATOR = new Creator<Materias>() {
        @Override
        public Materias createFromParcel(Parcel in) {
            return new Materias(in);
        }

        @Override
        public Materias[] newArray(int size) {
            return new Materias[size];
        }
    };

    @Override
    public String toString() {
        return
                "Materia:" +nombre + "\n"
                        + "Codigo:" +codigo + '\n'
                        + "Creditos:" +num_creditos +'\n'
                        + "Nota:" +letra + '\n' ;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNum_creditos() {
        return num_creditos;
    }

    public void setNum_creditos(int num_creditos) {
        this.num_creditos = num_creditos;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(codigo);
        parcel.writeString(nombre);
        parcel.writeInt(num_creditos);
        parcel.writeString(letra);
    }
}
