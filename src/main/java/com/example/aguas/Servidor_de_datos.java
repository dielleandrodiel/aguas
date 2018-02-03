package com.example.aguas;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import basededatos.Tsistema;
import basededatos.baseandroid;

public class Servidor_de_datos extends Activity{

    baseandroid basea;

    public String server;

    public String conexion1="http://200.80.52.117/~avisentini/lg_gestion";//casa
    public String conexion2="http://192.168.1.104/Aguas";//otra casa
    public String conexion3="http://192.168.1.2/Aguas";//vidoni oficina



    public RadioGroup rg;
    public RadioButton r0,r1,r2;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "No se puede volver atras!!!", Toast.LENGTH_SHORT)
                .show();
        return;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servidor);

        //ips=(TextView) findViewById(R.id.ip);
        //chek_ip=(CheckBox)findViewById(R.id.chek_ip);
        basea=new baseandroid(this, "administracion", null, 1);
        Tsistema.onCreate(basea.getWritableDatabase());
        server=Tsistema.servidor_de_datos(basea);//x defecto al entrar(casa)

        rg=(RadioGroup)findViewById(R.id.radg1);
        r0=(RadioButton)findViewById(R.id.r0);
        r1=(RadioButton)findViewById(R.id.r1);
        r2=(RadioButton)findViewById(R.id.r2);

        r0.setOnClickListener(new RadioButton.OnClickListener(){

            public void onClick(View v) {

                server=conexion1.toString();


            }
        });
        r1.setOnClickListener(new RadioButton.OnClickListener(){

            public void onClick(View v) {

                server=conexion2.toString();

            }
        });
        r2.setOnClickListener(new RadioButton.OnClickListener(){

            public void onClick(View v) {

                server=conexion3.toString();

            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        //Tsistema.onCreate(basea.getWritableDatabase());
        String server_actual=String.valueOf(Tsistema.servidor_de_datos(basea));
        if (server_actual.equals(conexion1.toString()))
            r0.setChecked(true);
        else if (server_actual.equals(conexion2.toString()))
            r1.setChecked(true);
        else if (server_actual.equals(conexion3.toString()))
            r2.setChecked(true);
        basea.close();
    }


    public void ip_ok (View view){//Boton aceptar de servidor de datos.

        baseandroid ad=basea;

        //PedidoGlobal.SERVIDOR=server;
        Tsistema.modificarServidor(ad.getWritableDatabase(),server.toString());
        Toast.makeText(getBaseContext(),
                "Se establecio la direccion del servidor de datos ",
                Toast.LENGTH_LONG).show();
        ad.getWritableDatabase().close();
        ad.close();
        this.finish();
        //}
    }

}