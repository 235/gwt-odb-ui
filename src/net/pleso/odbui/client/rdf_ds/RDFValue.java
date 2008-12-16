package net.pleso.odbui.client.rdf_ds;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;

public abstract class RDFValue {

	private static final String typeAttributeName = "type";
	private static final String valueAttributeName = "value";

	protected String value;
	private String type;
	
	protected static Element escaper = DOM.createDiv();
	
	protected RDFValue(JSONObject jsonObject) {
		this(jsonObject.get(RDFValue.valueAttributeName).isString().stringValue(), jsonObject.get(RDFValue.typeAttributeName).isString().stringValue());
	}
	
	protected RDFValue(String value, String type) {
		this.value = unescape(value);
		this.type = type;
		
		if (!this.acceptsType())
			throw new IllegalArgumentException(type + " is incorrect type.");
	}
	
	protected static native String escape(String value) /*-{
      return escape(value);
    }-*/;
	
	protected static native String unescape(String value) /*-{
      return unescape(value);
    }-*/;

	public RDFObject isRDFObject() {
		return null;
	}

	public RDFLiteral isRDFLiteral() {
		return null;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String getType() {
		return this.type;
	}
	
	protected abstract boolean acceptsType();
	
	public static RDFValue createRDFValue(JSONObject jsonObject) {
		RDFValue value = null;
		if (RDFObject.acceptsType(jsonObject.get(RDFValue.typeAttributeName).isString().stringValue()))
			value = new RDFObject(jsonObject);
		else if (RDFLiteral.acceptsType(jsonObject.get(RDFValue.typeAttributeName).isString().stringValue()))
			value = new RDFLiteral(jsonObject);
		
		if (value == null)
			throw new IllegalArgumentException(jsonObject.toString() + " type not recognized.");
		
		return value;
	}
	
	public String toString() {
		return "{ \"type\": \"" + this.type + "\" , " 
				+ "\"value\": \"" + this.value + "\" }";
	}
}
