package basededatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class baseandroid extends SQLiteOpenHelper {

	public baseandroid(Context context, String nombre, CursorFactory factory, int version) {
		super(context,nombre, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		Tusuarios.onCreate(db); //le paso el objeto sqllitedatabase
        Tllaves.onCreate(db);
        Tubicacion.onCreate(db);
        Triesgo.onCreate(db);
        Tobs.onCreate(db);
        Tsistema.onCreate(db);
        Tdetalles.onCreate(db);
        Tcabecera.onCreate(db);
        Tfuera_de_ruta.onCreate(db);

        //Tsistema.onCreate(db);
		/*
		db.execSQL(" CREATE TRIGGER [delete_cab_cascada]  BEFORE DELETE ON [tabla2] FOR EACH ROW"+
		        " BEGIN " +
				" DELETE FROM tabla1 WHERE tabla1.id_cabecera  = old.[_id];"+
			" END " );
			*/

    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {
		

	}	
	/*@Override
	public void onOpen(SQLiteDatabase db) {
	super.onOpen(db);
	if (!db.isReadOnly())
	{
	db.execSQL("PRAGMA foreign_keys=ON;");
	}
	}*/

}
