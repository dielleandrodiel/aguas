package com.example.aguas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import basededatos.Tcabecera;
import basededatos.Tdetalles;
import basededatos.Tllaves;
import basededatos.Tobs;
import basededatos.Triesgo;
import basededatos.Tsistema;
import basededatos.Tubicacion;
import basededatos.Tusuarios;
import basededatos.baseandroid;
import utilidades.Utilidades;

public class actualizacion  extends Activity{
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public baseandroid admin=new baseandroid(this, "administracion", null, 1);
    int suma,i,j,k,m,n,o=0;
    public ProgressDialog dialog;
    public SQLiteDatabase bd;
    public static JSONArray json1,json2,json3,json4,json5;
    public static JSONObject jsonObj1,jsonObj2,jsonObj3,jsonObj4,jsonObj5;
    public static String url1,url2,url3,url4,url5,error_texto;
    public boolean error,datos_transferidos,error_usuario=false;
    public actualizacion EstaActividad;
    public static TextView tverrores;
    public Button boton;
    public static EditText us;
    public static EditText cl;
    public static String us1,cl1;


    public boolean hayconexion() {
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cel_validacion_serv);

        tverrores=(TextView)findViewById(R.id.textView);
        boton=(Button)findViewById(R.id.ip_boton);
        us=(EditText)findViewById(R.id.usuarioserver);
        cl=(EditText)findViewById(R.id.claveserver);

        EstaActividad=this;
        error=false;

        bd=admin.getWritableDatabase();

        //bd.execSQL("DROP TABLE IF EXISTS " + Tusuarios.NombreTabla);
        Tusuarios.onCreate(bd);
        //  bd.execSQL("DROP TABLE IF EXISTS " + Tsistema.NombreTabla);
        //Tsistema.onCreate(bd);
        // bd.execSQL("DROP TABLE IF EXISTS " + Tllaves.NombreTabla);
        Tllaves.onCreate(bd);
        // bd.execSQL("DROP TABLE IF EXISTS " + Tubicacion.NombreTabla);
        Tubicacion.onCreate(bd);
        //bd.execSQL("DROP TABLE IF EXISTS " + Triesgo.NombreTabla);
        Triesgo.onCreate(bd);
        //bd.execSQL("DROP TABLE IF EXISTS " + Tobs.NombreTabla);
        Tobs.onCreate(bd);
        //bd.execSQL("DROP TABLE IF EXISTS " + Tdetalles.NombreTabla);
        Tdetalles.onCreate(bd);
        //bd.execSQL("DROP TABLE IF EXISTS " + Tcabecera.NombreTabla);
        Tcabecera.onCreate(bd);

        //bd.execSQL("DROP TABLE IF EXISTS " + Tfuera_de_ruta.NombreTabla);
        //Tfuera_de_ruta.onCreate(bd);



        json1 = new JSONArray();
        jsonObj1 = null;
        json2 = new JSONArray();
        jsonObj2 = null;
        json3 = new JSONArray();
        jsonObj3 = null;
        json4 = new JSONArray();
        jsonObj4 = null;
        json5 = new JSONArray();
        jsonObj5 = null;

        String Serv= Tsistema.servidor_de_datos(admin);
      //  Toast.makeText(getBaseContext(),
            //    Serv.toString(),
              //  Toast.LENGTH_LONG).show();

        url1=Serv.toString()+"/usuario.php";
        url2=Serv.toString()+"/llaves.php";
        url3=Serv.toString()+"/observaciones.php";
        url4=Serv.toString()+"/riesgos.php";
        url5=Serv.toString()+"/ubicacion.php";


        boton.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                if (hayconexion()){
                    us1=us.getText().toString().trim();
                    cl1=cl.getText().toString().trim();

                    if ((us1.length()>0)&(cl1.length()>0)) {
                        error=false;
                        error_texto="";
                        jsonObj1 = null;
                        jsonObj2 = null;
                        jsonObj3 = null;
                        jsonObj4 = null;
                        jsonObj5 = null;
                        datos_transferidos=false;
                        error_usuario=false;
                        dialog = new ProgressDialog(EstaActividad);
                        dialog.setTitle("AGUAS ANDROID...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setMessage(Html.fromHtml("<font color='blue'>" + "ACTUALIZANDO EL SISTEMA , ESPERE POR FAVOR..." + "</font>"));
                        dialog.setCancelable(false);

                        new MiTarea().execute(url1.toString());

                    }else{
                        Toast.makeText(getBaseContext(),
                                "COMPLETE TODOS LOS DATAS DE USUARIO Y CLAVE!!",
                                Toast.LENGTH_LONG).show();

                    }



                }else {


                    Toast.makeText(getBaseContext(),
                            "NECESITA ESTAR CONECTADO A UNA RED PARA ACTUALIZAR EL SISTEMA ",
                            Toast.LENGTH_LONG).show();
                    Toast.makeText(getBaseContext(),
                            "NO  SE PUDIERON DESCARGAR LAS MEDICIONES DESDE EL SERVIDOR",
                            Toast.LENGTH_LONG).show();

                    bd.close();
                    //EstaActividad.finish();

                    // Intent actividad = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    Intent actividad = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(actividad);




                }
            }

        });




    }

    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onBackPressed() {
        Toast.makeText(this,
                "No se puede volver atras!!!", Toast.LENGTH_SHORT)
                .show();
        return;
    }
    protected void onStart() {
        super.onStart();

        //  us.setText("");
        //  cl.setText("");

    }
    public void volverservidor(View view){

        Intent intent = new Intent(this,ingreso.class);
        startActivity(intent);


    }

    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
        if (admin!=null){
            admin.close();
        }
    }
    private class MiTarea extends AsyncTask<String, Float, Integer>{

        protected void onPreExecute() {
            // json = new JSONArray();
            // jsonObj1 = null;
            //  datos_transferidos=false;
            // tverrores.setVisibility(View.INVISIBLE);
            // dialog.setProgress(0);
            dialog.show();


        }
        @ Override
        protected Integer doInBackground(String... url1s) {


            if (datos_transferidos==false){
                try {

                    //json de usuarios
                    json1= Utilidades.Transferir(url1,us1,cl1,EstaActividad.getApplicationContext());
                    //json de llaves
                    json2= Utilidades.Transferir(url2,us1,cl1,EstaActividad.getApplicationContext());
                    //json de observaciones
                    json3= Utilidades.Transferir(url3,us1,cl1,EstaActividad.getApplicationContext());
                    //json de riesgos
                    json4= Utilidades.Transferir(url4,us1,cl1,EstaActividad.getApplicationContext());
                    //json de ubicacion
                    json5= Utilidades.Transferir(url5,us1,cl1,EstaActividad.getApplicationContext());

////JSON USUARIO
                    if ((json1 ==null)|((json1.toString().contains("cero")))){
                        error_texto+= "NO SE RECIBIERON DATOS DESDE EL SERVIDOR DE DATOS!!\\n";
                        error=true;
                        error_usuario=true;
                        dialog.dismiss();
                        bd.close();
                        this.cancel(true);
                        return 0;
                    }else{
                        datos_transferidos=true;
                        dialog.setProgress(0);
                        int suma=Integer.valueOf(json2.length()+json3.length()+json4.length()+json5.length());
                       // dialog.setMax(Integer.valueOf(json2.length()+json3.length()+json4.length()+json5.length()));
                        // dialog.show(); //Mostramos el dialogo antes de comenzar

                    }

                } catch (ConnectTimeoutException e) {
                    error_texto+=   "EL SERVIDOR NO RESPONDE !\\n ";
                    error=true;
                    dialog.dismiss();
                    bd.close();

                    // EstaActividad.finish();

                }
                catch(Exception e){
                    error_texto+=  e.toString()+ " ERROR GENERAL DEL SISTEMA!\\n ";
                    dialog.dismiss();
                    bd.close();
                    error=true;

                    // EstaActividad.finish();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }


            if ((!(json1.toString().indexOf("cero")==-1))|(json1.length()==0)) {
                error_texto+=   "EL USUARIO NO EXISTE ,NO SE RECIBIERON DATOS DESDE EL SERVIDOR !\\n ";
                error=true;
                error_usuario=true;
                dialog.dismiss();
                bd.close();
                this.cancel(true);
            }


            //ESTO SE REPITE PARA LOS OTROS JSONES


            try {

                jsonObj1 = json1.getJSONObject(0);
            } catch (JSONException e) {
                error_texto+=   e.toString()+"EL USUARIO NO EXISTE EN EL SERVIDOR,COMPRUEBE SU CONEXION ANTES DE ACTUALIZAR!\\n ";
                error=true;
                error_usuario=true;
                dialog.dismiss();
                bd.close();
                this.cancel(true);
                // EstaActividad.finish();
            }

/////////////////////////////////////////////////////FIN JSON USUARIO////////////
            //JSON  LLAVES
            //.....
            if ((!(json2.toString().indexOf("cero")==-1))|(json2.length()==0)) {
                error_texto+=   "NO HAY CODIGOS DE LLAVES !\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }

            try {

                jsonObj2 = json2.getJSONObject(0);
            } catch (JSONException e) {
                error_texto+=   e.toString()+"ERROR AL TRANSFERIR CODIGOS DE LLAVES!\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }



            k=json2.length();
            dialog.setMax(Integer.valueOf(k));
            if (json2 != null && k > 0) {

                for (int i= 0; i < k; i++) {
                    try {
                        jsonObj2 = json2.getJSONObject(i);
                    } catch (JSONException e) {
                        error_texto+=   "ERROR EN EL OBJETO  JSON !  ";
                        error=true;
                        bd.close();

                    }


                    try{
                        bd=admin.getWritableDatabase();
                        if (Tllaves.insertarLLave(bd, jsonObj2.getString("codigo_llave").toString(), jsonObj2.get("descripcion_llave").toString())==false){
                            error=true;
                            error_texto+="ERROR AL INSERTAR REGISTRO EN LA TABLA DE LLAVES!\n ";
                            bd.close();
                        }



                    } catch (JSONException e) {
                        error_texto+=  e.toString()+ "ERROR EN EL  JSON DE LLAVES!  ";
                        error=true;
                        bd.close();

                    }
                    publishProgress(i/Float.valueOf(suma));

                }

            }else{
                error=true;
                error_texto+="ERROR AL ACTUALIZAR LA TABLA DE LLAVES!\n ";
                bd.close();
            }

/////////////////////////////////////////////////////FIN JSON LLAVES////////////

            //JSON  OBSERVACIONES
            //.....
            if ((!(json3.toString().indexOf("cero")==-1))|(json3.length()==0)) {
                error_texto+=   "NO HAY CODIGOS DE OBSERVACIONES !\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }

            try {
                jsonObj3 = json3.getJSONObject(0);
            } catch (JSONException e) {
                error_texto+=   e.toString()+"ERROR AL TRANSFERIR CODIGOS DE LLAVES!\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }



            m=json3.length();
            dialog.setMax(Integer.valueOf(m));
            if (json3 != null && m > 0) {

                for (int i= 0; i < m; i++) {
                    try {
                        jsonObj3 = json3.getJSONObject(i);
                    } catch (JSONException e) {
                        error_texto+=   "ERROR EN EL OBJETO  JSON DE OBSERVACIONES !  ";
                        error=true;
                        bd.close();

                    }


                    try{
                        bd=admin.getWritableDatabase();
                        if (Tobs.insertarObs(bd, jsonObj3.get("codigo_obs").toString(),
                                jsonObj3.get("descripcion_obs").toString(),
                                jsonObj3.get("obs_adic").toString(),
                                jsonObj3.get("para_lect").toString(),
                                jsonObj3.get("para_obs").toString(),
                                jsonObj3.get("tipo_med").toString(),
                                jsonObj3.get("repaso").toString(),
                                jsonObj3.get("accion").toString())==false){
                            error=true;
                            error_texto+="ERROR AL INSERTAR REGISTRO EN LA TABLA DE OBSERVACIONES!\n ";
                            bd.close();
                        }



                    } catch (JSONException e) {
                        error_texto+=  e.toString()+ "ERROR EN EL OBJETO JSON DE OBSERVACIONES !  ";
                        error=true;
                        bd.close();

                    }
                    publishProgress(i/Float.valueOf(suma));

                }

            }else{
                error=true;
                error_texto+="ERROR AL ACTUALIZAR LA TABLA DE OBSERVACIONES!\n ";
                bd.close();
            }


/////////////////////////////////////////////////////FIN JSON OBSERVACIONES////////////


//////////////////////////////////////////////////JSON RIESGO///////////////////////////
            //.....
            if ((!(json4.toString().indexOf("cero")==-1))|(json4.length()==0)) {
                error_texto+=   "NO HAY CODIGOS DE RIESGOS DE ACCESO !\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }

            try {
                jsonObj4 = json4.getJSONObject(0);
            } catch (JSONException e) {
                error_texto+=   e.toString()+"ERROR AL TRANSFERIR CODIGOS DE RIESGOS!\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }



            n=json4.length();
            dialog.setMax(Integer.valueOf(n));
            if (json4 != null && n > 0) {

                for (int i= 0; i < n; i++) {
                    try {
                        jsonObj4 = json4.getJSONObject(i);
                    } catch (JSONException e) {
                        error_texto+=   "ERROR EN EL OBJETO  JSON DE RIESGOS DE ACCESO!  ";
                        error=true;
                        bd.close();

                    }


                    try{
                        bd=admin.getWritableDatabase();
                        if (Triesgo.insertarRiesgo(bd, jsonObj4.get("codigo_riesgo_acceso").toString(), jsonObj4.get("descripcion_riesgo").toString())==false){
                            error=true;
                            error_texto+="ERROR AL INSERTAR REGISTRO EN LA TABLA DE RIESGOS DE ACCESO!\n ";
                            bd.close();
                        }



                    } catch (JSONException e) {
                        error_texto+=  e.toString()+ "ERROR EN EL ALMACENAMIENTO DE RIESGOS !  ";
                        error=true;
                        bd.close();

                    }
                    publishProgress(i/Float.valueOf(suma));

                }

            }else{
                error=true;
                error_texto+="ERROR AL ACTUALIZAR LA TABLA DE RIESGOS DE ACCESO!\n ";
                bd.close();
            }



/////////////////////////////////////////////////////FIN JSON RIESGOS////////////

//////////////////////////////////////////////////JSON UBICACIONES///////////////
            //.....
            if ((!(json5.toString().indexOf("cero")==-1))|(json5.length()==0)) {
                error_texto+=   "NO HAY CODIGOS DE UBICACION !\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }

            try {

                jsonObj5 = json5.getJSONObject(0);

            } catch (JSONException e) {
                error_texto+=   e.toString()+"ERROR AL TRANSFERIR CODIGOS DE UBICACION!\\n ";
                error=true;
                dialog.dismiss();
                bd.close();

            }



            o=json5.length();
            dialog.setMax(Integer.valueOf(o));
            if (json5 != null && o > 0) {

                for (int i= 0; i < o; i++) {
                    try {
                        jsonObj5 = json5.getJSONObject(i);
                    } catch (JSONException e) {
                        error_texto+=   "ERROR EN EL OBJETO  JSON DE UBICACIONES!  ";
                        error=true;
                        bd.close();

                    }


                    try{
                        bd=admin.getWritableDatabase();
                        if (Tubicacion.insertar_ubicacion(bd, jsonObj5.get("codigo_ubicacion").toString(), jsonObj5.get("descripcion_ubicacion").toString())==false){
                            error=true;
                            error_texto+="ERROR AL INSERTAR REGISTRO EN LA TABLA DE UBICACIONES!\n ";
                            bd.close();
                        }



                    } catch (JSONException e) {
                        error_texto+=  e.toString()+ "ERROR EN EL ALMACENAMIENTO DE UBICACIONES !  ";
                        error=true;
                        bd.close();

                    }
                    publishProgress(i/Float.valueOf(suma));

                }

            }else{
                error=true;
                error_texto+="ERROR AL ACTUALIZAR LA TABLA DE RIESGOS DE ACCESO!\n ";
                bd.close();
            }


/////////////////////////////////////////////////////FIN JSON UBICACIONES////////////

            return (suma);
        }


        protected void onProgressUpdate (Float... valores) {


            int p = Math.round(valores[0]);
            dialog.setProgress(p);

            if ((error)&(this.isCancelled())){
                dialog.dismiss();
                bd.close();
                Toast.makeText(getBaseContext(),
                        "SE PRODUJERON UNO O MAS ERRORES EN LA TRANSFERENCIA!!",
                        Toast.LENGTH_LONG).show() ;

            }

        }
        @ Override
        protected void onPostExecute(Integer bytes) {

if (bytes==null)
    Toast.makeText(getBaseContext(),
            "NO SE RECIBIERON DATOS DESDE EL SERVIDOR!!",
            Toast.LENGTH_LONG).show();

            if (error) {
                // tverrores.setTextColor(getcolor;
                Toast.makeText(getBaseContext(),
                        "ERROR EN LA TRANSFERENCIA DE DATOS!!",
                        Toast.LENGTH_LONG).show();
                tverrores.setText("SE ENCONTRATON LOS SIGUIENTES ERRORES: "+error_texto.toString());
                tverrores.setVisibility(View.VISIBLE);
                this.cancel(true);

                     /*
                    try {


                        Class<?> clase = Class.forName("com.example.aguas.menu");
                        Intent intent = new Intent(getBaseContext(), clase);
                        startActivity(intent);

                    } catch (ClassNotFoundException e) {
                        Toast.makeText(getBaseContext(),
                                "ERROR DE SISTEMA ", Toast.LENGTH_SHORT)
                                .show();


                        //e.printStackTrace();
                    }
                    */
            } else {
                tverrores.setVisibility(View.INVISIBLE);
                try {
                    Tusuarios.idusuario = jsonObj1.getString("nombreusuario").toString();
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(),
                            "ERROR DE SISTEMA", Toast.LENGTH_SHORT)
                            .show();
                }

                if (Tusuarios.idusuario.toString().length() > 0) {
                    Tusuarios.usuariologueado = us1.toString();
                    Tusuarios.clavelogueada = cl1.toString();
                    SQLiteDatabase bd = admin.getWritableDatabase();

                    //Actualizo la tabla USUARIOS
                    if (Tusuarios.insertarUsuario(bd, Tusuarios.idusuario, Tusuarios.usuariologueado, Tusuarios.clavelogueada) == false) {
                        Toast.makeText(getBaseContext(),
                                "ERROR DE SISTEMA AL ACTUALIZAR EL USUARIO", Toast.LENGTH_SHORT)
                                .show();
                        this.cancel(true);

                    } else {
                        Toast.makeText(getBaseContext(),
                                "LOS DATOS HAN SIDO TRANSFERIDOS CON EXITO!! ",
                                Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                    try{


                        Class<?> clase =Class.forName("com.example.aguas.menu");
                        Intent intent= new Intent(getBaseContext(),clase);
                        startActivity(intent);

                    }
                    catch(ClassNotFoundException e ) {
                        Toast.makeText(getBaseContext(),
                                "ERROR DE SISTEMA", Toast.LENGTH_SHORT)
                                .show();
                        this.cancel(true);
                    }

                } else {
                    Toast.makeText(getBaseContext(),
                            "ID DE USUARIO INCORRECTO. ", Toast.LENGTH_SHORT)
                            .show();
                         this.cancel(true);
                }
            }
        }



        public void salirtransf(View view){
            try{


                Class<?> clase =Class.forName("com.example.aguas.menu");
                Intent intent= new Intent(getBaseContext(),clase);
                startActivity(intent);

            }
            catch(ClassNotFoundException e ) {
                Toast.makeText(getBaseContext(),
                        "ERROR DE SISTEMA , CIERRE LA VENTANA E INTENTELO DE NUEVO", Toast.LENGTH_SHORT)
                        .show();


            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(),
                    "EL USUARIO NO EXISTE ,NO SE RECIBIERON DATOS DESDE EL SERVIDOR ", Toast.LENGTH_SHORT)
                    .show();
        }
    }

}
