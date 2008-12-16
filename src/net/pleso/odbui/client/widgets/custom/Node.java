package net.pleso.odbui.client.widgets.custom;

import net.pleso.odbui.client.rdf_ds.RDFDataSet;
import net.pleso.odbui.client.rdf_ds.RDFLiteral;
import net.pleso.odbui.client.rdf_ds.RDFObject;
import net.pleso.odbui.client.rdf_ds.RDFPredicate;
import net.pleso.odbui.client.rdf_ds.RDFTriplet;
import net.pleso.odbui.client.widgets.connectable.ConnectableBox;

public class Node extends CustomBox {
	
	private RDFDataSet dataSet;
	private RDFObject rdfObject;
	
	private static RDFDataSet cachedDataSet = null;
	private static RDFPredicate related;
	private static RDFPredicate title;
	private static RDFPredicate coordinates;
	private static RDFPredicate content;
	private static RDFPredicate nodeType;
	private static RDFPredicate dimensions;
	
	private RDFLiteral coordinatesLiteral;
	private RDFLiteral titleLiteral;
	private RDFLiteral contentLiteral;
	private RDFLiteral dimensionsLiteral;

	public Node(RDFDataSet dataSet, RDFObject rdfObject) {
		super();
		
		this.dataSet = dataSet;
		this.rdfObject = rdfObject;
		
		initTriplets(this.dataSet);
		
		this.coordinatesLiteral = rdfObject.getSingleLiteral(coordinates);
		this.titleLiteral = rdfObject.getSingleLiteral(title);
		this.contentLiteral = rdfObject.getSingleLiteral(content);
		this.dimensionsLiteral = rdfObject.getSingleLiteral(dimensions);
		
		this.setText(this.titleLiteral.getValue());
		this.setContent(this.contentLiteral.getValue());
		
		String coords = this.coordinatesLiteral.getValue();
		String[] xy = coords.split(",");
		
		this.setPopupPosition(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
		
		String dims = this.dimensionsLiteral.getValue();
		String[] wh = dims.split(",");
		
		this.setSize(Integer.parseInt(wh[0]), Integer.parseInt(wh[1]));
	}
	
	private static void initTriplets(RDFDataSet dataSet) {
		if (cachedDataSet != dataSet) {
			related = dataSet.smartGetRDFPredicate("http://pleso.net/schemas/odbui#related");
			title = dataSet.smartGetRDFPredicate("http://pleso.net/schemas/odbui#title");
			coordinates = dataSet.smartGetRDFPredicate("http://pleso.net/schemas/odbui#coordinates");
			content = dataSet.smartGetRDFPredicate("http://pleso.net/schemas/odbui#content");
			nodeType = dataSet.smartGetRDFPredicate("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
			dimensions = dataSet.smartGetRDFPredicate("http://pleso.net/schemas/odbui#dimensions");
			
			cachedDataSet = dataSet;
		}
	}

	protected void contentUpdated() {
		super.contentUpdated();
		
		this.titleLiteral.setValue(this.getText());
		this.contentLiteral.setValue(this.getContent());
	}

	public void createConnectionWith(ConnectableBox newBox) {
		RDFTriplet newTriplet = new RDFTriplet(this.dataSet, this.rdfObject, related, ((Node)newBox).rdfObject);
		Relation.Create(this.dataSet, newTriplet, this, newBox);
	}
	
	public static Node createCustomBox(RDFDataSet dataSet) {
		initTriplets(dataSet);
		
		RDFObject newObject = new RDFObject(dataSet.generateURI(), "uri");
		
		if (dataSet.getObjectByName("http://pleso.net/schemas/odbui#Node") != null)
			new RDFTriplet(dataSet, newObject, nodeType, dataSet.getObjectByName("http://pleso.net/schemas/odbui#Node"));
		new RDFTriplet(dataSet, newObject, title, new RDFLiteral("", "literal"));
		new RDFTriplet(dataSet, newObject, coordinates, new RDFLiteral("0,0", "literal"));
		new RDFTriplet(dataSet, newObject, content, new RDFLiteral("", "literal"));
		new RDFTriplet(dataSet, newObject, dimensions, new RDFLiteral("150,100", "literal"));
		
		Node newNode = new Node(dataSet, newObject);
		
		return newNode;
	}
	
	protected CustomBox createCustomBox() {
		return createCustomBox(this.dataSet);
	}

	public void delete() {
		super.delete();
		
		this.dataSet.removeObject(this.rdfObject);
	}

	public void doMove() {
		super.doMove();
		
		int x = this.getLeftPosition();
		int y = this.getTopPosition();
		String coordinates = Integer.toString(x) + "," + Integer.toString(y);
		
		this.coordinatesLiteral.setValue(coordinates);
	}

	protected void onResize() {
		super.onResize();
		int width = this.getOffsetWidth();
		int height = this.getOffsetHeight();
		String dimensions = Integer.toString(width) + "," + Integer.toString(height);
		this.dimensionsLiteral.setValue(dimensions);		
	}
	
	public void storeState() {
		this.onResize();
		this.doMove();
		this.contentUpdated();
	}
}
