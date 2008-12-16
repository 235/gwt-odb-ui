package net.pleso.odbui.client.autoalign;

public class Vector {
	public double x = 0;
	public double y = 0;
	
	public Vector() {}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void copy_from(Vector v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public double length() {
		return Math.sqrt(x*x + y*y);
	}
	
	public void add(Vector v) {
		x += v.x;
		y += v.y;
	}
	
	public void add(int dx, int dy) {
		x += dx;
		y += dy;
	}
	
	public void zero() {
		x = 0;
		y = 0;
	}
	
	public static void minus(Vector result, Vector vector1, Vector vector2) {
		result.x = vector1.x - vector2.x;
		result.y = vector1.y - vector2.y;
	}
	
	public void devide(double d) {
		x /= d;
		y /= d;
	}
	
	public void multiply(double m) {
		x *= m;
		y *= m;
	}
	
	public static void multiply(Vector result, Vector vector, double m) {
		result.x = vector.x * m;
		result.y = vector.y * m;
	}
	
}
