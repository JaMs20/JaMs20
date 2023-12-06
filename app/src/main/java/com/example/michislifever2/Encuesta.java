package com.example.michislifever2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Encuesta extends AppCompatActivity {
    private EditText txtnom,txtape,txtopi;
    private ListView lista;


    private FirebaseFirestore db;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);

        CargarListaFirestore();

        db = FirebaseFirestore.getInstance();

        txtnom =findViewById(R.id.txtnom);
        txtape =findViewById(R.id.txtape);
        txtopi =findViewById(R.id.txtopi);

        lista =findViewById(R.id.lista);


    }

    public void enviarDatosFirestore(View view){
        String nombre = txtnom.getText().toString();
        String apellido = txtape.getText().toString();
        String opinion = txtopi.getText().toString();


        Map<String, Object> datos =new HashMap<>();
        datos.put("nombre",nombre);
        datos.put("apellido",apellido);
        datos.put("opinion",opinion);


        db.collection("datos")
                .document(nombre)
                .set(datos)
                .addOnSuccessListener(aVoid-> {
                    Toast.makeText(Encuesta.this, "Datos Enviados Correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e->{Toast.makeText(Encuesta.this, "Datos Enviados Correctamente"+ e.getMessage(), Toast.LENGTH_SHORT).show();});
    }
    public void CargarLista(View view){
        CargarListaFirestore();
    }

    public void CargarListaFirestore(){
        FirebaseFirestore db= FirebaseFirestore.getInstance();
        db.collection("datos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            List<String> listadatos =new ArrayList<>();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String linea = " || "+ document.getString("nombre")+
                                        " || "+ document.getString("apellido")+
                                        " || "+document.getString("opinion");
                                listadatos.add(linea);
                            }

                            ArrayAdapter<String> adaptedor=new ArrayAdapter<>(Encuesta.this,android.R.layout.simple_list_item_1, listadatos);
                            lista.setAdapter(adaptedor);
                        }
                        else{
                            Log.e("TAG","Erroe al obtener los datos de la mascota");
                        }
                    }
                });
    }
}