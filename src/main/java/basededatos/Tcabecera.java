package basededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class Tcabecera {

	public static final String NombreTabla = "acabecera";

	public static class CabeceraColumnas implements BaseColumns {
		
		public static final String _id = "_id";
		
		public static final String id_cabecera = "id_cabecera";

		public static final String mod_cod_ubic = "mod_cod_ubic";

		public static final String mod_tipo_llave = "mod_tipo_llave";

		public static final String mod_cod_riesgo = "mod_cod_riesgo";

		public static final String mod_can_dig = "mod_can_dig";

		public static final String fec_rec = "fec_rec";

		public static final String id_usuario = "id_usuario";


	}



	public static void onCreate(SQLiteDatabase db){

		StringBuilder sb = new StringBuilder();

		sb.append("CREATE TABLE IF NOT EXISTS "  + Tcabecera.NombreTabla + " (");

		sb.append(CabeceraColumnas._id+ " INTEGER PRIMARY KEY AUTOINCREMENT , ");

        sb.append(CabeceraColumnas.id_cabecera+" INTEGER  NOT NULL , ");
		
		sb.append(CabeceraColumnas.mod_cod_ubic+" TEXT  NULL  DEFAULT S ,");

		sb.append(CabeceraColumnas.mod_tipo_llave+" TEXT  NULL DEFAULT S , ");

		sb.append(CabeceraColumnas.mod_cod_riesgo+" TEXT  NULL  DEFAULT S , ");

		sb.append(CabeceraColumnas.mod_can_dig+" TEXT  NULL  DEFAULT S , ");

		sb.append(CabeceraColumnas.fec_rec+" TEXT  NULL  , ");

	    sb.append(CabeceraColumnas.id_usuario+" TEXT NOT NULL  , ");

        sb.append( "UNIQUE(id_cabecera)" );

		sb.append(");");

		db.execSQL(sb.toString());

	}



	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){


	}

	public static final boolean insertarcabecera(SQLiteDatabase base,int id_cabecera,
			String mod_cod_ubic,String mod_tipo_llave,String mod_cod_riesgo,String mod_can_dig,
            String id_usuario){

		//Alta de una cabecera
		ContentValues registro=new ContentValues();

		registro.put("id_cabecera",id_cabecera);
		registro.put("mod_cod_ubic",mod_cod_ubic.toString());
		registro.put("mod_tipo_llave",mod_tipo_llave.toString());
		registro.put("mod_cod_riesgo",mod_cod_riesgo.toString());
		registro.put("mod_can_dig",mod_can_dig.toString());
		registro.put("id_usuario",id_usuario.toString());

		long result = base.replace ("acabecera", null, registro);

		base.close();
		if  (result!=-1) return true;
		else return false;

	}
	public static final int ultimoId(SQLiteDatabase base){


		Cursor fila=base.rawQuery( " SELECT MAX(acabecera.id_cabecera) from acabecera ",null);
		if (fila.moveToFirst())
			return(fila.getInt(0));

		else return (1);

	}
    public static final boolean permite_ubicacion(SQLiteDatabase base){


        Cursor fila=base.rawQuery( " SELECT mod_cod_ubic from acabecera ",null);
        if (fila.moveToFirst()){

            if (fila.getString(0).equals("S"))
                return(true);
            else return (false);

        }else
            return true;
    }
    public static final boolean permite_llave(SQLiteDatabase base){


        Cursor fila=base.rawQuery( " SELECT mod_tipo_llave from acabecera ",null);
        if (fila.moveToFirst()){

            if (fila.getString(0).equals("S"))
                return(true);
            else return (false);

        }else
            return true;
    }
    public static final boolean permite_riesgo(SQLiteDatabase base){


        Cursor fila=base.rawQuery( " SELECT mod_cod_riesgo from acabecera ",null);
        if (fila.moveToFirst()){

            if (fila.getString(0).equals("S"))
            return(true);
            else return (false);

    }else
        return true;
    }
    public static final boolean permite_can_dig(SQLiteDatabase base){


        Cursor fila=base.rawQuery( " SELECT mod_can_dig from acabecera ",null);
        if (fila.moveToFirst()){

            if (fila.getString(0).equals("S"))
                return(true);
            else return (false);

        }else
            return false;
    }

    public static final void Setear_fecha_recepcion(SQLiteDatabase base,int idcabecera,String fecha){


        String[] idm = new String[1];
        idm[0]=String.valueOf(idcabecera);
        //idm[1]=String.valueOf(cantart);

        ContentValues values = new ContentValues();

        values.put("fec_rec",fecha.toString());

        base.update("acabecera", values," id_cabecera= ?", idm);
        base.close();

    }




    public static Cursor cabecera_medicion(SQLiteOpenHelper base){

        return (base.getReadableDatabase().rawQuery("select distinct * "+
                " from acabecera order by _id desc",null));

    }

    /*
	public static final void actualizaTotales(SQLiteDatabase base,int iddecabecera,float montot,int cantart){


		/*base.rawQuery("update acabecera set montototal= "+montot+
				"where acabecera.id_cabecera="+iddecabecera,null);
		base.rawQuery("update acabecera set articulostotal= "+cantart+
				"where acabecera.id_cabecera="+iddecabecera,null);*/
			
		/* String sql = "UPDATE "+ Tcabecera.NombreTabla + " SET "+ CabeceraColumnas.montototal +" = " +String.valueOf(montot)+","+
				 CabeceraColumnas.articulostotal +" = " +String.valueOf(cantart)+
				 " where "+CabeceraColumnas.id_cabecera+" = "+String.valueOf(iddecabecera);
		base.rawQuery(sql.toString(), null);
		//
		
		// = "UPDATE "+ Tcabecera.NombreTabla + " SET "+ CabeceraColumnas.articulostotal +" = " + "-" + cantart;
	//	base.rawQuery(sql, null);

		String[] idm = new String[1];
		idm[0]=String.valueOf(iddecabecera);
		//idm[1]=String.valueOf(cantart);
		
		ContentValues values = new ContentValues();

		values.put("montototal",montot);
		values.put("articulostotal",cantart);	

		//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
		base.update("acabecera", values," _id= ?", idm);
		base.close();	
		}
	
	public static void borrarpedido(SQLiteDatabase base,int idc){
		String[] idb = new String[1];
		idb[0]=String.valueOf(idc);
		//idb[1]=codfab.toString();
		
		//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
		base.delete("acabecera"," _id= ? ", idb);
		base.delete("apedido"," id_cabecera= ? ", idb);
		
		base.close();	
	
	}
	public static void borrarCabecera(SQLiteDatabase base,int idc){
		String[] idb = new String[1];
		idb[0]=String.valueOf(idc);
		//idb[1]=codfab.toString();
		
		//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
		base.delete("acabecera"," _id= ? ", idb);
		
		
		base.close();	
	
	}




public static Cursor filtrarxnroprov(SQLiteOpenHelper base,String nroprov){
	
	return (base.getReadableDatabase().rawQuery("select distinct acabecera._id,aclientes.nro_prov,aclientes.nyap_prov" +
			",acabecera.montototal,acabecera.fechaentrega,acabecera.articulostotal"+
			" from aclientes,acabecera	 where acabecera.nro_prov=aclientes.nro_prov"
			+" and acabecera.nro_prov like '%"+nroprov.toString()+"%'" , null));	
	
}
public static Cursor filtrarxfecha(SQLiteOpenHelper base,String fecha){
	
	return (base.getReadableDatabase().rawQuery("select distinct acabecera._id,aclientes.nro_prov,aclientes.nyap_prov" +
			",acabecera.montototal,acabecera.fechaentrega,acabecera.articulostotal"+
			" from aclientes,acabecera	 where acabecera.nro_prov=aclientes.nro_prov"			
			+" and acabecera.fechaentrega like '%"+fecha.toString()+"%'" , null));	
	

/*return (base.getReadableDatabase().rawQuery("select distinct acabecera._id,aclientes.nro_prov,aclientes.nyap_prov" +
			",acabecera.montototal,acabecera.fechaentrega"+
			" from aclientes,acabecera	 where acabecera.nro_prov=aclientes.nro_prov"
			+" and acabecera.fechaentrega like '%"+fecha.toString()+"%'" , null));	


}	
public static Cursor cant_pedidos(SQLiteOpenHelper base){
	
return (base.getReadableDatabase().rawQuery("select _id,count(nro_prov) as totped  from acabecera ",null));

}
public static final void marca_cab_Enviada(SQLiteDatabase base,int iddecabecera){


	String[] idm = new String[1];
	idm[0]=String.valueOf(iddecabecera);
	//idm[1]=String.valueOf(cantart);
	
	ContentValues values = new ContentValues();

	values.put("fueenviado","s");
	

	//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
	base.update("acabecera", values," _id= ?", idm);
	//base.close();	
	
}
public static final void marca_cab_NO_Enviada(SQLiteDatabase base,int iddecabecera){


	String[] idm = new String[1];
	idm[0]=String.valueOf(iddecabecera);
	//idm[1]=String.valueOf(cantart);
	
	ContentValues values = new ContentValues();

	values.put("fueenviado","n");
	

	//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
	base.update("acabecera", values," _id= ?", idm);
	//base.close();	
	
}



public static boolean BuscarClienteEnPedidos(SQLiteOpenHelper base,String nroprov){
	
	Cursor fila=base.getReadableDatabase().rawQuery("select distinct acabecera._id,aclientes.nro_prov"+
			" from aclientes,acabecera	 where acabecera.nro_prov=aclientes.nro_prov"
			+" and acabecera.nro_prov ='"+nroprov.toString()+"'", null);	
	if (fila.moveToFirst()){

		return (true);

	}

	else {

		return (false);			


	}
}
public static Cursor BuscarPedido(SQLiteOpenHelper base,String nroprov){
	
	Cursor fila=base.getReadableDatabase().rawQuery("select distinct acabecera._id,aclientes.nro_prov,acabecera._id,acabecera.montototal,acabecera.fechaentrega,acabecera.articulostotal"+
			" from aclientes,acabecera	 where acabecera.nro_prov=aclientes.nro_prov"
			+" and acabecera.nro_prov ='"+nroprov.toString()+"'", null);	
	if (fila.moveToFirst()){

		return (fila);

	}

	else {

		return (null);			


	}
}
public static void borrar_cabeceras_viejas(SQLiteDatabase base ,String fecha_actual){
	//String[] idb = new String[1];
	//idb[0]=String.valueOf(fecha_actual.toString());
	
	
	
	
	String Consulta="DELETE from apedido WHERE fecha < '17/11/2012'";
	base.rawQuery(Consulta,null);
	Consulta="DELETE from acabecera WHERE fechaentrega < '17/11/2012'";
	base.rawQuery(Consulta,null);
	//idb[1]=codfab.toString();
	
	//base.getWritableDatabase().update("apedido", values,wheres.toString(), ids);	
	//base.delete("acabecera"," fechaentrega < ? ", idb);
	//base.delete("apedido"," fecha < ? ", idb);
	
	//USAR ESTO///////====> WHERE julianday(fecha1)>julianday(fecha2)

	//Probar: DELETE FROM APEDIDO WHERE date(FECHA) < (Date('now'));
	//delete from apedido	where fecha < (select strftime('%d/%m/%Y',Date('now')))
	
	base.close();	

}



*/


}