package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.JavaScriptObject;

public class Stroke {
	private final JavaScriptObject stroke;

	public Stroke(Color color, int width) {
		this.stroke = getNativeStroke(color.getColor(), width);
	}
	
	public static void applyStroke(Shape shape, Stroke stroke) {
		if (shape == null || shape.getShape() == null)
			throw new IllegalArgumentException("shape cant be null");
		
		if (stroke == null)
			throw new IllegalArgumentException("stroke cant be null");
		
		setNativeStroke(shape.getShape(), stroke.stroke);
	}

	private static native JavaScriptObject getNativeStroke(String color_, int width_) /*-{
		return {color: color_, width: width_};
	}-*/;
	
	private static native JavaScriptObject setNativeStroke(JavaScriptObject shape, JavaScriptObject stroke) /*-{
		return shape.setStroke(stroke);
	}-*/;
}
