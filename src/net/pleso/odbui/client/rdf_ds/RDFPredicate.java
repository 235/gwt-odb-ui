package net.pleso.odbui.client.rdf_ds;

import com.google.gwt.json.client.JSONObject;

public class RDFPredicate {

	protected static final String typeAttributeName = "type";
	private static final String valueAttributeName = "value";
	private static final String typeAttributeValue = "uri";

	private RDFNamespace namespace;
	private String name;
	private String shortName;

	public RDFPredicate(RDFDataSet rdfDataSet, JSONObject jsonObject) {
		if (!(jsonObject.get(typeAttributeName).isString().stringValue()).equals(typeAttributeValue))
			throw new IllegalArgumentException(jsonObject.toString() + " type attribute must have type '" + typeAttributeValue + "'.");
		
		this.name = jsonObject.get(valueAttributeName).isString().stringValue();
		int index = this.name.lastIndexOf("/");
		this.shortName = this.name.substring(index + 1);
		String namespace = this.name.substring(0, index);
		
		this.namespace = rdfDataSet.smartGetRDFNamespace(new RDFNamespace(namespace));
	}
	
	public RDFPredicate(RDFDataSet rdfDataSet, String uri) {
		this.name = uri;
		int index = this.name.lastIndexOf("/");
		this.shortName = this.name.substring(index + 1);
		String namespace = this.name.substring(0, index);
		
		this.namespace = rdfDataSet.smartGetRDFNamespace(new RDFNamespace(namespace));
	}

	public String getName() {
		return name;
	}
	
	public String getShortName() {
		return shortName;
	}

	public RDFNamespace getNamespace() {
		return namespace;
	}
	
	public String toString() {
		return "{ \"type\": \"uri\" , " 
				+ "\"value\": \"" + this.name + "\" }";
	}
	
}
