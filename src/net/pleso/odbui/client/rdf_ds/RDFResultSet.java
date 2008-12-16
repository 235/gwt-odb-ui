package net.pleso.odbui.client.rdf_ds;

import com.google.gwt.json.client.JSONObject;

public class RDFResultSet {
	
	public static final String subjectAttributeName = "s";
	public static final String predicateAttributeName = "p";
	public static final String objectAttributeName = "o";
	
	private RDFDataSet dataSet;
	
	public RDFResultSet(JSONObject jsonObject) {
		this.dataSet = new RDFDataSet(jsonObject.get("results").isObject().get("bindings").isArray());
	}
	
	public RDFResultSet() {
		this.dataSet = new RDFDataSet();
	}

	public RDFDataSet getDataSet() {
		return dataSet;
	}
	
	public String toString() {
		return  "{ \"head\": { \"vars\": [ \"" 
				+ subjectAttributeName + "\" , \"" 
				+ predicateAttributeName + "\" , \"" 
				+ objectAttributeName + "\" ] } , \"results\": { \"bindings\": "
				+ this.dataSet.toString()
				+ "}}";
	}
}
