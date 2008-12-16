package net.pleso.odbui.client.rdf_ds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public class RDFDataSet {

	private HashMap objects = new HashMap();
	private HashMap predicates = new HashMap();
	private ArrayList triplets = new ArrayList();
	private HashMap namespaces = new HashMap();
	
	public RDFDataSet(JSONArray tripletItems) {
		for(int i = 0; i < tripletItems.size(); i++) {
			JSONObject triplet = tripletItems.get(i).isObject();
			if (triplet == null)
				throw new IllegalArgumentException(tripletItems.get(i).toString() + " must bu result object.");
			new RDFTriplet(this, triplet);
		}
	}
	
	public RDFDataSet() {
	}
	
	private void addRDFObject(RDFObject rdfObject) {
		if (this.objects.containsKey(rdfObject.getValue()))
			throw new IllegalArgumentException(rdfObject.getValue() + " object already in dataset.");
		
		this.checkObjectURI(rdfObject);
		
		this.objects.put(rdfObject.getValue(), rdfObject);
	}
	
	private void addRDFPredicate(RDFPredicate rdfPredicate) {
		if (this.predicates.containsKey(rdfPredicate.getName()))
			throw new IllegalArgumentException(rdfPredicate.getName() + " predicate already in dataset.");
		
		this.predicates.put(rdfPredicate.getName(), rdfPredicate);
	}
	
	private void addRDFNamespace(RDFNamespace rdfNamespace) {
		if (this.namespaces.containsKey(rdfNamespace.getNamespace()))
			throw new IllegalArgumentException(rdfNamespace.getNamespace() + " namespace already in dataset.");
		
		this.namespaces.put(rdfNamespace.getNamespace(), rdfNamespace);
	}
	
	public void addTriplet(RDFTriplet triplet) {
		if (this.triplets.contains(triplet))
			throw new IllegalArgumentException("Triplet already added to dataset.");
		
		this.triplets.add(triplet);
	}
	
	private RDFObject getRDFObjectByName(String name) {
		return (RDFObject) this.objects.get(name);
	}
	
	private RDFPredicate getRDFPredicateByName(String name) {
		return (RDFPredicate) this.predicates.get(name);
	}
	
	private RDFNamespace getRDFNamespaceByName(String name) {
		return (RDFNamespace) this.namespaces.get(name);
	}
	
	public RDFObject smartGetRDFObject(RDFObject rdfObject) {
		if (rdfObject == null)
			return null;
		RDFObject result = this.getRDFObjectByName(rdfObject.getValue());
		if (result == null) {
			this.addRDFObject(rdfObject);
			result = rdfObject;
		}
		return result;
	}
	
	public RDFPredicate smartGetRDFPredicate(RDFPredicate rdfPredicate) {
		if (rdfPredicate == null)
			return null;
		RDFPredicate result = getRDFPredicateByName(rdfPredicate.getName());
		if (result == null) {
			this.addRDFPredicate(rdfPredicate);
			result = rdfPredicate;
		}
		return result;
	}
	
	public RDFPredicate smartGetRDFPredicate(String uri) {
		RDFPredicate result = getRDFPredicateByName(uri);
		if (result == null) {
			result = new RDFPredicate(this, uri);
			this.addRDFPredicate(result);
		}
		return result;
	}
	
	public RDFNamespace smartGetRDFNamespace(RDFNamespace rdfNamespace) {
		if (rdfNamespace == null)
			return null;
		RDFNamespace result = this.getRDFNamespaceByName(rdfNamespace.getNamespace());
		if (result == null) {
			this.addRDFNamespace(rdfNamespace);
			result = rdfNamespace;
		}
		return result;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("[");
		for (int i = 0; i < this.triplets.size(); i++) {
			sb.append(this.triplets.get(i).toString());
			if (i != this.triplets.size())
				sb.append(",\n");
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	public RDFObject[] getObjects() {
		Collection values = this.objects.values();
		RDFObject[] results = new RDFObject[values.size()];
		int c = 0;
		for (Iterator iterator = values.iterator(); iterator.hasNext();) {
			results[c++] = (RDFObject) iterator.next();
			
		}
		
		return results;
	}
	
	public RDFObject getObjectByName(String name) {
		return (RDFObject)this.objects.get(name);
	}
	
	public RDFPredicate getPredicateByName(String name) {
		return (RDFPredicate) this.predicates.get(name);
	}
	
	public void removeObject(RDFObject rdfObject) {
		// Вбиваємо зі датасету об"єкт.
		this.objects.remove(rdfObject);
		// А потім його тріплети/
		int i = 0;
		while(i < this.triplets.size()) {
			RDFTriplet triplet = (RDFTriplet) this.triplets.get(i);
			if (triplet.getSubject() == rdfObject || triplet.getObject() == rdfObject)
				this.triplets.remove(i);
			else
				i++;
		}
	}
	
	public void removeTriplet(RDFTriplet rdfTriplet) {
		// Видаляємо тріплет з датасету.
		this.triplets.remove(rdfTriplet);
		// Видаляєемо тріплет зі всіх об"єкта.
		rdfTriplet.getSubject().removeTriplet(rdfTriplet);
	}
	
	private int uriCounter = 0;
	private static final String uriPrefix = "http://protege.stanford.edu/rdfKB_943350_Instance";
	
	public String generateURI() {
		uriCounter++;
		return uriPrefix + new Integer(uriCounter).toString();
	}
	
	public void checkObjectURI(RDFObject rdfObject) {
		String uri = rdfObject.getValue();
		if (uri.startsWith(uriPrefix)) {
			String suffix = uri.substring(uriPrefix.length());
			try {
				int c = Integer.parseInt(suffix);
				if (uriCounter < c)
					uriCounter = c;
			} catch (NumberFormatException ex) {
			}
		}
	}
	
}
