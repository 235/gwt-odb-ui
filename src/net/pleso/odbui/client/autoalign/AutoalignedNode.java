package net.pleso.odbui.client.autoalign;

import net.pleso.odbui.client.widgets.connectable.ConnectableBox;
import net.pleso.odbui.client.widgets.point.Point;
import net.pleso.odbui.client.widgets.point.PointEventListener;

public class AutoalignedNode implements PointEventListener {

	private ConnectableBox wrappedBox;

	private Point point;
	private Point size;

	private int area_width;
	private int area_height;

	private double m = 1;
	private double r;
	private Vector c = new Vector();
	private Vector f = new Vector();
	private Vector v = new Vector();

	private Vector c_old = new Vector();
	private Vector f_old = new Vector();
	private Vector v_old = new Vector();

	private boolean updatingBox = false;

	/**
	 * @param wrappedBox
	 */
	public AutoalignedNode(ConnectableBox wrappedBox, int area_width,
			int area_height) {
		super();
		this.area_height = area_height;
		this.area_width = area_width;

		this.wrappedBox = wrappedBox;

		this.point = this.wrappedBox.getPoint();
		this.size = this.wrappedBox.getSize();
		
		this.point.addPointEventListener(this);
		this.size.addPointEventListener(this);
		
		this.updateDimensions();
	}

	private void updateDimensions() {
		double r_nom = Math.sqrt(this.size.getX() * this.size.getX()
				+ this.size.getY() * this.size.getY()) / 2;
		this.r = r_nom * AutoalignConsts.k_r;
		this.c.x = this.size.getX() / 2 + this.point.getX();
		this.c.y = this.size.getY() / 2 + this.point.getY();

	}

	public void updateBox() {
		this.updatingBox = true;
		this.wrappedBox.setPopupPosition((int) this.c.x - this.size.getX() / 2,
				(int) this.c.y - this.size.getY() / 2);
		this.wrappedBox.doMove();
		this.updatingBox = false;
	}

	public Vector getC() {
		return this.c;
	}

	public Vector getV() {
		return this.v;
	}

	public double getR() {
		return r;
	}

	public void addForce(Vector force) {
		if (!this.wrappedBox.isDragging())
			f.add(force);
	}

	public Vector getF() {
		return this.f;
	}
	
	public void reserveValues() {
		this.c_old.copy_from(this.c);
		this.f_old.copy_from(this.f);
		this.v_old.copy_from(this.v);
	}
	
	public void restoreValues() {
		this.c.copy_from(this.c_old);
		this.f.copy_from(this.f_old);
		this.v.copy_from(this.v_old);
	}

	public void doMove(double dt) {
		if (!this.wrappedBox.isDragging()) {
			// a = F / m
			f.devide(m);
			// dv = a * dt
			f.multiply(dt);
			// v = v + dv
			v.add(f);
			// c = c + v * dt
			c.x = c.x + v.x * dt;
			c.y = c.y + v.y * dt;

			if (c.x - this.size.getX() / 2 < 0)
				c.x = this.size.getX() / 2;
			if (c.y - this.size.getY() / 2 < 0)
				c.y = this.size.getY() / 2;
			if (c.x + this.size.getX() / 2 > this.area_width)
				c.x = this.area_width - this.size.getX() / 2;
			if (c.y + this.size.getY() / 2 > this.area_height)
				c.y = this.area_height - this.size.getY() / 2;
			
			
		} else {
			this.v.zero();
		}

		f.zero();
	}

	public void lala() {
		// (v + dt * F / m) * dt
		// T = 2*pi*sqrt(l/g)
	}

	public void onPointChange(Point p) {
		if (!this.updatingBox) {
			this.updateDimensions();
			this.v.zero();
			this.f.zero();
		}

	}

	public void onPointDelete(Point p) {
		// видалення забезпечує парент
	}

}
