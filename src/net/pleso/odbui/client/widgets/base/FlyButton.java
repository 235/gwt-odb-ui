package net.pleso.odbui.client.widgets.base;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SourcesMouseEvents;

public class FlyButton extends PushButton implements SourcesMouseEvents {

	private MouseListenerCollection mouseListeners;

	public FlyButton(String title, String styleName) {
		super();

		this.setVisible(false);
		this.setStyleName(styleName);
		this.setTitle(title);

		DOM.setStyleAttribute(this.getElement(), "position", "absolute");
	}

	public void setTop(int top) {
		DOM.setStyleAttribute(this.getElement(), "top", top + "px");
	}

	public void setRight(int right) {
		DOM.setStyleAttribute(this.getElement(), "right", right + "px");
	}

	public void setLeft(int left) {
		DOM.setStyleAttribute(this.getElement(), "left", left + "px");
	}

	public void setBottom(int bottom) {
		DOM.setStyleAttribute(this.getElement(), "bottom", bottom + "px");
	}

	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {

		case Event.ONMOUSEDOWN:
		case Event.ONMOUSEUP:
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEOUT:
			if (mouseListeners != null) {
				mouseListeners.fireMouseEvent(this, event);
			}
			break;
		}
		
		super.onBrowserEvent(event);
	}

	public void addMouseListener(MouseListener listener) {
		if (mouseListeners == null) {
			mouseListeners = new MouseListenerCollection();
		}
		mouseListeners.add(listener);
	}

	public void removeMouseListener(MouseListener listener) {
		if (mouseListeners != null) {
			mouseListeners.remove(listener);
		}
	}
}
