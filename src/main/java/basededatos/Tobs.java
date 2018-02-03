package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tobs {

    public static final String NombreTabla = "aobs";

    public static class ObsColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String codigo_obs = "codigo_obs ";

        public static final String descripcion_obs = "descripcion_obs";

        public static final String obs_adic = "obs_adic";

        public static final String para_lect = "para_lect ";

        public static final String para_obs = "para_obs ";

        public static final String tipo_med = "tipo_med";

        public static final String repaso = "repaso ";

        public static final String accion = "accion";


    }
    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE  IF NOT EXISTS "  + Tobs.NombreTabla  + "(");

        sb.append(ObsColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");

        sb.append(ObsColumnas.codigo_obs + " TEXT  NULL DEFAULT '00' , ");

        sb.append(ObsColumnas.descripcion_obs+ "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.obs_adic + "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.para_lect + "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.para_obs + "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.tipo_med + "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.repaso+ "  TEXT  NOT NULL, ");

        sb.append(ObsColumnas.accion + "  TEXT  NOT NULL, ");

        sb.append( "UNIQUE(codigo_obs)" );

        sb.append(");");

        db.execSQL(sb.toString());

    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + Tobs.NombreTabla);
        //aca usar alter table
        //Tobs.onCreate(db);
    }

    public static  boolean insertarObs(SQLiteDatabase base,String cod,String descripcion,String obs_adic,
                                       String para_lect,String para_obs ,String tipo_med ,String repaso,
                                       String accion){



        ContentValues registro=new ContentValues();
        registro.put("codigo_obs",cod.toString());
        registro.put("descripcion_obs",descripcion.toString());
        registro.put("obs_adic",obs_adic.toString());
        registro.put("para_lect",para_lect.toString());
        registro.put("para_obs",para_obs.toString());
        registro.put("tipo_med",tipo_med.toString());
        registro.put("repaso",repaso.toString());
        registro.put("accion",accion.toString());

        long result = base.replace (Tobs.NombreTabla, null, registro);
        base.close();
        if  (result!=-1) return true;
        else return false;
    }
    public static Cursor todas(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select * from aobs  ORDER BY codigo_obs", null));
    }
    public static boolean tiene_E(SQLiteOpenHelper base,String cod_obs){

        Cursor c= base.getReadableDatabase().rawQuery("select tipo_med from aobs where codigo_obs='"+
                String.valueOf(cod_obs)+"'", null);

        if (c.moveToFirst()){


            if (c.getString(0).equals("E"))
                return (true);
                else
                return(false);


        }

        else {

            return (false);


        }
    }
    public static boolean permite_obs_adic(SQLiteOpenHelper base,int cod_obs){

        Cursor c= base.getReadableDatabase().rawQuery("select obs_adic from aobs where codigo_obs="+
                String.valueOf(cod_obs), null);

        if (c.moveToFirst()){


            if (c.getString(0).equals("S"))
                return (true);
            else
                return(false);


        }

        else {

            return (false);


        }
    }

    public static int idobs(SQLiteDatabase base ,String cod_obs){
        int regreso=0;
        Cursor fila=base.rawQuery( "select _id from aobs  where codigo_obs="+cod_obs.toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getInt(0);
        fila.close();
        return(regreso);

    }
    public static String cod_obs(SQLiteDatabase base ,int _id){
        String regreso="0";
        Cursor fila=base.rawQuery( "select codigo_obs from aobs  where _id="+String.valueOf(_id).toString(),null);
        if (fila.moveToFirst())
            regreso=fila.getString(0).toString();
        fila.close();
        return(regreso);
    }



}
