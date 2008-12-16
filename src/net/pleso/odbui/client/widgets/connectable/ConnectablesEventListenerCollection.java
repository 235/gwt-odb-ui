package net.pleso.odbui.client.widgets.connectable;

import java.util.ArrayList;
import java.util.Iterator;

public class ConnectablesEventListenerCollection extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6872536779339658896L;

	public void boxAdded(ConnectableBox box) {
		for (Iterator it = iterator(); it.hasNext();) {
			ConnectablesEventListener listener = (ConnectablesEventListener) it.next();
			listener.boxAdded(box);
		}
	}
	
	public void boxRemoved(ConnectableBox box) {
		for (Iterator it = iterator(); it.hasNext();) {
			ConnectablesEventListener listener = (ConnectablesEventListener) it.next();
			listener.boxRemoved(box);
		}
	}
	
	public void connectorAdded(Connector connector) {
		for (Iterator it = iterator(); it.hasNext();) {
			ConnectablesEventListener listener = (ConnectablesEventListener) it.next();
			listener.connectionAdded(connector);
		}
	}
	
	public void connectorRemoved(Connector connector) {
		for (Iterator it = iterator(); it.hasNext();) {
			ConnectablesEventListener listener = (ConnectablesEventListener) it.next();
			listener.connectionRemoved(connector);
		}
	}
}
