package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tsistema{


	public static final String NombreTabla = "ssistema";

	public static class SistemaColumnas implements BaseColumns {

		public static final String _id = "_id";

		public static final String reintentos_login = "reintentos_login";

		public static final String Log_estado = "Log_estado";

		public static final String servidor_de_datos = "servidor_de_datos";

		public static final String rango_gps = "rango_gps";

        public static final String permitir_descargar = "permitir_descargar";


			}
	public static void onCreate(SQLiteDatabase db){

		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE  IF NOT EXISTS "+ Tsistema.NombreTabla  + "(");

		sb.append(SistemaColumnas._id+ " INTEGER PRIMARY KEY , ");

		sb.append(SistemaColumnas.reintentos_login + " INTEGER  NULL DEFAULT '5', ");

		sb.append(SistemaColumnas.Log_estado + "  TEXT   NULL DEFAULT 'NO', ");
		
		sb.append(SistemaColumnas.servidor_de_datos + "  TEXT  NULL DEFAULT 'http://200.80.52.117/~avisentini/lg_gestion' , ");
		
		sb.append(SistemaColumnas.rango_gps + "  REAL   NULL DEFAULT 200, ");

        sb.append(SistemaColumnas.permitir_descargar + "  INTEGER  NULL ");

        sb.append(");");

		db.execSQL(sb.toString());

	}


	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + Tsistema.NombreTabla);
		//aca usar alter table 
		Tsistema.onCreate(db);
	}
	//obtener fecha actual en android:

	
	public static  boolean insertarSistema(SQLiteDatabase base,
			int reintentos_login,String Log_estado,
			double rango_gps,String url){


		//Alta de un registro de sistema
		ContentValues registro=new ContentValues();
		
		registro.put("reintentos_login",reintentos_login);
		registro.put("Log_estado",Log_estado.toString());
		registro.put("rango_gps",rango_gps);
		registro.put("servidor_de_datos",url.toString());
        long result = base.replace ("ssistema", null, registro);
		base.close();
		if  (result!=-1) return true;
		else return false; 
	}
	public static void  modificarServidor(SQLiteDatabase base,
			String servidor){

		//base.execSQL("UPDATE " + Tsistema.NombreTabla  + " set servidor_de_datos='"+servidor.toString()+"'");
		 //base.execSQL("INSERT INTO  " + Tsistema.NombreTabla  + "(servidor_de_datos) values( '"+servidor.toString()+"')");
		base.execSQL("REPLACE INTO  " + Tsistema.NombreTabla  +"(_id,reintentos_login,Log_estado,servidor_de_datos,rango_gps)"+ " values(1,5,'sin_datos','"+servidor.toString()+"',200)");
	base.close();
	}
	public static Cursor todos(SQLiteOpenHelper base){

		return (base.getReadableDatabase().rawQuery("select * from ssistema ", null));
	}
	
	public static int reintentos(SQLiteOpenHelper base){
		Cursor fila=base.getReadableDatabase().rawQuery(
				"select reintentos_login from ssistema",null);
int retornar=5;
		if (fila.moveToFirst()){
			retornar=fila.getInt(0);
			fila.close();
			base.close();
			return (retornar);

		}

		else {
            fila.close();
			base.close();
            return (retornar);			


		}
	}	
	public static String Log_estado(SQLiteOpenHelper base){
		Cursor fila=base.getReadableDatabase().rawQuery(
				"select Log_estado from ssistema",null);
String devolver="NO";
		if (fila.moveToFirst()){
        devolver=fila.getString(0);
		fila.close();
        return (devolver);

		}

		else {
            fila.close();
			return (devolver);			


		}
	}
	public static double  rango_gps(SQLiteOpenHelper base){
		Cursor fila=base.getReadableDatabase().rawQuery(
				"select rango_gps from ssistema",null);
double dev=200;
		if (fila.moveToFirst()){
         dev=fila.getDouble(0);
			fila.close();
            return (dev);

		}

		else {
            fila.close();
			return (dev);			


		}
	}

    public static int  permite_descarga(SQLiteOpenHelper base){
        Cursor fila=base.getReadableDatabase().rawQuery(
                "select permitir_descargar from ssistema",null);
        int dev=0;//cero permite descarga de datos
        if (fila.moveToFirst()){
            dev=fila.getInt(0);
            fila.close();
            return (dev);

        }

        else {
            fila.close();
            return (dev);


        }
    }

    public static void setear_permite_descarga(SQLiteDatabase base,int sn){
            //base.execSQL("UPDATE " + Tsistema.NombreTabla  + " set servidor_de_datos='"+servidor.toString()+"'");
            //base.execSQL("INSERT INTO  " + Tsistema.NombreTabla  + "(servidor_de_datos) values( '"+servidor.toString()+"')");
            base.execSQL("UPDATE  " + Tsistema.NombreTabla  +" set permitir_descargar="+String.valueOf(sn));
            base.close();
        }



	public static String servidor_de_datos(SQLiteOpenHelper base){
		Cursor fila=base.getReadableDatabase().rawQuery(
				"select servidor_de_datos from ssistema",null);
String devolver="http://200.80.52.117/~avisentini/lg_gestion";//url donde estan los php
		if (fila.moveToFirst()){
        devolver=fila.getString(0);
		fila.close();
        base.close();
		return (devolver);

		}

		else {
           
			fila.close();
			base.close();
			return (devolver);			


		}
	}

}






