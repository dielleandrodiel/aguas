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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import basededatos.Tcabecera;
import basededatos.Tdetalles;
import basededatos.Tfuera_de_ruta;
import basededatos.Tsistema;
import basededatos.Tusuarios;
import basededatos.baseandroid;
import utilidades.Utilidades;

public class recibir_mediciones  extends Activity{
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public baseandroid admin=new baseandroid(this, "administracion", null, 1);
    int k=0;
    public ProgressDialog dialog;
    public SQLiteDatabase bd;
    public static JSONArray json;
    public static JSONObject jsonObj;
    public static String url,error_texto;
    public boolean error,datos_transferidos=false;
    public recibir_mediciones EstaActividad;
    public static TextView tverrores,tb;
    public Button boton,vuelve;
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
        setContentView(R.layout.recibe);

        tverrores=(TextView)findViewById(R.id.textView);
        tb= (TextView) findViewById(R.id.texto);
        vuelve=(Button)findViewById(R.id.buttontransfok);
        vuelve.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                EstaActividad.finish();
            }

        });


        EstaActividad=this;
        error=false;

        bd=admin.getWritableDatabase();
        bd.execSQL("DROP TABLE IF EXISTS " + Tfuera_de_ruta.NombreTabla);
        Tfuera_de_ruta.onCreate(bd);

        bd.execSQL("DROP TABLE IF EXISTS " + Tdetalles.NombreTabla);
        Tdetalles.onCreate(bd);

        bd.execSQL("DROP TABLE IF EXISTS " + Tcabecera.NombreTabla);
        Tcabecera.onCreate(bd);

        Tsistema.onCreate(bd);

        json = new JSONArray();
        jsonObj = null;

        String Serv= Tsistema.servidor_de_datos(admin);
        url=Serv.toString()+"/detalles.php";




                if (hayconexion()){

                        error=false;
                        error_texto="";
                        jsonObj = null;

                        datos_transferidos=false;
                        dialog = new ProgressDialog(EstaActividad);
                        dialog.setTitle("AGUAS ANDROID...");
                        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        dialog.setMessage(Html.fromHtml("<font color='blue'>" + "DESCARGANDO DATOS, ESPERE POR FAVOR..." + "</font>"));
                        dialog.setCancelable(false);

                        new MiTarea().execute(url.toString());





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




    protected void onStart() {
        super.onStart();


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

                    json= Utilidades.TraerDatos(url,Tusuarios.idusuario, Tusuarios.usuariologueado, Tusuarios.clavelogueada,EstaActividad.getApplicationContext());



                    if ((json ==null)|((json.toString().contains("cero")))){
                        error_texto+= "NO HAY DATOS EN EL SERVIDOR !!\\n";
                        error=true;
                        dialog.dismiss();
                        bd.close();
                        this.cancel(true);
                        return 0;
                    }else{
                        datos_transferidos=true;
                        dialog.setProgress(0);
                        dialog.setMax(Integer.valueOf(json.length()));
                        // dialog.show(); //Mostramos el dialogo antes de comenzar

                    }

                } catch (ConnectTimeoutException e) {
                    error_texto+=   "EL SERVIDOR NO RESPONDE !\n ";
                    error=true;
                    dialog.dismiss();
                    bd.close();

                    // EstaActividad.finish();

                        error_texto+=   "ERROR EN LA TRANSFERENCIA DE DATOS !\n  ";
                       // error=true;
                       // dialog.dismiss();
                      //  bd.close();
                         this.cancel(true);

                    e.printStackTrace();

                }catch(Exception e){
                    error_texto+=  e.toString()+ " ERROR GENERAL DEL SISTEMA!\n ";
                    dialog.dismiss();
                    bd.close();
                    error=true;
                    this.cancel(true);


            }

            }
            if ((!(json.toString().indexOf("cero")==-1))|(json.length()==0)) {
                error_texto+=   "NO SE RECIBIERON DATOS DESDE EL SERVIDOR !\\n ";
                error=true;
                dialog.dismiss();
                bd.close();
                this.cancel(true);
                return 0;
            }




            try {
            jsonObj = json.getJSONObject(0);
            } catch (JSONException e) {
                error_texto+=   e.toString()+"NO HAY DATOS PARA TRANSFERIR!\\n ";
                error=true;
                dialog.dismiss();
                bd.close();
                this.cancel(true);
                return 0;
            }




            k=json.length();
            if (json != null && k > 0) {

                for (int i= 0; i < k; i++) {
                    try {
                        jsonObj = json.getJSONObject(i);
                    } catch (JSONException e) {
                        error_texto+= "ERROR EN EL OBJETO  JSON DE MEDICIONES !\n  ";
                        error=true;
                        bd.close();
                        this.cancel(true);
                    }


                    try {
                        bd=admin.getWritableDatabase();//abre la base
                        if (Tdetalles.insertardetalle(bd,
                                jsonObj.getInt("id_cabecera"),
                                jsonObj.get("secuencia").toString(),
                                jsonObj.get("tipo_med").toString(),
                                jsonObj.get("num_med").toString(),
                                jsonObj.getDouble("est_med"),
                                jsonObj.get("codigo_obs").toString(),
                                jsonObj.get("fec_med_ant").toString(),
                                jsonObj.getDouble("est_med_ant"),
                                jsonObj.getInt("can_dig"),
                                jsonObj.get("calle").toString(),
                                jsonObj.getInt("altura"),
                                jsonObj.get("codigo_ubicacion").toString(),
                                jsonObj.get("codigo_llave").toString(),
                                jsonObj.get("codigo_riesgo_acceso").toString(),
                                jsonObj.get("ubicacion").toString())==false){
                            error=true;
                            error_texto+=   "ERROR AL INSERTAR  EN LA BASE LOCAL!!\n ";
                            dialog.dismiss();
                            bd.close();
                            this.cancel(true);
                           }// dialog.dismiss();



                    } catch (JSONException e) {
                        error_texto+=   "ERROR DE JSON DE DETALLE!!\n";
                        dialog.dismiss();
                        bd.close();
                        error=true;

                    }

                    publishProgress(Float.valueOf(i));

                }
                try {
                    bd=admin.getWritableDatabase();//abre la base
                    if (Tcabecera.insertarcabecera(bd,
                            jsonObj.getInt("id_cabecera"),
                            jsonObj.get("mod_cod_ubic").toString(),
                            jsonObj.get("mod_tipo_llave").toString(),
                            jsonObj.get("mod_cod_riesgo").toString(),
                            jsonObj.get("mod_can_dig").toString(),
                            jsonObj.get("id_usuario").toString())==false){
                        error_texto+=   "ERROR AL INSERTAR MEDICIONES EN LA BASE LOCAL!!\n ";
                        error=true;
                        dialog.dismiss();
                        bd.close();
                        this.cancel(true);
                    }// dialog.dismiss();



                } catch (JSONException e) {
                    error_texto+=   "ERROR DE JSON DE CABECERA!!\n";
                    dialog.dismiss();
                    bd.close();
                    error=true;
                    this.cancel(true);
                }
            }else{
                error=true;
                error_texto+="ERROR AL ACTUALIZAR LAS MEDICIONES!\n ";
                bd.close();
                this.cancel(true);
                return 0;
            }


            return Integer.valueOf(k);
        }


        protected void onProgressUpdate (Float... valores) {


            int p = Math.round(valores[0]);
            dialog.setProgress(p);
            if (error){
              //  dialog.dismiss();
                bd.close();
                Toast.makeText(getBaseContext(),
                        "SE PRODUJERON UNO O MAS ERRORES EN LA TRANSFERENCIA!!",
                        Toast.LENGTH_LONG).show() ;
                               //  this.cancel(true);
            }

        }
        @ Override
        protected void onPostExecute(Integer bytes) {

            if (error) {
                // tverrores.setTextColor(getcolor;
                Toast.makeText(getBaseContext(),
                        "ERROR EN LA TRANSFERENCIA DE DATOS!!",
                        Toast.LENGTH_LONG).show();
                tverrores.setText("SE ENCONTRATON LOS SIGUIENTES ERRORES: "+error_texto.toString());
                tverrores.setVisibility(View.VISIBLE);
                PedidoGlobal.datos_recibidos=0;
                vuelve.setVisibility(View.VISIBLE);
                Tsistema.setear_permite_descarga(admin.getWritableDatabase(),0);

            } else {
                tb.setText("LOS DATOS SE DESCARGARON CON EXITO.");
                tverrores.setVisibility(View.INVISIBLE);
                vuelve.setVisibility(View.VISIBLE);
                PedidoGlobal.datos_recibidos=1;
                Tsistema.setear_permite_descarga(admin.getWritableDatabase(),1);
                Tcabecera.Setear_fecha_recepcion(admin.getWritableDatabase(),Tcabecera.ultimoId(admin.getWritableDatabase()),PedidoGlobal.getFechaActualCel().toString());

                        dialog.dismiss();

                    }


        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(getBaseContext(),
                    "NO HAY DATOS EN EL SERVIDOR PARA ESTE USUARIO,O LA IP DE CONEXION ES INCORECTA!!",
                    Toast.LENGTH_LONG).show() ;
            vuelve.setVisibility(View.VISIBLE);
            if (error) {
                // tverrores.setTextColor(getcolor;
                Toast.makeText(getBaseContext(),
                        "SE ENCONTRATON LOS SIGUIENTES ERRORES: "+error_texto.toString(),
                        Toast.LENGTH_LONG).show();
                tverrores.setText("SE ENCONTRATON LOS SIGUIENTES ERRORES: "+error_texto.toString());
                tverrores.setVisibility(View.VISIBLE);
                PedidoGlobal.datos_recibidos=0;
                Tsistema.setear_permite_descarga(admin.getWritableDatabase(),0);

            }
        }
    }

}
