package model.logic;



import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import javafx.scene.shape.Line;
import model.data_structures.LinearProbingHT;
import model.data_structures.ListaEncadenada;
import model.data_structures.Node1;
import model.data_structures.Ordenamientos;
import model.data_structures.ArregloDinamico;
import model.data_structures.Comparendo;
import model.data_structures.SeparateChainingHT;
import model.data_structures.codigoInfraccion; 

/**
 * Definicion del modelo del mundo
 *
 */
public class Modelo
{
	/**
	 * Atributos del modelo del mundo
	 */

	private LinearProbingHT<String ,Comparendo> datosLinearProbing;
	private SeparateChainingHT<String, Comparendo> datosSeparateChaining;

	/**
	 * Constructor del modelo del mundo con capacidad dada
	 * @param tamano
	 */
	public Modelo()
	{
		datosLinearProbing= new LinearProbingHT<>();
		datosSeparateChaining= new SeparateChainingHT<>();
	}



	public Comparendo[] cargarInfo() throws ParseException{
		String llaveUltimoelemento="";
		Comparendo []res = new Comparendo[2]; 

		try {
			////// tesing
			Gson gson = new Gson();

			String path = "./data/comparendos_dei_2018_small.geojson";
			JsonReader reader;

			List<String> lista = new ArrayList<String>();

			reader = new JsonReader(new FileReader(path));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonArray ja = elem.getAsJsonObject().get("features").getAsJsonArray();
			SimpleDateFormat parser = new SimpleDateFormat("yyyy/MM/dd");
			for(JsonElement e: ja)
			{
				int id = e.getAsJsonObject().get("properties").getAsJsonObject().get("OBJECTID").getAsInt();
				String fecha= e.getAsJsonObject().get("properties").getAsJsonObject().get("FECHA_HORA").getAsString();
				String medio = e.getAsJsonObject().get("properties").getAsJsonObject().get("MEDIO_DETE").getAsString();
				String Clasevehi= e.getAsJsonObject().get("properties").getAsJsonObject().get("CLASE_VEHI").getAsString();
				String tipoServicio = e.getAsJsonObject().get("properties").getAsJsonObject().get("TIPO_SERVI").getAsString();
				String Infraccion =e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
				String DescInfra=e.getAsJsonObject().get("properties").getAsJsonObject().get("DES_INFRAC").getAsString();
				String Localidad = e.getAsJsonObject().get("properties").getAsJsonObject().get("LOCALIDAD").getAsString();


				Comparendo user = new Comparendo(id,fecha, medio, Clasevehi, tipoServicio, Infraccion, DescInfra, Localidad );
				datosLinearProbing.put(fecha+Clasevehi+Infraccion, user);
				datosSeparateChaining.put(fecha+Clasevehi+Infraccion, user);
				
				llaveUltimoelemento = fecha+Clasevehi+Infraccion;
			}
			System.out.println(Arrays.toString(lista.toArray()));
			res[0] = datosLinearProbing.get(darLlavePrimerComparendo());
			res[1] = datosLinearProbing.get(llaveUltimoelemento);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res;
	}
	public String darLlavePrimerComparendo(){
		String res ="";
		try {
			////// tesing
			Gson gson = new Gson();

			String path = "./data/comparendos_dei_2018_small.geojson";
			JsonReader reader;

			List<String> lista = new ArrayList<String>();

			reader = new JsonReader(new FileReader(path));
			JsonElement elem = JsonParser.parseReader(reader);
			JsonElement e = elem.getAsJsonObject().get("features").getAsJsonArray().get(0);

			String fecha= e.getAsJsonObject().get("properties").getAsJsonObject().get("FECHA_HORA").getAsString();
			String Clasevehi= e.getAsJsonObject().get("properties").getAsJsonObject().get("CLASE_VEHI").getAsString();
			String Infraccion =e.getAsJsonObject().get("properties").getAsJsonObject().get("INFRACCION").getAsString();
			res = fecha+Clasevehi+Infraccion;

			System.out.println(Arrays.toString(lista.toArray()));

		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;

	}
	public ArrayList<Comparendo> darComparendosFeClaInfSeparateChaning(String llave){
		ArrayList<Comparendo> res = new ArrayList<Comparendo>();
		ListaEncadenada<String, Comparendo> comparendos = datosSeparateChaining.darListaEncadenadaCompleta(llave);
		Iterable<Node1<String, Comparendo>> iterador = comparendos.keys1();
		Iterator<Node1<String, Comparendo>> iter = iterador.iterator();
		while(iter.hasNext()){
			Node1<String, Comparendo> nodo2 = iter.next();
			res.add(nodo2.val);
		}
		Ordenamientos.sortMerge(res, 0, res.size()-1);
		return res;
	}
	
	public int darTamaniotablaLinear(){return datosLinearProbing.darTamaniotabla();}
	public int darNumeroElementosLinear(){return datosLinearProbing.darNumeroElementos();}
	public int darTamaniotablaSeparate(){return datosSeparateChaining.darTamaniotabla();}
	public int darNumeroElementosSeparate(){return datosSeparateChaining.darNumeroElementos();}
	
	public boolean existeLlaveLinearProbing(String key)
	{
		return datosLinearProbing.contains(key);
	}
	
public ArrayList<Comparendo> buscarPorKeyLinearProbing(String key)
	{
		ArrayList<Comparendo> retorno= new ArrayList<>();
		LinearProbingHT<String ,Comparendo> copia=datosLinearProbing;
		Comparendo actual= copia.get(key);
		while(actual!=null)
		{
			retorno.add(actual);
			copia.delete(key);
			actual=copia.get(key);
		}	
		Ordenamientos.sortMerge(retorno, 0,retorno.size()-1);
		
		return retorno;
	}
	
	public Comparator<Comparendo> darComparador(String caracteristicaComparable){

		if(caracteristicaComparable.equals("ID"))
		{

			Comparator<Comparendo> ID = new Comparator<Comparendo>()
			{
				@Override
				public int compare(Comparendo o1, Comparendo o2) 
				{
					if(o1.darID()<o2.darID())return -1;
					else if (o1.darID()>o2.darID())
						return 1;
					return 0;	
				}
			};
			return ID;

		}
		else return null;
	}
}


















