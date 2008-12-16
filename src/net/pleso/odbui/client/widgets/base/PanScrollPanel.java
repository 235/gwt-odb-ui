package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class PanScrollPanel extends ScrollPanel implements EventPreview {

	protected void onAttach() {
		super.onAttach();
		GlobalEventPreviewManager.getInstance().addEventPreview(this);
	}

	protected void onDetach() {
		GlobalEventPreviewManager.getInstance().removeEventPreview(this);
		super.onDetach();
	}

	private boolean dragging;
	private int dragStartX, dragStartY;
	private int dragStartScrollX, dragStartScrollY;
	
	private Widget child;

	public PanScrollPanel() {
		super();
	}

	public PanScrollPanel(Widget child) {
		super(child);
		this.child = child;
	}

	public boolean onEventPreview(Event event) {
		int type = DOM.eventGetType(event);
		int x = DOM.eventGetClientX(event) + Window.getScrollLeft();
		int y = DOM.eventGetClientY(event) + Window.getScrollTop();
		int button = DOM.eventGetButton(event);
		switch (type) {
		case Event.ONMOUSEDOWN: {
			// Драгає якщо таргет не є дочірним елементом рут панелі попапів, але є чайлдом даної панелі.
			if (!dragging
					&& (button == Event.BUTTON_LEFT || button == Event.BUTTON_MIDDLE)
					&& !DOM.isOrHasChild(PopupPanel.getRootPanel().getElement(), DOM.eventGetTarget(event))
					&& DOM.isOrHasChild(this.child.getElement(), DOM.eventGetTarget(event))
					&& x >= this.getAbsoluteLeft()
					&& y >= this.getAbsoluteLeft()
					&& x <= this.getAbsoluteLeft() + this.getOffsetWidth()
					&& y <= this.getAbsoluteTop() + this.getOffsetHeight()) {
				dragging = true;
				DOM.setCapture(this.getElement());
				dragStartX = x;
				dragStartY = y;
				dragStartScrollX = this.getHorizontalScrollPosition();
				dragStartScrollY = this.getScrollPosition();
				
				setCursorState("move");
				
				return false;
			}
			break;
		}
		case Event.ONMOUSEUP: {
			if (dragging) {
				dragging = false;
				DOM.releaseCapture(this.getElement());
				
				setCursorState("default");
				
				return false;
			}
			break;
		}
		case Event.ONMOUSEMOVE: {
			if (dragging) {
				int newPosScrollX = dragStartScrollX - (x - dragStartX);
				int newPosScrollY = dragStartScrollY - (y - dragStartY);

				this.setHorizontalScrollPosition(newPosScrollX);
				this.setScrollPosition(newPosScrollY);
				
				return false;
			}
			break;
		}
		}
		return true;
	}
	
	private void setCursorState(String cursor_state){
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", cursor_state);
	}

	public void add(Widget w) {
		this.child = w;
		super.add(w);
	}
}
