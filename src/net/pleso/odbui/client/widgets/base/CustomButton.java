package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;
import net.pleso.odbui.client.widgets.gfx.Color;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;

public class CustomButton extends Composite implements EventPreview {
	
	private Label body;
	
	public CustomButton(String caption, String title) {
		this(caption, title, "custom-button");
	}
	
	public CustomButton(String caption, String title, String style) {
		this.body = new Label(caption);
		this.body.setStyleName(style);
		
		initWidget(this.body);
		
		this.setTitle(title);
	}
	
	public void addClickListener(ClickListener listener) {
		this.body.addClickListener(listener);
	}
	
	public void addMouseListener(MouseListener listener) {
		this.body.addMouseListener(listener);
	}
	
	public boolean onEventPreview(Event event) {
		// We need to preventDefault() on mouseDown events (outside of the
		// DialogBox content) to keep text from being selected when it
		// is dragged.
		if (DOM.eventGetType(event) == Event.ONMOUSEDOWN) {
			if (DOM.isOrHasChild(this.getElement(), DOM
					.eventGetTarget(event))) {
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
	
	private Color color = null;
	
	public void setColor(Color color) {
		DOM.setStyleAttribute(this.body.getElement(), "background", color.getColor() + " none repeat scroll 0% 0%");
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
		                  
		                  
	}
}
