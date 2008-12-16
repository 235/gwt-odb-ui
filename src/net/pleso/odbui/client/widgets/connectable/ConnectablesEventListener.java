package net.pleso.odbui.client.widgets.connectable;

public interface ConnectablesEventListener {

	void boxAdded(ConnectableBox box);
	void connectionAdded(Connector connector);
	void boxRemoved(ConnectableBox box);
	void connectionRemoved(Connector connector);
}
