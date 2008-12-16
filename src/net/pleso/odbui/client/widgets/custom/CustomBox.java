package net.pleso.odbui.client.widgets.custom;

import net.pleso.odbui.client.widgets.base.FlyButton;
import net.pleso.odbui.client.widgets.base.Line;
import net.pleso.odbui.client.widgets.base.PopupPanel;
import net.pleso.odbui.client.widgets.base.fly_buttons.FlyButtonsManager;
import net.pleso.odbui.client.widgets.connectable.ConnectableBox;
import net.pleso.odbui.client.widgets.connectable.ConnectableCollection;
import net.pleso.odbui.client.widgets.gfx.DefaultGFX;
import net.pleso.odbui.client.widgets.gfx.DefaultStrokes;
import net.pleso.odbui.client.widgets.point.GFXPoint;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class CustomBox extends ConnectableBox {

	private FlyButton btnDelete;
	private FlyButton btnAdd;
	private FlyButton btnEdit;
	
	private DeckPanel contentPanel;
	private HTML content;
	private SimplePanel contentContainer;
	private ContentEditControl contentEditControl;

	private FlowPanel panel;

	private boolean addingNode = false;

	private Line addingLine = null;
	private ConnectableBox newBox;

	private int flyButtonOffset = 18;

	private int minEditModeHeight = 250;
	private int minEditModeWidth = 300;

	private class AddButtonMouseListener implements MouseListener {

		public void onMouseDown(Widget sender, int x, int y) {
			addingNode = true;
			DOM.setCapture(sender.getElement());
		}

		public void onMouseEnter(Widget sender) {
		}

		public void onMouseLeave(Widget sender) {
		}

		public void onMouseMove(Widget sender, int x, int y) {
			if (addingNode) {
				int px = PopupPanel.getRootPanel().getAbsoluteLeft();
				int py = PopupPanel.getRootPanel().getAbsoluteTop();

				int cx = sender.getAbsoluteLeft() + x - px;
				int cy = sender.getAbsoluteTop() + y - py;

				if (addingLine == null) {

					GFXPoint basePoint = new GFXPoint();
					basePoint.setPoint(cx, cy);
					GFXPoint addingPoint = new GFXPoint();
					addingPoint.setPoint(cx, cy);

					addingLine = new Line(DefaultGFX.getInstsnce(), basePoint,
							addingPoint, DefaultStrokes.getDarkGreyStroke());
				}

				addingLine.getEnd().setPoint(cx, cy);
			}
		}

		public void onMouseUp(Widget sender, int x, int y) {
			if (addingNode && addingLine != null) {
				int cx = addingLine.getEnd().getX();
				int cy = addingLine.getEnd().getY();

				newBox = ConnectableCollection.getInstance().getBoxByPoint(cx,
						cy);

				if (newBox == null) {
					newBox = createCustomBox();
					newBox.setText("added");

					newBox.setPopupPositionAndShow(new PositionCallback() {
						public void setPosition(int offsetWidth,
								int offsetHeight) {
							newBox.setPopupPosition(addingLine.getEnd().getX()
									- offsetWidth / 2, addingLine.getEnd()
									.getY()
									- offsetHeight / 2);
						}
					});
					newBox.storeState();
					((CustomBox) newBox).doEdit();
				} else if (ConnectableCollection.getInstance()
						.isConnectableBoxPairExists(CustomBox.this, newBox)) {
					newBox = null;
				} else if (newBox == CustomBox.this) {
					newBox = null;
				}

				if (newBox != null) {
					createConnectionWith(newBox);
				}
			}

			addingNode = false;
			if (addingLine != null) {
				addingLine.delete();
				addingLine = null;
			}
			DOM.releaseCapture(sender.getElement());
		}

	}

	public CustomBox() {
		super();

		this.panel = new FlowPanel();
		this.contentPanel = new DeckPanel();
		this.content = new HTML();
		this.content.setStyleName("content");

		this.contentEditControl = new ContentEditControl();

		this.btnDelete = new FlyButton("Delete", "remove-fly-button");
		this.btnDelete.setTop(1);
		this.btnDelete.setRight(1);
		this.btnDelete.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				CustomBox.this.delete();
			}
		});
		this.getWidgetPanel().add(this.btnDelete);

		this.btnAdd = new FlyButton("Add", "create-fly-button");
		this.btnAdd.setRight(-flyButtonOffset);
		this.btnAdd.addMouseListener(new AddButtonMouseListener());
		this.getWidgetPanel().add(this.btnAdd);

		this.btnEdit = new FlyButton("Edit", "edit-fly-button");
		this.btnEdit.setBottom(0);
		this.btnEdit.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				doEdit();
			}
		});
		this.getWidgetPanel().add(this.btnEdit);

		this.contentEditControl.getBtnSave().addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				doUpdate();
			}
		});

		this.contentEditControl.getBtnCancel().addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				switchMode(false);
			}
		});

		this.contentContainer = new SimplePanel();
		this.contentContainer.setStyleName("content-container");
		this.contentContainer.add(content);
		this.contentPanel.add(this.contentContainer);
		this.contentPanel.add(this.contentEditControl);

		this.switchMode(false);
		
		this.panel.add(this.contentPanel);

		super.setWidget(this.panel);
	}

	public void setWidget(Widget w) {
		// .
		// super.setWidget(w);
	}

	private Integer heightBeforeEdit = null;
	private Integer widthBeforeEdit = null;

	private void doEdit() {
		contentEditControl.setContent(getText(), content.getHTML());
		switchMode(true);
	}

	private void doUpdate() {
		content.setHTML(contentEditControl.getContentHTML());
		setText(contentEditControl.getTitleText());
		switchMode(false);
		contentUpdated();
	}

	private boolean editing;

	private void switchMode(boolean edit) {
		this.editing = edit;

		if (edit) {
			contentPanel.showWidget(1);

			boolean resized = false;
			
			widthBeforeEdit = new Integer(getOffsetWidth());
			if (this.getOffsetWidth() < minEditModeWidth) {
				setWidth(minEditModeWidth);
				resized = true;
			}

			heightBeforeEdit = new Integer(getOffsetHeight());
			if (this.getOffsetHeight() < minEditModeHeight) {
				setHeight(minEditModeHeight);
				resized = true;
			}
			
			if (resized)
				this.onResize();
			
			contentEditControl.refreshHeight(getFix_height().intValue()
					- getCaptionPanelContainerHeight());
			contentEditControl.setRichTextEditorFocus(true);
			
			this.btnEdit.setVisible(false);
		} else {
			contentPanel.showWidget(0);

			if (widthBeforeEdit != null || heightBeforeEdit != null) {
				setSize(widthBeforeEdit.intValue(), heightBeforeEdit.intValue());
				this.onResize();
			}
			
			if (FlyButtonsManager.thisFlyButtonsHolderShown(this))
				this.btnEdit.setVisible(true);
		}
		

		this.setAllowResizing(!edit);

		this.setPopupPosition(this.getPosX(this.getLeftPosition()), this
				.getPosY(this.getTopPosition()));

		this.updateConnections();

		this.makeTopmost();
	}

	public void setContent(String html) {
		this.content.setHTML(html);
	}

	public String getContent() {
		return this.content.getHTML();
	}

	protected CustomBox createCustomBox() {
		return new CustomBox();
	}

	protected void contentUpdated() {
	}

	public void createConnectionWith(ConnectableBox newBox) {
		CustomConnector.Create(this, newBox);
	}	
	
	public boolean onEventPreview(Event event) {
		int type = DOM.eventGetType(event);
		switch (type) {
		case Event.ONDBLCLICK: {
			Element target = DOM.eventGetTarget(event);
			// Визначення поведінки даблкліка по кепшну.
			if (DOM.isOrHasChild(this.getCaption().getElement(), target))
				if (this.isExtended()) {
					if (this.editing)
						// Запис якщо редагування.
						this.doUpdate();
					else
						// Редагування якщо просто розгорнуто.
						this.doEdit();
				} else {
					// Розгортання якщо згорнуто.
					this.extend();
				}
		}
		}

		return super.onEventPreview(event);
	}

	public void doMove() {
		super.doMove();
	}

	protected void onResize() {
		this.btnAdd.setTop((this.getOffsetHeight() - flyButtonOffset) / 2);
		this.btnEdit.setLeft((this.size.getX() - flyButtonOffset) / 2);
		
		// TODO перенести ці події у віртуальні перевизначені методи розширення і колапса
		if (FlyButtonsManager.thisFlyButtonsHolderShown(this))
			if (this.btnEdit.isVisible()) {
				if(!this.isExtended())
					this.btnEdit.setVisible(false);
			} else {
				if (this.isExtended() && !this.editing)
					this.btnEdit.setVisible(true);
			}		
		
		super.onResize();
	}

	protected boolean mustShowFlyButtons(int boxX, int boxY) {
		return this.isShowing() && boxX >= 0 && boxX < this.size.getX() + flyButtonOffset && boxY >= 0 && boxY < this.size.getY();
	}

	public boolean hideFlyButtons() {
		if (addingNode || !super.hideFlyButtons())
			return false;

		this.btnDelete.setVisible(false);
		this.btnAdd.setVisible(false);
		this.btnEdit.setVisible(false);
		
		return true;
	}

	public void showFlyButtons() {
		super.showFlyButtons();
		
		this.btnDelete.setVisible(true);
		this.btnAdd.setVisible(true);
		if (!this.editing && this.isExtended())
			this.btnEdit.setVisible(true);
	}

	public void show() {
		super.show();
		
		this.btnAdd.setTop((this.size.getY() - flyButtonOffset) / 2);
		this.btnEdit.setLeft((this.size.getX() - flyButtonOffset) / 2);
	}

}