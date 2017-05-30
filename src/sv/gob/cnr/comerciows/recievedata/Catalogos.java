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
		String result="06"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) {
	        // buffer.append(line);
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("province");
	    result=jsonDep.getString(code);	 
		return result;
	}
	public String getSexo(String code)  throws IOException 
	{
		String result="06"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) {
	        // buffer.append(line);
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("sex");
	    result=jsonDep.getString(code);	 
		return result;
	}
	public String getMunicipio(String code)  throws IOException 
	{
		String result="06"; 
		String json = "";
	    InputStream s =Catalogos.class.getResourceAsStream("/catalogo.txt");  	    
	    BufferedReader br = new BufferedReader(new InputStreamReader(s));
	    String content = "";	 
	    while ((content = br.readLine()) != null) {
	        // buffer.append(line);
	    	json += content;
	    }	    
	    
	    JSONObject jsonObject =new JSONObject(json);
	    JSONObject jsonDep= jsonObject.getJSONObject("town");
	    result=jsonDep.getString(code);	 
		return result;
	}
	public String matchDepartamento(String code)
	{
		String result="06";

   switch(code) {
   case "provinceAhuachapan" :
       result="01";
      break; 
   
   case "provinceSantaAna" :      
      result="02";
      break;  
    case "provinceSonsonate" :      
      result="03";
      break; 
    case "provinceChalatenango" :      
      result="04";
      break; 
    case "provinceLaLibertad" :      
      result="05";
      break; 
    case "provinceSanSalvador" :      
      result="06";
      break; 
    case "provinceCuscatlan" :      
      result="07";
      break; 
    case "provinceLaPaz" :      
      result="08";
      break; 
    case "provinceCabanas" :      
      result="09";
      break; 
    case "provinceSanVicente" :      
      result="10";
      break; 
    case "provinceUsulutan" :      
      result="11";
      break; 
    case "provinceSanMiguel" :      
      result="12";
      break; 
    case "provinceMorazan" :      
      result="13";
      break; 
    case "provinceLaUnion" :      
      result="14";
      break;    
    
   default :  
      result="06";


	}

	return result;


	}

	public String matchSexo(String code)
	{
		String result="0602";
		switch(code) 
		{ 

		//ahuchapan	
			case "townAhuachapan" :      
      			result="0101";
      			break; 
			case "townApaneca" :      
      			result="0102";
      			break; 
			case "townAtiquizaya" :      
      			result="0103";
      			break;
			case "townConcepcionDeAtaco" :      
      			result="0104"; 
      			break; 
			case "townElRefugio" :      
      			result="0105";
      			break; 
			case "townGuaymango" :      
      			result="0106";
      			break; 
			case "townJujutla" :      
      			result="0107";
      			break; 
			case "townSanFranciscoMenendez" :      
      			result="0108";
      			break; 
			case "townSanLorenzoAhuachapan" :      
      			result="0109";
      			break; 
			case "townSanPedroPuxtla" :      
      			result="0110";
      			break; 
			case "townTacuba" :      
      			result="0111";
      			break; 
			case "townTurin" :      
      			result="0112";
      			break; 

      	//fin ahuchapan 
      	//santa ana 
			case "townCandelariaDeLaFrontera" :      
      			result="0112";
      			break; 
			case "townCoatepeque" :      
      			result="0112";
      			break; 
			case "townChalchuapa" :      
      			result="0112";
      			break; 
			case "townElCongo" :      
      			result="0112";
      			break; 
			case "townElPorvenir" :      
      			result="0112";
      			break; 
			case "townMasahuat" :      
      			result="0112";
      			break; 
			case "townMetapan" :      
      			result="0112";
      			break; 
			case "townSanSntonioPajonal" :      
      			result="0112";
      			break; 
			case "townSanSebastianSalitrillo" :      
      			result="0112";
      			break; 
			case "townSantaAna" :      
      			result="0112";
      			break; 
			case "townSantaRosaGuachipilin" :      
      			result="0112";
      			break; 
			case "townSantiagoDeLaFrontera" :      
      			result="0112";
      			break; 
			case "townTexistepeque" :      
      			result="0112";
      			break;      			
      	//fin santa ana 

      	//sonsonate
			case "townAcajutla" :      
      			result="0301";
      			break;  
			case "townArmenia" :      
      			result="0302";
      			break; 
			case "townCaluco" :      
      			result="0303";
      			break; 
			case "townCuisnahuat" :      
      			result="0304";
      			break; 
			case "townSantaIsabelIshuatan" :      
      			result="0305";
      			break; 
			case "townIzalco" :      
      			result="0306";
      			break; 
			case "townJuayua" :      
      			result="0307";
      			break; 
			case "townNahuizalco" :      
      			result="0308";
      			break; 
			case "townNahulingo" :      
      			result="0309";
      			break; 
			case "townSalcoatitan" :      
      			result="0310";
      			break; 
			case "townSanSntonioDelMonte" :      
      			result="0311";
      			break; 
			case "townSanJulian" :      
      			result="0312";
      			break; 
			case "townSantaCatarinaMasahuat" :      
      			result="0313";
      			break; 
			case "townSantoDomingoSonsonate" :      
      			result="0314";
      			break; 
			case "townSonsonate" :      
      			result="0315";
      			break; 
			case "townSonzacate" :      
      			result="0316";
      			break; 
      	//fin sonsonate

      	//chalatenango
			case "townAguaCaliente" :      
      			result="0401";
      			break; 
			case "townArcatao" :      
      			result="0402";
      			break; 
			case "townAzacualpa" :      
      			result="0403";
      			break; 
			case "townCitala" :      
      			result="0404";
      			break; 
			case "townComalapa" :      
      			result="0405";
      			break; 
			case "townConcepcionQuezaltepeque" :      
      			result="0406";
      			break; 
			case "townChalatenango" :      
      			result="0407";
      			break; 
			case "townDulceNombreDeMaria" :      
      			result="0408";
      			break; 
			case "townElCarrizal" :      
      			result="0409";
      			break; 
			case "townElParaiso" :      
      			result="0410";
      			break; 
			case "townLaLaguna" :      
      			result="0411";
      			break; 
			case "townLaPalma" :      
      			result="0412";
      			break; 
			case "townLaReina" :      
      			result="0413";
      			break; 
			case "townLasVueltas" :      
      			result="0414";
      			break; 
			case "townNombreDeJesus" :      
      			result="0415";
      			break; 
			case "townNuevaConcepcion" :      
      			result="0416";
      			break; 
			case "townNuevaTrinidad" :      
      			result="0417";
      			break; 
			case "townOjosDeAgua" :      
      			result="0418";
      			break; 
			case "townPotonico" :      
      			result="0419";
      			break ;
			case "townSanAntonioDeLaCruz" :      
      			result="0420";
      			break ;
			case "townSanAntonioLosRanchos" :      
      			result="0421";
      			break ;
			case "townSanFernandoChalatenango" :      
      			result="0422";
      			break ;
			case "townSanFranciscoLempa" :      
      			result="0423";
      			break ;
			case "townSanFranciscoMorazan" :      
      			result="0424";
      			break ;
			case "townSanIgnacio" :      
      			result="0425";
      			break ;
			case "townSanIsidroLabrador" :      
      			result="0426";
      			break ;
			case "townSanJoseCancasque" :      
      			result="0427";
      			break ;
			case "townSanJoseLasFlores" :      
      			result="0428";
      			break ;
			case "townSanLuisDelCarmen" :      
      			result="0429";
      			break ;
			case "townSanMiguelDeMercedes" :      
      			result="0430";
      			break ;
			case "townSanRafael" :      
      			result="0431";
      			break ;
			case "townSantaRita" :      
      			result="0432";
      			break ;
			case "townTejutla" :      
      			result="0433";
      			break ;
      	//fin chalatenango

      	//la libertad
      	//fin la libertad

      	//san salvador
      	//fin san salvador

      	//cuscatlan
      	//fin cuscatlan

      	//la paz
      	//fin la paz

      	//cabañas
      	//fin cabañas

      	//san vicente
      	//fin san vicente

        //usulutan
      	//fin usulutan

      	//san miguel
      	//fin san miguel

      	//morazan
      	//fin morazan

      	//la union
      	//fin la union




    
  		 default :  
     		 result="0602";
		}


return result;

	}
}