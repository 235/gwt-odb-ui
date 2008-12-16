package net.pleso.odbui.client.widgets.connectable;

import net.pleso.odbui.client.widgets.anchor.Anchor;
import net.pleso.odbui.client.widgets.base.CurvedLine;
import net.pleso.odbui.client.widgets.gfx.DefaultGFX;
import net.pleso.odbui.client.widgets.gfx.DefaultStrokes;

public class Connector extends CurvedLine {

	private ConnectableBox box1;
	private ConnectableBox box2;

	public Connector(Anchor start, Anchor end, ConnectableBox box1,
			ConnectableBox box2) {
		super(DefaultGFX.getInstsnce(), start.getAnchorPoint(), start
				.getAnchorDirectionPoint(), end.getAnchorPoint(), end
				.getAnchorDirectionPoint(), DefaultStrokes.getDarkGreyBoldStroke());

		start.setAnchorVectorPoint(end.getAnchorPoint());
		end.setAnchorVectorPoint(start.getAnchorPoint());

		//    ?    (   ,   ?     )
		start.onPointChange(start.getAnchorVectorPoint());
		//  -          .
		//this.updatePath();

		if (box1 == null)
			throw new IllegalArgumentException("box1 cant be null.");
		if (box2 == null)
			throw new IllegalArgumentException("box2 cant be null.");
		if (!((box1.hasAnchor(start) && box2.hasAnchor(end)) || (box2
				.hasAnchor(start) && box1.hasAnchor(end))))
			throw new IllegalArgumentException("Incorrect anchors.");
		
		if (ConnectableCollection.getInstance().isConnectableBoxPairExists(box1, box2))
			throw new IllegalArgumentException("Box already connected.");
		
		this.box1 = box1;
		this.box2 = box2;
		
		ConnectableCollection.getInstance().addConnectableBoxPair(this);
	}
	
	private boolean deleted = false;

	public void delete() {
		if (!deleted) {
			deleted = true;
			ConnectableCollection.getInstance().removeBoxPair(this);
		}
		super.delete();
	}

	public ConnectableBox getBox1() {
		return box1;
	}

	public ConnectableBox getBox2() {
		return box2;
	}
}