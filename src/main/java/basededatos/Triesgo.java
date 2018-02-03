package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Triesgo {

    public static final String NombreTabla = "ariesgo";

    public static class RiesgoColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String codigo_riesgo = "codigo_riesgo ";

        public static final String descripcion_riesgo = "descripcion_riesgo";


    }
    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();



        sb.append("CREATE TABLE  IF NOT EXISTS "  + Triesgo.NombreTabla  + "(");

        sb.append(RiesgoColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");

        sb.append(RiesgoColumnas.codigo_riesgo + " TEXT NOT NULL DEFAULT '00', ");

        sb.append(RiesgoColumnas.descripcion_riesgo + "  TEXT  NOT NULL , ");

        sb.append( "UNIQUE(codigo_riesgo)" );

        sb.append(");");

        db.execSQL(sb.toString());

    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + Triesgo.NombreTabla);
        //aca usar alter table
        //Triesgo.onCreate(db);
    }

    public static  boolean insertarRiesgo(SQLiteDatabase base,String cod,String descripcion){



        ContentValues registro=new ContentValues();
        registro.put("codigo_riesgo",cod.toString());
        registro.put("descripcion_riesgo",descripcion.toString());

        long result = base.replace (Triesgo.NombreTabla, null, registro);
        base.close();
        if  (result!=-1) return true;
        else return false;
    }
    public static Cursor todos(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from ariesgo  ORDER BY codigo_riesgo", null));
    }
    public static int idriesgo(SQLiteDatabase base ,String cod_riesgo){
        int regreso=0;
        Cursor fila=base.rawQuery( "select _id from ariesgo  where codigo_riesgo="+cod_riesgo.toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getInt(0);
       fila.close();
        return(regreso);

    }
    public static String cod_riesgo(SQLiteDatabase base ,int _id){
        String regreso="0";
        Cursor fila=base.rawQuery( "select codigo_riesgo from ariesgo  where _id="+String.valueOf(_id).toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getString(0).toString();
        fila.close();
        return(regreso);
    }



}
