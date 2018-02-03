package com.example.aguas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import basededatos.Tcabecera;
import basededatos.Tdetalles;
import basededatos.Tfuera_de_ruta;
import basededatos.Tobs;
import basededatos.Tusuarios;
import basededatos.baseandroid;

public class fuera extends Activity {
    public Cursor obs_Cursor=null;
    public static int idobs;
    Spinner combo_obs;
    EditText tmedi,nmedi,callemedi,alturamedi,lecturamedi;
    baseandroid admin;
    String cod_obs,descrp_obs;
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(this,
                "No se puede volver atras en esta pantalla.", Toast.LENGTH_SHORT)
                .show();
        return;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cel_fuera_ruta);
        tmedi=(EditText)findViewById(R.id.ettipo);
        nmedi=(EditText)findViewById(R.id.etnro);
        callemedi=(EditText)findViewById(R.id.etcalle);
        alturamedi=(EditText)findViewById(R.id.etaltura);
        lecturamedi=(EditText)findViewById(R.id.etlectura);
        lecturamedi .addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String valor= lecturamedi .getText().toString();
                if (valor.length()!=0){
                    try{
                        Util.esNumero(valor, 10, 1, false);
                    }catch(UtilException e){
                        if(valor.length() != 0){
                            lecturamedi.getText().delete(valor.length() - 1, valor.length());
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        admin=new baseandroid(this, "administracion", null, 1);

        //idobs= Tobs.idobs(admin.getReadableDatabase(), cursor_medicion.getString(6).toString());

        combo_obs= (Spinner) this.findViewById(R.id.fcomboobs);
        obs_Cursor = Tobs.todas(admin);

        if ((obs_Cursor.getCount()!=0) & (obs_Cursor!=null)){

            combo_obs.setEnabled(true);


            String[] from4 = new String[] {"descripcion_obs"};
            int[] to4 = new int[] {android.R.id.text1};

            @SuppressWarnings("deprecation")
            SimpleCursorAdapter mAdapter4 =
                    new SimpleCursorAdapter
                            (this,android.R.layout.simple_spinner_item, obs_Cursor, from4, to4);
            mAdapter4.setDropDownViewResource

                    (android.R.layout.simple_spinner_dropdown_item);
            combo_obs.setAdapter(mAdapter4);

            combo_obs.setOnItemSelectedListener(new SpinnerListener());
            // combo_ubicacion.setOnItemClickListener(onListClick);
            // combo_ubicacion.setSelection(-1);
            //combo_ubicacion.set
        }else{
            Toast.makeText(getBaseContext(),
                    "No hay observaciones  en la base de datos local , haga click en la opcion de menu DESCARGAR DATOS "
                    , Toast.LENGTH_LONG)
                    .show();
            combo_obs.setEnabled(false);

        }
        combo_obs.setEnabled(true);
        combo_obs.setSelection(0);

        admin.close();

    }

    public class SpinnerListener implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> adapter, View arg1, int position,
                                   long arg3) {

            if (adapter.getAdapter().toString().equals(combo_obs.getAdapter().toString())){
                obs_Cursor.moveToPosition(position);
                idobs = obs_Cursor.getInt(0);

                cod_obs=obs_Cursor.getString(1).toString();
                descrp_obs=obs_Cursor.getString(2).toString();

            }

        }
        public void onNothingSelected(AdapterView<?> arg0) {


        }
    }

public void fguarda(View view){
    double lectu=0.0;
    String tipo,calle,numero="-";
    int altura=0;


    //control lectura
    if (lecturamedi.getText().toString().length()==0)
    lectu=0.0;
    else
    lectu=Double.valueOf(lecturamedi.getText().toString());

    //control tipo
    if (tmedi.getText().toString().length()==0)
        tipo="-";
    else
        tipo=tmedi.getText().toString();

    //control calle
    if (callemedi.getText().toString().length()==0)
        calle="-";
    else
        calle=callemedi.getText().toString();

    //control numero
    if (nmedi.getText().toString().length()==0)
        numero="-";
    else
        numero=nmedi.getText().toString();

    //control altura
    if (alturamedi.getText().toString().length()==0)
        altura=0;
    else
       altura=Integer.valueOf(alturamedi.getText().toString());




    if (!(Tdetalles.buscar_nro_medidor(admin,nmedi.getText().toString()))){

    //  if (nmedi.getText().toString().length()>0){

        admin=new baseandroid(this, "administracion", null, 1);
       try{
       if( Tfuera_de_ruta.insertar_fuera_ruta(
               admin.getWritableDatabase(),
               (Tcabecera.ultimoId(admin.getReadableDatabase())),
               tipo,
               numero,
               lectu,
               Tobs.cod_obs(admin.getWritableDatabase(), idobs),
               calle,
               altura,
               Tusuarios.usuariologueado.toString(),
               "n",//enviado
               "s",//cargado
               PedidoGlobal.getFechaActualCel().toString()//fecha medicion

       )){
           Toast.makeText(getApplicationContext(),
                   "LOS DATOS SE GUARDARON EXITOSAMENTE!!", Toast.LENGTH_LONG)
                   .show();

           tmedi.setText("");
           nmedi.setText("");
           lecturamedi.setText("");
           combo_obs.setSelection(0);
           callemedi.setText("");
           alturamedi.setText("");


       }else{
           Toast.makeText(getApplicationContext(),
                   "ERROR AL GUARDAR LOS DATOS!!", Toast.LENGTH_LONG)
                   .show();
       }
    }catch(NumberFormatException e){
        Toast.makeText(getApplicationContext(),
                "INGRESE TODOS LOS DATOS REQUERIDOS CON EL FORMATO ADECUADO!!", Toast.LENGTH_LONG)
                .show();

    }
  /*    }else{
          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("MEDIDOR FUERA DE LA RUTA");
          builder.setMessage("EL CAMPO TIPO Y NUMERO DE MEDIDOR ES OBLIGATORIO!!: "+nmedi.getText().toString());
          builder.setPositiveButton("OK",null);
          builder.create();
          builder.show();

      }*/
    }else{
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NO SE PUEDE GUARDAR LA MEDICION");
        builder.setMessage("EL NUMERO DE MEDIDOR: "+nmedi.getText().toString()+" YA ESTA EN LA RUTA!!!");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        obs_Cursor.close();
        admin.close();
    }

    public void fmenu(View view){

        Intent intent = new Intent(this,mediciones.class);
        startActivity(intent);



    }

}