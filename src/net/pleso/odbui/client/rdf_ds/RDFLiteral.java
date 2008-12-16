package net.pleso.odbui.client.rdf_ds;

import com.google.gwt.json.client.JSONObject;


public class RDFLiteral extends RDFValue {

	private static final String typeAttributeValue = "literal";
	
	private String xmlLangAttributeName = "xml:lang";
	private String xmlLangAttrubuteValue;
	
	public RDFLiteral(JSONObject jsonObject) {
		super(jsonObject);
		
		if (jsonObject.containsKey(xmlLangAttributeName))
			this.xmlLangAttrubuteValue = jsonObject.get(xmlLangAttributeName).isString().stringValue();
	}
	
	public RDFLiteral(String value, String type) {
		super(value, type);
	}
	
	public boolean acceptsType() {
		return RDFLiteral.acceptsType(this.getType());
	}
	
	public static boolean acceptsType(String typeName) {
		return typeName.equals(RDFLiteral.typeAttributeValue);
	}

	public RDFLiteral isRDFLiteral() {
		return this;
	}
	
	public String toString() {
		String val = escape(this.getValue());
		return "{ \"type\": \"literal\" , " 
				+ (this.xmlLangAttrubuteValue != null ? "\"xml:lang\": \"" + this.xmlLangAttrubuteValue + "\" , " : "") 
				+ "\"value\": \"" + val + "\" }";
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
