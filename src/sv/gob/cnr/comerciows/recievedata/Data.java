package sv.gob.cnr.comerciows.recievedata;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.icafe4j.image.ImageColorType;
import com.icafe4j.image.ImageParam;
import com.icafe4j.image.options.TIFFOptions;
import com.icafe4j.image.quant.DitherMethod;
import com.icafe4j.image.quant.DitherMatrix;
import com.icafe4j.image.tiff.TIFFTweaker;
import com.icafe4j.image.tiff.TiffFieldEnum.Compression;
import com.icafe4j.io.FileCacheRandomAccessOutputStream; 
import com.icafe4j.io.RandomAccessOutputStream;
import com.sun.javafx.binding.StringFormatter;


@WebService 
public class Data {
	
	// service de prueba
	private List<String> L_errores;
	private String errores;
	
	@WebMethod
	public String crearSolicitud( String param) throws IOException {
		
		Boolean mat=false;
		Boolean cons=false;
		Boolean bal=false;
		Boolean otro=false;
		String res="error";
		String test="";
		if(isJSONValid(param))
		{
		JSONObject jsonObjectGlobal = new JSONObject(param);
		String user = "miempresa.gob.sv";
		try{
			//user=jsonObjectGlobal.getString("user");
		}
		catch(Exception e){
			errores+="error al obtener user";
		}		
		//JSONArray jsonA = jsonObjectGlobal.getJSONObject("request").getJSONArray("registrations");
		try
		{ 
			//crear usuario
			int idU=1;
			//idU=crearUser(user);  
			int idP=0;
		    //idP=crearPresentacion(param,idU,"002");
			System.out.println("presentacion");
			//crearPreForma(param,idP,idU,"constitucionXML");
			test=crearPreForma(param,0,idU,"constitucionXML"); 
			//crearAnexo(idP, idU,"constitucion",param);//anexo constitucion
			//crearAnexo(idP, idU,"matricula",param); //anexo matricula
			//crearAnexo(idP, idU,"balance",param);	//anexo balance		
			res= "" + idU;//response
			String idSol = jsonObjectGlobal.get("id").toString();
			JSONObject newjSON= new JSONObject();
			newjSON.put("status", "recieved");
			newjSON.put("numPresentacion", idP);//pendiente getNumPresentacion
			newjSON.put("mensaje", " ");
			res=  newjSON.toString();
		}
		catch (Exception e)
		{
			
			 errores+= e;
			 JSONObject newjSON= new JSONObject();
			 newjSON.put("status", "error");
			 newjSON.put("numPresentacion", "0");
			 newjSON.put("mensaje", errores);
			 res=  newjSON.toString();
			 try{					
				}
				catch(Exception e2)
				{					
				}
		}
		}
		else
		{
			 JSONObject newjSON= new JSONObject();
			 newjSON.put("status", "error");
			 newjSON.put("numPresentacion", "0");
			 newjSON.put("mensaje", "JSON invalido");
			 res=  newjSON.toString();
		}		 
		return res;
		//return test;
	}	
	 
	//ejecutar insert update delete
	private String ejecutar(String sql) 
		{
			try{
				DBOracle baseDatos = new DBOracle();
				Statement sentencia;
				baseDatos.conectar();
	            sentencia = baseDatos.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
	            sentencia.executeUpdate(sql);
	            baseDatos.getConexion().commit();
	            sentencia.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				return "Error";
			}
			return "True"; 
		}
	
	//ejecutar select
	private ResultSet consultar(String sql) {
        ResultSet resultado = null;
        try {
        	DBOracle baseDatos = new DBOracle();
			baseDatos.conectar();
        	Statement sentencia;
            sentencia = baseDatos.getConexion().createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultado = sentencia.executeQuery(sql);
            baseDatos.getConexion().commit();
            baseDatos.getConexion().close();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }        return resultado;
    }
	
	//crea usuario si no existe
	private int crearUser(String correo) 
	{
		int idUser=0;
		ResultSet resultado = null;
		//validar si existe
		resultado=consultar("select count(*) as c from ECNR_OW.ecnr_usuarios where usr_usuario= '" + correo + "'");		
		try { 
				resultado.next();
				int c=resultado.getInt("c");
				if(resultado.getInt("c") > 0)
			{
				resultado=consultar("select USR_ID  from ECNR_OW.ecnr_usuarios where usr_usuario= '" + correo + "'");		
				resultado.next();
				idUser=resultado.getInt("USR_ID");
			}
			else
			{
				Boolean a=true;
				resultado=consultar("select CNT_CONTADOR as c from ECNR_OW.ecnr_contadores where cnt_tabla='USUARIOS'");		
				resultado.next();
				int id=resultado.getInt("c");
				while(a)
				{
					id=id +1;
					resultado=consultar("select count(*) as c from ECNR_OW.ecnr_usuarios where usr_id='" + id + "'");		
					resultado.next();
					if(resultado.getInt("c")==0)
					{
						a=false;
					}
				}				
				ejecutar("insert into ECNR_OW.ecnr_usuarios (USR_ID,USR_USUARIO,USR_PRF_ID) values ("+ id +",'" + correo + "',28)");
				resultado=consultar("select USR_ID  from ECNR_OW.ecnr_usuarios where usr_usuario= '" + correo + "'");		
				resultado.next();
				idUser=resultado.getInt("USR_ID");
				//actualizar contador
				ejecutar("update ECNR_OW.ecnr_contadores set cnt_contador = " + idUser + " where cnt_tabla='USUARIOS'");
							
			}
		} catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			errores+=" * Error al crear usuario";
			e.printStackTrace();
		}
		return idUser;
	}
	
	//crear presentacion
	private int crearPresentacion(String param, int idU,String serv)
	{
		int idpre=0;
		String cod="0000";
		String tipo="02";
		JSONObject jsonObject = new JSONObject(param); 
		String idSol = jsonObject.get("id").toString();
		ResultSet resultado = null;
		Boolean a=true;
		Calendar now = Calendar.getInstance();   // Gets the current date and time
		int year = now.get(Calendar.YEAR);
		
		if(serv.equals("001"))
		{
			cod="0001";
		}
		else if(serv.equals("002"))
		{
			cod="1101";
			tipo="TOBJ";
		}
		try{
			resultado=consultar("select CNT_VALOR as c from ECNR_OW.contador where cnt_nombre='PRE_ID'");		
			resultado.next();
			int id=resultado.getInt("C");
			String numPres="";
			while(a)
			{
				id=id +1;
				resultado=consultar("select count(*) as c from ECNR_OW.ecnr_presentaciones where pre_id='" + id + "'");		
				resultado.next();
				int count= resultado.getInt("C");
				if(count==0)
				{
					a=false;
				}
			}
			//obterner numero presentacion
			numPres="ME"+ year;
			resultado=consultar("select count(*) as c from ECNR_OW.contador where cnt_nombre='"+numPres+"'");
			resultado.next();
			int count= resultado.getInt("C");
			if(count==0)
			{
		    ejecutar("insert into ECNR_OW.contador(cnt_id,cnt_nombre,cnt_valor,cnt_descripcion) values((select max(cnt_id) from ECNR_OW.contador)+1,'"+numPres+"',1,'CONTADOR DE NUMERO ANUAL "+numPres+"')");
				//insertar nuevo si no existe
			}
			resultado=consultar("select cnt_valor as c from ECNR_OW.contador where cnt_nombre='"+numPres+"'");
			resultado.next();
			int corrNumPres= resultado.getInt("C") + 1;
			String formcorr = String.format("%06d", corrNumPres);
			//numPres= numPres + corrNumPres;
			//insertar presentacion
			ejecutar( "insert into ECNR_OW.ecnr_presentaciones(pre_id,pre_numero,sis_codigo,sus_codigo,cse_tipo,\n"
					 + "cse_codigo,pre_depto,pre_express,pre_envio_postal,pre_estado,pre_fecha_pres,usr_id,fec_crea)\n" 
				  	 + "values (" + id + ", '" + numPres + formcorr + "','"+serv+"',1,'"+ tipo+"','"+cod+"','06','N','N',1,CURRENT_DATE,"+idU+",CURRENT_DATE)");
			//actualizar contador			
			ejecutar("update ECNR_OW.contador set cnt_valor = " + id + " where cnt_nombre='PRE_ID'");
			ejecutar("update ECNR_OW.contador set cnt_valor = " + corrNumPres + " where cnt_nombre='"+ numPres +"'");
			idpre=id;			
		}
		catch (SQLException e) {
			e.printStackTrace();			
		}		
		return idpre;
	}
	
	//crear pre forma
	private String crearPreForma(String param,int idpre, int idU, String titulo) throws JSONException, IOException
	{		 
		Boolean a=true;
		String strxml="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
        strxml=strxml.concat("<servicioIntegralXML>");
		ResultSet resultado = null;
		JSONObject jsonObject = new JSONObject(param); 
		jsonObject = jsonObject.getJSONObject("request");
		//XML datos generales
		JSONObject genJs = generalXML(param);
		JSONObject conJs = constitucionXML(param);
		JSONObject matJs = matXML(param);
		String xml = XML.toString(genJs,"generalXML");		
		strxml=strxml.concat(xml);
		xml=XML.toString(conJs,"constitucionXML");
		strxml=strxml.concat(xml);
		xml=XML.toString(matJs,"matriculaXML");
		strxml=strxml.concat(xml); 
		strxml=strxml.concat("</servicioIntegralXML>");
		//validar si existe
		
		
		/*try
		{
			resultado=consultar("select CNT_VALOR as c from ECNR_OW.contador where cnt_nombre='FOR_ID'");		
			resultado.next();
			int id=resultado.getInt("C");
			while(a)
			{
				id=id +1;
				resultado=consultar("select count(*) as c from ECNR_OW.ecnr_pre_forma where for_id='" + id + "'");		
				resultado.next();
				int count= resultado.getInt("C");
				if(count==0)
				{
					a=false;
				}
			}
			//insertar forma
			ejecutar( "insert into ECNR_OW.ecnr_pre_forma (for_id,pre_id,for_version,for_xml,usu_crea,fec_crea)\n"
					+ " values ("+id+"," + idpre + ",0,'" + strxml + "',"+idU+",CURRENT_DATE)");
			//actualizar contador			
			ejecutar("update ECNR_OW.contador set cnt_valor = " + id + " where cnt_nombre='FOR_ID'");
			idpre=id;
			
		}
		catch(Exception e)
		{
			int r=0;
		}return idpre;*/
		return strxml;
	}
	
	//crear anexos
	private int crearAnexo(int idp,int idU,String tipo, String p) throws SQLException, IOException
	{
		Boolean a=true; 
		int idanexo=0;		
		DBOracle baseDatos = new DBOracle(); 
		baseDatos.conectar(); 
		ResultSet resultado = null;
		JSONObject jsonObject=new JSONObject(p);
		JSONArray pagosJA = jsonObject.getJSONObject("request").getJSONArray("payments");
		JSONArray docsJA = jsonObject.getJSONObject("request").getJSONArray("documentUploads");
		List<BufferedImage> images = new ArrayList<BufferedImage>(); 
		String miempresa="https://roxana1-els.eregistrations.org";//cambiar segun el port a utilizar		
		if(pagosJA.length()>0)
		{
			for (int i = 0; i <  pagosJA.length(); i++) 
			 {
				JSONObject item=pagosJA.getJSONObject(i);
				String type="";
				if(tipo.equals("constitucion"))
				{
					type="companyIncorporation";
				}
				if(tipo.equals("matricula"))
				{
					type="companyRegistration";					
				}
				System.out.println( tipo +"****************1" + item.getString("code"));
				if(item.getString("code").equals(type))
				{
					JSONArray urlsJS=item.getJSONArray("files");
					for(int i2 =0; i2< urlsJS.length(); i2 ++)
					{ 
						BufferedImage[] pages = null;
						JSONObject urlJo = urlsJS.getJSONObject(i2);
						URL url=new URL(miempresa + urlJo.getString("url"));
						String extension = "";
						int ind = url.getFile().lastIndexOf('.');
				        if (ind > 0) 
				        {
				            extension = url.getFile().substring(ind+1);
				        }				        
				        if(extension.toLowerCase().equals("pdf"))
				        {					        	 
				        	PDDocument pdf = new PDDocument();
				        	pages = new BufferedImage[pdf.getNumberOfPages()];
				        	pdf=PDDocument.load(url);
				        	for (int i3 = 0; i3 < pages.length; i3++) 
				            {
				                PDPage page = (PDPage) pdf.getDocumentCatalog().getAllPages().get(i);
				                BufferedImage imageP; 
				                try 
				                {
				                  imageP = page.convertToImage(BufferedImage.TYPE_INT_RGB, 288); //works
//				                  image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300); // does not work				                  
				                  images.add(imageP);						         
				                  
				                } catch (IOException e) 
				                {				                	
				                    e.printStackTrace();
				                }
				            }		        	
				        }
				        System.out.println(extension + "****************4");
				        if(extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg") || extension.toLowerCase().equals("png") )
				        {
				        	images.add(ImageIO.read(url));				        	
				        }
				        if(extension.toLowerCase().equals("tiff"))
				        {
				        	images.add(ImageIO.read(url));		    		
				        }						
					} 
					
				}
			 }
			
		}
		System.out.println("pagos");
		if(tipo.equals("constitucion"))
		{
			if(docsJA.length()>0)
			{
				for (int i = 0; i <  docsJA.length(); i++) 
				 {
					JSONObject item=docsJA.getJSONObject(i); 
					  
					if(item.getString("code").equals("companyDeed"))
					{
						JSONArray urlsJS=item.getJSONArray("files");
						for(int i2 =0; i2< urlsJS.length(); i2 ++)
						{
							
							JSONObject urlJo = urlsJS.getJSONObject(i2);
							URL url=new URL(miempresa + urlJo.getString("url"));
							String extension = "";
							int ind = url.getFile().lastIndexOf('.');
					        if (ind > 0) 
					        {
					            extension = url.getFile().substring(ind+1);
					        }
					        try{
					        if(extension.toLowerCase().equals("pdf"))
					        {	
					        	System.out.println("case pdf");
					        	PDDocument pdf = new PDDocument();
					        	pdf=PDDocument.load(url);
					        	BufferedImage[] pages = new BufferedImage[pdf.getNumberOfPages()];					        						        	
			                    System.out.println("size pdf " + pages.length);
					        	for (int i3 = 0; i3 < pages.length; i3++) 
					            {
					                PDPage page = (PDPage) pdf.getDocumentCatalog().getAllPages().get(i3);
					                BufferedImage imageP; 
					                try {
					                  imageP = page.convertToImage(BufferedImage.TYPE_INT_RGB, 288); //works
//					                    image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 300); // does not work
					                   
					                  images.add(imageP);
					                } catch (IOException e) {
					                    e.printStackTrace();
					                    System.out.println(" catch case pdf");
					                }
					            }
					        }
					        	
					        }
					        catch(Exception e)
					        {
					        	System.out.println("catch pdf");
					        }
					        if(extension.toLowerCase().equals("jpg") || extension.toLowerCase().equals("jpeg") || extension.toLowerCase().equals("png") )
					        {
					        	 images.add(ImageIO.read(url));	
					        	
					        }
					        if(extension.toLowerCase().equals("tiff"))
					        {
					        	 images.add(ImageIO.read(url));
					    		
					        }							
						}						
					}
				 }				
			}
		}
		
		BufferedImage[] imagenes = new BufferedImage[images.size()];
		System.out.println("size doc" + images.size());
		for(int n = 0; n < images.size(); n++)
		{
			imagenes[n] = images.get(n);
		}
		
		FileOutputStream fos = new FileOutputStream("C://Temp//a.tiff"); 
        System.out.println(" file saved in temp");
        RandomAccessOutputStream rout = new FileCacheRandomAccessOutputStream(fos);
        ImageParam.ImageParamBuilder builder = ImageParam.getBuilder();
        ImageParam[] param = new ImageParam[1];
        TIFFOptions tiffOptions = new TIFFOptions();
        tiffOptions.setTiffCompression(Compression.DEFLATE_ADOBE);
        builder.imageOptions(tiffOptions);
        builder.colorType(ImageColorType.GRAY_SCALE);//.ditherMatrix(DitherMatrix.getBayer8x8Default()).applyDither(true).ditherMethod(DitherMethod.FLOYD_STEINBERG);
        param[0] = builder.build();
        TIFFTweaker.writeMultipageTIFF(rout, param, imagenes);
        rout.close();
        fos.close();		
		resultado=consultar("select CNT_VALOR as c from ECNR_OW.contador where cnt_nombre='ANE_ID'");		
		resultado.next();
		int id=resultado.getInt("C");
		while(a)
		{
			id=id +1;
			resultado=consultar("select count(*) as c from ECNR_OW.ecnr_pre_anexo where ane_id='" + id + "'");		
			resultado.next();
			int count= resultado.getInt("C");
			if(count==0)
			{
				a=false;
			}
		}
		//insertar anexo        		
		PreparedStatement pstmt = baseDatos.getConexion().prepareStatement("insert into ECNR_OW.ecnr_pre_anexo(ane_id,pre_id,tan_id,ane_archivo,usu_crea,fec_crea) values (?, ?, ?, ?, ?, CURRENT_DATE)");
		InputStream in = new FileInputStream("C://Temp//a.tiff");
		pstmt.setInt(1, id);//id anexo
		pstmt.setInt(2, idp);//id presentacion
		int tan=9;
		if(tipo.equals("matricula"))
		{
			tan=10;
		}
		pstmt.setInt(3, tan);//tipo anexo
		pstmt.setBinaryStream(4, in);//archivo
		pstmt.setInt(5, idU);//id user
		pstmt.executeUpdate();
		baseDatos.getConexion().commit();		
		//actualizar contador
		ejecutar("update ECNR_OW.contador set cnt_valor = " + id + " where cnt_nombre='ANE_ID'");		
		return idanexo;
	}
	

 	//validar formato json 
	private boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) { 
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
		
	//genera formato xml de informacion general para pre_forma
	private JSONObject generalXML(String p) throws JSONException, IOException
	{	

		Catalogos catalogo= new Catalogos();
		JSONObject jsonObject = new JSONObject(p);
		JSONObject jsonObjectRes = new JSONObject();
		JSONObject servObj = new JSONObject();
		JSONArray jsonServ= jsonObject.getJSONObject("request").getJSONArray("registrations");
		String idSol = jsonObject.get("id").toString(); 		
		JSONArray jsonA = jsonObject.getJSONObject("request").getJSONArray("registrations");
		ArrayList<String> list = new ArrayList<String>();    
		for (int i = 0; i <  jsonA.length(); i++) 
			 {
				String cod=jsonA.getJSONObject(i).getString("code");
				list.add(cod);  			
			 }	
		servObj.append("code", list);
		jsonObjectRes.append("fechaIngreso",jsonObject.get("submittedTimestamp").toString());
		jsonObjectRes.append("fechaAprovacion",jsonObject.get("submittedTimestamp").toString());
		jsonObjectRes.append("emailSol","miempresa.gob.sv"); //jsonObject.getString("user"));
		jsonObjectRes.append("servicios",servObj);
		JSONObject dirNJson = new JSONObject();
		if(jsonObject.getJSONObject("request").getJSONObject("data").getBoolean("isNotificationAddressSame"))
		{
			JSONObject dir = jsonObject.getJSONObject("request").getJSONObject("data").getJSONObject("businessAddress");
			dirNJson.append("calle", dir.getString("street"));
			if(dir.has("building"))
			{
				dirNJson.append("edificio", dir.getString("building"));
			}
			if(dir.has("house"))
			{
				dirNJson.append("casa", dir.getString("house"));
			}
			dirNJson.append("colonia", dir.getString("borough"));
			if(dir.has("complement"))
			{
				dirNJson.append("complemento", dir.getString("complement"));
			}
			//String code=catalogo.getDepartamento(dir.getString("province"));
			String code=catalogo.getDepartamento("provinceSanSalvador");
			dirNJson.append("departamento", code); 
			code=catalogo.getMunicipio("townApaneca");
			dirNJson.append("municipio", code); 
			dirNJson.append("telefono", dir.getString("phone")); 
			if(dir.has("mobile"))
			{
				dirNJson.append("celular", dir.getString("mobile"));
			}			 
			if(dir.has("fax"))
			{
				dirNJson.append("fax", dir.getString("fax"));
			}			 
			dirNJson.append("email", dir.getString("personalEmail"));  
			jsonObjectRes.append("direccionNotificacion",dirNJson);
		}
		else
		{			
			JSONObject dir = jsonObject.getJSONObject("request").getJSONObject("data").getJSONObject("notificationAddress");
			dirNJson.append("calle", dir.getString("street"));
			if(dir.has("building"))
			{
				dirNJson.append("edificio", dir.getString("building"));
			}
			if(dir.has("house"))
			{
				dirNJson.append("casa", dir.getString("house"));
			}
			dirNJson.append("colonia", dir.getString("borough"));
			if(dir.has("complement"))
			{
				dirNJson.append("complemento", dir.getString("complement"));
			}
			dirNJson.append("departamento", dir.getString("province"));
			dirNJson.append("municipio", dir.getString("town"));
			dirNJson.append("telefono", dir.getString("phone")); 
			if(dir.has("mobile"))
			{
				dirNJson.append("celular", dir.getString("mobile"));
			}			 
			if(dir.has("fax"))
			{
				dirNJson.append("fax", dir.getString("fax"));
			}			 
			dirNJson.append("email", dir.getString("personalEmail"));
			jsonObjectRes.put("direccionNotificacion",dirNJson);
		}
		JSONObject assistJson = new JSONObject();//quien recoje los documentos
		if(!jsonObject.getJSONObject("request").getJSONObject("data").getBoolean("isSelfPickup"))
		{
			JSONArray asist = jsonObject.getJSONObject("request").getJSONObject("data").getJSONArray("assistants");
			assistJson.put("primerNombre", asist.getJSONObject(0).getString("firstName"));
			if(asist.getJSONObject(0).has("otherNames"))
			{
				assistJson.put("otrosNombres", asist.getJSONObject(0).getString("otherNames"));
			} 
			assistJson.put("primerApellido", asist.getJSONObject(0).getString("lastName"));
			if(asist.getJSONObject(0).has("otherLastNames"))
			{
				assistJson.put("otrosApellidos", asist.getJSONObject(0).getString("otherLastNames"));
			} 
			if(asist.getJSONObject(0).has("alias"))
			{
				assistJson.put("conocidoPor", asist.getJSONObject(0).getString("alias"));
			} 
			assistJson.put("tipoDoc", asist.getJSONObject(0).getJSONObject("idDocumentsChoice").getString("code"));
			if(asist.getJSONObject(0).has("residencyCardNumber"))
			{
				assistJson.put("numDoc", asist.getJSONObject(0).getString("residencyCardNumber"));
			}
			else if(asist.getJSONObject(0).has("passportNumber"))
			{
				assistJson.put("numDoc", asist.getJSONObject(0).getString("passportNumber"));
			}
			else if(asist.getJSONObject(0).has("duiNumber"))
			{
				assistJson.put("numDoc", asist.getJSONObject(0).getString("duiNumber"));
			}
			else
			{
				assistJson.put("numDoc","0");
			}
			assistJson.put("nit", asist.getJSONObject(0).getString("nitNumber"));			
			 
		}
		else
		{
			
			JSONArray asist = jsonObject.getJSONObject("request").getJSONObject("data").getJSONArray("representatives");
			JSONObject asistJ = new JSONObject();
			for (int i = 0; i <  asist.length(); i++) 
			 {
				if(asist.getJSONObject(i).getBoolean("isCompanyLegalRepresentative"))
				{
					asistJ=asist.getJSONObject(i);
				}
			 }
			
			assistJson.put("primerNombre", asistJ.getString("firstName"));
			if(asistJ.has("otherNames"))
			{
				assistJson.put("otrosNombres", asistJ.getString("otherNames"));
			} 
			assistJson.put("primerApellido", asistJ.getString("lastName"));
			if(asistJ.has("otherLastNames"))
			{
				assistJson.put("otrosApellidos", asistJ.getString("otherLastNames"));
			} 
			if(asistJ.has("alias"))
			{
				assistJson.put("conocidoPor", asistJ.getString("alias"));
			} 
			assistJson.put("tipoDoc", asistJ.getJSONObject("idDocumentsChoice").get("code"));
			if(asistJ.has("residencyCardNumber"))
			{
				assistJson.put("numDoc", asistJ.getString("residencyCardNumber"));
			}
			else if(asistJ.has("passportNumber"))
			{
				assistJson.put("numDoc", asistJ.getString("passportNumber"));
			}
			else if(asistJ.has("duiNumber"))
			{
				assistJson.put("numDoc", asistJ.getString("duiNumber"));
			}
			else
			{
				assistJson.put("numDoc","0");
			}
			assistJson.put("nit", asistJ.getString("nitNumber"));
		}
		jsonObjectRes.put("personaAutorizada",assistJson);		
		jsonObjectRes.append("id",idSol);
		
		return jsonObjectRes;
	}
	
	//genera formato xml de informacion de matricula para pre_forma
	private JSONObject matXML(String p)
	{
		JSONObject jsonObject = new JSONObject(p);
		JSONObject jsonObjectRes = new JSONObject();
		JSONObject jsonData = jsonObject.getJSONObject("request").getJSONObject("data");
		JSONObject jsonDir = jsonData.getJSONObject("businessAddress");
		
		jsonObjectRes.append("nombreNegocio",jsonData.getString("designation"));
		jsonObjectRes.append("codigoSector",jsonData.getString("businessSector"));
		
		JSONArray jsonA = jsonObject.getJSONObject("request").getJSONObject("data").getJSONArray("economicActivities");
		ArrayList<String> list = new ArrayList<String>();    
		for (int i = 0; i <  jsonA.length(); i++) 
			 {
				String cod=jsonA.getJSONObject(i).getString("code");
				list.add(cod);  			
			 }
		jsonObjectRes.append("codigoActividad",list);
		
		jsonObjectRes.append("nombreActividad",jsonData.getString("rangeOfActivity"));
		jsonObjectRes.append("departamento",jsonDir.getString("province"));
		jsonObjectRes.append("municipio",jsonDir.getString("town"));
		//direccion
		JSONObject dir = jsonObject.getJSONObject("request").getJSONObject("data").getJSONObject("businessAddress");
		JSONObject dirNJson = new JSONObject();
		dirNJson.append("calle", dir.getString("street"));
		if(dir.has("building"))
		{
			dirNJson.append("edificio", dir.getString("building"));
		}
		if(dir.has("house"))
		{
			dirNJson.append("casa", dir.getString("house"));
		}
		dirNJson.append("colonia", dir.getString("borough"));
		if(dir.has("complement"))
		{
			dirNJson.append("complemento", dir.getString("complement"));
		} 
		dirNJson.append("telefono", dir.getString("phone")); 
		if(dir.has("mobile"))
		{
			dirNJson.append("celular", dir.getString("mobile"));
		}			 
		if(dir.has("fax"))
		{
			dirNJson.append("fax", dir.getString("fax"));
		}			 
		dirNJson.append("email", dir.getString("personalEmail"));  
		jsonObjectRes.append("direccion",dirNJson);
		
		//sucursales
		JSONArray sucursales= new JSONArray();
		if(jsonData.getInt("branchCount")>0)
		{
			JSONArray sucJS = jsonData.getJSONArray("branches");
			if(sucJS.length()>0)
			{
				for (int i = 0; i <  sucJS.length(); i++) 
				 {
					JSONObject sucursal=new JSONObject();
					JSONObject item = sucJS.getJSONObject(i);
					sucursal.append("nombreNegocio", item.getString("companyName"));
					sucursal.append("tipoNegocio", item.getString("branchType"));
					
					//preguntar si es la misma de la empresa(pendiente)
					if(true)
					{
						sucursal.append("nombreActividad",jsonData.getString("rangeOfActivity"));
						sucursal.append("codigoActividad",list);
					}
					else
					{
						
					}
					//direccion
					JSONObject sucDirJ=item.getJSONObject("address");
					JSONObject dirSJson = new JSONObject();
					dirSJson.append("calle", sucDirJ.getString("street"));
					if(sucDirJ.has("building"))
					{
						dirSJson.append("edificio", sucDirJ.getString("building"));
					}
					if(sucDirJ.has("house"))
					{
						dirSJson.append("casa", sucDirJ.getString("house"));
					}
					dirSJson.append("colonia", sucDirJ.getString("borough"));
					if(sucDirJ.has("complement"))
					{
						dirSJson.append("complemento", sucDirJ.getString("complement"));
					} 
					dirSJson.append("telefono", sucDirJ.getString("phone")); 
					if(sucDirJ.has("mobile"))
					{
						dirSJson.append("celular", sucDirJ.getString("mobile"));
					}			 
					if(sucDirJ.has("fax"))
					{
						dirSJson.append("fax", sucDirJ.getString("fax"));
					}			 
					dirSJson.append("email", sucDirJ.getString("personalEmail"));  
					sucursal.append("direccion",dirSJson);					
					sucursales.put(sucursal);
					
				 }
				jsonObjectRes.append("sucursales", sucursales);
			}
		}
		
		
		
		return jsonObjectRes;
	}
	
	//genera formato xml de informacion de consticion para pre_forma
	private JSONObject constitucionXML(String p)
	{
		JSONObject jsonObjectRes = new JSONObject();
		JSONObject jsonObject = new JSONObject(p);
		JSONObject jsonData = jsonObject.getJSONObject("request").getJSONObject("data");
		//tipo sociedad
		String ts = jsonData.getJSONObject("companyType").getString("code");
		String tsr = "S14";
		if(ts.equals("sa"))
		{
			tsr="S01";
		}
		if(ts.equals("sacv"))
		{
			tsr="S02";
		}
		if(ts.equals("srl"))
		{
			tsr="S09";
		}
		if(ts.equals("srlcv"))
		{
			tsr="S10";
		}
		jsonObjectRes.append("tipoSociedad",tsr);
		jsonObjectRes.append("denominacion",jsonData.getString("designation"));
		if(jsonData.has("abbreviation"))
		{
			jsonObjectRes.append("abreviatura",jsonData.getString("abbreviation"));
		}
		jsonObjectRes.append("departamento","0");
		jsonObjectRes.append("municipio","0");
		jsonObjectRes.append("fechaEscritura","0");				
		jsonObjectRes.append("pais",jsonData.getJSONObject("registrationCountry").getString("code"));
		//tipo administracion
		String ta=jsonData.getJSONObject("managementType").getString("code");
		String tar="AD";
		if(ta.equals("board"))
		{
			tar="JD";
		}
		jsonObjectRes.append("tipoAdministracion",tar);
		jsonObjectRes.append("periodoAdministracion",jsonData.getString("managementDuration"));
		jsonObjectRes.append("miembrosAdministracion",jsonData.getJSONArray("representatives").length());
		jsonObjectRes.append("capitalSocial",jsonData.getDouble("shareCapital"));
		//jsonObjectRes.append("capitalMinimo",jsonData.getString("user"));
		
		JSONArray asist = jsonObject.getJSONObject("request").getJSONObject("data").getJSONArray("representatives");
		JSONObject asistJ = new JSONObject();
		JSONObject socioNJ = new JSONObject();
		//representate legal
	 	JSONArray repres= new JSONArray();
	 	ArrayList<JSONObject> list = new ArrayList<JSONObject>();  
		for (int i = 0; i <  asist.length(); i++) 
		 {
			if(asist.getJSONObject(i).getBoolean("isCompanyLegalRepresentative"))
			{
				JSONObject repre=new JSONObject();
				asistJ=asist.getJSONObject(i);
				repre.put("primerNombre", asistJ.getString("firstName"));
				if(asistJ.has("otherNames"))
				{
					repre.put("otrosNombres", asistJ.getString("otherNames"));
				} 
				repre.put("primerApellido", asistJ.getString("lastName"));
				if(asistJ.has("otherLastNames"))
				{
					repre.put("otrosApellidos", asistJ.getString("otherLastNames"));
				} 
				if(asistJ.has("alias"))
				{
					repre.put("conocidoPor", asistJ.getString("alias"));
				} 
				repre.put("tipoDoc", asistJ.getJSONObject("idDocumentsChoice").get("code"));
				if(asistJ.has("residencyCardNumber"))
				{
					repre.put("numDoc", asistJ.getString("residencyCardNumber"));
				}
				else if(asistJ.has("passportNumber"))
				{
					repre.put("numDoc", asistJ.getString("passportNumber"));
				}
				else if(asistJ.has("duiNumber"))
				{
					repre.put("numDoc", asistJ.getString("duiNumber"));
				}
				else
				{
					repre.put("numDoc","0");
				}
				repre.put("nit", asistJ.getString("nitNumber"));
				if(asistJ.has("legalRepresentativePosition"))
				{
					repre.put("cargo", asistJ.getString("legalRepresentativePosition"));
				}
				jsonObjectRes.append("representateLegal", repre);
			}	
			       
				 	JSONObject repre=new JSONObject();
					asistJ=asist.getJSONObject(i);
					repre.put("primerNombre", asistJ.getString("firstName"));
					if(asistJ.has("otherNames"))
					{
						repre.put("otrosNombres", asistJ.getString("otherNames"));
					} 
					repre.put("primerApellido", asistJ.getString("lastName"));
					if(asistJ.has("otherLastNames"))
					{
						repre.put("otrosApellidos", asistJ.getString("otherLastNames"));
					} 
					if(asistJ.has("alias"))
					{
						repre.put("conocidoPor", asistJ.getString("alias"));
					} 
					repre.put("tipoDoc", asistJ.getJSONObject("idDocumentsChoice").get("code"));
					if(asistJ.has("residencyCardNumber"))
					{
						repre.put("numDoc", asistJ.getString("residencyCardNumber"));
					}
					else if(asistJ.has("passportNumber"))
					{
						repre.put("numDoc", asistJ.getString("passportNumber"));
					}
					else if(asistJ.has("duiNumber"))
					{
						repre.put("numDoc", asistJ.getString("duiNumber"));
					}
					else
					{
						repre.put("numDoc","0");
					}
					repre.put("nit", asistJ.getString("nitNumber"));
					if(asistJ.has("legalRepresentativePosition"))
					{
						repre.put("cargo", asistJ.getString("legalRepresentativePosition"));
					}
					if(asistJ.has("sharesAmount"))
					{
						repre.put("cantidadAcciones", asistJ.getInt("sharesAmount"));
					}
					//jsonObjectRes.append("socioNatural", repre); 
					repres.put(repre);
					list.add(repre);
		 }
		if(repres.length() > 0)
		{
			jsonObjectRes.append("socioNatural", repres); 
		}
		
		if(jsonData.has("partners"))
		{
			JSONArray jurid = jsonObject.getJSONObject("request").getJSONObject("data").getJSONArray("partners");
			JSONObject juridJ = new JSONObject();
			list.clear();
		    JSONArray jurds= new JSONArray();
			for (int i = 0; i <  jurid.length(); i++) 
			 {
				JSONObject repre=new JSONObject();
				juridJ=jurid.getJSONObject(i);
				ts=juridJ.getJSONObject("partnerType").getString("code");
				tsr = "S14";
				if(ts.equals("sa"))
				{
					tsr="S01";
				}
				if(ts.equals("sacv"))
				{
					tsr="S02";
				}
				if(ts.equals("srl"))
				{
					tsr="S09";
				}
				if(ts.equals("srlcv"))
				{
					tsr="S10";
				}
				repre.put("tipoSociedad", tsr);
				repre.put("denominacion", juridJ.getString("designation"));
				repre.put("nit", juridJ.getString("nitNumber"));
				repre.put("cantidadAcciones", juridJ.getInt("sharesAmount"));
				jurds.put(repre);
				
			 }
			if(jurds.length() > 0)
			{
				jsonObjectRes.append("socioJuridico", jurds);
			}		
			
		}
		
		
		
		
		
		return jsonObjectRes;
	}

	//genera formato xml de informacion de consticion para pre_forma ***PENDIENTE***
	private JSONObject balXML(String p)
	{
		JSONObject jsonObject = new JSONObject();
		return jsonObject;
	}


	
	
}

	

    
