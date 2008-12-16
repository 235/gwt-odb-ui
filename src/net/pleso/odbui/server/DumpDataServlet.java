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
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;

public class DumpDataServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/plain");
		OutputStream outputStream = response.getOutputStream();
		
		// Create a new query
		String queryString = 
					 " PREFIX odbui: <http://pleso.net/schemas/odbui#> " +
					 " CONSTRUCT { ?s ?p ?o }" + 
					 " WHERE { ?s ?p ?o } ";

		Query query = QueryFactory.create(queryString) ;
		
		Store store = SDBFactory
		.connectStore(SDBStoreDescUtils.loadDefault());
        
        Dataset ds = SDBFactory.connectDataset(store) ;
        QueryExecution qe = QueryExecutionFactory.create(query, ds) ;
        try {
            Model rs = qe.execConstruct();
            rs.write(outputStream, "RDF/XML");
        } finally { 
        	qe.close();
        	store.getConnection().close();
        	store.close();
        	outputStream.close();
        }
	}

}
