package com.example.aguas;

public class Util{

    public static double esNumero(String valor, long cantidadDeEnteros, long cantidadDeDecimales, boolean admiteSignoNegativo)throws UtilException{

        try{

            double retorno = 0.0;

            //Valido que el valor no este en blanco
            if (valor == null || valor.trim().length() == 0)
                throw new UtilException ("Debe indicar el valor");

            //Valido que si ingresaron un punto, no sea el primer valor
            if (valor.indexOf(".") != -1 && (valor.length() == 1)){
                throw new UtilException ("Debe indicar la cantidad de enteros y dedcimales. Ej.: 3.5");
            }

            //En caso que el valor ingresado en la primera posición sea el signo menos asigno
            //-0 para que no de error por no ser numero.
            if (valor.equals("-") && (valor.length() == 1))
                valor = "-0";

            //Valido que no sea un número
            try{
                retorno = Double.valueOf(valor);
            }catch(NumberFormatException e){
                //retorno = 0.0
                throw new UtilException ("El valor no es válido");
            }

            //Valido que no sea un valor menor a cero
            if (admiteSignoNegativo == false){
                if (retorno < 0.0)
                    throw new UtilException ("El valor no puede ser negativo");
            }

            //Valido la cantidad de enteros y decimales. Para saber si existen
            //decimales busco el punto decimal. En caso que el punto decimal no
            //este es por que no existen decimales.
            if (valor.indexOf(".") != -1){

                //Valido la cantidad de enteros
                if (cantidadDeEnteros == 0)
                    throw new UtilException ("El valor no permite decimales.");

                //Valido la cantidad de decimales
                if (cantidadDeDecimales == 0)
                    throw new UtilException ("El valor no permite decimales.");


                //Si la longitud es uno significa que ingreso solo el punto. Si es
                //distinta de uno asumo que ingreso enteros y decimales.
                if (valor.length() != 1){

                    int cantidadEntero = valor.substring(0, valor.indexOf(".")).length();
                    int cantidadDecimales = valor.substring(valor.indexOf(".") + 1, valor.length()).length();

                    if (cantidadEntero > cantidadDeEnteros){

                        String ent = "enteros";
                        if (String.valueOf(cantidadEntero).length() == 1)
                            ent = " entero";

                        throw new UtilException ("El valor debe tener "+ cantidadDeEnteros + ent);
                    }

                    if (cantidadDecimales > cantidadDeDecimales){

                        String dec =" decimales";
                        if (String.valueOf(cantidadDecimales).length() == 1)
                            dec = " decimal";

                        throw new UtilException ("El valor debe tener "+ cantidadDeDecimales + dec);
                    }

                }

            }else{

                int cantidadEntero = valor.length();

                if (cantidadEntero > cantidadDeEnteros){

                    String ent = "enteros";
                    if (String.valueOf(cantidadEntero).length() == 1)
                        ent = " entero";

                    throw new UtilException ("El valor debe tener "+ cantidadDeEnteros + ent);

                }

            }

            return retorno;

        }catch(UtilException e){
            throw e;
        }catch(Exception e){
            throw new UtilException ("Error de sistema");
        }
    }
}

class UtilException extends Exception{

    public UtilException() {
    }

    public UtilException(String msg) {
        super(msg);
    }
}