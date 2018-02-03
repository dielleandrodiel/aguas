package com.example.aguas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import basededatos.Tcabecera;
import basededatos.Tdetalles;
import basededatos.Tllaves;
import basededatos.Tobs;
import basededatos.Triesgo;
import basededatos.Tsistema;
import basededatos.Tubicacion;
import basededatos.baseandroid;

public class mediciones extends Activity {
   public static String cantidad_de_digitos="10",lec="";
   public static int mod_cd=0;//cantidad de digitos por defecto si no se permite la modificacion.
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public Cursor ubicacion_Cursor=null;
    public Cursor llaves_Cursor=null;
    public Cursor riesgo_Cursor=null;
    public Cursor obs_Cursor=null;

    public static int idobs,idllave,idubicacion,idriesgo=0;//llenar con los codigos que se traen en la base
    public Cursor cursor_medicion=null;

    Spinner combo_ubicacion;
    Spinner combo_llaves;
    Spinner combo_riesgo;
    Spinner combo_obs;

    EditText nrosec,tynmedi,domicilio,ubicacion,observaciones,lectura,obs_adic,cant_digitos;
    TextView totales,restantes;
    baseandroid admin;

    Button boton_guardar,boton_buscar,boton_otro,boton_fuera,boton_cancelar;

    String cod_ubicacion,descrp_ubicacion;
    String cod_llave,descrp_llave;
    String cod_riesgo,descrp_riesgo;
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
        setContentView(R.layout.lecturas);

        boton_guardar=(Button)findViewById(R.id.button2);
        boton_otro=(Button)findViewById(R.id.button3);
        boton_fuera=(Button)findViewById(R.id.button4);
        boton_buscar=(Button)findViewById(R.id.button5);
        boton_cancelar=(Button)findViewById(R.id.button6);

        nrosec=(EditText)findViewById(R.id.editText);
        nrosec.setEnabled(false);
        tynmedi=(EditText)findViewById(R.id.editText2);
        tynmedi.setEnabled(false);
        domicilio=(EditText)findViewById(R.id.editText3);
        domicilio.setEnabled(false);
        ubicacion=(EditText)findViewById(R.id.editText4);
        ubicacion.setEnabled(false);

        observaciones=(EditText)findViewById(R.id.editText5);
        lectura=(EditText)findViewById(R.id.editText6);
        obs_adic=(EditText)findViewById(R.id.editText8);
        cant_digitos=(EditText)findViewById(R.id.editText9);
        totales=(TextView)findViewById(R.id.textView15);
        restantes=(TextView)findViewById(R.id.textView13);

        //validaciones

        lectura .addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()==0)
                    boton_guardar.setEnabled(false);
                 else
                    boton_guardar.setEnabled(true);
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                lec=s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String valor= lectura .getText().toString();
                if (valor.length()!=0){
                try{
                    Util.esNumero(valor, 10, 1, false);
                }catch(UtilException e){
                    if(valor.length() != 0){
                        lectura.getText().delete(valor.length() - 1, valor.length());
                    }
                }
                }
                }
        });
        ////////




    }

    public void irmenu(View view){

        Intent intent = new Intent(this,menu.class);
        startActivity(intent);


    }
    public void guarda_mediciones(View view){

    if (lectura.getText().toString().length()==0)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ERROR EN EL CAMPO LECTURA");
        builder.setMessage("EL CAMPO DE LECTURA DEBE SER INGRESADO!");
        builder.setPositiveButton("OK",null);
        builder.create();
        builder.show();
    }else{


        int cantidig=10;
        String obs1 ="-",obs2="-";
        int cantidad_digitos;
        int indiceDePunto = lectura.getText().toString().indexOf('.');
        if(indiceDePunto < 0)
            cantidad_digitos=lectura.getText().length();
        else
            cantidad_digitos=lectura.getText().length()-1;




       int codigo_de_obs=Integer.valueOf(Tobs.cod_obs(admin.getWritableDatabase(),idobs));

       // Toast.makeText(getBaseContext(),
         //       "Tiene E: "+String.valueOf((Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase()
           //             ,idobs)))))+"codigo de obs: "+String.valueOf(codigo_de_obs),
             //   Toast.LENGTH_LONG).show() ;
        if (
             ((Double.valueOf(lectura.getText().toString()))<=Double.valueOf(Tdetalles.medicion_anterior(admin,nrosec.getText().toString())))
             &&
             ( (codigo_de_obs!=15)&&(codigo_de_obs!=19) )
             &&
             ((Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase(),idobs) )))==false)
           )
        {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
           // builder.setTitle("LECTURA ACTUAL MENOR QUE LA LECTURA ANTERIOR");
            builder.setTitle("L. ACTUAL: "+(String.valueOf(Double.valueOf(lectura.getText().toString())))+" <  L. ANTERIOR: "+String.valueOf(Tdetalles.medicion_anterior(admin,nrosec.getText().toString())));
            builder.setMessage("Solo Se permite codigo de observacion 15 (Lectura actual menor que anterior) o codigo 19 (Vuelta de medidor)");
            builder.setPositiveButton("OK",null);
            builder.create();
            builder.show();
            lectura.setText(lec.toString());
        } else{

            if (cant_digitos.getText().toString().length()==0)
                cantidig=0;
            else
                cantidig=Integer.valueOf(cant_digitos.getText().toString());

            if (cantidig>=(cantidad_digitos)){





                    admin=new baseandroid(this, "administracion", null, 1);

                    if ((obs_adic.getText().toString().length()==0)&(Tobs.permite_obs_adic(admin,idobs))){

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("ERROR EN EL CAMPO OBSERVACIONES ADICIONALES");
                        builder.setMessage("EL CAMPO DE OBSERVACIONES ADICIONALES DEBE SER INGRESADO!");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                lectura.setText("0");
                            }
                        });
                        builder.create();
                        builder.show();


                    } else{


    if (observaciones.getText().toString().length()!=0) obs1=observaciones.getText().toString();
    if (obs_adic.getText().toString().length()!=0) obs2=obs_adic.getText().toString();

                        try{

                    Tdetalles.guardar_medicion(
                            admin,
                            (Tcabecera.ultimoId(admin.getReadableDatabase())),
                            nrosec.getText().toString(),
                            Double.valueOf(lectura.getText().toString()),//carga el usr
                            Tobs.cod_obs(admin.getWritableDatabase(),idobs),
                            obs1,//carga el usr
                            cantidig,////cantidad de digitos
                            Tubicacion.cod_ubicacion(admin.getWritableDatabase(),idubicacion),
                            Tllaves.cod_llave(admin.getWritableDatabase(),idllave),
                            Triesgo.cod_riesgo(admin.getWritableDatabase(),idriesgo),
                            "nada ",//geoposicion
                            PedidoGlobal.getFechaActualCel().toString(),//fecha medicion real
                            PedidoGlobal.getHoraActualCel().toString(),//hora medicion real
                            "n",//enviado
                            ubicacion.getText().toString(),
                            "s",//cargado
                            obs2,
                            PedidoGlobal.getFechaActualCel().toString()//fecha medicion
                                                                                                );
                        }catch(NumberFormatException e){
                            Toast.makeText(getApplicationContext(),
                                   "INGRESE TODOS LOS DATOS REQUERIDOS CON EL FORMATO ADECUADO!!", Toast.LENGTH_LONG)
                                    .show();
                            lectura.setText(lec.toString());
                        }

                         Toast.makeText(getBaseContext(),
                        "MEDICION GUARDADA",
                                 Toast.LENGTH_LONG).show() ;


                boton_otro.setEnabled(true);
                boton_fuera.setEnabled(true);
                boton_guardar.setEnabled(true);
                boton_buscar.setVisibility(view.INVISIBLE);

                nrosec.setText("");
                nrosec.setEnabled(false);
                tynmedi.setText("");
                tynmedi.setEnabled(false);
                domicilio.setText("");
                domicilio.setEnabled(false);
                ubicacion.setText("");
                ubicacion.setEnabled(false);
                observaciones.setText("");
                lectura.setText("");
                obs_adic.setText("");
                cant_digitos.setText("");

                cursor_medicion= Tdetalles.cargar_medicion(admin);
                cursor_medicion.moveToFirst();

                if ((cursor_medicion.getCount()==0)|(cursor_medicion==null)){
                    Toast.makeText(getApplicationContext(),
                            "SE COMPLETARON LAS MEDICIONES ASIGNADAS ,haga click en el boton DESCARGAR DATOS del menu principal", Toast.LENGTH_LONG)
                            .show();
                    cursor_medicion.close();
                    boton_guardar.setEnabled(false);
                    totales.setText("COMPLETO");
                    restantes.setText("0");
                    PedidoGlobal.datos_recibidos=0;
                    Tsistema.setear_permite_descarga(admin.getWritableDatabase(),0);
                }
                else{




                    boton_guardar.setEnabled(true);
                    nrosec.setText(cursor_medicion.getString(2).toString());
                    tynmedi.setText(cursor_medicion.getString(3).toString()+"-"+cursor_medicion.getString(4).toString());
                    domicilio.setText(cursor_medicion.getString(12).toString()+String.valueOf(cursor_medicion.getInt(13)));
                    ubicacion.setText(cursor_medicion.getString(23).toString());



                    idllave=Tllaves.idllave(admin.getReadableDatabase(),cursor_medicion.getString(15).toString());
                    idubicacion=Tubicacion.idubicacion(admin.getReadableDatabase(),cursor_medicion.getString(14).toString());
                    idobs=Tobs.idobs(admin.getReadableDatabase(),cursor_medicion.getString(6).toString());
                    idriesgo=Triesgo.idriesgo(admin.getReadableDatabase(),cursor_medicion.getString(16).toString());

                    combo_llaves.setSelection(idllave);
                    combo_ubicacion.setSelection(idubicacion);
                    combo_obs.setSelection(idobs);
                    combo_riesgo.setSelection(idriesgo);

                    if(Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase(),idobs)))) {
                        lectura.setText("0");
                        lectura.setEnabled(false);
                       // cant_digitos.setText("0");
                       // cant_digitos.setEnabled(false);
                    }else{
                        lectura.setText(lec);
                        lectura.setEnabled(true);
                     //   cant_digitos.setText("");
                        //cant_digitos.setEnabled(true);
                    }
                    cantidad_de_digitos=String.valueOf(Tdetalles.cant_digitos(admin,nrosec.getText().toString()));
                    cant_digitos.setText(cantidad_de_digitos);

                    totales.setText(String.valueOf(Tdetalles.Cantidad_mediciones_cargadas(admin))
                            +"/"+String.valueOf(Tdetalles.Cantidad_mediciones_totales(admin)));
                    restantes.setText(String.valueOf(Tdetalles.Cantidad_mediciones_restantes(admin)));
                   // cursor_medicion.close();
                }
                }

            }else{

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("ERROR EN EL INGRESO DE CAMPO LECTURA-CANTIDAD DIGITOS");
                builder.setMessage("El campo lectura no puede tener mas digitos que el campo cantidad de digitos!!");
                lectura.setText(lec.toString());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        lectura.setText(lec);
                    }
                });
                builder.create();
                builder.show();

            }


        }
    }
        if (Tcabecera.permite_can_dig(admin.getReadableDatabase()))
            cant_digitos.setEnabled(true);
        else
            cant_digitos.setEnabled(false);

        lectura.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();

        boton_guardar.setEnabled(true);
        obs_adic.setEnabled(true);
        observaciones.setEnabled(true);
        boton_otro.setEnabled(true);
        boton_fuera.setEnabled(true);
        boton_buscar.setVisibility(View.INVISIBLE);
        boton_cancelar.setVisibility(View.INVISIBLE);

        admin=new baseandroid(this, "administracion", null, 1);
        cursor_medicion= Tdetalles.cargar_medicion(admin);
        cursor_medicion.moveToFirst();

        if ((cursor_medicion.getCount()==0)|(cursor_medicion==null)){

            Toast.makeText(getApplicationContext(),
                    "No hay mediciones para cargar , haga click en el boton DESCARGAR DATOS O ENVIAR DATOS del menu principal", Toast.LENGTH_LONG)
                    .show();
           // cursor_medicion.close();
            boton_guardar.setEnabled(false);

        }
        else{
            boton_guardar.setEnabled(true);
           try{


            nrosec.setText(cursor_medicion.getString(2).toString());
            tynmedi.setText(cursor_medicion.getString(3).toString()+"-"+cursor_medicion.getString(4).toString());
            domicilio.setText(cursor_medicion.getString(12).toString()+String.valueOf(cursor_medicion.getInt(13)));
            ubicacion.setText(cursor_medicion.getString(23).toString());

            cantidad_de_digitos=String.valueOf(Tdetalles.cant_digitos(admin,nrosec.getText().toString()));
            cant_digitos.setText(cantidad_de_digitos);

               idllave=Tllaves.idllave(admin.getReadableDatabase(),cursor_medicion.getString(15).toString());
               if (String.valueOf(idllave).length()==0)
               idllave=1;

               idubicacion=Tubicacion.idubicacion(admin.getReadableDatabase(),cursor_medicion.getString(14).toString());
               if (String.valueOf(idubicacion).length()==0)
                   idubicacion=1;

               idobs=Tobs.idobs(admin.getReadableDatabase(),cursor_medicion.getString(6).toString());
               if (String.valueOf(idobs).length()==0)
                   idobs=1;

            idriesgo=Triesgo.idriesgo(admin.getReadableDatabase(),cursor_medicion.getString(16).toString());
               if (String.valueOf(idriesgo).length()==0)
                   idriesgo=1;

           }catch (Exception e){
               Toast.makeText(getBaseContext(),
                      String.valueOf( cursor_medicion.getCount())+"---"+  e.toString()+  "HUBO UN ERROR AL DESPLAZARSE AL SIGUIENTE REGISTRO!! "
                       , Toast.LENGTH_LONG)
                       .show();
           }



            if(Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase(),idobs)))) {
                lectura.setText("0");
                lectura.setEnabled(false);
                //cant_digitos.setText("0");
                cant_digitos.setEnabled(false);
            }else{
                lectura.setText("lect");
                lectura.setEnabled(true);
               // cant_digitos.setText("");
               //cant_digitos.setEnabled(true);
            }






            totales.setText(String.valueOf(Tdetalles.Cantidad_mediciones_cargadas(admin))
                    +"/"+String.valueOf(Tdetalles.Cantidad_mediciones_totales(admin)));
            restantes.setText(String.valueOf(Tdetalles.Cantidad_mediciones_restantes(admin)));
          //  cursor_medicion.close();

        }



        combo_ubicacion= (Spinner) this.findViewById(R.id.spinner2);
        ubicacion_Cursor = Tubicacion.todas(admin);
        startManagingCursor(ubicacion_Cursor);

        combo_llaves= (Spinner) this.findViewById(R.id.spinner3);
        llaves_Cursor = Tllaves.todas(admin);
        startManagingCursor(llaves_Cursor);


        combo_riesgo= (Spinner) this.findViewById(R.id.spinner4);
        riesgo_Cursor = Triesgo.todos(admin);
       startManagingCursor(riesgo_Cursor);


        combo_obs= (Spinner) this.findViewById(R.id.spinner);
        obs_Cursor = Tobs.todas(admin);
       startManagingCursor(obs_Cursor);


        if ((ubicacion_Cursor.getCount()!=0) & (ubicacion_Cursor!=null)){

            combo_ubicacion.setEnabled(true);


            String[] from = new String[] {"descripcion_ubicacion"};
            int[] to = new int[] {android.R.id.text1};

            @SuppressWarnings("deprecation")
            SimpleCursorAdapter mAdapter =
                    new SimpleCursorAdapter
                            (this,android.R.layout.simple_spinner_item, ubicacion_Cursor, from, to);
            mAdapter.setDropDownViewResource

                    (android.R.layout.simple_spinner_dropdown_item);
            combo_ubicacion.setAdapter(mAdapter);

            combo_ubicacion.setOnItemSelectedListener(new SpinnerListener());
            // combo_ubicacion.setOnItemClickListener(onListClick);
            // combo_ubicacion.setSelection(-1);
            //combo_ubicacion.set
        }else{
            Toast.makeText(getBaseContext(),
                    "No hay ubicaciones  en la base de datos local , haga click en la opcion de menu ACTUALIZAR SISTEMA "
                    , Toast.LENGTH_LONG)
                    .show();
            combo_ubicacion.setEnabled(false);

        }
        combo_ubicacion.setEnabled(true);
        combo_ubicacion.setSelection(idubicacion);


        if ((llaves_Cursor.getCount()!=0) & (llaves_Cursor!=null)){

            combo_llaves.setEnabled(true);


            String[] from1 = new String[] {"descripcion_llave"};
            int[] to1 = new int[] {android.R.id.text1};

            @SuppressWarnings("deprecation")
            SimpleCursorAdapter mAdapter2 =
                    new SimpleCursorAdapter
                            (this,android.R.layout.simple_spinner_item, llaves_Cursor, from1, to1);
            mAdapter2.setDropDownViewResource

                    (android.R.layout.simple_spinner_dropdown_item);
            combo_llaves.setAdapter(mAdapter2);

            combo_llaves.setOnItemSelectedListener(new SpinnerListener());

        }else{
            Toast.makeText(getBaseContext(),
                    "No hay LLaves  en la base de datos local , haga click enla opcion de menu ACTUALIZAR SISTEMA "
                    , Toast.LENGTH_LONG)
                    .show();
            combo_llaves.setEnabled(false);

        }
        combo_llaves.setEnabled(true);
        combo_llaves.setSelection(Integer.valueOf(idllave));

        if ((riesgo_Cursor.getCount()!=0) & (riesgo_Cursor!=null)){

            combo_riesgo.setEnabled(true);


            String[] from3 = new String[] {"descripcion_riesgo"};
            int[] to3 = new int[] {android.R.id.text1};

            @SuppressWarnings("deprecation")
            SimpleCursorAdapter mAdapter3 =
                    new SimpleCursorAdapter
                            (this,android.R.layout.simple_spinner_item, riesgo_Cursor, from3, to3);
            mAdapter3.setDropDownViewResource

                    (android.R.layout.simple_spinner_dropdown_item);
            combo_riesgo.setAdapter(mAdapter3);

            combo_riesgo.setOnItemSelectedListener(new SpinnerListener());
            // combo_ubicacion.setOnItemClickListener(onListClick);
            // combo_ubicacion.setSelection(-1);
            //combo_ubicacion.set
        }else{
            Toast.makeText(getBaseContext(),
                    "No hay riesgos  en la base de datos local , haga click en la opcion de menu ACTUALIZAR SISTEMA "
                    , Toast.LENGTH_LONG)
                    .show();
            combo_riesgo.setEnabled(false);


        }
        combo_riesgo.setEnabled(true);
        combo_riesgo.setSelection(idriesgo);

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
        combo_obs.setSelection(idobs);

        if (Tcabecera.permite_llave(admin.getReadableDatabase()))
            combo_llaves.setEnabled(true);
        else
            combo_llaves.setEnabled(false);

        if (Tcabecera.permite_ubicacion(admin.getReadableDatabase()))
            combo_ubicacion.setEnabled(true);
        else
            combo_ubicacion.setEnabled(false);

        if (Tcabecera.permite_riesgo(admin.getReadableDatabase()))
            combo_riesgo.setEnabled(true);
        else
            combo_riesgo.setEnabled(false);


        if (Tcabecera.permite_can_dig(admin.getReadableDatabase())){
            cant_digitos.setEnabled(true);
            //cant_digitos.setText("");
        }else{
            cant_digitos.setEnabled(false);
        }

        admin.close();
    }





    public class SpinnerListener implements AdapterView.OnItemSelectedListener {


        public void onItemSelected(AdapterView<?> adapter, View arg1, int position,
                                   long arg3) {

           // lectura.setText(lec.toString());
            if (adapter.getAdapter().toString().equals(combo_ubicacion.getAdapter().toString())){

                ubicacion_Cursor.moveToPosition(position);
                idubicacion= ubicacion_Cursor.getInt(0);

                cod_ubicacion=ubicacion_Cursor.getString(1).toString();
                descrp_ubicacion=ubicacion_Cursor.getString(2).toString();
               // lectura.setText(lec.toString());

            }
            else  if (adapter.getAdapter().toString().equals(combo_llaves.getAdapter().toString())){
                llaves_Cursor.moveToPosition(position);
                idllave= llaves_Cursor.getInt(0);

                // Toast.makeText(getBaseContext(),
                //			"Ha seleccionado: "+
                //				ubicacion_Cursor.getString(1).toString(), Toast.LENGTH_SHORT)
                //	.show();


                cod_llave=llaves_Cursor.getString(1).toString();
                descrp_llave=llaves_Cursor.getString(2).toString();

            }
            else  if (adapter.getAdapter().toString().equals(combo_riesgo.getAdapter().toString())){
                riesgo_Cursor.moveToPosition(position);
                idriesgo= riesgo_Cursor.getInt(0);
                // Toast.makeText(getBaseContext(),
                //			"Ha seleccionado: "+
                //				ubicacion_Cursor.getString(1).toString(), Toast.LENGTH_SHORT)
                //	.show();


                cod_riesgo=riesgo_Cursor.getString(1).toString();
                descrp_riesgo=riesgo_Cursor.getString(2).toString();

            }
            else if (adapter.getAdapter().toString().equals(combo_obs.getAdapter().toString())){
                obs_Cursor.moveToPosition(position);
                idobs = obs_Cursor.getInt(0);
                // Toast.makeText(getBaseContext(),
                //			"Ha seleccionado: "+
                //				ubicacion_Cursor.getString(1).toString(), Toast.LENGTH_SHORT)
                //	.show();

              /*  Toast.makeText(getBaseContext(),
                        "Tiene E: "+String.valueOf(Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase()
                                ,idobs))))+"codigo de obs: "+Tobs.cod_obs(admin.getWritableDatabase()
                                ,idobs),
                        Toast.LENGTH_LONG).show() ;
                  */
                if(Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase(),idobs)))) {
                    lectura.setText("0");
                    lectura.setEnabled(false);
                   // cant_digitos.setText("0");
                    cant_digitos.setEnabled(false);
                }else{
                    lectura.setText(lec);
                    lectura.setEnabled(true);
                  //  cant_digitos.setText("");
                 //  cant_digitos.setEnabled(true);
                }

                cod_obs=obs_Cursor.getString(1).toString();
                descrp_obs=obs_Cursor.getString(2).toString();

            }
            if (lectura.getText().toString().length()==0)
              lectura.setText(lec.toString());
        }
        public void onNothingSelected(AdapterView<?> arg0) {


        }
    }
    public void otro(View view){
        nrosec.setText("");
        nrosec.setEnabled(false);
        tynmedi.setText("");
        tynmedi.setEnabled(true);
        domicilio.setText("");
        domicilio.setEnabled(false);
        ubicacion.setText("");
        ubicacion.setEnabled(false);
        observaciones.setText("");
        observaciones.setEnabled(false);
        lectura.setText("");
        lectura.setEnabled(false);
        obs_adic.setText("");
        obs_adic.setEnabled(false);
      //  cant_digitos.setText(String.valueOf(Tdetalles.cant_digitos(admin,Integer.valueOf(nrosec.getText().toString()))));
        if (Tcabecera.permite_can_dig(admin.getReadableDatabase()))
            cant_digitos.setEnabled(true);
        else
            cant_digitos.setEnabled(false);


        combo_riesgo.setEnabled(false);
        combo_ubicacion.setEnabled(false);
        combo_obs.setEnabled(false);
        combo_llaves.setEnabled(false);
        boton_guardar.setEnabled(true);
        boton_otro.setEnabled(false);
        boton_fuera.setEnabled(false);
        boton_buscar.setEnabled(true);
        boton_buscar.setVisibility(View.VISIBLE);
        boton_cancelar.setEnabled(true);
        boton_cancelar.setVisibility(View.VISIBLE);
    }

    public void fuera(View view){
      /*
        nrosec.setText("");
        nrosec.setEnabled(false);
        tynmedi.setText("");
        tynmedi.setEnabled(true);
        domicilio.setText("");
        domicilio.setEnabled(false);
        ubicacion.setText("");
        ubicacion.setEnabled(false);
        observaciones.setText("");
        observaciones.setEnabled(false);
        lectura.setText("");
        lectura.setEnabled(false);
        obs_adic.setText("");
        obs_adic.setEnabled(false);
        cant_digitos.setText("");
        cant_digitos.setEnabled(false);
        combo_riesgo.setEnabled(false);
        combo_ubicacion.setEnabled(false);
        combo_obs.setEnabled(true);
        combo_llaves.setEnabled(false);
        boton_guardar.setEnabled(true);
        boton_otro.setEnabled(false);
        boton_fuera.setEnabled(false);
        boton_buscar.setEnabled(false);
        boton_buscar.setVisibility(View.INVISIBLE);
        */
        Intent intent = new Intent(this,fuera.class);
        startActivity(intent);
    }


    public void busca(View view){
        boton_guardar.setEnabled(false);
        tynmedi.requestFocus();
      //  cursor_medicion.close();
if (tynmedi.getText().toString().length()==0){
    Toast.makeText(getBaseContext(),"INGRESE NRO DE MEDIDOR A BUSCAR!!", Toast.LENGTH_SHORT).show();
}else{

    Cursor cursor_busca=Tdetalles.buscar_medicion(admin,tynmedi.getText().toString());
    cursor_busca.moveToFirst();
     if ((cursor_busca.getCount()==0) | (cursor_busca==null)){
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setTitle("BUSQUEDA DE MEDIDOR POR NUMERO");
         builder.setMessage("NO SE ENCONTRO EL MEDIDOR CON EL NUMERO: "+tynmedi.getText().toString());
         builder.setPositiveButton("OK",null);
         builder.create();
         builder.show();
     }else{
         combo_obs.setEnabled(true);
         domicilio.setEnabled(true);
         observaciones.setEnabled(true);
         lectura.setEnabled(true);
         obs_adic.setEnabled(true);
         if (Tcabecera.permite_llave(admin.getReadableDatabase()))
             combo_llaves.setEnabled(true);
         else
             combo_llaves.setEnabled(false);

         if (Tcabecera.permite_ubicacion(admin.getReadableDatabase()))
             combo_ubicacion.setEnabled(true);
         else
             combo_ubicacion.setEnabled(false);

         if (Tcabecera.permite_riesgo(admin.getReadableDatabase()))
             combo_riesgo.setEnabled(true);
         else
             combo_riesgo.setEnabled(false);

         if (Tcabecera.permite_can_dig(admin.getReadableDatabase()))
             cant_digitos.setEnabled(true);
         else
             cant_digitos.setEnabled(false);


         boton_guardar.setEnabled(true);
         boton_otro.setEnabled(true);
         boton_fuera.setEnabled(true);
         boton_buscar.setEnabled(true);
         boton_buscar.setVisibility(View.INVISIBLE);
         boton_cancelar.setEnabled(true);
         boton_cancelar.setVisibility(View.INVISIBLE);
         try{


             nrosec.setText(cursor_busca.getString(2).toString());
             tynmedi.setText(cursor_busca.getString(3).toString()+"-"+cursor_busca.getString(4).toString());
             domicilio.setText(cursor_busca.getString(12).toString()+String.valueOf(cursor_busca.getInt(13)));
             ubicacion.setText(cursor_busca.getString(23).toString());

             idllave=Tllaves.idllave(admin.getReadableDatabase(),cursor_medicion.getString(15).toString());
             idubicacion=Tubicacion.idubicacion(admin.getReadableDatabase(),cursor_medicion.getString(14).toString());
             idobs=Tobs.idobs(admin.getReadableDatabase(),cursor_medicion.getString(6).toString());
             idriesgo=Triesgo.idriesgo(admin.getReadableDatabase(),cursor_medicion.getString(16).toString());
             cant_digitos.setText(String.valueOf(Tdetalles.cant_digitos(admin,nrosec.getText().toString())));
         }catch (Exception e){
             Toast.makeText(getBaseContext(),
                     String.valueOf(cursor_busca.getCount())+"---"+  e.toString()+  "HUBO UN ERROR AL DESPLAZARSE AL SIGUIENTE REGISTRO!! "
                     , Toast.LENGTH_LONG)
                     .show();

         }



         if(Tobs.tiene_E(admin,(Tobs.cod_obs(admin.getWritableDatabase(),idobs)))) {
             lectura.setText("0");
             lectura.setEnabled(false);
            // cant_digitos.setText("0");
             cant_digitos.setEnabled(false);
         }else{
             lectura.setText(lec);
             lectura.setEnabled(true);
           //  cant_digitos.setText("");
             //cant_digitos.setEnabled(true);
         }






         totales.setText(String.valueOf(Tdetalles.Cantidad_mediciones_cargadas(admin))
                 +"/"+String.valueOf(Tdetalles.Cantidad_mediciones_totales(admin)));
         restantes.setText(String.valueOf(Tdetalles.Cantidad_mediciones_restantes(admin)));
        // cursor_busca.close();


     }




    }
    }
    public void cancelab(View view){

       this.onStart();
    }
 }
