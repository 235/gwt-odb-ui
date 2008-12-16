package net.pleso.odbui.client.widgets.connectable;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;
import net.pleso.odbui.client.widgets.base.CurvedLine;
import net.pleso.odbui.client.widgets.base.PanScrollPanel;
import net.pleso.odbui.client.widgets.base.Rectangle;
import net.pleso.odbui.client.widgets.gfx.DefaultColors;
import net.pleso.odbui.client.widgets.gfx.DefaultStrokes;
import net.pleso.odbui.client.widgets.gfx.GFX;
import net.pleso.odbui.client.widgets.gfx.GFXGroup;
import net.pleso.odbui.client.widgets.point.Point;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollListener;
import com.google.gwt.user.client.ui.Widget;

public class PanNavigator implements EventPreview, ConnectablesEventListener,
		ScrollListener {

	private PanScrollPanel panScrollPanel;
	private int sourceWidth;
	private int sourceHeight;
	private int scaledWidth;
	private int scaledHeight;
	private int scaledClientWidth;
	private int scaledClientHeight;
	private int clientWidth;
	private int clientHeight;
	private double scale = 0;
	private GFXGroup navigatorGFXGroup;
	private GFXGroup panFrameGFXGroup;
	private GFXGroup miniaturesNodesGFXGroup;
	private GFXGroup miniaturesCurvesGFXGroup;
	
	private boolean panningView = false;
	private int startX;
	private int startY;

	private Point viewPoint = new Point(0, 0);

	public PanNavigator(Panel panel,
			ConnectableCollection connectableCollection,
			PanScrollPanel panScrollPanel, GFX sourceGFX, int targetWidth) {
		this.panScrollPanel = panScrollPanel;
		this.panScrollPanel.addScrollListener(this);

		connectableCollection.addConnectablesEventListener(this);

		this.sourceWidth = sourceGFX.getOffsetWidth();
		this.sourceHeight = sourceGFX.getOffsetHeight();

		if (this.sourceWidth != 0) {
			this.scale = (double) targetWidth / this.sourceWidth;
		}

		this.scaledWidth = targetWidth;
		this.scaledHeight = (int) (this.sourceHeight * this.scale);

		GFX gfx = new GFX(panel, this.scaledWidth, this.scaledHeight);

		this.navigatorGFXGroup = new GFXGroup(gfx);
		this.miniaturesCurvesGFXGroup = new GFXGroup(this.navigatorGFXGroup);
		this.miniaturesNodesGFXGroup = new GFXGroup(this.navigatorGFXGroup);
		this.panFrameGFXGroup = new GFXGroup(this.navigatorGFXGroup);

		this.nativeScale(this.navigatorGFXGroup.getSurface(), this.scale);

		this.clientWidth = DOM.getElementPropertyInt(this.panScrollPanel
				.getElement(), "clientWidth");
		this.clientHeight = DOM.getElementPropertyInt(this.panScrollPanel
				.getElement(), "clientHeight");

		this.scaledClientWidth = (int) (clientWidth * this.scale);
		this.scaledClientHeight = (int) (clientHeight * this.scale);

		new Rectangle(this.panFrameGFXGroup, this.viewPoint, new Point(
				clientWidth, clientHeight), DefaultStrokes.getRedBoldStroke());

		GlobalEventPreviewManager.getInstance().addEventPreview(this);
	}

	private int getEventX(Event event) {
		int px = this.navigatorGFXGroup.getUiObject().getAbsoluteLeft();

		return DOM.eventGetClientX(event) + Window.getScrollLeft() - px;
	}

	private int getEventY(Event event) {
		int py = this.navigatorGFXGroup.getUiObject().getAbsoluteTop();

		return DOM.eventGetClientY(event) + Window.getScrollTop() - py;
	}

	public boolean onEventPreview(Event event) {
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN: {
			if (DOM.isOrHasChild(this.navigatorGFXGroup.getUiObject()
					.getElement(), DOM.eventGetTarget(event))) {

				int x = getEventX(event);
				int y = getEventY(event);

				int vpx = (int) (this.viewPoint.getX() * this.scale);
				int vpy = (int) (this.viewPoint.getY() * this.scale);

				// TODO є глюки із позиціонуванням
				
				if (x >= vpx && x < vpx + this.scaledClientWidth && y >= vpy
						&& y < vpy + this.scaledClientHeight) {
					this.startX = x - vpx;
					this.startY = y - vpx;
				} else {
					if (x < vpx)
						this.startX = x;
					else if (x >= vpx + this.scaledClientWidth)
						this.startX = this.scaledClientWidth
								- (this.scaledWidth - x);

					if (y < vpy)
						this.startY = y;
					else if (y >= vpy + this.scaledClientHeight)
						this.startY = this.scaledClientHeight
								- (this.scaledHeight - y);

					this.setScrollInMini(x, y);
				}

				this.setCursorState("move");
				this.panningView = true;

				return false;
			}
			break;
		}
		case Event.ONMOUSEMOVE: {
			if (this.panningView) {

				int x = getEventX(event);
				int y = getEventY(event);

				this.setScrollInMini(x, y);

				return false;
			}
			break;
		}
		case Event.ONMOUSEUP: {
			if (this.panningView)
				this.setCursorState("default");
				this.panningView = false;

			break;
		}
		}

		return true;
	}

	public void boxAdded(ConnectableBox box) {
		new Rectangle(this.miniaturesNodesGFXGroup, box.getPoint(), box.getSize(),
				DefaultStrokes.getBlackBoldStroke())
				.setFill(DefaultColors.lightBlue);
	}

	public void connectionAdded(Connector connector) {
		new CurvedLine(this.miniaturesCurvesGFXGroup, connector.getStart(), connector
				.getStartVector(), connector.getEnd(),
				connector.getEndVector(), DefaultStrokes.getBlackBoldStroke());
	}

	public void onScroll(Widget widget, int scrollLeft, int scrollTop) {
		this.viewPoint.setPoint(scrollLeft, scrollTop);
	}

	private native void nativeScale(JavaScriptObject group, double scale) /*-{
		group.setTransform($wnd.dojox.gfx.matrix.scaleAt(scale, 0, 0));
	}-*/;

	private void setScrollInMini(int x, int y) {
		setScroll((int) ((x - this.startX) / this.scale),
				(int) ((y - this.startY) / this.scale));
	}

	private void setScroll(int left, int top) {
		left = left < 0 ? 0 : left;
		left = left > this.sourceWidth - this.clientWidth ? this.sourceWidth
				- this.clientWidth : left;
		top = top < 0 ? 0 : top;
		top = top > this.sourceHeight - this.clientHeight ? this.sourceHeight
				- this.clientHeight : top;

		this.panScrollPanel.setScrollPosition(top);
		this.panScrollPanel.setHorizontalScrollPosition(left);
	}
	
	private void setCursorState(String cursor_state){
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", cursor_state);
	}

	public void boxRemoved(ConnectableBox box) {
	}

	public void connectionRemoved(Connector connector) {
	}
}
