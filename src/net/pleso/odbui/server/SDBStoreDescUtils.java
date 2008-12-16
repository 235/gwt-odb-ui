package net.pleso.odbui.server;

import java.io.IOException;
import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sdb.StoreDesc;

public class SDBStoreDescUtils {
	
	/**
	 * Load store description from resource in N3 format
	 * @param resource Path to resource in N3 format
	 * @return StoreDesc instance
	 * @throws IOException
	 */
	public static StoreDesc load(String resource) throws IOException {
		Model model = ModelFactory.createDefaultModel();
		InputStream stream = Resources.getResourceAsStream(resource);
		model.read(stream, "", "N3");
		return StoreDesc.read(model);
	}
	
	public static StoreDesc loadDefault() throws IOException {
		return load(getDefaultConfigFile());
	}

	public static String getDefaultConfigFile(){
		return "net/pleso/odbui/server/sdb-pgsql.ttl";
	}
}
