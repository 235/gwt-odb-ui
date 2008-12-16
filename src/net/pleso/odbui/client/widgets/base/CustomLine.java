package net.pleso.odbui.client.widgets.base;

import net.pleso.odbui.client.widgets.gfx.CustomPath;
import net.pleso.odbui.client.widgets.point.GFXPoint;
import net.pleso.odbui.client.widgets.point.Point;
import net.pleso.odbui.client.widgets.point.PointEventListener;

public abstract class CustomLine extends CustomPath implements PointEventListener {

	protected GFXPoint start;
	protected GFXPoint end;
	
	private boolean deleted = false;
	
	public void onPointChange(Point p) {
		if (p == this.start || p == this.end)
			updatePath();
	}

	public void onPointDelete(Point p) {
		//      -   .
		if (p == this.start || p == this.end)
			this.delete();
	}
	
	public void delete() {
		if (!deleted) {
			this.deleted = true;
			
			this.start.delete();
			this.end.delete();
			
			this.remove();
		}
	}

	public GFXPoint getStart() {
		return start;
	}

	public GFXPoint getEnd() {
		return end;
	}
}
