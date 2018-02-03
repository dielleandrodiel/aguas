package com.example.aguas;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import basededatos.Tsistema;
import basededatos.Tusuarios;
import basededatos.baseandroid;

public class validar extends Activity {
    private int intentos=0;
    private int intentos_login;
    private EditText ustext;
    private EditText  clavetxt;
    public baseandroid admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cel_validacion);

        admin=new baseandroid(this, "administracion", null, 1);
        Tsistema.onCreate(admin.getReadableDatabase());
        intentos_login= Tsistema.reintentos(admin);

        ustext=(EditText)findViewById(R.id.usuario);
        clavetxt=(EditText)findViewById(R.id.clave);
        //clavetxt.setInputType(InputType.TYPE_CLASS_NUMBER);
        //clavetxt.setTransformationMethod(PasswordTransformationMethod.getInstance());


    }

    public void ingresar(View view){
        SQLiteDatabase bd=admin.getWritableDatabase();

        String usuario=ustext.getText().toString();
        String clave=clavetxt.getText().toString();


        if (Tusuarios.validarUsuario(bd, usuario, clave)){


            if  (Tusuarios.masdeunUsuario(bd)) {
                Toast.makeText(this, "Bienvenido: " + Tusuarios.usuariologueado.toString() + " Cargando los datos para su sesion...", Toast.LENGTH_LONG).show();


                Intent intent = new Intent(this,menu.class);
                startActivity(intent);

            }
            else{

                Intent intent = new Intent(this,menu.class);
                startActivity(intent);

                Toast.makeText(this, "Bienvenido: "+Tusuarios.usuariologueado.toString(), Toast.LENGTH_LONG).show();
            }
            //*/*/*/*//BORRAR LOS PEDIDOS QUE HAY EN LA BASE LOCAL ,CUYA FECHA DE ENTREGA YA PASO.**********
            // baseandroid bd=new baseandroid(this, "administracion", null, 1);



            ////////////////////////////////////////********///////////////////////////////////////////

        }else {
            intentos++;
            ustext.setText("");
            clavetxt.setText("");
            Toast.makeText(this, "El nombre de usuario o clave es incorrecto!! , le quedan: "+String.valueOf(intentos_login-intentos)+" intentos.", Toast.LENGTH_LONG).show();
            bd.close();
            if (intentos==intentos_login) {
                Toast.makeText(this, "Se ha superado el maximo de intentos para acceder!!", Toast.LENGTH_LONG).show();
                bd.close();
                this.finish();}
        }
        bd.close();
    }

    public void salirval(View view){

        Intent intent = new Intent(this,ingreso.class);
        startActivity(intent);


    }

}