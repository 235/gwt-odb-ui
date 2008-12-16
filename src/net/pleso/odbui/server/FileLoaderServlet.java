package net.pleso.odbui.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

import com.hp.hpl.jena.rdf.model.InfModel;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ValidityReport;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;
import com.hp.hpl.jena.util.FileManager;

public class FileLoaderServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		InputStream in = FileManager.get().open("net/pleso/odbui/server/datafiles/odbui-sampledata.json");
		if (in == null) {
		    throw new IllegalArgumentException("Input file not found");
		}
		
		String line = null;
		BufferedReader inreader = new BufferedReader(new InputStreamReader(in));
		StringBuffer buffer = new StringBuffer();
		while ((line = inreader.readLine()) != null) {
			buffer.append(line);
		}
		
		Model model = ModelFactory.createDefaultModel();
		ClientJSONParser parser = new ClientJSONParser();
		try {
			parser.parse(model, buffer.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			new ServletException(e);
		}
		saveDataToDatabase(model);
	}

//	protected void doGet(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		// create an empty model
//		Model model = loadModelFromFile("net/pleso/odbui/server/datafiles/odbui-sampledata.rdf");
//		Model schema = loadModelFromFile("net/pleso/odbui/server/datafiles/odbui.rdfs");
//		
//		Iterator validityResults = validateRDFS(model, schema);
//		if (validityResults != null) {
//			String validityException = null;
//			for (Iterator i = validityResults; i.hasNext(); ) {
//				validityException = validityException + " - " + i.next() + "\n";
//	        }
//			throw new ServletException("RDFS validity exception: \n" + validityException);
//		}
//		
//		// Create a new query
//		 String queryString = 
//					 " SELECT ?s ?p ?o " + 
//					 " WHERE { ?s ?p ?o } ";
//
//		Query query = QueryFactory.create(queryString);
//
//		QueryExecution qe = QueryExecutionFactory.create(query, model);
//		ResultSet results = qe.execSelect();
//		
//		response.setContentType("application/json");
//        OutputStream outputStream = response.getOutputStream();
//
//		// Output query results	
//		ResultSetFormatter.outputAsJSON(outputStream, results);
//
//		// Important - free up resources used running the query
//		qe.close(); 
//		
//		saveDataToDatabase(model);
//	}
	
	private Iterator validateRDFS(Model model, Model schema) {
		InfModel infmodel = ModelFactory.createRDFSModel(model, schema);
	    ValidityReport validity = infmodel.validate();
	    if (validity.isValid()) {
	       return null;
	    } else {
	       return validity.getReports();
	    }
		
	}

	private Model loadModelFromFile(String fileName){
		// create an empty model
		 Model model = ModelFactory.createDefaultModel();
		 
		// use the FileManager to find the input file
		InputStream in = FileManager.get().open(fileName);
		if (in == null) {
		    throw new IllegalArgumentException("Input file not found");
		}
		 
		// read the RDF/XML file
		model.read(in, "");
		
		return model;
	}
	
	private void saveDataToDatabase(Model model) throws IOException{
		Store store = SDBFactory.connectStore(SDBStoreDescUtils.loadDefault());
		store.getTableFormatter().truncate();

		Model in_model = SDBFactory.connectDefaultModel(store);
		in_model.add(model);
		
		in_model.close();
		store.getConnection().close();
		store.close();
	}
}
