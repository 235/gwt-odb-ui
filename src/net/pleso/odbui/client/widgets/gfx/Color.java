package net.pleso.odbui.client.widgets.gfx;

public class Color {
	private final String color;

	public Color(int red, int green, int blue) {
		this.color = getStringColor(red, green, blue);
	}

	public Color(String color) {
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	private String getStringColor(int red, int green, int blue) {
		return "#" + appendLeadZero(Integer.toHexString(red))
				+ appendLeadZero(Integer.toHexString(green))
				+ appendLeadZero(Integer.toHexString(blue));
	}

	private String appendLeadZero(String hexByte) {
		if (hexByte.length() == 1)
			return "0" + hexByte;
		else
			return hexByte;
	}
}
