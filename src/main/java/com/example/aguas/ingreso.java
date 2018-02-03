package com.example.aguas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

public class ingreso extends Activity {
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
        setContentView(R.layout.menu_principal);



    }

    public void validar(View view){
        Intent intent = new Intent(this,validar.class);
        startActivity(intent);
    }

    public void actualizar(View view){

        Intent intent = new Intent(this,actualizacion.class);
        startActivity(intent);


    }
    public void server(View view){
        Intent intent = new Intent(this,Servidor_de_datos.class);
        startActivity(intent);


    }
    public void salir(View view){
              this.finish();


    }
}