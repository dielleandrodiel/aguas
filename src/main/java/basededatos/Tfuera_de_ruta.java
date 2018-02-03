package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tfuera_de_ruta {
  
    public static final String NombreTabla = "afuera_ruta";

    public static class FueraColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String id_cabecera = "id_cabecera";

        public static final String tipo_med = "tipo_med";//tipo

        public static final String num_med = "num_med";// numero de medidor

        public static final String est_med = "est_med";//lectura

        public static final String codigo_obs = "codigo_obs";//cod_obs

       // public static final String observacion = "observacion";

       // public static final String can_dig = "can_dig";

        public static final String fec_med = "fec_med";

        public static final String enviado = "enviado";

        public static final String cargado = "cargado";

        public static final String id_usuario = "id_usuario";


        public static final String calle = "calle";//calle

        public static final String altura = "altura";//altura

         /*
        public static final String codigo_ubicacion = "codigo_ubicacion";

        public static final String codigo_llave = "codigo_llave";

        public static final String codigo_riesgo_acceso = "codigo_riesgo_acceso";

        public static final String geo_pos_med= "geo_pos_med";

        public static final String fec_med_real = "fec_med_real";

        public static final String hora_med_real = "hora_med_real";

        public static final String cod_clave = "cod_clave";

        public static final String ubicacion = "ubicacion";

        public static final String obs_adic = "obs_adic";
        
        */

    }



    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS "  + Tfuera_de_ruta.NombreTabla  + " (");

        sb.append(FueraColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT , ");

        sb.append(FueraColumnas.id_cabecera+ " INTEGER NOT NULL, ");

        sb.append(FueraColumnas.tipo_med + " TEXT  NULL, ");

        sb.append(FueraColumnas.num_med+ " TEXT  NULL, ");

        sb.append(FueraColumnas .est_med + " REAL   NULL, ");

        sb.append(FueraColumnas.codigo_obs + "  TEXT NULL DEFAULT 0, ");

      //  sb.append(FueraColumnas .observacion + "  TEXT   NULL, ");

      // sb.append(FueraColumnas .can_dig + " INTEGER   NULL, ");

        sb.append(FueraColumnas.fec_med + " TEXT NULL, ");

        sb.append(FueraColumnas.enviado + " TEXT  NULL DEFAULT n, ");

        sb.append(FueraColumnas.cargado + " TEXT  NULL DEFAULT n, ");

        sb.append(FueraColumnas.id_usuario + " TEXT  NOT NULL,  ");


        sb.append(FueraColumnas .calle + " TEXT  NULL, ");

        sb.append(FueraColumnas .altura + " INTEGER    NULL ");

        /*
        sb.append(FueraColumnas .codigo_ubicacion+ " TEXT NULL  DEFAULT 0, ");

        sb.append(FueraColumnas .codigo_llave + " TEXT  NULL DEFAULT 0, ");

        sb.append(FueraColumnas .codigo_riesgo_acceso + " TEXT NULL  DEFAULT 0, ");

        sb.append(FueraColumnas .geo_pos_med + " TEXT  NULL, ");

        sb.append(FueraColumnas .fec_med_real+ " TEXT  NULL, ");

        sb.append(FueraColumnas .hora_med_real+ " TEXT  NULL, ");

        sb.append(FueraColumnas .cod_clave + " TEXT  NULL, ");

        sb.append(FueraColumnas .ubicacion + " TEXT  NULL, ");

        sb.append(FueraColumnas .obs_adic + "  TEXT   NULL ");

        */

        sb.append(");");

        db.execSQL(sb.toString());

    }




    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){



    }




    public static final boolean insertar_fuera_ruta(SQLiteDatabase base,

                                                int  id_cabecera,

                                               // int secuencia,

                                                String tipo_med,

                                                String num_med,

                                                double est_med ,

                                                String codigo_obs ,

                                              //  String fec_med_ant,

                                               // double est_med_ant,

                                               // int can_dig,

                                                String calle ,

                                                 int altura ,

                                              //  String codigo_ubicacion,

                                             //   String codigo_llave,

                                             //   String codigo_riesgo_acceso,

                                             //   String ubicacion

                                              String id_usuario,

                                              String enviado,

                                              String cargado,

                                              String fec_med



    ){

        //Alta de un fuera de ruta
        ContentValues registro=new ContentValues();
        registro.put( "id_cabecera",id_cabecera);
       // registro.put( "secuencia",secuencia);
        registro.put( "tipo_med",tipo_med.toString());
        registro.put( "num_med",num_med.toString());
        registro.put( "est_med" ,est_med);
        registro.put( "codigo_obs" ,codigo_obs.toString());
       // registro.put( "fec_med_ant",fec_med_ant.toString());
       // registro.put( "est_med_ant",est_med_ant);
       // registro.put( "can_dig",can_dig);
        registro.put( "calle" ,calle.toString());
        registro.put( "altura" ,altura);
       // registro.put( "codigo_ubicacion",codigo_ubicacion.toString());
       // registro.put( "codigo_llave",codigo_llave.toString());
       // registro.put( "codigo_riesgo_acceso" ,codigo_riesgo_acceso.toString());
       // registro.put( "ubicacion" ,ubicacion.toString());
        registro.put( "id_usuario" ,id_usuario.toString());
        registro.put( "enviado" ,enviado.toString());
        registro.put( "cargado" ,cargado.toString());
        registro.put( "fec_med" ,fec_med.toString());


        long result = base.replace ("afuera_ruta", null, registro);

        base.close();
        if  (result!=-1) return true;
        else return false;

    }





    public static int Cantidad_mediciones_fuera_totales(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from afuera_ruta" , null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (0);


        }
    }
    public static int Cantidad_mediciones_fuera_no_enviadas(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from afuera_ruta where afuera_ruta.enviado='n'" , null);

        if (c.moveToFirst()){

            return (c.getInt(0));

        }

        else {

            return (0);


        }
    }

    public static Cursor registros_a_enviar(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select * from afuera_ruta where afuera_ruta.enviado='n' and cargado='s'" , null);
       return(c);

    }



    public static int Cantidad_mediciones_cargadas(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from afuera_ruta where afuera_ruta.cargado='s'" , null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (0);


        }
    }
    public static void marca_enviada(SQLiteOpenHelper base ,int _id){

        String[] ids = new String[1];
        ids[0]=String.valueOf(_id).toString();
        ContentValues values = new ContentValues();
        values.put("enviado","s");
        base.getWritableDatabase().update("afuera_ruta", values, " _id= ? ", ids);
        //base.close();

    }
    public static void marca_NO_enviada(SQLiteOpenHelper base ,int _id){

        String[] ids = new String[1];
        ids[0]=String.valueOf(_id).toString();
        ContentValues values = new ContentValues();
        values.put("enviado","n");
        base.getWritableDatabase().update("afuera_ruta", values, " _id= ? ", ids);
        //base.close();

    }
    public static void cancelar_envio(SQLiteOpenHelper base){

        base.getWritableDatabase().rawQuery("UPDATE afuera_ruta SET enviado='n' where (afuera_ruta.[enviado]='n') and (afuera_ruta.[cargado]='s')",null );



    }
    public static void confirmar_envio(SQLiteOpenHelper base){

       // base.getWritableDatabase().rawQuery("UPDATE afuera_ruta SET enviado='s' where (afuera_ruta.[enviado]='n') and (afuera_ruta.[cargado]='s')",null );

        String[] ids = new String[2];
        ids[0]="n";
        ids[1]="s";
        ContentValues values = new ContentValues();

        values.put("enviado","s");


        //base.getWritableDatabase().update("adetalles", values,wheres.toString(), ids);
        base.getWritableDatabase().update("afuera_ruta", values, " enviado= ? and cargado = ? ", ids);
        //base.close()

    }
}