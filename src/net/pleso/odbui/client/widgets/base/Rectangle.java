package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.widgets.gfx.CustomPath;
import net.pleso.odbui.client.widgets.gfx.GFX;
import net.pleso.odbui.client.widgets.gfx.Stroke;
import net.pleso.odbui.client.widgets.point.Point;
import net.pleso.odbui.client.widgets.point.PointEventListener;

public class Rectangle extends CustomPath implements PointEventListener {
	
	private Point point;
	private Point size;
	
	private boolean deleted = false;
	
	public Rectangle(GFX gfx, Point point, Point size, Stroke stroke) {
		if (gfx == null)
			throw new IllegalArgumentException("gfx cant be null");
		if (point == null)
			throw new IllegalArgumentException("start cant be null");
		if (size == null)
			throw new IllegalArgumentException("end cant be null");
		if (stroke == null)
			throw new IllegalArgumentException("stroke cant be null");
		
		this.gfx = gfx;
		
		this.point = point;
		this.size = size;
	
		this.createPath();
		
		this.point.addPointEventListener(this);
		this.size.addPointEventListener(this);
		
		this.setStroke(stroke);
	}

	public void onPointChange(Point p) {
		updatePath();
	}

	public void onPointDelete(Point p) {
		this.delete();
	}
	
	public void delete() {
		if (!deleted) {
			this.deleted = true;
			
			this.point.delete();
			this.size.delete();
			
			this.remove();
		}
	}
	
	protected String getRawPath() {
		return "M" + this.point.getX() + " " + this.point.getY() + 
		" h" + this.size.getX() + " v" + this.size.getY() + " h-" + this.size.getX() + " Z";
	}

}
