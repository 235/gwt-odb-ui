package net.pleso.odbui.client.widgets.point;


public class GFXPoint extends Point {

	public void setGFXPoint(int x, int y) {
		super.setPoint(x/* + DefaultGFX.getInstsnce().getAbsoluteLeft()*/, y/* + DefaultGFX.getInstsnce().getAbsoluteTop()*/);
	}

	public void setGFXx(int x) {
		super.setX(x/* + DefaultGFX.getInstsnce().getAbsoluteLeft()*/);
	}

	public void setGFXy(int y) {
		super.setY(y/* + DefaultGFX.getInstsnce().getAbsoluteTop()*/);
	}
	
	public int getGFXx() {
		return super.getX();// - DefaultGFX.getInstsnce().getAbsoluteLeft();
	}

	public int getGFXy() {
		return super.getY();// - DefaultGFX.getInstsnce().getAbsoluteTop();
	}
}