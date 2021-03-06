package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class CustomPath extends Shape {
	
	protected abstract String getRawPath();
	
	protected void createPath() {
		this.shape = nativeCreatePath(this.gfx.getSurface(), this.getRawPath());
	}
	
	protected void updatePath() {
		nativeSetPath(this.shape, this.getRawPath());
	}
	
	private static native JavaScriptObject nativeCreatePath(JavaScriptObject surface, String rawPath) /*-{
		return surface.createPath(rawPath);
	}-*/;
	
	private static native JavaScriptObject nativeSetPath(JavaScriptObject path, String rawPath) /*-{
		return path.setShape(rawPath);
	}-*/;
}
