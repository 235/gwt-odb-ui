package net.pleso.odbui.server;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;

public class GetDataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("application/json");
		OutputStream outputStream = response.getOutputStream();
		
		// Create a new query
		String queryString = 
					 " SELECT ?s ?p ?o " + 
					 " WHERE { ?s ?p ?o } ";

		Query query = QueryFactory.create(queryString) ;
		
		Store store = SDBFactory
		.connectStore(SDBStoreDescUtils.loadDefault());
        
        Dataset ds = SDBFactory.connectDataset(store) ;
        QueryExecution qe = QueryExecutionFactory.create(query, ds) ;
        try {
            ResultSet rs = qe.execSelect() ;
            ResultSetFormatter.outputAsJSON(outputStream, rs);
        } finally { 
        	qe.close();
        	store.getConnection().close();
        	store.close();
        	outputStream.close();
        }
	}

}
