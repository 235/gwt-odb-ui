package net.pleso.odbui.client.widgets.roundedcorners;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ImageBundle;

public class AbsoluteRoundedCorners {
	
	/**
	* This {@link ImageBundle} is used for all the rounded corners images.
	*/
	public interface RoundCornersImages extends ImageBundle {

		/**
		 * @gwt.resource left_top.gif
		 */
		AbstractImagePrototype left_top();
		
		/**
		 * @gwt.resource right_top.gif
		 */
		AbstractImagePrototype right_top();
		
		/**
		 * @gwt.resource left_bottom.gif
		 */
		AbstractImagePrototype left_bottom();
		
		/**
		 * @gwt.resource right_bottom.gif
		 */
		AbstractImagePrototype right_bottom();

	}

	private RoundCornersImages images = (RoundCornersImages) GWT.create(RoundCornersImages.class);
	
	
	public void createRoundedCorners(HasWidgets widget) {
		Image left_top = images.left_top().createImage();
		setCoordinates(new Integer(-1), null, new Integer(-1), null, left_top.getElement());
		widget.add(left_top);
		
		Image right_top = images.right_top().createImage();
		setCoordinates(new Integer(-1), new Integer(-1), null, null, right_top.getElement());
		widget.add(right_top);
		
		Image left_bottom = images.left_bottom().createImage();
		setCoordinates(null, null, new Integer(-1), new Integer(-2), left_bottom.getElement());
		widget.add(left_bottom);
		
		Image right_bottom = images.right_bottom().createImage();
		setCoordinates(null, new Integer(-1), null, new Integer(-2), right_bottom.getElement());
		widget.add(right_bottom);
	}

	public void setCoordinates(Integer top, Integer right, Integer left, Integer bottom, Element element) {
		DOM.setStyleAttribute(element, "position", "absolute");
		if (top != null)
			DOM.setStyleAttribute(element, "top", top + "px");
		if (right != null)
			DOM.setStyleAttribute(element, "right", right + "px");
		if (left != null)
			DOM.setStyleAttribute(element, "left", left + "px");
		if (bottom != null)
			DOM.setStyleAttribute(element, "bottom", bottom + "px");
	}
}
