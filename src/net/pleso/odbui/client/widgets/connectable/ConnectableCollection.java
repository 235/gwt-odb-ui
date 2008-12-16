package net.pleso.odbui.client.widgets.connectable;

import java.util.ArrayList;
import java.util.Iterator;

public class ConnectableCollection {

	public class ConnectableBoxPair {

		private ConnectableBox box1;
		private ConnectableBox box2;

		public ConnectableBoxPair(ConnectableBox box1, ConnectableBox box2) {
			this.box1 = box1;
			this.box2 = box2;
		}

		public boolean has(ConnectableBox box1, ConnectableBox box2) {
			return (box1 == this.box1 && box2 == this.box2)
					|| (box2 == this.box1 && box1 == this.box2);
		}
		
		public boolean has(ConnectableBox box) {
			return box == this.box1	|| box == this.box2;
		}

		public ConnectableBox getBox1() {
			return box1;
		}

		public ConnectableBox getBox2() {
			return box2;
		}
	}

	private static ConnectableCollection instance = new ConnectableCollection();
	
	private ConnectablesEventListenerCollection connectablesEventListenerCollection = null;

	private ArrayList boxes;
	private ArrayList boxPairs;

	public ConnectableCollection() {
		this.boxes = new ArrayList();
		this.boxPairs = new ArrayList();
	}

	public void addBox(ConnectableBox box) {
		this.boxes.add(box);
		if (this.connectablesEventListenerCollection != null)
			this.connectablesEventListenerCollection.boxAdded(box);
	}

	public void removeBox(ConnectableBox box) {
		this.boxes.remove(box);
		
		int i = 0;
		while(i < this.boxPairs.size()) {
			ConnectableBoxPair pair = (ConnectableBoxPair) this.boxPairs.get(i);
			if (pair.has(box))
				this.boxPairs.remove(i);
			else				
				i++;
		}
		
		if (this.connectablesEventListenerCollection != null)
			this.connectablesEventListenerCollection.boxRemoved(box);
	}
	
	public void deleteAllBoxes() {
		while(this.boxes.size() > 0)
			((ConnectableBox)this.boxes.get(0)).delete();
	}
	
	public void removeBoxPair(Connector connector) {
		int i = 0;
		while(i < this.boxPairs.size()) {
			ConnectableBoxPair pair = (ConnectableBoxPair) this.boxPairs.get(i);
			if (pair.has(connector.getBox1(), connector.getBox2()))
				this.boxPairs.remove(i);
			else				
				i++;
		}
		if (this.connectablesEventListenerCollection != null)
			this.connectablesEventListenerCollection.connectorRemoved(connector);
	}
	
	public ConnectableBox getBoxByPoint(int x, int y) {
		for (Iterator it = this.boxes.iterator(); it.hasNext();) {
			ConnectableBox box = (ConnectableBox) it.next();
			if (box.testHit(x, y))
				return box;
		}

		return null;
	}
	
	public void makeBoxTopmost(ConnectableBox box) {
		for (Iterator it = this.boxes.iterator(); it.hasNext();) {
			ConnectableBox b = (ConnectableBox) it.next();
			if (b != box)
				b.setZIndex(1);
		}
		
		box.setZIndex(2);
	}
	
	public void addConnectableBoxPair(Connector connector) {
		this.boxPairs.add(new ConnectableBoxPair(connector.getBox1(), connector.getBox2()));
		if (this.connectablesEventListenerCollection != null)
			this.connectablesEventListenerCollection.connectorAdded(connector);
	}
	
	public boolean isConnectableBoxPairExists(ConnectableBox box1, ConnectableBox box2) {
		for (Iterator it = this.boxPairs.iterator(); it.hasNext();) {
			ConnectableBoxPair pair = (ConnectableBoxPair) it.next();
			if (pair.has(box1, box2))
				return true;
		}
		
		return false;
	}

	public static ConnectableCollection getInstance() {
		return instance;
	}

	public ArrayList getBoxes() {
		return boxes;
	}

	public ArrayList getBoxPairs() {
		return boxPairs;
	}
	
	public void addConnectablesEventListener(ConnectablesEventListener listener) {
		if (this.connectablesEventListenerCollection == null)
			this.connectablesEventListenerCollection = new ConnectablesEventListenerCollection();
		this.connectablesEventListenerCollection.add(listener);
	}
	
	public void removeConnectablesEventListener(ConnectablesEventListener listener) {
		if (this.connectablesEventListenerCollection != null)
			this.connectablesEventListenerCollection.remove(listener);
	}
}
