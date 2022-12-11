package com.example.gpacalculator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText nombreMateria;
    private EditText codigoMateria;
    private EditText numCreditos;
    private EditText notaLetras;
    private Button button;
    private Button buttonVerMaterias;
    private TextView gpaView;

    String filename = "myfile";
    private ArrayList<Materias> materiasArray = new ArrayList<Materias>();

    activity_viewMaterias ac_Vm = new activity_viewMaterias();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombreMateria =(EditText) findViewById(R.id.nombreMateria);
        codigoMateria =(EditText) findViewById(R.id.codigoMateria);
        numCreditos =(EditText) findViewById(R.id.numCreditos);
        notaLetras =(EditText) findViewById(R.id.notaLetras);
        gpaView = (TextView) findViewById(R.id.gpaView);
        button = (Button) findViewById(R.id.button);
        buttonVerMaterias = (Button)findViewById(R.id.buttonVerMaterias);

        loadFile();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codeM = codigoMateria.getText().toString().toUpperCase();
                String nomM = nombreMateria.getText().toString().toUpperCase();
                int credM = Integer.parseInt(numCreditos.getText().toString());
                String letraM = notaLetras.getText().toString().toUpperCase();

                //


                if(codeM.equals("") || nomM.equals("")|| (credM >6) || letraM.equals("")){
                    showToast(v,"Error en los campos");
                }else{
                    if(materiasArray.isEmpty()){
                        AddMaterias(codeM,nomM,credM,letraM,v);
                    }else{
                        if(validarDuplicados(nomM,codeM)){
                            showToast(v,"Esta materia ya existe");
                        }else{
                            AddMaterias(codeM,nomM,credM,letraM,v);
                        }

                    }
                }
            }});

        buttonVerMaterias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_viewMaterias.class);
                intent.putParcelableArrayListExtra("lista", materiasArray);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadFile();
        gpaCalculator();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveFile();
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
    public void AddMaterias(String codeM, String nomM, int credM, String letraM,View v){
            Materias mate = new Materias(codeM,nomM,credM,letraM);
            materiasArray.add(mate);
            //
            showToast(v,"Agregado Correctamente");
            gpaCalculator();
            clearCampos();
    }

    private void gpaCalculator(){

        int creditos = 0;
        int calcGpa = 0;

        for (Materias m:materiasArray) {

            creditos += m.getNum_creditos();
            switch (m.getLetra()){
                case "A":
                    calcGpa += m.getNum_creditos() * 4;
                    break;
                case "B":
                    calcGpa += m.getNum_creditos() * 3;
                    break;
                case "C":
                    calcGpa += m.getNum_creditos() * 2;
                    break;
                case "D":
                    calcGpa += m.getNum_creditos()  * 1;
                    break;
                case "F":
                    calcGpa += m.getNum_creditos() * 0;
                    break;
                default:
                    calcGpa += 0;
            }

        }

        double GPA = (double) calcGpa/creditos;
        System.out.println("GPA: " + GPA);
        gpaView.setText("GPA:\n" + String.format("%.3g%n", GPA));

    }

    public boolean validarDuplicados(String nombreMa, String codiMa){
        for(Materias m : materiasArray){
            if (m.getNombre().equalsIgnoreCase(nombreMa) ||
                    m.getCodigo().equalsIgnoreCase(codiMa)){
                return true;
            }
        }
        return false;
    }

    public void clearCampos(){
        nombreMateria.setText("");
        codigoMateria.setText("");
        numCreditos.setText("0");
        notaLetras.setText("");
    }

    private void saveFile() {

        try (FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(materiasArray);
            oos.close();
            System.out.println("The Object  was succesfully written to a file");

        } catch (Exception e) {}

    }

    private void loadFile() {

        try (FileInputStream fis = context.openFileInput(filename);
             ObjectInputStream ois = new ObjectInputStream(fis);) {

            try {
                materiasArray = (ArrayList<Materias>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            System.out.println(materiasArray.toString());
            gpaCalculator();

        } catch (Exception e) {}

    }

}