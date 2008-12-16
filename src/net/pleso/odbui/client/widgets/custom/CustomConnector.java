package net.pleso.odbui.client.widgets.custom;

import net.pleso.odbui.client.util.GlobalEventPreviewManager;
import net.pleso.odbui.client.widgets.anchor.Anchor;
import net.pleso.odbui.client.widgets.base.CustomButton;
import net.pleso.odbui.client.widgets.base.CustomImageButton;
import net.pleso.odbui.client.widgets.base.FlyButton;
import net.pleso.odbui.client.widgets.base.PopupPanel;
import net.pleso.odbui.client.widgets.base.PopupPanel.PositionCallback;
import net.pleso.odbui.client.widgets.base.fly_buttons.FlyButtonsManager;
import net.pleso.odbui.client.widgets.base.fly_buttons.IFlyButtonsHolder;
import net.pleso.odbui.client.widgets.connectable.ConnectableBox;
import net.pleso.odbui.client.widgets.connectable.Connector;
import net.pleso.odbui.client.widgets.gfx.Color;
import net.pleso.odbui.client.widgets.gfx.Stroke;
import net.pleso.odbui.client.widgets.point.Point;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomConnector extends Connector implements EventPreview, IFlyButtonsHolder {

	private PopupPanel optionsPanel;
	private FlowPanel toolbar;
	private CustomImageButton btnDelete;
	private FlyButton btnShowMenu;

	private int cx;
	private int cy;
	
	// Половина ширини кнопки показу меню.
	private int showMenuButtonOffset2 = 8;
	// Додатковий припуск на те, щоб по цій кнопці можна було попасти соваючи мишу.
	private int showMenuButtonAdditionalOffset = 12;
	
	private int menuOffsetWidth2;
	private int menuOffsetHeight2;
	
	private boolean menuShown = false;

	private static Color[] colors = new Color[] { new Color(100, 100, 100),
			new Color(200, 10, 10), new Color(10, 200, 10),
			new Color(10, 10, 200), };

	private class ColorButtonClickListener implements ClickListener {
		public void onClick(Widget sender) {
			CustomButton button = (CustomButton) (sender.getParent());
			setStroke(new Stroke(button.getColor(), 2));
		}
	}

	public static CustomConnector Create(ConnectableBox box1,
			ConnectableBox box2) {
		return new CustomConnector(box1.requestNewAnchor(), box2
				.requestNewAnchor(), box1, box2);
	}

	public CustomConnector(Anchor start, Anchor end, ConnectableBox box1,
			ConnectableBox box2) {
		super(start, end, box1, box2);

		this.updateCenter();
		
		this.btnDelete = new CustomImageButton("delete", "Delete");
		this.btnDelete.addStyleName("delete-connection");
		this.btnDelete.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				CustomConnector.this.delete();
			}
		});
		
		this.toolbar = new FlowPanel();
		this.toolbar.setStyleName("toolbar");
		this.toolbar.add(this.btnDelete);
		this.toolbar.setStyleName("toolbar");

		for (int i = 0; i < colors.length; i++) {
			String style = "color-button";
			if (i == 0)
				style = "left-" + style;
			if (i == colors.length - 1)
				style = "right-" + style;

			CustomButton button = new CustomButton("", colors[i].getColor(),
					style);
			button.setColor(colors[i]);
			button.addClickListener(new ColorButtonClickListener());
			this.toolbar.add(button);
		}
		
		this.optionsPanel = new PopupPanel();
		this.optionsPanel.setWidget(this.toolbar);
		this.optionsPanel.setZIndex(2);
		
		this.btnShowMenu = new FlyButton("Show toolbar", "connection-fly-button");
		this.btnShowMenu.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				btnShowMenu.setVisible(false);
				optionsPanel.setPopupPositionAndShow(new PositionCallback() {
					public void setPosition(int offsetWidth, int offsetHeight) {
						menuOffsetWidth2 = offsetWidth / 2;
						menuOffsetHeight2 = offsetHeight /2;
						optionsPanel.setPopupPosition(cx - menuOffsetWidth2, cy - menuOffsetHeight2);
						menuShown = true;
					}});
			}});
		DOM.setStyleAttribute(this.btnShowMenu.getElement(), "zIndex", "1");
		PopupPanel.getRootPanel().add(this.btnShowMenu);

		GlobalEventPreviewManager.getInstance().addEventPreview(this);
	}

	private void updateCenter() {
		this.cx = (this.start.getX() + this.end.getX()) / 2;
		this.cy = (this.start.getY() + this.end.getY()) / 2;
	}

	private boolean deleted = false;

	public void delete() {
		if (!deleted) {
			deleted = true;
			GlobalEventPreviewManager.getInstance().removeEventPreview(this);
			this.optionsPanel.hide();
		}
		super.delete();
	}

	public boolean onEventPreview(Event event) {
		int type = DOM.eventGetType(event);
		switch (type) {
			case Event.ONMOUSEMOVE: {
				int px = PopupPanel.getRootPanel().getAbsoluteLeft();
				int py = PopupPanel.getRootPanel().getAbsoluteTop();
				
				int x = DOM.eventGetClientX(event) + Window.getScrollLeft() - px;
				int y = DOM.eventGetClientY(event) + Window.getScrollTop() - py;
	
				boolean hit = false;
				if (FlyButtonsManager.thisFlyButtonsHolderShown(this) && menuShown) {
					hit = x >= (this.cx - menuOffsetWidth2)
					&& x <= (this.cx + menuOffsetWidth2)
					&& y >= (this.cy - menuOffsetHeight2)
					&& y <= (this.cy + menuOffsetHeight2);
				} else {
					hit = x >= (this.cx - showMenuButtonOffset2 - showMenuButtonAdditionalOffset)
					&& x <= (this.cx + showMenuButtonOffset2 + showMenuButtonAdditionalOffset)
					&& y >= (this.cy - showMenuButtonOffset2 - showMenuButtonAdditionalOffset)
					&& y <= (this.cy + showMenuButtonOffset2 + showMenuButtonAdditionalOffset);
				}
				
				if (hit)
					FlyButtonsManager.showOwnFlyButtons(this);
				else
					FlyButtonsManager.timedHideOwnFlyButtons(this);
	
			}
		}
		return true;
	}

	public void onPointChange(Point p) {
		this.updateCenter();

		super.onPointChange(p);
	}
	
	public boolean hideFlyButtons() {
		this.optionsPanel.hide();
		this.btnShowMenu.setVisible(false);
		this.menuShown = false;
		
		return true;
	}

	public void showFlyButtons() {
		this.btnShowMenu.setLeft(this.cx - this.showMenuButtonOffset2);
		this.btnShowMenu.setTop(this.cy - this.showMenuButtonOffset2);
		this.btnShowMenu.setVisible(true);
		this.menuShown = false;
	}
}
