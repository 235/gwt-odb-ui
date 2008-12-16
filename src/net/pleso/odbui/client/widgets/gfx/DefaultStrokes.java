package net.pleso.odbui.client.widgets.gfx;

import com.google.gwt.core.client.GWT;

public class DefaultStrokes {
	
	private static class Impl {
		
		private static final Stroke redBoldStroke = new Stroke(DefaultColors.red, 10); 
		
		private static final Stroke blackBoldStroke = new Stroke(DefaultColors.black, 8); 
		
		private static final Stroke darkGreyBoldStroke = new Stroke(DefaultColors.darkGrey, 2); 
		
		private static final Stroke darkGreyStroke = new Stroke(DefaultColors.darkGrey, 1);

		public Stroke getRedBoldStroke() {
			return redBoldStroke;
		}

		public Stroke getBlackBoldStroke() {
			return blackBoldStroke;
		}

		public Stroke getDarkGreyBoldStroke() {
			return darkGreyBoldStroke;
		}

		public Stroke getDarkGreyStroke() {
			return darkGreyStroke;
		}
	}
	
	private static class ImplIE6 extends Impl {
		
		private static final Stroke redBoldStroke = new Stroke(DefaultColors.red, 2); 
		
		private static final Stroke blackBoldStroke = new Stroke(DefaultColors.black, 1);

		public Stroke getRedBoldStroke() {
			return redBoldStroke;
		}

		public Stroke getBlackBoldStroke() {
			return blackBoldStroke;
		} 
	}
	
	private static Impl impl = (Impl) GWT.create(Impl.class);

	public static Stroke getRedBoldStroke() {
		return impl.getRedBoldStroke();
	}

	public static Stroke getBlackBoldStroke() {
		return impl.getBlackBoldStroke();
	}

	public static Stroke getDarkGreyBoldStroke() {
		return impl.getDarkGreyBoldStroke();
	}

	public static Stroke getDarkGreyStroke() {
		return impl.getDarkGreyStroke();
	}
	
}
