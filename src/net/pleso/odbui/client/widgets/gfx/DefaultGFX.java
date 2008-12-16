package net.pleso.odbui.client.widgets.gfx;

public class DefaultGFX {
	private static GFX instsnce;

	public static GFX getInstsnce() {
		return instsnce;
	}

	public static void setInstsnce(GFX instsnce) {
		DefaultGFX.instsnce = instsnce;
	}
}
