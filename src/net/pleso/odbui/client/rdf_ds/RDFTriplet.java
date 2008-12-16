package net.pleso.odbui.client.rdf_ds;

import com.google.gwt.json.client.JSONObject;

public class RDFTriplet {
	
	private RDFObject subject;
	private RDFPredicate predicate;
	private RDFValue object;
	
	public RDFTriplet(RDFDataSet rdfDataSet, JSONObject jsonTriplet) {
		if (jsonTriplet == null)
			throw new IllegalArgumentException("jsonTriplet can't be null.");
		if (rdfDataSet == null)
			throw new IllegalArgumentException("rdfDataSet can't be null.");
		
		RDFObject subject = new RDFObject(jsonTriplet.get(RDFResultSet.subjectAttributeName).isObject());
		RDFPredicate predicate = new RDFPredicate(rdfDataSet, jsonTriplet.get(RDFResultSet.predicateAttributeName).isObject());
		RDFValue value = RDFValue.createRDFValue(jsonTriplet.get(RDFResultSet.objectAttributeName).isObject());
		
		this.subject = rdfDataSet.smartGetRDFObject(subject);
		this.predicate = rdfDataSet.smartGetRDFPredicate(predicate);
		
		RDFObject object = rdfDataSet.smartGetRDFObject(value.isRDFObject());
		if (object != null)
			this.object = object;
		else
			this.object = value;
		
		this.subject.addTriplet(this);
		
		rdfDataSet.addTriplet(this);
	}
	
	public RDFTriplet(RDFDataSet rdfDataSet, RDFObject subject, RDFPredicate predicate, RDFValue value) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = value;
		
		this.subject.addTriplet(this);
		
		
		rdfDataSet.addTriplet(this);
	}
	
	public RDFObject getSubject() {
		return subject;
	}
	
	public RDFPredicate getPredicate() {
		return predicate;
	}
	
	public RDFValue getObject() {
		return object;
	}
	
	public String toString() {
		return "{\n" +
				"\"" + RDFResultSet.subjectAttributeName + "\": " + this.subject.toString() + ",\n" +
				"\"" + RDFResultSet.predicateAttributeName + "\": " + this.predicate.toString() + ",\n" +
				"\"" + RDFResultSet.objectAttributeName + "\": " + this.object.toString() + "\n" +
				"}";
	}
}
