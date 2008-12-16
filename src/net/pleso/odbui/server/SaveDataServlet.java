package net.pleso.odbui.server;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sdb.SDBFactory;
import com.hp.hpl.jena.sdb.Store;

public class SaveDataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Store store = SDBFactory.connectStore(SDBStoreDescUtils.loadDefault());
		store.getTableFormatter().truncate();

		String inputJSON = getReportParamValue(request, "data");
		
		PrintWriter out = response.getWriter();

		Model model = SDBFactory.connectDefaultModel(store);
		
		try {
			try {
				ClientJSONParser parser = new ClientJSONParser();
				parser.parse(model, inputJSON);
			} catch (Exception e) {
				e.printStackTrace();
				new ServletException(e);
			}

		} finally {
			model.write(out);
			model.close();
			store.getConnection().close();
			store.close();
			out.close();
		}
	}

	private String getReportParamValue(HttpServletRequest request,
			String paramName) throws IllegalArgumentException {
		// Get the value of a request parameter; the name is case-sensitive
		String value = request.getParameter(paramName);
		if (value == null) {
			throw new IllegalArgumentException("The request parameter '"
					+ paramName + "' was not present in the POST request");
		} else if ("".equals(value)) {
			throw new IllegalArgumentException("The request parameter '"
					+ paramName
					+ "' was present in the query string but has no value");
		}
		return value;
	}

}
