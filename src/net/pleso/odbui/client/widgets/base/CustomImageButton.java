package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;

public class CustomImageButton extends Composite implements EventPreview, MouseListener {

	private Image image;
	private String url;

	public CustomImageButton(String caption, String title) {
		this(caption, title, "custom-button");
	}

	public CustomImageButton(String image, String title, String style) {
		this.url = image + ".gif";
		
		this.image = new Image(this.url);
		this.image.setStyleName(style);
		
		this.image.addMouseListener(this);
		
		initWidget(this.image);

		this.setTitle(title);
	}

	public void addClickListener(ClickListener listener) {
		this.image.addClickListener(listener);
	}

	public void addMouseListener(MouseListener listener) {
		this.image.addMouseListener(listener);
	}

	public boolean onEventPreview(Event event) {
		// We need to preventDefault() on mouseDown events (outside of the
		// DialogBox content) to keep text from being selected when it
		// is dragged.
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			if (DOM.isOrHasChild(this.getElement(), DOM.eventGetTarget(event))) {
				DOM.eventPreventDefault(event);
			}
		}

		return true;
	}

	protected void onAttach() {
		super.onAttach();
		GlobalEventPreviewManager.getInstance().addEventPreview(this);
	}

	protected void onDetach() {
		GlobalEventPreviewManager.getInstance().removeEventPreview(this);
		super.onDetach();
	}

	public void onMouseDown(Widget sender, int x, int y) {
		
	}

	public void onMouseEnter(Widget sender) {
		this.image.setUrl("h" + this.url);
	}

	public void onMouseLeave(Widget sender) {
		this.image.setUrl(this.url);
	}

	public void onMouseMove(Widget sender, int x, int y) {
		
	}

	public void onMouseUp(Widget sender, int x, int y) {
		
	}
}
