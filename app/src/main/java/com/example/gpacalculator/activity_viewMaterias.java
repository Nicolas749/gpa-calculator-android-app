package com.example.gpacalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

public class activity_viewMaterias extends AppCompatActivity {

    private ListView listMaterias;
    private Button buttonOrder;
    private Button buttonReset;
    private Button buttonEdit;
    private EditText editMaterias;
    private EditText editCredito;
    private EditText editCodigos;
    private EditText editNotas;
    private Button buttonDel;
    String item ;
    int indexValue;

    String filename = "myfile";
    ArrayList<Materias> listaM2 = new ArrayList<Materias>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_materias);

        listMaterias = (ListView) findViewById(R.id.listMaterias);
        buttonOrder = (Button) findViewById(R.id.buttonOrder);
        buttonReset = (Button) findViewById(R.id.buttonReset);
        buttonEdit = (Button) findViewById(R.id.buttonEdit);
        editMaterias = (EditText)  findViewById(R.id.editMaterias);
        editCredito = (EditText)  findViewById(R.id.editCredito);
        editCodigos = (EditText)  findViewById(R.id.editCodigos);
        editNotas = (EditText)  findViewById(R.id.editNotas);
        buttonDel =(Button) findViewById(R.id.buttonDel);

        //ArrayList<Materias> listaM2 = getIntent().getParcelableArrayListExtra("lista");
        loadFile();

        listaM2.sort(Comparator.comparing(Materias::getCodigo));
        ArrayAdapter adaptador =  new ArrayAdapter(this, android.R.layout.simple_spinner_item,listaM2);
        listMaterias.setAdapter(adaptador);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(listaM2.isEmpty())){
                    listaM2.sort(Comparator.comparing(Materias::getLetra));
                    listMaterias.setAdapter(adaptador);
                    adaptador.notifyDataSetChanged();
                    saveFile();
                }else{
                    showToast(view,"No hay materias agregadas");
                }
            }
        });

        //borrar todas las materias
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!(listaM2.isEmpty())){
                    listaM2.clear();
                    showToast(view,"Materias eliminadas correctamente");
                    listMaterias.setAdapter(adaptador);
                    adaptador.notifyDataSetChanged();
                    saveFile();
                }else{
                    showToast(view,"No hay materias para eliminar");
                }
            }
        });


        //Seleccionar Items del list View
        listMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
                indexValue = i;
                showToast(view,"La " + item +" ha sido seleccionada" );
                editMaterias.setText(listaM2.get(indexValue).getNombre());
                editCredito.setText(listaM2.get(indexValue).getNum_creditos()+"");
                editCodigos.setText(listaM2.get(indexValue).getCodigo());
                editNotas.setText(listaM2.get(indexValue).getLetra());

            }
        });

        //update editar materia en especifoc
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validarDuplicado()){
                    showToast(view,"Esta materia ya existe");
                }else{
                    String str1 = editMaterias.getText().toString();
                    String str2 = editCodigos.getText().toString();
                    int str3 = Integer.parseInt(editCredito.getText().toString());
                    String str4 = editNotas.getText().toString();
                    Materias maEdit = new Materias(str2,str1,str3,str4);
                    listaM2.set(indexValue, maEdit);
                    adaptador.notifyDataSetChanged();
                    saveFile();
                }

            }
        });
        //eliminar materia en especifico
        buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    listaM2.remove(indexValue);
                    adaptador.notifyDataSetChanged();
                saveFile();
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        saveFile();
    }

    public void showToast(View view, String mensaje) {
        Toast toast = Toast.makeText(this, mensaje,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public boolean validarDuplicado(){
        for(Materias m : listaM2){
            if (m.getNombre().equalsIgnoreCase(editMaterias.getText().toString()) ||
                    m.getCodigo().equalsIgnoreCase(editCodigos.getText().toString())){
                return true;
            }
        }
        return false;
    }

    private void saveFile() {

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(listaM2);
            oos.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception e) {}

    }

    private void loadFile() {

        try (FileInputStream fis = context.openFileInput(filename);
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            try {
                listaM2 = (ArrayList<Materias>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(listaM2.toString());

        } catch (Exception e) {}

    }

}