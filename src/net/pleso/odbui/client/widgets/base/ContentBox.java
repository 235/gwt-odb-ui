package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.widgets.base.fly_buttons.FlyButtonsManager;
import net.pleso.odbui.client.widgets.base.fly_buttons.IFlyButtonsHolder;
import net.pleso.odbui.client.widgets.base.verticalscrollpanel.VerticalScrollPanel;
import net.pleso.odbui.client.widgets.point.Point;
import net.pleso.odbui.client.widgets.roundedcorners.AbsoluteRoundedCorners;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ContentBox extends PopupPanel implements HasHTML, MouseListener, EventPreview, IFlyButtonsHolder {

	protected Point point;
	protected Point size;
	
	private HTML caption;
	private Widget child;
	private boolean dragging;
	private int dragStartX, dragStartY;
	private FlowPanel panel;
	private SimplePanel captionPanelContainer;
	private HorizontalPanel captionPanel;
	
	private FlyButton btnCollapse;
	private FlyButton btnExtend;
	
	private VerticalScrollPanel scrollPanel;
	
	private SimplePanel backgroundPanel;
	
	private int heightBeforeCollapse;
	
	// TODO розібратись із цим чудом.
	private int captionPanelContainerHeight = 20;
	
	public class CollapseButtonClick implements ClickListener {
		public void onClick(Widget sender) {
			if (child != null) {
				child.setVisible(false);
			}

			btnCollapse.setVisible(false);
			btnExtend.setVisible(true);
			
			heightBeforeCollapse = fix_height.intValue();
			
			setHeight("auto");
			scrollPanel.setHeight("auto");
			
			// TODO resize
			onResize();
			
			size.setY(getOffsetHeight());
		}
	}

	public ContentBox() {
		super();
		
		this.caption = new HTML();
		this.panel = new FlowPanel();
		this.backgroundPanel = new SimplePanel();
		this.backgroundPanel.setStyleName("content-box-background");
		this.panel.add(backgroundPanel);
		this.captionPanel = new HorizontalPanel();
		
		this.btnCollapse = new FlyButton("collapse", "fold-fly-button");
		this.btnCollapse.setLeft(1);
		this.btnCollapse.setTop(1);
		this.btnCollapse.addClickListener(new CollapseButtonClick());
		this.panel.add(this.btnCollapse);
		
		this.btnExtend = new FlyButton("extend", "unfold-fly-button");
		this.btnExtend.setLeft(1);
		this.btnExtend.setTop(1);
		this.btnExtend.addClickListener(new ClickListener(){
			public void onClick(Widget sender) {
				extend();
			}});
		this.panel.add(this.btnExtend);
		
		this.captionPanel.add(this.caption);
		this.captionPanel.setCellWidth(this.caption, "100%");
		this.captionPanel.setCellVerticalAlignment(caption, HorizontalPanel.ALIGN_MIDDLE);
		this.captionPanel.setWidth("100%");
		this.captionPanel.setHeight("100%");
		this.captionPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		
		this.captionPanelContainer = new SimplePanel();
		this.captionPanelContainer.setHeight(captionPanelContainerHeight + "px");
		this.captionPanelContainer.add(captionPanel);
		this.captionPanelContainer.setStyleName("content-box-caption-panel-container");

		this.panel.add(this.captionPanelContainer);
		
		this.scrollPanel = new VerticalScrollPanel();
		this.scrollPanel.setWidth("100%");
		this.panel.add(this.scrollPanel);
		
		this.panel.setStyleName("content-box-internal");
		super.setWidget(panel);

		this.setStyleName("content-box");
		this.caption.setStyleName("content-box-caption");
		this.caption.addMouseListener(this);
		
		// Вияснити нашо тут цей конструктор і чого викликається до цього
		// сетпопап позишн.
		this.point = new Point(0, 0);
		
		// TODO resize
		this.size = new Point(minWidth, minHeight);
		this.setSize(minWidth, minHeight);
		
		initRoundCorners();
	}
	
	private void initRoundCorners(){
		AbsoluteRoundedCorners rc = new AbsoluteRoundedCorners();
		rc.createRoundedCorners(this.panel);
	}

	public String getHTML() {
		return this.caption.getHTML();
	}

	public String getText() {
		return this.caption.getText();
	}
	
	private boolean resizing = false;
	protected boolean isResizing() {
		return resizing;
	}
	private int resizing_x;
	private int resizing_y;
	private int resizing_x_size;
	private int resizing_y_size;
	private boolean resizing_cursor = false;
	
	// resize offset
	private static final int offset = 5;
	
	private static final int minHeight = 40;
	private static final int minWidth = 50;
	
	public boolean onEventPreview(Event event) {
		// We need to preventDefault() on mouseDown events (outside of the
		// DialogBox content) to keep text from being selected when it
		// is dragged.
		
		switch (DOM.eventGetType(event)) {
		case Event.ONMOUSEDOWN: {
			if (DOM.isOrHasChild(this.caption.getElement(), DOM.eventGetTarget(event))) {
				DOM.eventPreventDefault(event);
			} else if (allowResizing) {
				
				int mouse_g_x = DOM.eventGetClientX(event) + Window.getScrollLeft();
				int mouse_g_y = DOM.eventGetClientY(event) + Window.getScrollTop();
				
				resizing_x_size = this.getOffsetWidth();
				resizing_y_size = this.getOffsetHeight();

				int corner_x = this.getAbsoluteLeft() + resizing_x_size;
				int corner_y = this.getAbsoluteTop() + resizing_y_size;
				
				if (mouse_g_x >= corner_x - offset
						&& mouse_g_x < corner_x
						&& mouse_g_y >= corner_y - offset
						&& mouse_g_y < corner_y) {
					resizing = true;
					resizing_x = mouse_g_x;
					resizing_y = mouse_g_y;
					
					setCursorState("se-resize"); 
					
					return false;
				}
			}
			break;
		}
		case Event.ONMOUSEMOVE: {
			if (this.resizing) {
				int mouse_g_x = DOM.eventGetClientX(event) + Window.getScrollLeft();
				int mouse_g_y = DOM.eventGetClientY(event) + Window.getScrollTop();
				
				int dx = mouse_g_x - resizing_x;
				int dy = mouse_g_y - resizing_y;
				
				// TODO resize
				setSize(Math.max(resizing_x_size + dx, minWidth), Math.max(resizing_y_size + dy, minHeight));
				
				onResize();
				
				return false;
			} else {
				int mouse_g_x = DOM.eventGetClientX(event) + Window.getScrollLeft();
				int mouse_g_y = DOM.eventGetClientY(event) + Window.getScrollTop();
				
				int boxX = mouse_g_x - this.getAbsoluteLeft();
				int boxY = mouse_g_y - this.getAbsoluteTop();
				if (this.mustShowFlyButtons(boxX, boxY))
					FlyButtonsManager.showOwnFlyButtons(this);
				else
					FlyButtonsManager.timedHideOwnFlyButtons(this);
				
				if (allowResizing) {
					int corner_x = this.getAbsoluteLeft() + this.getOffsetWidth();
					int corner_y = this.getAbsoluteTop() + this.getOffsetHeight();
					
					if (mouse_g_x >= corner_x - offset
							&& mouse_g_x < corner_x
							&& mouse_g_y >= corner_y - offset
							&& mouse_g_y < corner_y) {
						if (!resizing_cursor) {
							setCursorState("se-resize");
							resizing_cursor = true;
						}
					} else {
						if (resizing_cursor) {
							setCursorState("default");
							resizing_cursor = false;
						}
					}	
				}
				
			}
			break;
		}
		case Event.ONMOUSEUP: {
			if (this.resizing) {
				this.resizing = false;
				setCursorState("default");
			}
			break;
		}
		}

		return super.onEventPreview(event);
	}
	
	protected void extend() {
		if (!this.isExtended()) {
			setHeight(heightBeforeCollapse + "px");
			
			if (child != null) {
				child.setVisible(true);
			}
			
			btnCollapse.setVisible(true);
			btnExtend.setVisible(false);
			
			
			// TODO resize
			onResize();
			
			// TODO невідповідає новій концепції офсета висоти
			this.size.setY(getOffsetHeight());
		}
	}
	
	private static void setCursorState(String cursor_state){
		DOM.setStyleAttribute(RootPanel.get().getElement(), "cursor", cursor_state);
	}

	public void onMouseDown(Widget sender, int x, int y) {
		if (sender == this.caption) {
			dragging = true;
			DOM.setCapture(caption.getElement());
			dragStartX = x;
			dragStartY = y;
			
			this.onStartDrag();
		}
	}

	public void onMouseEnter(Widget sender) {
	}

	public void onMouseLeave(Widget sender) {
	}

	public void onMouseMove(Widget sender, int x, int y) {
		if (sender == this.caption) {
			if (dragging) {
				int absX = x + getAbsoluteLeft();
				int absY = y + getAbsoluteTop();
				
				int newPosX = absX - dragStartX - getRootPanel().getAbsoluteLeft();
				int newPosY = absY - dragStartY - getRootPanel().getAbsoluteTop();
				
				setPopupPosition(newPosX, newPosY);

				doMove();
			}
		}
	}
	
	protected int getPosX(int x) {
		return x;
	}
	
	protected int getPosY(int y) {
		return y;
	}
	
	protected void doMove() {

	}

	public void onMouseUp(Widget sender, int x, int y) {
		if (sender == this.caption) {
			dragging = false;
			DOM.releaseCapture(caption.getElement());
		}
	}

	public boolean remove(Widget w) {
		if (child != w) {
			return false;
		}

		scrollPanel.remove(w);
		return true;
	}

	public void setHTML(String html) {
		caption.setHTML(html);
	}

	public void setText(String text) {
		caption.setText(text);
	}

	public void setWidget(Widget w) {
		// If there is already a widget, remove it.
		if (child != null) {
			scrollPanel.remove(child);
		}

		// Add the widget to the center of the cell.
		if (w != null) {
			scrollPanel.add(w);
		}

		child = w;
	}
	
	// приходиться запамятоваувати висоту, яка встановлюється, щоб потім
	// використати її при розрахунках висот
	// інших елементів. ми не можемо використовувати this.getOffsetHeight(). Він
	// вертає різні значення в різних ситуаціях.
	private Integer fix_height = null;
	
	public Integer getFix_height() {
		return fix_height;
	}
	
	public void setHeight(String height) {
		super.setHeight(height);
		if (height.endsWith("px"))
			fix_height = Integer.valueOf(height.replaceAll("px", ""));
		setScrollPanelHeight();
	}
	
	private void setScrollPanelHeight(){
		int newHeight = this.fix_height.intValue() - this.captionPanelContainerHeight;
		if (newHeight < 0)
			newHeight = 0;
		scrollPanel.setHeight(newHeight + "px");
	}

	public void setPopupPosition(int left, int top) {
		left = this.getPosX(left);
		top = this.getPosY(top);
		super.setPopupPosition(left, top);
		if (this.point == null)
			this.point = new Point(left, top);
		else
			this.point.setPoint(left, top);
	}
	
	protected void onStartDrag() {
		
	}
	
	protected void onResize() {
		
	}
	
	protected void onShow() {
		super.onShow();
		setScrollPanelHeight();
	}

	public int getCaptionPanelContainerHeight() {
		return captionPanelContainerHeight;
	}

	protected VerticalScrollPanel getScrollPanel() {
		return scrollPanel;
	}
	
	private boolean allowResizing = true;
	
	public boolean isAllowResizing() {
		return allowResizing;
	}

	public void setAllowResizing(boolean allowResizing) {
		this.allowResizing = allowResizing;
	}
	
	// Метод викликається коли треба записати стан ноди (кудись після змін)
	public void storeState() {
		
	}

	protected HTML getCaption() {
		return caption;
	}
	
	protected void setSize(int width, int height) {
		this.setWidth(width + "px");
		this.setHeight(height + "px");
		
		this.size.setPoint(width, height);
	}
	
	protected void setWidth(int width) {
		this.setWidth(width + "px");
		this.size.setX(width);
	}
	
	protected void setHeight(int height) {
		this.setHeight(height + "px");
		this.size.setY(height);
	}

	public Point getPoint() {
		return point;
	}

	public Point getSize() {
		return size;
	}

	public boolean isDragging() {
		return dragging;
	}

	public FlowPanel getWidgetPanel() {
		return panel;
	}
	
	protected boolean isExtended() {
		return this.child != null && this.child.isVisible();
	}
	
	/**
	 * Визначає чи мають бути показані випливаючі кнопки при даних координатах
	 * миші.
	 * 
	 * @param boxX
	 *            x координата миші відносно бокса
	 * @param boxY
	 *            y координата миші відносно бокса
	 * @return <code>true</code> якщо випливаючі кнопки мають бути показані
	 */
	protected boolean mustShowFlyButtons(int boxX, int boxY) {
		return this.isShowing() && boxX >= 0 && boxX <= this.size.getX() && boxY >= 0 && boxY <= this.size.getY();
	}
	
	/**
	 * Метод просить показати випливаючі кнопки бокса.
	 * 
	 * @return <code>false</code> якщо метод виконуватись не хоче, тобто
	 *         кнопки або вже показані або операція заборонена
	 */
	public void showFlyButtons() {
		if (this.isExtended()) {
			this.btnCollapse.setVisible(true);
		}
		makeActive();
	}
	
	public boolean hideFlyButtons() {
		if (this.dragging || this.resizing || this.resizing_cursor)
			return false;
		
		if (this.isExtended()) {
			this.btnCollapse.setVisible(false);
		}
		
		makePassive();
		
		return true;
	}
	
	private void makeActive(){
		this.addStyleName("content-box_active");
		this.panel.addStyleName("content-box-internal_active");
		this.backgroundPanel.addStyleName("content-box-background_active");
		this.captionPanelContainer.addStyleName("content-box-caption-panel-container_active");
	}
	
	private void makePassive(){
		this.removeStyleName("content-box_active");
		this.panel.removeStyleName("content-box-internal_active");
		this.backgroundPanel.removeStyleName("content-box-background_active");
		this.captionPanelContainer.removeStyleName("content-box-caption-panel-container_active");
	}
	
}
