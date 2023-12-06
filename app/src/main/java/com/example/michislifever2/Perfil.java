package com.example.michislifever2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;

public class Perfil extends AppCompatActivity {

    Spinner spSpinner;
    String[] categorias= new String[]{"Juguetes", "Alimentación", "Higiene"};

    EditText  edtNom, edtDes, edtCost,edtIdProd;
    ListView lista;

    LinearLayout lnCarga, lnFormulario;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        edtIdProd = findViewById(R.id.edtIdProd);
        edtNom = findViewById(R.id.edtNom);
        edtDes = findViewById(R.id.edtDes);
        edtCost = findViewById(R.id.edtCost);
        spSpinner = findViewById(R.id.spSpinner);
        lista = findViewById(R.id.lstLista);

        //Pantalla de carga
        lnCarga = findViewById(R.id.lnCarga);
        lnFormulario = findViewById(R.id.lnFormulario);

        //AsyncTask
        MyAsyncTask asyncTask = new MyAsyncTask();
        asyncTask.execute();

        //Poblar Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categorias);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSpinner.setAdapter(spinnerAdapter);

        //Cargar la Lista
        CargarLista();
    }

    public void onClickAgregar(View view){
        DataHelper dh = new DataHelper(this, "producto.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("nom", edtNom.getText().toString());
        reg.put("des", edtDes.getText().toString());
        reg.put("cost", edtCost.getText().toString());
        reg.put("cat", spSpinner.getSelectedItem().toString());
        long resp = bd.insert("producto", null, reg);
        bd.close();
        if (resp == -1){
            Toast.makeText(this,"No se pudo Ingresar",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Dato Ingresado Correctamente",Toast.LENGTH_LONG).show();
        }
        CargarLista();
        Limpiar();
    }

    private void CargarLista() {
        DataHelper dh = new DataHelper(this, "producto.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        Cursor c = bd.rawQuery("SELECT idProd, nom, des, cat, cost FROM producto", null);
        String[] arr = new String[c.getCount()];
        if(c.moveToFirst() == true){
            int i = 0;
            do{
                String linea = "|| " + c.getInt(0) + " || " + c.getString(1)+
                        " || " + c.getString(2) + "||" + c.getString(3)+
                        " || "+" ||"+ c.getString(4) ;
                arr[i] = linea;
                i++;
            }while (c.moveToNext() == true);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_expandable_list_item_1, arr);
            lista.setAdapter(adapter);
            bd.close();
        }
    }

    public void onClickModificar(View view){
        DataHelper dh = new DataHelper(this, "producto.db", null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        ContentValues reg = new ContentValues();
        reg.put("nom", edtNom.getText().toString());
        reg.put("des", edtDes.getText().toString());
        reg.put("cost", edtCost.getText().toString());
        reg.put("cat", spSpinner.getSelectedItem().toString());
        long resp  = bd.update("producto", reg, "idProd=?", new String[]
                {edtIdProd.getText().toString()});
        bd.close();
        if(resp == -1){
            Toast.makeText(this, "No se pudo Modificar",
                    Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Dato Modificado",
                    Toast.LENGTH_LONG).show();
        }
        CargarLista();
        Limpiar();
    }

    public void onClickEliminar(View view){
        DataHelper dh = new DataHelper(this, "producto.db",  null, 1);
        SQLiteDatabase bd = dh.getWritableDatabase();
        String bIdProd = edtIdProd.getText().toString();
        long resp = bd.delete("producto", "idProd="+ bIdProd, null);
        bd.close();
        if(resp ==-1){
            Toast.makeText(this, "No se pudo Eliminar",
                    Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Dato Eliminado",
                    Toast.LENGTH_LONG).show();
        }
        Limpiar();
        CargarLista();
    }

    public void Limpiar(){
        edtIdProd.setText("");
        edtDes.setText("");
        edtNom.setText("");
        edtCost.setText("");
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... voids){
            try{
                Thread.sleep(3000); //Duración de la pantalla de carga.
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "Bienvenido";
        }
        @Override
        protected void onPostExecute(String result){
            //Desaparecer pantalla de carga
            lnCarga.setVisibility(lnCarga.INVISIBLE);
            //Aparecer formulario y lista
            lnFormulario.setVisibility(lnFormulario.VISIBLE);
            lista.setVisibility(lista.VISIBLE);
        }
    }
}
