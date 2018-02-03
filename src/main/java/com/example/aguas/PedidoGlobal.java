package com.example.aguas;
//En la opcion de menu Enviar Datos deberia usar todos los datos de esta clase y enviarlos con httpclient//


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import basededatos.Tcabecera;
import basededatos.Tdetalles;
import basededatos.Tfuera_de_ruta;

//clase PedidoGlobal
///
public class PedidoGlobal extends Activity {
    //public   baseandroid admin=new baseandroid(this, "administracion", null, 1);
    //SQLiteDatabase bd=admin.getWritableDatabase();
    //public PedidoGlobal yo=this;

    public static Context actividad;
    ///
    //
    //atributos


    public static
    // posibles atributos para la cabecera del pedido

            int idcabecera,cant_lineas,datos_recibidos=0;

    public static

    String fecha,fechainicio,fechacierre,ip,tynmedidor;



    public static

    ///Otras atributos de esta clase
            JSONArray  Lineastodas = new JSONArray();//array de objetos Json.

    private ProgressDialog progreso;
    ////


    public static void setActividad(Context actividad) {
        PedidoGlobal.actividad = actividad;
    }



    public static JSONObject codificaMediciones(SQLiteOpenHelper bd){// devuelve un json convertido en string para mandar al servidor php.

        Cursor cus = Tcabecera.cabecera_medicion(bd);// cabecera de las mediciones.
        int cant_mediciones= Tdetalles.cant_med_para_enviar(bd);//cantidad de mediciones que voy a enviar

        JSONObject cabecera = new JSONObject();//objeto json para guardar la cabecera.
        JSONObject lineas;//array json para guardar las lineas de la cabecera.

        try {

            if ((cus.moveToFirst())&(cant_mediciones!=0)) {//guarda cabecers y el metodo  traer lineas , las lineas de esa cabecera.


                        String idcabecera =cus.getString(1);// cus.getString(1);////id de la cabecera que es igual en las lineas
                        cabecera.put("NrodeMediciones",String.valueOf(cant_mediciones));
                        cabecera.put("fec_rec",cus.getString(6));
                        cabecera.put("id_usuario",cus.getString(7));

                      lineas= new JSONObject();

                        JSONObject jsono = traerLineas(bd,idcabecera);//traer las lineas de esa cabecera
                        cabecera.put("Detalle",jsono);//meto las lineas en el jsonobj referenciadas por su id pedido

                    }




        } catch (JSONException e) {
                e.toString();

        }


     catch (Exception e) {
         e.toString();

     }


        if (cus != null && !cus.isClosed()) {
            cus.close();
        }
        return cabecera;
    }



    //traerLineas me trae las lineas para una cabecera en particular

    private static JSONObject traerLineas(SQLiteOpenHelper bdatos, String idp) {
        //	baseandroid bdatos=new baseandroid(actividad, "administracion", null, 1);

        PedidoGlobal.ip=idp.toString();

        Cursor c=Tdetalles.cargar_mediciones_no_env(bdatos);//todas las lineas no enviadas de un detalle

        //Cursor cus = base.getWritableDatabase().fetchRow(a);
        //Cursor cus=null;

        JSONObject detalle = new JSONObject();
        int cont=1;
        cant_lineas=0;
        try {

            if (c.moveToFirst()) {
                do {
                    if ( c.getString(21).toString().equals("n")){

                        detalle.put("id_cabecera"+String.valueOf(cont),String.valueOf(c.getInt(1)));
                        detalle.put("secuencia"+String.valueOf(cont),(c.getString(2)));
                        detalle.put("est_med"+String.valueOf(cont),c.getString(5));
                        detalle.put("codigo_obs"+String.valueOf(cont),c.getString(6));
                        detalle.put("observacion"+String.valueOf(cont),c.getString(7));
                        detalle.put("can_dig"+String.valueOf(cont),String.valueOf(c.getInt(10)));
                        detalle.put("fec_med"+String.valueOf(cont),c.getString(11));
                        detalle.put("codigo_ubic"+String.valueOf(cont),c.getString(14));
                        detalle.put("codigo_llave"+String.valueOf(cont),c.getString(15));
                        detalle.put("codigo_riesgo_acceso"+String.valueOf(cont),c.getString(16));
                        detalle.put("fecha_medicion_real"+String.valueOf(cont),c.getString(18));
                        detalle.put("hora_medicion_real"+String.valueOf(cont),c.getString(19));
                        detalle.put("obs_adic"+String.valueOf(cont),c.getString(24));
                        cont++;
                        cant_lineas++;
                      //  Tdetalles.marca_enviada(bdatos,idp,c.getString(2).toString());//marco como linea enviada
                    }
                } while (c.moveToNext());

            }

        } catch (Exception e) {
           // Tcabecera.marca_cab_NO_Enviada(bdatos.getWritableDatabase(),idcabecera);
          // Tdetalles.marca_NO_enviada(bdatos,ip,c.getString(2).toString());

        }
        c.close();
        bdatos.close();

        return detalle;// devuelvo un objeto con todas las lineas de ese id de cabecera

    }


    public static   JSONObject codificaFuera_ruta( SQLiteOpenHelper bd){
       int _id=0;
        Cursor cursFR = Tfuera_de_ruta.registros_a_enviar(bd);//Cursor para recorrer los reg de f de ruta no enviados.
        int cant_de_registros=cursFR.getCount();

        JSONObject cabeceraF = new JSONObject();//objeto contenedor
        JSONObject lineasF;//array para guardar las lineas  .

        try {
            int c1=0;
            if (cursFR.moveToFirst()) {
                do {
                    c1++;



                    _id =cursFR.getInt(0);

                    lineasF= new JSONObject();
                    lineasF.put("cant_registros",String.valueOf(cant_de_registros));
                    lineasF.put("id_cabecera",cursFR.getString(1));
                    lineasF.put("tipo_med",cursFR.getString(2));
                    lineasF.put("num_med",cursFR.getString(3));
                    lineasF.put("est_med",cursFR.getString(4));
                    lineasF.put("codigo_obs",cursFR.getString(5));
                    lineasF.put("fec_med",cursFR.getString(6));
                    lineasF.put("id_usuario",cursFR.getString(9));
                    lineasF.put("calle", cursFR.getString(10));
                    lineasF.put("altura", cursFR.getString(11));
                    cabeceraF.put("registro"+String.valueOf(c1), lineasF);

                //    Tfuera_de_ruta.marca_enviada(bd,_id);


                }while (cursFR.moveToNext());

            }





        } catch (Exception e) {
        // Tfuera_de_ruta.marca_NO_enviada(bd,_id);


        }
        if (cursFR != null && !cursFR.isClosed()) {
            cursFR.close();
        }

        return cabeceraF;


    }









    public static String getFechaActualCel()

    {
        final Calendar c = Calendar.getInstance();
        int anio = c.get(Calendar.YEAR);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);
        return (new StringBuilder().append(dia)
                .append("-").append(mes).append("-").append(anio)
                .append(" ")).toString();
    }

    public static String getHoraActualCel(){
        final Calendar c = Calendar.getInstance();
        int hora = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int seg = c.get(Calendar.SECOND);
        return (new StringBuilder().append(hora)
                .append(":").append(min).append(":").append(seg).toString());
        //.append("seg")).toString();

    }
}
