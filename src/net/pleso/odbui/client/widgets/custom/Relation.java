package net.pleso.odbui.client.widgets.custom;

import net.pleso.odbui.client.rdf_ds.RDFDataSet;
import net.pleso.odbui.client.rdf_ds.RDFTriplet;
import net.pleso.odbui.client.widgets.anchor.Anchor;
import net.pleso.odbui.client.widgets.connectable.ConnectableBox;

public class Relation extends CustomConnector {

	private RDFDataSet dataSet;
	private RDFTriplet rdfTriplet;
	
	public Relation(RDFDataSet dataSet, RDFTriplet rdfTriplet, Anchor start, Anchor end, ConnectableBox box1,
			ConnectableBox box2) {
		super(start, end, box1, box2);
		this.dataSet = dataSet;
		this.rdfTriplet = rdfTriplet;
	}
	
	private boolean deleted = false;

	public void delete() {
		if (!deleted) {
			deleted = true;
			super.delete();
		}
		
		this.dataSet.removeTriplet(this.rdfTriplet);
	}
	
	public static Relation Create(RDFDataSet dataSet, RDFTriplet rdfTriplet, ConnectableBox box1,
			ConnectableBox box2) {
		return new Relation(dataSet, rdfTriplet, box1.requestNewAnchor(), box2
				.requestNewAnchor(), box1, box2);
	}
}
