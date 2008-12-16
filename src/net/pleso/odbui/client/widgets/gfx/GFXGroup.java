package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.JavaScriptObject;

public class GFXGroup extends GFX {

	private final JavaScriptObject group;
	
	public GFXGroup(GFX gfx) {
		super(gfx);
		
		this.group = nativeCreateGroup(gfx.getSurface());
	}
	
	private native JavaScriptObject nativeCreateGroup(JavaScriptObject surface) /*-{
		return surface.createGroup();
	}-*/;

	public JavaScriptObject getSurface() {
		return group;
	}
}
