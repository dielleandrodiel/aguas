package com.example.aguas;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import basededatos.Tdetalles;
import basededatos.Tsistema;
import basededatos.Tusuarios;
import basededatos.baseandroid;

public class Envio extends Activity  {

	public int error;
    public String error_de_texto="";
	public String url;
	public ProgressDialog dialogo;
	public JSONObject  datos ;
	public static TextView tv2;
	public CheckBox chek1;
	public baseandroid admin;
	public StringBuilder sb1= new StringBuilder(); 
	public Button  botonvolver;
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

    public void enviarFuera(){

		Intent i = new Intent(this,Enviar_fuera_ruta.class);
		i.putExtra("uno",Boolean.valueOf(chek1.isChecked()));
		startActivityForResult(i, 1);

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
		setContentView(R.layout.envio);
		admin=new baseandroid(this, "administracion", null, 1);	

		botonvolver=(Button)findViewById(R.id.venviar);
		tv2=(TextView)findViewById(R.id.tvenviar);
		chek1=(CheckBox)findViewById(R.id.pede);
		//chek2=(CheckBox)findViewById(R.id.nopedenv);
		String Serv= Tsistema.servidor_de_datos(admin);
        url=Serv.toString()+"/update.php";

		dialogo = new ProgressDialog(this);
		dialogo.setTitle("TRANSFIRIENDO DATOS AL SERVIDOR...");
		dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialogo.setCancelable(false);

		datos = PedidoGlobal.codificaMediciones(admin);
		//Toast.makeText(getBaseContext(),
			//	datos.toString(),
			//	Toast.LENGTH_LONG).show();
		botonvolver.setEnabled(true);
		admin.close();

		if ((hayconexion())&(datos!=null)){
		   tv2.setVisibility(View.VISIBLE);
			botonvolver.setEnabled(false);
			new Tarea().execute(url.toString());


		}else {
            if ((datos.toString().length()==0)){
                tv2.setText("NO HAY MEDICIONES PARA ENVIAR!!");
                Toast.makeText(getBaseContext(),
                        "NO HAY MEDICIONES CARGADAS ",
                        Toast.LENGTH_LONG).show();
            }else{


            tv2.setText("ERROR EN LA COMUNICACION DEL CLIENTE CON EL SERVIDOR!!");
			Toast.makeText(getBaseContext(),
					"NO HAY CONEXION DE RED ACTIVA EN SU DISPOSITIVO ",
					Toast.LENGTH_LONG).show();
                //Tdetalles.cancelar_envio(admin);
                botonvolver.setEnabled(true);
            }
			admin.close();
		}
		botonvolver.setEnabled(true);
	}




	@Override
	protected void onStart() {
		super.onStart();
	}


	@Override
	protected void onDestroy() {
		admin.close();
		super.onDestroy();
		botonvolver.setEnabled(false);
	}

	public void vuelve(View view){
		tv2.setVisibility(View.INVISIBLE);
		chek1.setChecked(false);
		//chek2.setChecked(false);
		admin.close();
		botonvolver.setEnabled(false);

        Intent intent = new Intent(this,menu.class);
        startActivity(intent);
	}

	private class Tarea extends AsyncTask<String, Float, Integer>{

		protected void onPreExecute() {

			botonvolver.setEnabled(false);
			chek1.setChecked(false);
			//chek2.setChecked(false);
		    tv2.setVisibility(View.INVISIBLE);


			tv2.setText("ENVIANDO DATOS AL SERVIDOR...");

			dialogo.setProgress(0);
			dialogo.setMax(Integer.valueOf(datos.length()));

			if (Integer.valueOf(datos.length())!=0)
				dialogo.show(); //Mostramos el dialogo antes de comenzar

		}

		@ Override
		protected Integer doInBackground(String... urls) {
			error=0;
			//proceso	

			if (hayconexion()){		


				if (datos.length()>0)  
				{  

					try {

						HttpParams httpParameters = new BasicHttpParams();
						HttpConnectionParams.setConnectionTimeout(httpParameters, 20000);
						HttpConnectionParams.setSoTimeout(httpParameters, 42000);

						HttpClient httpclient = new DefaultHttpClient(httpParameters); 

						String Serv=Tsistema.servidor_de_datos(admin);
						HttpPost httppost = new HttpPost(Serv.toString()+"/update.php");
						

						//try {  
						List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
						nameValuePairs.add(new BasicNameValuePair("Contrasena", Tusuarios.clavelogueada));
						nameValuePairs.add(new BasicNameValuePair("Datos",datos.toString()));  
						httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));   

						HttpResponse response =httpclient.execute(httppost);  


						HttpEntity entity = response.getEntity();
						BufferedReader buf = new BufferedReader(new InputStreamReader(entity.getContent()));

						String line = null;
						while ((line = buf.readLine()) != null) {
							sb1.append(line+"\r\n");

						}
					/*	Toast.makeText(getBaseContext(),
								sb1.toString(),
							    Toast.LENGTH_SHORT).show();
						*/
						for (int j=0;j<datos.length();j++){publishProgress(Float.valueOf(j)); }

					} catch (ClientProtocolException e) {  
						error=1;//ClientProtocolException
					} catch (IOException e) {  
						error=2;//IOException
                        error_de_texto=e.toString();
					}
					catch(Exception e){
						error=3;//Exception
						error_de_texto=e.toString();
					}



				}else{
					error=4;//no hay pedidos para enviar
				}


			}else{
				error=5;//no hay conexion a la red
			}

			return datos.length();

		}

		protected void onProgressUpdate (Float... valores) {
			int p = Math.round(valores[0]);
			dialogo.setProgress(p);
		}

		@ Override
		protected void onPostExecute(Integer bytes) { 

			//gestion de errores en la transferencia de mediciones.

			if (error==0){
				//0//todo ok!!
                Tdetalles.confirmar_envio(admin);
				chek1.setChecked(true);	
				tv2.setText("LOS DATOS HAN SIDO TRANSFERIDOS CON EXITO");
				tv2.setVisibility(View.VISIBLE);
                admin.close();
				dialogo.dismiss();
				botonvolver.setEnabled(true);

                            Toast.makeText(getBaseContext(),
                            "LAS MEDICIONES HAN  SIDO TRANSFERIDAS CON EXITO",
							Toast.LENGTH_LONG).show();


			
			}
			else if (error==1){//1//ERROR ClientProtocolException

				Toast.makeText(getBaseContext(),
						"ERROR AL CONECTAR CON EL SERVIDOR... ",
						Toast.LENGTH_SHORT).show();
				tv2.setText("LOS DATOS NO SE HAN PODIDO TRANSFERIR AL SERVIDOR");
                tv2.setVisibility(View.VISIBLE);
				chek1.setChecked(false);
				// chek2.setChecked(false);

                //Tdetalles.cancelar_envio(admin);
				dialogo.dismiss();
				botonvolver.setEnabled(true);
				admin.close();
				
			}
			else if (error==2){
				//error 2;// ERROR IOException
				Toast.makeText(getBaseContext(),
						"ERROR DE CONEXION CON EL SERVIDOR , INTENTELO DE NUEVO... ",
						Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(),
                        error_de_texto.toString(), Toast.LENGTH_SHORT)
                        .show();
				tv2.setText("ERROR DE TRANSFERENCIA DE DATOS ");
                tv2.setVisibility(View.VISIBLE);
				chek1.setChecked(false);
				// chek2.setChecked(false);

                //Tdetalles.cancelar_envio(admin);
				dialogo.dismiss();
				botonvolver.setEnabled(true);
				admin.close();
			}


			else if (error==3){
				//	error 3 //ERROR GENERAL Exception

				Toast.makeText(getBaseContext(),
                        "ERROR DE COMUNICACION CLIENTE/SERVIDOR!! ", Toast.LENGTH_SHORT)
                        .show();

                Toast.makeText(getBaseContext(),
                       error_de_texto.toString(), Toast.LENGTH_SHORT)
                        .show();
				tv2.setText("SE PRODUJO UN ERROR EN LA TRANSFERENCIA DE DATOS");
                tv2.setVisibility(View.VISIBLE);
				chek1.setChecked(false);
				// chek2.setChecked(false);
                //Tdetalles.cancelar_envio(admin);
                dialogo.dismiss();
				botonvolver.setEnabled(true);
				admin.close();
			}
			else if (error==4){
				//error 4 no Mediciones;

				Toast.makeText(getBaseContext(),
						"NO HAY MEDICIONES PARA ENVIAR", Toast.LENGTH_LONG)
						.show();
				tv2.setText("NO HAY MEDICIONES CARGADAS PARA ENVIAR ");
                tv2.setVisibility(View.VISIBLE);
				chek1.setChecked(false);
              //  Tdetalles.cancelar_envio(admin);
				dialogo.dismiss();		
				botonvolver.setEnabled(true);
				admin.close();

			}
			else if (error==5){
				//error 5 ERRROR no hay conexion a la red
				Toast.makeText(getBaseContext(),
						"NO ESTA CONECTADO A NINGUNA RED", Toast.LENGTH_LONG)
						.show();
				tv2.setText("NO HAY CONEXION EN SU DISPOSITIVO");
                tv2.setVisibility(View.VISIBLE);
				chek1.setChecked(false);
				//chek2.setChecked(false);
               // Tdetalles.cancelar_envio(admin);
				dialogo.dismiss();	
				botonvolver.setEnabled(true);
				admin.close();
			}

		/*
		Toast.makeText(getBaseContext(),
			sb1.toString(), Toast.LENGTH_LONG)
				.show();
        */
            //////////****************////////////////////////////////////////////////

            enviarFuera();
			//////////****************////////////////////////////////////////////////				
/*JSONObject fuera=PedidoGlobal.codificaFuera_ruta(admin);
            Toast.makeText(getBaseContext(),
                    fuera.toString(), Toast.LENGTH_LONG)
                    .show();
*/
		}



	}
}


