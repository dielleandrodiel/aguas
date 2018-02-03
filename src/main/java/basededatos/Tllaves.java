package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tllaves {

    public static final String NombreTabla = "allaves";

    public static class LLavesColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String codigo_llave = "codigo_llave ";

        public static final String descripcion_llave = "descripcion_llave";


    }
    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE  IF NOT EXISTS "  + Tllaves.NombreTabla  + "(");

       sb.append(LLavesColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");

        sb.append(LLavesColumnas.codigo_llave + " TEXT  NULL DEFAULT '00', ");

        sb.append(LLavesColumnas.descripcion_llave + "  TEXT  NOT NULL, ");

        sb.append( "UNIQUE(codigo_llave)" );

        sb.append(");");

        db.execSQL(sb.toString());

    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + Tllaves.NombreTabla);
        //aca usar alter table
        //Tllaves.onCreate(db);
    }

    public static  boolean insertarLLave(SQLiteDatabase base,String cod,String descripcion){


        ContentValues registro=new ContentValues();
        registro.put("codigo_llave",cod.toString());
        registro.put("descripcion_llave",descripcion.toString());

       long result = base.replace (Tllaves.NombreTabla, null, registro);
        base.close();
        if  (result!=-1) return true;
        else return false;
    }
    public static Cursor todas(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from allaves  ORDER BY codigo_llave", null));
    }
    public static int idllave(SQLiteDatabase base ,String cod_llave){
int regreso=0;
        Cursor fila=base.rawQuery( "select _id from allaves  where codigo_llave="+cod_llave.toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getInt(0);
         fila.close();
        return(regreso);

    }
    public static String cod_llave(SQLiteDatabase base ,int _id){
String regreso="0";
        Cursor fila=base.rawQuery( "select codigo_llave from allaves  where _id="+String.valueOf(_id).toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getString(0).toString();
       fila.close();
        return(regreso);
    }
}
