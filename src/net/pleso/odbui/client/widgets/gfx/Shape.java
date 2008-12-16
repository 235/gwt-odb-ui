package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class Shape {
	
	protected JavaScriptObject shape;
	protected GFX gfx;
	
	public JavaScriptObject getShape() {
		return shape;
	}
	
	public void setStroke(Stroke stroke) {
		Stroke.applyStroke(this, stroke);
	}
	
	public void setFill(Color color) {
		nativeSetFill(this.shape, color.getColor());
	}
	
	public void remove() {
		nativeRemovePath(this.gfx.getSurface(), this.shape, true);
	}
	
	private static native JavaScriptObject nativeRemovePath(JavaScriptObject surface, JavaScriptObject shape, boolean silently) /*-{
		return surface.remove(shape, silently);
	}-*/;
	
	private static native JavaScriptObject nativeSetFill(JavaScriptObject shape, String color) /*-{
		return shape.setFill(color);
	}-*/;
}
