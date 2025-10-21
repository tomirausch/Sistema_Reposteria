package reposteria;

import reposteria.persistencia.BaseDatos;
import reposteria.presentacion.Consola;

public class Main {
    public static void main(String[] args) {
        BaseDatos db = BaseDatos.getInstance();
        Consola consola = new Consola(db.getConnection());
        consola.iniciar();
        db.cerrar();
    }
}