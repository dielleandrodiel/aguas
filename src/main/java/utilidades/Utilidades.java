package utilidades;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.example.aguas.actualizacion;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public final class Utilidades extends Activity{

    public Utilidades() {
        // Utilidades para el Sistema notas de pedido.
    }

    //metodo para obtener el numero del celu
    public String obtenerNroUsuario(){
        TelephonyManager mTelephonyManager;
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyManager.getLine1Number();
    }

    //mostrar una barra de progereso

    //   final ProgressBar progress = (ProgressBar)findViewById(R.id.create_progress);
    //   progress.setMax(100);
    //   progress.setProgress(0);
    //    new Thread(new Runnable() {
    //   public void run() {
    //    for (int i=0;i<100;i++) {
    //        progress.setProgress(i);
    //    }
    //  }
    // }



    //metodo para traer datos del servidor

    public static JSONArray TraerDatos(String url_PHP,String p3,String p1,String p2,Context vista)throws ConnectTimeoutException,JSONException {//hacer esto en un hilo implements runnable


        if ( url_PHP.length()>0 )
        {
            // defino en timeout para la conexion por si el servidor no responde
            try{
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 2000000);
                HttpConnectionParams.setSoTimeout(httpParameters, 4200000);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);


                HttpPost httppost = new HttpPost(url_PHP.toString());
                //try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("parametro1",p1.toString()));
                nameValuePairs.add(new BasicNameValuePair("parametro2",p2.toString()));
                nameValuePairs.add(new BasicNameValuePair("parametro3",p3.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response =httpclient.execute(httppost);
                //////////////////////////Respuesta de servidor/////////////////////////////////////////////



                // Obtengo el contenido de la respuesta
                HttpEntity entity = response.getEntity();

                //Convierto el stream a un String

                BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder sbfecha = new StringBuilder();
                String line = null;

                while ((line = buf.readLine()) != null) {
                    sbfecha.append(line+"\r\n");
                }


                JSONArray jsonArray = new JSONArray(sbfecha.toString());//le asigno la resp

                return jsonArray;

            } catch (ClientProtocolException e) {
                Toast.makeText(vista,
                        " ERROR AL INTENTAR CONECTAR CON EL SERVIDOR ! ",
                        Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(vista,
                        " ERROR de ENTRADA/SALIDA ! ",
                        Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(vista,

                        e.toString()+ " ERROR Al INICIALIZAR JSON ! ",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(vista,
                        e.toString()+ " ERROR DE CONEXON CON EL SERVIDOR! ", Toast.LENGTH_SHORT)
                        .show();

            }




        }



        Toast.makeText(vista,
                "NO SE PUDO TRANSFERIR LA INFORMACION DESDE EL SERVIDOR ! ", Toast.LENGTH_SHORT)
                .show();

        StringBuilder sb = new StringBuilder("cero");
        JSONArray jsonerror = new JSONArray(sb.toString());
        return jsonerror;



    }
    public static JSONArray Transferir(String url_PHP,String p1,String p2,Context vista)throws ConnectTimeoutException,JSONException {//hacer esto en un hilo implements runnable

        if ( url_PHP.length()>0 )
        {
            // Defino en timeout para la conexion por si el servidor no responde
            try{

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
                HttpConnectionParams.setSoTimeout(httpParameters, 42000);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);


                HttpPost httppost = new HttpPost(url_PHP.toString());
                //try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("parametro1",p1.toString()));
                nameValuePairs.add(new BasicNameValuePair("parametro2",p2.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response =httpclient.execute(httppost);
                //////////////////////////Respuesta de servidor/////////////////////////////////////////////



                // Obtengo el contenido de la respuesta
                HttpEntity entity = response.getEntity();

                //Convierto el stream a un String

                BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder sbfecha = new StringBuilder();
                String line = null;

                while ((line = buf.readLine()) != null) {
                    sbfecha.append(line+"\r\n");
                }
              //  actualizacion.error_texto+=sbfecha.toString();

                        JSONArray jsonArray = new JSONArray(sbfecha.toString());//le asigno la resp

                // Toast.makeText(vista,
                //      "Respuesta: "+sbfecha.toString(),
                //     Toast.LENGTH_SHORT).show();

                return jsonArray;


            } catch (ClientProtocolException e) {

                actualizacion.error_texto+= " ERROR DE PROTOCOLO DEL CLIENTE ! \n";

            } catch (IOException e) {
                actualizacion.error_texto+= "ERROR DE ENTRADA/SALIDA! \n";
            } catch (JSONException e) {
                actualizacion.error_texto+= "ERROR AL CODIFICAR JSON ! \n";
            } catch (Exception e) {
                actualizacion.error_texto+= "ERROR GENERAL DEL SISTEMA ! \n";

            }




        }



        actualizacion.error_texto+= " ERROR DE TRANSFERENCIA DE DATOS ! \n";
        StringBuilder sb = new StringBuilder("cero");
        JSONArray jsonerror = new JSONArray(sb.toString());
        return jsonerror;






    }

    public static String TraerTexto(String url_PHP,String p1,String p2)throws ConnectTimeoutException {


        if ( url_PHP.length()>0 )
        {
            // defino en timeout para la conexion por si el servidor no responde
            try{
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
                HttpConnectionParams.setSoTimeout(httpParameters, 42000);

                HttpClient httpclient = new DefaultHttpClient(httpParameters);


                HttpPost httppost = new HttpPost(url_PHP.toString());
                //try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("parametro1",p1.toString()));
                nameValuePairs.add(new BasicNameValuePair("parametro2",p2.toString()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response =httpclient.execute(httppost);
                //////////////////////////Respuesta de servidor/////////////////////////////////////////////



                // Obtengo el contenido de la respuesta
                HttpEntity entity = response.getEntity();

                //Convierto el stream a un String

                BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));
                StringBuilder sbfecha = new StringBuilder();
                String line = null;

                while ((line = buf.readLine()) != null) {
                    sbfecha.append(line+"\r\n");
                }



                if (sbfecha.toString().indexOf("cero")==-1){
                    return sbfecha.toString();
                }	else  return null;

            }   catch (ClientProtocolException e) {
                return null;
            } catch (IOException e) {
                return null;


            }


        }else
            return null;

    }
    public static java.util.Date texto_a_fecha(String strFecha){
        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date fecha = null;
        try {

            fecha = formatoDelTexto.parse(strFecha, new ParsePosition(0));
            return (fecha);
        } catch (ParseException ex) {
            return fecha;

        }

    }
/*
public static int CompararFechas(String fecha1,String fecha2) {
	 Calendar cal1 = new GregorianCalendar();
	 cal1.set(Date.valueOf(fecha1);
	   
	  Calendar cal2 = new GregorianCalendar();
	  cal2.setTime(fecha2);
	   
	   // Comparaciones
	   
	  if ( cal1.before(cal2) ) return(0);//es anterior
	  else return(1); // Cal1 NO es anterior a cal2
	  
	
	   
	
	   
}
*/



}