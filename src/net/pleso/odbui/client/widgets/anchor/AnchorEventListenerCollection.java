package net.pleso.odbui.client.widgets.anchor;

import java.util.ArrayList;
import java.util.Iterator;

public class AnchorEventListenerCollection extends ArrayList {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9147402051515200042L;

	public void fireAnchorVectorPointChanged(Anchor sender) {
		for (Iterator it = iterator(); it.hasNext();) {
			AnchorEventListener listener = (AnchorEventListener) it
					.next();
			listener.anchorVectorPointChanged(sender);
		}
	}
	
	public void fireAnchorDeleted(Anchor sender) {
		ArrayList copy = (ArrayList) this.clone();
		for (Iterator it = copy.iterator(); it.hasNext();) {
			AnchorEventListener listener = (AnchorEventListener) it
					.next();
			listener.anchorDeleted(sender);
		}
	}
}
