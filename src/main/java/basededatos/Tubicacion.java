package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tubicacion{

    public static final String NombreTabla = "aubicacion";

    public static class UbicacionColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String codigo_ubicacion = "codigo_ubicacion ";

        public static final String descripcion_ubicacion = "descripcion_ubicacion";


    }
    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE  IF NOT EXISTS "  + Tubicacion.NombreTabla  + "(");

        sb.append(UbicacionColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");

        sb.append(UbicacionColumnas.codigo_ubicacion + " TEXT  NULL DEFAULT '00', ");

        sb.append(UbicacionColumnas.descripcion_ubicacion + "  TEXT  NOT NULL, ");

        sb.append( "UNIQUE(codigo_ubicacion)" );

        sb.append(");");

        db.execSQL(sb.toString());

    }
    public static Cursor todas(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from aubicacion  ORDER BY codigo_ubicacion", null));
    }

    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + Tubicacion.NombreTabla);
        //aca usar alter table
        //Tubicacion.onCreate(db);
    }

    public static  boolean insertar_ubicacion(SQLiteDatabase base,String cod,String descripcion){


        ContentValues registro=new ContentValues();
        registro.put("codigo_ubicacion",cod.toString());
        registro.put("descripcion_ubicacion",descripcion.toString());

        long result = base.replace (Tubicacion.NombreTabla, null, registro);
        base.close();
        if  (result!=-1) return true;
        else return false;
    }
    public static int idubicacion(SQLiteDatabase base ,String cod_ubicacion){
        int regreso=0;
        Cursor fila=base.rawQuery( "select _id from aubicacion  where codigo_ubicacion="+cod_ubicacion.toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getInt(0);
       fila.close();
        return(regreso);

    }
    public static String cod_ubicacion(SQLiteDatabase base ,int _id){
        String regreso="0";
        Cursor fila=base.rawQuery( "select codigo_ubicacion from aubicacion  where _id="+String.valueOf(_id).toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getString(0).toString();
        fila.close();
        return(regreso);
    }

}
