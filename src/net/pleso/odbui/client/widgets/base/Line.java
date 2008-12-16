package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.widgets.gfx.GFX;
import net.pleso.odbui.client.widgets.gfx.Stroke;
import net.pleso.odbui.client.widgets.point.GFXPoint;

public class Line extends CustomLine {
	
	public Line(GFX gfx, GFXPoint start, GFXPoint end, Stroke stroke) {
		
		if (gfx == null)
			throw new IllegalArgumentException("gfx cant be null");
		if (start == null)
			throw new IllegalArgumentException("start cant be null");
		if (end == null)
			throw new IllegalArgumentException("end cant be null");
		if (stroke == null)
			throw new IllegalArgumentException("stroke cant be null");
		
		this.gfx = gfx;
		
		this.start = start;
		this.end = end;
		
		this.start.addPointEventListener(this);
		this.end.addPointEventListener(this);
		
		this.createPath();
		
		this.setStroke(stroke);
	}
	
	protected String getRawPath() {
		return "M" + this.start.getGFXx() + " " + this.start.getGFXy() + 
		" L" + this.end.getGFXx() + " " + this.end.getGFXy();
	}
}
