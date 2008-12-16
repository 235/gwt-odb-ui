package net.pleso.odbui.client.widgets.base.verticalscrollpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.ScrollListenerCollection;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SourcesScrollEvents;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class VerticalScrollPanel extends SimplePanel implements
		SourcesScrollEvents {
	
	private static class Impl {

		public void setAlwaysShowScrollBars(boolean alwaysShow, Element element) {
			DOM.setStyleAttribute(element, "overflowX", "hidden");
			DOM.setStyleAttribute(element, "overflowY", alwaysShow ? "scroll"
					: "auto");
		}
	}
	
	private static class ImplIE6 extends Impl {

		public void setAlwaysShowScrollBars(boolean alwaysShow, Element element) {
			DOM.setStyleAttribute(element, "overflow", alwaysShow ? "scroll"
					: "auto");
		}
	}

	
	private static Impl impl = (Impl) GWT.create(Impl.class);

	private ScrollListenerCollection scrollListeners;

	/**
	 * Creates an empty scroll panel.
	 */
	public VerticalScrollPanel() {
		setAlwaysShowScrollBars(false);
		sinkEvents(Event.ONSCROLL);
	}

	/**
	 * Creates a new scroll panel with the given child widget.
	 * 
	 * @param child the widget to be wrapped by the scroll panel
	 */
	public VerticalScrollPanel(Widget child) {
		this();
		setWidget(child);
	}

	public void addScrollListener(ScrollListener listener) {
		if (scrollListeners == null) {
			scrollListeners = new ScrollListenerCollection();
		}
		scrollListeners.add(listener);
	}

	/**
	 * Ensures that the specified item is visible, by adjusting the panel's scroll
	 * position.
	 * 
	 * @param item the item whose visibility is to be ensured
	 */
	public void ensureVisible(UIObject item) {
		Element scroll = getElement();
		Element element = item.getElement();
		ensureVisibleImpl(scroll, element);
	}

	/**
	 * Gets the horizontal scroll position.
	 * 
	 * @return the horizontal scroll position, in pixels
	 */
	public int getHorizontalScrollPosition() {
		return DOM.getElementPropertyInt(getElement(), "scrollLeft");
	}

	/**
	 * Gets the vertical scroll position.
	 * 
	 * @return the vertical scroll position, in pixels
	 */
	public int getScrollPosition() {
		return DOM.getElementPropertyInt(getElement(), "scrollTop");
	}

	public void onBrowserEvent(Event event) {
		if (DOM.eventGetType(event) == Event.ONSCROLL) {
			if (scrollListeners != null) {
				scrollListeners.fireScroll(this, getHorizontalScrollPosition(),
						getScrollPosition());
			}
		}
	}

	public void removeScrollListener(ScrollListener listener) {
		if (scrollListeners != null) {
			scrollListeners.remove(listener);
		}
	}

	/**
	 * Sets whether this panel always shows its scroll bars, or only when
	 * necessary.
	 * 
	 * @param alwaysShow <code>true</code> to show scroll bars at all times
	 */
	public void setAlwaysShowScrollBars(boolean alwaysShow) {
		impl.setAlwaysShowScrollBars(alwaysShow, getElement());
	}

	/**
	 * Sets the horizontal scroll position.
	 * 
	 * @param position the new horizontal scroll position, in pixels
	 */
	public void setHorizontalScrollPosition(int position) {
		DOM.setElementPropertyInt(getElement(), "scrollLeft", position);
	}

	/**
	 * Sets the vertical scroll position.
	 * 
	 * @param position the new vertical scroll position, in pixels
	 */
	public void setScrollPosition(int position) {
		DOM.setElementPropertyInt(getElement(), "scrollTop", position);
	}

	private native void ensureVisibleImpl(Element scroll, Element e) /*-{
	    if (!e)
	      return; 

	    var item = e;
	    var realOffset = 0;
	    while (item && (item != scroll)) {
	      realOffset += item.offsetTop;
	      item = item.offsetParent;
	    }

	    scroll.scrollTop = realOffset - scroll.offsetHeight / 2;
	  }-*/;

}
