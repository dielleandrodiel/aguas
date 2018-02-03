package com.example.aguas;

/**
 * Created by leandrodiel on 19/06/13.
 */

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import basededatos.Tdetalles;
import basededatos.Tfuera_de_ruta;
import basededatos.baseandroid;

public class menu extends Activity {
    Button boton_descarga,boton_medicion,boton_enviar;
    public baseandroid admin=new baseandroid(this, "administracion", null, 1);
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
        setContentView(R.layout.menu_opciones);
     boton_descarga=(Button)findViewById(R.id.button2);
     boton_medicion=(Button)findViewById(R.id.button);
     boton_enviar=(Button)findViewById(R.id.button3);
    }

    public void toma_lectura(View view){

        Intent intent = new Intent(this,mediciones.class);
        startActivity(intent);


    }
    public void salir_lecturas(View view){

        Intent intent = new Intent(this,ingreso.class);
        startActivity(intent);


    }
    public void descargar(View view){

        Intent intent = new Intent(this,recibir_mediciones.class);
        startActivity(intent);


    }
    public void enviar(View view){
    /*JSONObject m= PedidoGlobal.codificaMediciones(admin);
    Toast.makeText(getApplicationContext(),
    m.toString(), Toast.LENGTH_LONG)
            .show();
            */
        Intent intent = new Intent(this,Envio.class);
        startActivity(intent);

     // this.onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tdetalles.onCreate(admin.getReadableDatabase());
        Cursor hay_medicion=null;
        hay_medicion= Tdetalles.cargar_medicion(admin);//cargado ='n'
        hay_medicion.moveToFirst();

        Cursor hay_para_enviar=null;
        hay_para_enviar= Tdetalles.cargar_mediciones_no_env(admin);//cargado='s' y enviado='n'
        hay_para_enviar.moveToFirst();

        Cursor hay_fuera_para_enviar=null;
        hay_fuera_para_enviar= Tfuera_de_ruta.registros_a_enviar(admin);//cargado='s' y enviado='n'
        hay_fuera_para_enviar.moveToFirst();



        if ((hay_medicion.getCount()==0)|(hay_medicion==null)) {
            boton_medicion.setText("NO HAY MEDICIONES PARA CARGAR");
            boton_medicion.setEnabled(false);


         if (((hay_para_enviar.getCount()==0)|(hay_para_enviar==null))&((hay_fuera_para_enviar.getCount()==0)|(hay_fuera_para_enviar==null))){
            boton_descarga.setText("DESCARGAR DATOS");
            boton_descarga.setEnabled(true);
            boton_enviar.setText("NO HAY DATOS PARA ENVIAR");
            boton_enviar.setEnabled(false);

         }else{
             boton_descarga.setText("YA SE  DESCARGARON LOS DATOS");
             boton_descarga.setEnabled(false);
             boton_enviar.setText("ENVIAR DATOS");
             boton_enviar.setEnabled(true);

         }

        }
        else{
            boton_medicion.setText("TOMAR LECTURAS");
            boton_medicion.setEnabled(true);
            boton_descarga.setText("YA SE  DESCARGARON LOS DATOS");
            boton_descarga.setEnabled(false);

            if (((hay_para_enviar.getCount()==0)|(hay_para_enviar==null))&((hay_fuera_para_enviar.getCount()==0)|(hay_fuera_para_enviar==null))){

                boton_enviar.setText("NO HAY DATOS PARA ENVIAR");
                boton_enviar.setEnabled(false);


            }else{

                boton_enviar.setText("ENVIAR DATOS");
                boton_enviar.setEnabled(true);

            }

        }
        hay_medicion.close();
        hay_para_enviar.close();
        hay_fuera_para_enviar.close();
        admin.close();
    }


}