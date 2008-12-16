package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.widgets.gfx.GFX;
import net.pleso.odbui.client.widgets.gfx.Stroke;
import net.pleso.odbui.client.widgets.point.GFXPoint;
import net.pleso.odbui.client.widgets.point.Point;

public class CurvedLine extends CustomLine {
	
	private GFXPoint startVector;
	private GFXPoint endVector;
	
	public CurvedLine(GFX gfx, GFXPoint start, GFXPoint startVector, GFXPoint end, GFXPoint endVector, Stroke stroke) {
		
		if (gfx == null)
			throw new IllegalArgumentException("gfx cant be null");
		if (start == null)
			throw new IllegalArgumentException("start cant be null");
		if (end == null)
			throw new IllegalArgumentException("end cant be null");
		if (startVector == null)
			throw new IllegalArgumentException("startVector cant be null");
		if (endVector == null)
			throw new IllegalArgumentException("endVector cant be null");
		if (stroke == null)
			throw new IllegalArgumentException("stroke cant be null");
		
		this.gfx = gfx;
		
		this.start = start;
		this.end = end;
		this.startVector = startVector;
		this.endVector = endVector;
		
		this.start.addPointEventListener(this);
		this.end.addPointEventListener(this);
		this.startVector.addPointEventListener(this);
		this.endVector.addPointEventListener(this);
		
		this.createPath();
		
		this.setStroke(stroke);
	}
	
	public void onPointChange(Point p) {
		if (p == this.startVector || p == this.endVector || p == this.start || p == this.end)
			updatePath();
	}
	
	protected String getRawPath() {
		return "M" + this.start.getGFXx() + " " + this.start.getGFXy() + 
		" C" + this.startVector.getGFXx() + " " + this.startVector.getGFXy() + " " + 
		this.endVector.getGFXx() + " " + this.endVector.getGFXy() + " " + 
		this.end.getGFXx() + " " + this.end.getGFXy();
	}

	public GFXPoint getStartVector() {
		return startVector;
	}

	public GFXPoint getEndVector() {
		return endVector;
	}
}
