package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PopupPanel extends SimplePanel implements EventPreview {

	// Цю панель треба задати ззовні, вона визначає відносний початок координат.
	private static Panel customRootPanel = null;

	private Element[] reserverElements = null;

	public static void setCustomRootPanel(Panel rootPanel) {
		customRootPanel = rootPanel;
	}

	public static Panel getRootPanel() {
		if (customRootPanel == null) {
			return RootPanel.get();
		} else {
			return customRootPanel;
		}
	}

	/**
	 * A callback that is used to set the position of a {@link PopupPanel} right
	 * before it is shown.
	 */
	public interface PositionCallback {

		/**
		 * Provides the opportunity to set the position of the PopupPanel right
		 * before the PopupPanel is shown. The offsetWidth and offsetHeight
		 * values of the PopupPanel are made available to allow for positioning
		 * based on its size.
		 * 
		 * @param offsetWidth
		 *            the offsetWidth of the PopupPanel
		 * @param offsetHeight
		 *            the offsetHeight of the PopupPanel
		 * @see PopupPanel#setPopupPositionAndShow(PositionCallback)
		 */
		public void setPosition(int offsetWidth, int offsetHeight);
	}

	private boolean showing;

	// the left style attribute in pixels
	private int leftPosition = -1;

	// The top style attribute in pixels
	private int topPosition = -1;

	/**
	 * Creates an empty popup panel. A child widget must be added to it before
	 * it is shown.
	 */
	public PopupPanel() {
		// Default position of popup should be in the upper-left corner of the
		// window. By setting a default position, the popup will not appear in
		// an undefined location if it is shown before its position is set.
		setPopupPosition(0, 0);
	}

	/**
	 * Gets the panel's offset height in pixels. Calls to
	 * {@link #setHeight(String)} before the panel's child widget is set will
	 * not influence the offset height.
	 * 
	 * @return the object's offset height
	 */
	public int getOffsetHeight() {
		return super.getOffsetHeight();
	}

	/**
	 * Gets the panel's offset width in pixels. Calls to
	 * {@link #setWidth(String)} before the panel's child widget is set will not
	 * influence the offset width.
	 * 
	 * @return the object's offset width
	 */
	public int getOffsetWidth() {
		return super.getOffsetWidth();
	}

	/**
	 * Gets the popup's left position relative to the browser's client area.
	 * 
	 * @return the popup's left position
	 */
	public int getPopupLeft() {
		return DOM.getElementPropertyInt(getElement(), "offsetLeft");
	}

	/**
	 * Gets the popup's top position relative to the browser's client area.
	 * 
	 * @return the popup's top position
	 */
	public int getPopupTop() {
		return DOM.getElementPropertyInt(getElement(), "offsetTop");
	}

	public String getTitle() {
		return DOM.getElementProperty(getContainerElement(), "title");
	}

	public boolean onEventPreview(Event event) {

		if (this.reserverElements != null) {
			Element target = DOM.eventGetTarget(event);
			if (DOM.isOrHasChild(this.getElement(), target)) {
				for (int i = 0; i < this.reserverElements.length; i++) {
					if (DOM.isOrHasChild(this.reserverElements[i], target))
						return true;
				}
				return false;
			}
		}

		return true;
	}

	/**
	 * Sets the popup's position relative to the browser's client area. The
	 * popup's position may be set before calling {@link #show()}.
	 * 
	 * @param left
	 *            the left position, in pixels
	 * @param top
	 *            the top position, in pixels
	 */
	public void setPopupPosition(int left, int top) {
		// Keep the popup within the browser's client area, so that they can't
		// get
		// 'lost' and become impossible to interact with. Note that we don't
		// attempt
		// to keep popups pegged to the bottom and right edges, as they will
		// then
		// cause scrollbars to appear, so the user can't lose them.
		if (left < 0) {
			left = 0;
		}
		if (top < 0) {
			top = 0;
		}

		// Save the position of the popup
		leftPosition = left;
		topPosition = top;

		// Set the popup's position manually, allowing setPopupPosition() to be
		// called before show() is called (so a popup can be positioned without
		// it
		// 'jumping' on the screen).
		Element elem = getElement();
		DOM.setStyleAttribute(elem, "left", left + "px");
		DOM.setStyleAttribute(elem, "top", top + "px");
	}

	/**
	 * Sets the popup's position using a {@link PositionCallback}, and shows
	 * the popup. The callback allows positioning to be performed based on the
	 * offsetWidth and offsetHeight of the popup, which are normally not
	 * available until the popup is showing. By positioning the popup before it
	 * is shown, the the popup will not jump from its original position to the
	 * new position.
	 * 
	 * @param callback
	 *            the callback to set the position of the popup
	 * @see PositionCallback#setPosition(int offsetWidth, int offsetHeight)
	 */
	public void setPopupPositionAndShow(PositionCallback callback) {
		setVisible(false);
		show();
		callback.setPosition(getOffsetWidth(), getOffsetHeight());
		setVisible(true);
	}

	public void setTitle(String title) {
		Element containerElement = getContainerElement();
		if (title == null || title.length() == 0) {
			DOM.removeElementAttribute(containerElement, "title");
		} else {
			DOM.setElementAttribute(containerElement, "title", title);
		}
	}

	/**
	 * Sets whether this object is visible.
	 * 
	 * @param visible
	 *            <code>true</code> to show the object, <code>false</code>
	 *            to hide it
	 */
	public void setVisible(boolean visible) {
		// We use visibility here instead of UIObject's default of display
		// Because the panel is absolutely positioned, this will not create
		// "holes" in displayed contents and it allows normal layout passes
		// to occur so the size of the PopupPanel can be reliably determined.
		DOM.setStyleAttribute(getElement(), "visibility", visible ? "visible"
				: "hidden");
	}

	/**
	 * Shows the popup. It must have a child widget before this method is
	 * called.
	 */
	public void show() {
		if (showing) {
			return;
		}
		showing = true;
		GlobalEventPreviewManager.getInstance().addEventPreview(this);

		// Set the position attribute, and then attach to the DOM. Otherwise,
		// the PopupPanel will appear to 'jump' from its static/relative
		// position
		// to its absolute position (issue #1231).
		DOM.setStyleAttribute(getElement(), "position", "absolute");
		if (topPosition != -1) {
			setPopupPosition(leftPosition, topPosition);
		}
		getRootPanel().add(this);

		onShow();
	}

	/**
	 * This method is called when a widget is detached from the browser's
	 * document. To receive notification before the PopupPanel is removed from
	 * the document, override the {@link Widget#onUnload()} method instead.
	 */
	protected void onDetach() {
		GlobalEventPreviewManager.getInstance().removeEventPreview(this);
		super.onDetach();
	}

	public void hide() {
		if (!showing) {
			return;
		}
		showing = false;

		getRootPanel().remove(this);
	}

	public int getLeftPosition() {
		return leftPosition;
	}

	public int getTopPosition() {
		return topPosition;
	}

	public void setZIndex(int zindex) {
		DOM.setStyleAttribute(this.getElement(), "zIndex", new Integer(zindex)
				.toString());
	}
	
	public int getZIndex() {
		return Integer.parseInt(DOM.getStyleAttribute(this.getElement(), "zIndex"));
	}

	public boolean isShowing() {
		return showing;
	}

	protected void onShow() {
	}

	public void setReserverElements(Element[] reserverElements) {
		this.reserverElements = reserverElements;
	}
}
