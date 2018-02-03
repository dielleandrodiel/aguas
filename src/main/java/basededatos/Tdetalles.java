package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import basededatos.Tcabecera.CabeceraColumnas;

public final class Tdetalles {
    //public  static int cant=0;
    public static final String NombreTabla = "adetalles";

    public static class DetalleColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String id_cabecera = "id_cabecera";

        public static final String secuencia = "secuencia";

        public static final String tipo_med = "tipo_med";

        public static final String num_med = "num_med";

        public static final String est_med = "est_med";

        public static final String codigo_obs = "codigo_obs";

        public static final String observacion = "observacion";

        public static final String fec_med_ant = "fec_med_ant";

        public static final String est_med_ant = "est_med_ant";

        public static final String can_dig = "can_dig";

        public static final String fec_med = "fec_med";

        public static final String calle = "calle";

        public static final String altura = "altura";

        public static final String codigo_ubicacion = "codigo_ubicacion";

        public static final String codigo_llave = "codigo_llave";

        public static final String codigo_riesgo_acceso = "codigo_riesgo_acceso";

        public static final String geo_pos_med= "geo_pos_med";

        public static final String fec_med_real = "fec_med_real";

        public static final String hora_med_real = "hora_med_real";

        public static final String cod_clave = "cod_clave";

        public static final String enviado = "enviado";

        public static final String ubicacion = "ubicacion";

        public static final String cargado = "cargado";

        public static final String obs_adic = "obs_adic";

    }



    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS "  + Tdetalles.NombreTabla  + " (");

        sb.append(DetalleColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT , ");

        sb.append(DetalleColumnas.id_cabecera+ " INTEGER NOT NULL, ");

        sb.append(DetalleColumnas.secuencia+ " TEXT NOT NULL, ");

        sb.append(DetalleColumnas.tipo_med + " TEXT  NULL, ");

        sb.append(DetalleColumnas.num_med+ " TEXT  NULL, ");

        sb.append(DetalleColumnas.est_med + " REAL   NULL, ");

        sb.append(DetalleColumnas.codigo_obs + "  TEXT DEFAULT '00', ");

        sb.append(DetalleColumnas.observacion + "  TEXT   NULL, ");

        sb.append(DetalleColumnas.fec_med_ant+ " TEXT NULL, ");

        sb.append(DetalleColumnas.est_med_ant+" REAL NULL, ");

        sb.append(DetalleColumnas.can_dig + " INTEGER   NULL, ");

        sb.append(DetalleColumnas.fec_med + " TEXT NULL, ");

        sb.append(DetalleColumnas.calle + " TEXT  NULL, ");

        sb.append(DetalleColumnas.altura + " INTEGER    NULL, ");

        sb.append(DetalleColumnas.codigo_ubicacion+ " TEXT  DEFAULT '00', ");

        sb.append(DetalleColumnas.codigo_llave + " TEXT  DEFAULT '00', ");

        sb.append(DetalleColumnas.codigo_riesgo_acceso + " TEXT DEFAULT '00', ");

        sb.append(DetalleColumnas.geo_pos_med + " TEXT  NULL, ");

        sb.append(DetalleColumnas.fec_med_real+ " TEXT  NULL, ");

        sb.append(DetalleColumnas.hora_med_real+ " TEXT  NULL, ");

        sb.append(DetalleColumnas.cod_clave + " TEXT  NULL, ");

        sb.append(DetalleColumnas.enviado + " TEXT  DEFAULT 'n', ");

        sb.append(DetalleColumnas.cargado + " TEXT DEFAULT 'n', ");

        sb.append(DetalleColumnas.ubicacion + " TEXT  NULL, ");

        sb.append(DetalleColumnas.obs_adic + "  TEXT   NULL, ");

        sb.append( "UNIQUE(secuencia)" );

        sb.append("FOREIGN KEY(" + DetalleColumnas.id_cabecera + ")" +

                " REFERENCES " + Tcabecera.NombreTabla + "(" +

                CabeceraColumnas.id_cabecera + ")");

        sb.append("FOREIGN KEY(" + DetalleColumnas.codigo_obs + ")" +

                " REFERENCES " + Tobs.NombreTabla + "(" +

                Tobs.ObsColumnas.codigo_obs+ ")");

        sb.append("FOREIGN KEY(" + DetalleColumnas.cod_clave + ")" +

                " REFERENCES " + Tllaves.NombreTabla + "(" +

                Tllaves.LLavesColumnas.codigo_llave+ ")");

        sb.append("FOREIGN KEY(" + DetalleColumnas.codigo_riesgo_acceso+ ")" +

                " REFERENCES " + Triesgo.NombreTabla + "(" +

                Triesgo.RiesgoColumnas.codigo_riesgo+ ")");

        sb.append("FOREIGN KEY(" + DetalleColumnas.codigo_ubicacion + ")" +

                " REFERENCES " + Tubicacion.NombreTabla + "(" +

                Tubicacion.UbicacionColumnas.codigo_ubicacion+ ")");




        sb.append(");");

        db.execSQL(sb.toString());

    }




    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){



    }




    public static final boolean insertardetalle(SQLiteDatabase base,

                                                int  id_cabecera,

                                                String secuencia,

                                                String tipo_med,

                                                String num_med,

                                                double est_med ,

                                                String codigo_obs ,

                                                String fec_med_ant,

                                                double est_med_ant,

                                                int can_dig,

                                                String calle ,

                                                int altura ,

                                                String codigo_ubicacion,

                                                String codigo_llave,

                                                String codigo_riesgo_acceso,

                                                String ubicacion



                                                                 ){

        //Alta de un detalle de medicion
        ContentValues registro=new ContentValues();
        registro.put( "id_cabecera",id_cabecera);
        registro.put( "secuencia",secuencia.toString());
        registro.put( "tipo_med",tipo_med.toString());
        registro.put( "num_med",num_med.toString());
        registro.put( "est_med" ,est_med);
        registro.put( "codigo_obs" ,codigo_obs.toString());
        registro.put( "fec_med_ant",fec_med_ant.toString());
        registro.put( "est_med_ant",est_med_ant);
        registro.put( "can_dig",can_dig);
        registro.put( "calle" ,calle.toString());
        registro.put( "altura" ,altura);
        registro.put( "codigo_ubicacion",codigo_ubicacion.toString());
        registro.put( "codigo_llave",codigo_llave.toString());
        registro.put( "codigo_riesgo_acceso" ,codigo_riesgo_acceso.toString());
        registro.put( "ubicacion" ,ubicacion.toString());


        long result = base.replace ("adetalles", null, registro);

        base.close();
        if  (result!=-1) return true;
        else return false;

    }

	public static void guardar_medicion(SQLiteOpenHelper base,

                                        int  id_cabecera,

                                        String secuencia,

                                        double est_med ,

                                        String codigo_obs ,

                                        String  observacion,

                                        int can_dig,

                                        String codigo_ubicacion,

                                        String codigo_llave,

                                        String codigo_riesgo_acceso ,

                                        String geo_pos_med ,

                                        String fec_med_real,

                                        String hora_med_real,

                                        String enviado,

                                        String ubicacion,

                                        String cargado,

                                        String obs_adic,

                                        String fec_med){

		String[] ids = new String[2];
		ids[0]=String.valueOf(id_cabecera);
		ids[1]=secuencia.toString();
		ContentValues values = new ContentValues();

		values.put("est_med", est_med);
        values.put("codigo_obs",codigo_obs.toString());
        values.put("observacion", observacion.toString());
        values.put("can_dig", can_dig);
        values.put("codigo_ubicacion", codigo_ubicacion.toString());
        values.put("codigo_llave",codigo_llave.toString().toString());
        values.put("codigo_riesgo_acceso", codigo_riesgo_acceso.toString());
        values.put("geo_pos_med", geo_pos_med.toString());
        values.put("fec_med_real", fec_med_real.toString());
        values.put("hora_med_real", hora_med_real.toString());
        values.put("enviado", enviado.toString());
        values.put("ubicacion", ubicacion.toString());
        values.put("cargado",cargado.toString());
        values.put("obs_adic",obs_adic.toString());
        values.put("fec_med",fec_med.toString());



		//base.getWritableDatabase().update("adetalles", values,wheres.toString(), ids);	
		base.getWritableDatabase().update("adetalles", values, " id_cabecera= ? and secuencia = ? ", ids);
		base.getWritableDatabase().close();

	}
    public static Cursor cargar_medicion(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from adetalles where adetalles.secuencia= (select min(adetalles.secuencia) from adetalles where adetalles.[cargado]='n')" , null));
    }
    public static Cursor  buscar_medicion(SQLiteOpenHelper base,String nro_medicion){

        return (base.getReadableDatabase().rawQuery("select * from adetalles where (adetalles.num_med='"+nro_medicion.toString()+"')and (adetalles.[cargado]='n')" , null));

    }

    public static boolean  buscar_nro_medidor(SQLiteOpenHelper base,String nro_medidor){

       Cursor fila= (base.getReadableDatabase().rawQuery("select adetalles.num_med from adetalles where (adetalles.num_med='"+nro_medidor.toString()+"')" , null));


        if (fila.moveToFirst()){


            return (true);


        }
        else{
            return (false);
        }


    }
    public static Cursor cargar_mediciones_no_env(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from adetalles where (adetalles.[enviado]='n') and (adetalles.[cargado]='s')" , null));



    }
    public static int cant_med_para_enviar(SQLiteOpenHelper base){

        Cursor fila= (base.getReadableDatabase().rawQuery("select count(secuencia) from adetalles where (adetalles.[enviado]='n') and (adetalles.[cargado]='s')" , null));
        if (fila.moveToFirst()){


            return (fila.getInt(0));


        }
        else{
            return (0);
        }



    }
    public static void cancelar_envio(SQLiteOpenHelper base){

       base.getWritableDatabase().rawQuery("UPDATE adetalles SET enviado='n' where (adetalles.[enviado]='n') and (adetalles.[cargado]='s')",null );



    }
    public static void confirmar_envio(SQLiteOpenHelper base){

       // base.getWritableDatabase().rawQuery("UPDATE adetalles SET enviado='s' where (adetalles.[enviado]='n') and (adetalles.[cargado]='s')",null );

        String[] ids = new String[2];
        ids[0]="n";
        ids[1]="s";
        ContentValues values = new ContentValues();

        values.put("enviado","s");


        //base.getWritableDatabase().update("adetalles", values,wheres.toString(), ids);
        base.getWritableDatabase().update("adetalles", values, " enviado= ? and cargado = ? ", ids);
        //base.close()

    }


    public static double medicion_anterior(SQLiteOpenHelper base,String secuencia){

        Cursor c= base.getReadableDatabase().rawQuery("select est_med_ant from adetalles where secuencia='"+
               secuencia.toString()+"'", null);

        if (c.moveToFirst()){


            return (c.getDouble(0));


        }

        else {

            return (0.0);


        }
    }
    public static int cant_digitos(SQLiteOpenHelper base,String secuencia){

        Cursor c= base.getReadableDatabase().rawQuery("select can_dig from adetalles where secuencia='"+
               secuencia.toString()+"'", null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (10);


        }
    }
    public static int Cantidad_mediciones_totales(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from adetalles" , null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (0);


        }
    }
    public static int Cantidad_mediciones_restantes(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from adetalles where adetalles.cargado='n'" , null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (0);


        }
    }
    public static int Cantidad_mediciones_cargadas(SQLiteOpenHelper base){

        Cursor c= base.getReadableDatabase().rawQuery("select count(*) from adetalles where adetalles.cargado='s'" , null);

        if (c.moveToFirst()){


            return (c.getInt(0));


        }

        else {

            return (0);


        }
    }
    public static void marca_enviada(SQLiteOpenHelper base ,String idcab,String secuencia){

        String[] ids = new String[2];
        ids[0]=idcab.toString();
        ids[1]=secuencia.toString();
        ContentValues values = new ContentValues();

        values.put("enviado","s");


        //base.getWritableDatabase().update("adetalles", values,wheres.toString(), ids);
        base.getWritableDatabase().update("adetalles", values, " id_cabecera= ? and secuencia = ? ", ids);
        //base.close();

    }
    public static void marca_NO_enviada(SQLiteOpenHelper base ,String idcab,String secuencia){

        String[] ids = new String[2];
        ids[0]=idcab.toString();
        ids[1]=secuencia.toString();
        ContentValues values = new ContentValues();

        values.put("enviado","n");


        //base.getWritableDatabase().update("adetalles", values,wheres.toString(), ids);
        base.getWritableDatabase().update("adetalles", values, " id_cabecera= ? and secuencia = ? ", ids);
        //base.close();

    }


    /*
	public static Double SumarSubtotales(SQLiteOpenHelper base ,String idpedido,String codexcluir){
		Cursor fila=base.getReadableDatabase().rawQuery( "select sum(adetalles.subtotal) "+
				" from adetalles,acabecera" +
				" where adetalles.id_cabecera = acabecera._id"+
				" and adetalles.id_cabecera='"+idpedido.toString()+ "'" 
				+ " and adetalles.acod_fab!='"+codexcluir.toString()+ "'" ,null);

		if (fila.moveToFirst()){

			return (fila.getDouble(0));


		}

		else {

			return (0.0);			


		}  
	}

	public static Double TotalFactura(SQLiteOpenHelper base ,String idpedido){
		Cursor fila=base.getReadableDatabase().rawQuery( "select sum(adetalles.subtotal) "+
				" from adetalles,acabecera" +
				" where adetalles.id_cabecera = acabecera._id"+
				" and adetalles.id_cabecera='"+idpedido.toString()+ "'" ,null);

		if (fila.moveToFirst()){

			return (fila.getDouble(0));


		}

		else {

			return (0.0);			


		}  
	}

	
	
	

    /*
	


	
	

*/
}