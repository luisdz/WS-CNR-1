package sv.gob.cnr.comerciows.recievedata;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBOracle {
	
	private Connection conexion;

	public Connection getConexion() {
	     return conexion;
	}

	public void setConexion(Connection conexion) {
	this.conexion = conexion;
	}

	public String  conectar() {
		String res="error";
	try {
	Class.forName("oracle.jdbc.OracleDriver");
	String BaseDeDatos = "jdbc:oracle:thin:@desa-scan:1525/cnrdes";

	conexion = DriverManager.getConnection(BaseDeDatos, "LM8679","CAMBIAME1"); 
	if (conexion != null) {
	System.out.println("Conexion exitosa!");
	res="Conexion exitosa!";
	} else {
	System.out.println("Conexion fallida!");
	res="Conexion exitosa!";
	}

	} catch (Exception e) {
	     e.printStackTrace();
	}
	return res;
	}

}
