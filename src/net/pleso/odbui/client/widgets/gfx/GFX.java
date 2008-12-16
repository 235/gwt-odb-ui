package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;

public class GFX {
	
	protected final JavaScriptObject surface;
	private final UIObject uiObject;
	
	public GFX(UIObject uiObject, int width, int height) {
		if (uiObject == null)
			throw new IllegalArgumentException("uiObject cant be null.");
		
		this.uiObject = uiObject;
		this.uiObject.setWidth(width + "px");
		this.uiObject.setHeight(height + "px");
		this.surface = nativeCreateSurface(uiObject.getElement(), width, height);
	}
	
	public GFX(UIObject uiObject) {
		if (uiObject == null)
			throw new IllegalArgumentException("uiObject cant be null.");
		
		this.uiObject = uiObject;
		this.surface = nativeCreateSurface(uiObject.getElement(), getOffsetWidth(), getOffsetHeight());
	}
	
	protected GFX(GFX gfx) {
		this.surface = gfx.surface;
		this.uiObject = gfx.uiObject;
	}
	
	private native JavaScriptObject nativeCreateSurface(Element element, int width, int height) /*-{
		return $wnd.dojox.gfx.createSurface(element, width, height);
	}-*/;

	public JavaScriptObject getSurface() {
		return surface;
	}
	
	public int getAbsoluteLeft() {
		return this.uiObject.getAbsoluteLeft();
	}
	
	public int getAbsoluteTop() {
		return this.uiObject.getAbsoluteTop();
	}
	
	public int getOffsetWidth() {
		return this.uiObject.getOffsetWidth();
	}
	
	public int getOffsetHeight() {
		return this.uiObject.getOffsetHeight();
	}

	public UIObject getUiObject() {
		return uiObject;
	}
}
