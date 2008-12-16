package net.pleso.odbui.client.rdf_ds;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.json.client.JSONObject;

public class RDFObject extends RDFValue {

	private static final String typeAttributeValue1 = "bnode";
	private static final String typeAttributeValue2 = "uri";

	private HashMap triplets = new HashMap();

	public RDFObject(String value, String type) {
		super(value, type);
	}
	
	public RDFObject(JSONObject jsonObject) {
		super(jsonObject);
	}

	public void addTriplet(RDFTriplet rdfTriplet) {
		if (rdfTriplet == null)
			throw new IllegalArgumentException("Triplet cant be null.");

		if (rdfTriplet.getSubject() != this)
			throw new IllegalArgumentException("Triplet has another subject.");

		ArrayList triplets = this.forceGetTripletsBucket(rdfTriplet);
		if (triplets.contains(rdfTriplet))
			throw new IllegalArgumentException(
					"Triplet already exists in object.");
		triplets.add(rdfTriplet);
	}
	
	private ArrayList forceGetTripletsBucket(RDFTriplet rdfTriplet) {
		String name = rdfTriplet.getPredicate().getName();
		ArrayList result = null;
		if (this.triplets.containsKey(name)) {
			result = (ArrayList) this.triplets.get(name);
		} else {
			result = new ArrayList();
			this.triplets.put(name, result);
		}

		return result;
	}
	
	private ArrayList softGetTripletsBucket(RDFTriplet rdfTriplet) {
		String name = rdfTriplet.getPredicate().getName();
		ArrayList result = null;
		if (this.triplets.containsKey(name)) {
			result = (ArrayList) this.triplets.get(name);
		}

		return result;
	}
	
	public boolean acceptsType() {
		return RDFObject.acceptsType(this.getType());
	}

	public static boolean acceptsType(String typeName) {
		return typeName.equals(RDFObject.typeAttributeValue1)
				|| typeName.equals(RDFObject.typeAttributeValue2);
	}

	public RDFObject isRDFObject() {
		return this;
	}

	public String getSingleLiteralValue(RDFPredicate predicate) {
		ArrayList results = (ArrayList) this.triplets.get(predicate.getName());
		return ((RDFValue) ((RDFTriplet) results.get(0)).getObject())
				.getValue();
	}
	
	public RDFLiteral getSingleLiteral(RDFPredicate predicate) {
		ArrayList results = (ArrayList) this.triplets.get(predicate.getName());
		return ((RDFTriplet) results.get(0)).getObject().isRDFLiteral();
	}

	public RDFObject[] getMultiObjects(RDFPredicate predicate) {
		ArrayList results = (ArrayList) this.triplets.get(predicate.getName());
		RDFObject[] objects = new RDFObject[results.size()];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = (RDFObject) ((RDFTriplet) results.get(i)).getObject();
		}

		return objects;
	}
	
	public RDFTriplet[] getMultiTriplets(RDFPredicate predicate) {
		ArrayList results = (ArrayList) this.triplets.get(predicate.getName());
		if (results == null)
			return new RDFTriplet[0];
		RDFTriplet[] objects = new RDFTriplet[results.size()];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = (RDFTriplet) results.get(i);
		}

		return objects;
	}

	public boolean containsPredicates(RDFPredicate[] predicates) {
		for (int i = 0; i < predicates.length; i++) {
			if (!this.triplets.containsKey((predicates[i].getName())))
				return false;

		}
		return true;
	}
	
	public boolean removeTriplet(RDFTriplet rdfTriplet) {
		ArrayList triplets = this.softGetTripletsBucket(rdfTriplet);
		if (triplets != null)
			return triplets.remove(rdfTriplet);
		return false;
	}
	
	public void clearTriplets() {
		this.triplets.clear();
	}
}
