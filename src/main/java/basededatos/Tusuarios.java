package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public final class Tusuarios {
    public static String usuariologueado="";//="leandro" ;
    public static String clavelogueada="";//="00000";
    public static String idusuario="";//="00000";
    //public static SQLiteDatabase baseusuario;


    public static final String NombreTabla = "ausuarios";

    public static class UsuariosColumnas implements BaseColumns {

        public static final String _id = "_id";

        public static final String id_usuario = "id_usuario";

        public static final String nombreusuario = "nombreusuario";

        public static final String claveusuario = "claveusuario";

    }
    public static void onCreate(SQLiteDatabase db){

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE  IF NOT EXISTS "  + Tusuarios.NombreTabla  + "(");

        sb.append(UsuariosColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT, ");

        sb.append(UsuariosColumnas.id_usuario + " TEXT NOT NULL, ");

        sb.append(UsuariosColumnas.nombreusuario + "  TEXT  NOT NULL, ");

        sb.append(UsuariosColumnas.claveusuario + " TEXT  NOT NULL,");

        sb.append( "UNIQUE(id_usuario)" );

        sb.append(");");

        db.execSQL(sb.toString());

    }


    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //db.execSQL("DROP TABLE IF EXISTS " + Tusuarios.NombreTabla);
        //aca usar alter table
        //Tusuarios.onCreate(db);
    }

    public static  boolean insertarUsuario(SQLiteDatabase base,String id,String usuario,String clave){


        //Alta de un usuario
        ContentValues registro=new ContentValues();
        registro.put("id_usuario",id.toString());
        registro.put("nombreusuario",usuario.toString());
        registro.put("claveusuario",clave.toString());

        //String sql= " base.INSERT OR REPLACE INTO ausuarios(id_usuario,nombreusuario,claveusuario) VALUES (id,usuario,clave);"


        long result = base.replace ("ausuarios", null, registro);
        base.close();
        if  (result!=-1) return true;
        else return false;
    }
    public static boolean validarUsuario(SQLiteDatabase base,String usuario,String clave){
        //validacion del usuario en la base local
        Cursor fila=base.rawQuery("select nombreusuario,claveusuario from ausuarios where nombreusuario='"+usuario.toString()+ "'"
                + "and claveusuario='"+clave.toString()+ "'",null);

        if (fila.moveToFirst()){

            usuariologueado=usuario;
            clavelogueada=clave;
            //baseusuario=base;
            //String usrvalido = fila.getString(0);
            //String clavevalida= fila.getString(1);
            fila.close();
            return true;
        }
        else
            fila.close();
        return false;

    }
    public static boolean masdeunUsuario(SQLiteDatabase base){

        Cursor fila=base.rawQuery("select * from ausuarios",null);

        if (fila.getCount()>1){

            fila.close();
            return true;
        }
        else
            fila.close();
        return false;

    }

}
