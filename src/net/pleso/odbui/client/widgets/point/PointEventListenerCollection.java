package net.pleso.odbui.client.widgets.point;

import java.util.ArrayList;
import java.util.Iterator;

public class PointEventListenerCollection extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8964016332179163797L;

	public void fireChange(Point sender) {
		for (Iterator it = iterator(); it.hasNext();) {
			PointEventListener listener = (PointEventListener) it.next();
			listener.onPointChange(sender);
		}
	}

	public void fireDelete(Point sender) {
		for (Iterator it = iterator(); it.hasNext();) {
			PointEventListener listener = (PointEventListener) it.next();
			listener.onPointDelete(sender);
		}
	}
}
