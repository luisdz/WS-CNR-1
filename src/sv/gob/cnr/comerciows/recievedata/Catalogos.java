package sv.gob.cnr.comerciows.recievedata;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import sv.gob.cnr.comerciows.recievedata.DBOracle;
public class Catalogos 
{

	public String getDepartamento(String code)  throws IOException 
	{
		String result="14"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) 
	    { 
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("province");
	    result=jsonDep.getString(code);	 
		return result;
	}
	public String getSexo(String code)  throws IOException 
	{
		String result="H"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) { 
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("sex");
	    result=jsonDep.getString(code);	 
		return result;
	}
	public String getMunicipio(String code)  throws IOException 
	{
		String result="0614"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) { 
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("town");
	    result=jsonDep.getString(code);	 
		return result;
	}
	 

}