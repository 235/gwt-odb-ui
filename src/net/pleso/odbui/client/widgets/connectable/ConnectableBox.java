package net.pleso.odbui.client.widgets.connectable;

import java.util.ArrayList;

import net.pleso.odbui.client.widgets.anchor.Anchor;
import net.pleso.odbui.client.widgets.anchor.AnchorEventListener;
import net.pleso.odbui.client.widgets.base.ContentBox;
import net.pleso.odbui.client.widgets.gfx.DefaultGFX;
import net.pleso.odbui.client.widgets.point.Point;

public class ConnectableBox extends ContentBox implements AnchorEventListener {

	private ArrayList anchors = new ArrayList();

	private int midX;
	private int midY;
	private int leftX;
	private int rightX;
	private int topY;
	private int bottomY;

	public ConnectableBox() {
		super();

		ConnectableCollection.getInstance().addBox(this);
	}

	private void updateBounds() {
		int px = getRootPanel().getAbsoluteLeft();
		int py = getRootPanel().getAbsoluteTop();

		this.midX = this.getAbsoluteLeft() + this.getOffsetWidth() / 2 - px;
		this.midY = this.getAbsoluteTop() + this.getOffsetHeight() / 2 - py;
		this.leftX = this.getAbsoluteLeft() - px;
		this.rightX = this.getAbsoluteLeft() + this.getOffsetWidth() - px;
		this.topY = this.getAbsoluteTop() - py;
		this.bottomY = this.getAbsoluteTop() + this.getOffsetHeight() - py;
	}

	public boolean testHit(int x, int y) {
		return x >= this.getLeftPosition()
				&& x < (this.getLeftPosition() + this.getOffsetWidth())
				&& y >= this.getTopPosition()
				&& y < (this.getTopPosition() + this.getOffsetHeight());
	}

	private void updateOneAnchor(Anchor anchor) {
		Point point = anchor.getAnchorPoint();
		Point directionPoint = anchor.getAnchorDirectionPoint();
		Point vectorPoint = anchor.getAnchorVectorPoint();
		if (vectorPoint != null) {
			boolean nwTop = vectorPoint.getY() <= -vectorPoint.getX() + rightX
					+ topY;
			boolean neTop = vectorPoint.getY() <= vectorPoint.getX() + topY
					- leftX;
			boolean nwBottom = vectorPoint.getY() <= -vectorPoint.getX()
					+ leftX + bottomY;
			boolean neBottom = vectorPoint.getY() <= vectorPoint.getX()
					+ bottomY - rightX;

			if (nwTop && neTop && vectorPoint.getY() < topY) {
				int offset = Math.abs(topY - vectorPoint.getY()) / 2;

				if (directionPoint.setPointSilently(midX, topY - offset)
						|| point.setPointSilently(midX, topY))
					point.doPointChange();
			} else if (nwBottom && !neTop && vectorPoint.getX() < leftX) {
				int offset = Math.abs(leftX - vectorPoint.getX()) / 2;

				if (directionPoint.setPointSilently(leftX - offset, midY)
						|| point.setPointSilently(leftX, midY))
					point.doPointChange();
			} else if (!nwTop && neBottom && vectorPoint.getX() > rightX) {
				int offset = Math.abs(rightX - vectorPoint.getX()) / 2;

				if (directionPoint.setPointSilently(rightX + offset, midY)
						|| point.setPointSilently(rightX, midY))
					point.doPointChange();
			} else if (!nwBottom && !neBottom && vectorPoint.getY() > bottomY) {
				int offset = Math.abs(bottomY - vectorPoint.getY()) / 2;

				if (directionPoint.setPointSilently(midX, bottomY + offset)
						|| point.setPointSilently(midX, bottomY))
					point.doPointChange();
			} else {
				if (directionPoint.setPointSilently(midX, midY)
						|| point.setPointSilently(midX, midY))
					point.doPointChange();
			}
		} else {
			if (directionPoint.setPointSilently(midX, midY)
					|| point.setPointSilently(midX, midY))
				point.doPointChange();
		}
	}

	private void updateConnectionPoint(Anchor anchor) {
		updateBounds();
		updateOneAnchor(anchor);
	}

	private void updateConnectionPoints() {
		updateBounds();
		for (int i = 0; i < this.anchors.size(); i++) {
			updateOneAnchor((Anchor) this.anchors.get(i));
		}
	}

	public void doMove() {
		this.updateConnections();
	}

	public void show() {
		super.show();
		this.updateConnections();
	}

	public void updateConnections() {
		this.updateConnectionPoints();
	}

	public Anchor requestNewAnchor() {
		Anchor newAnchor = new Anchor();
		newAnchor.addAnchorVectorEventListener(this);
		this.anchors.add(newAnchor);

		this.updateConnectionPoint(newAnchor);

		return newAnchor;
	}

	public void anchorVectorPointChanged(Anchor sender) {
		this.updateConnectionPoint(sender);
	}

	public void delete() {
		while (this.anchors.size() > 0) {
			((Anchor) this.anchors.get(0)).delete();
		}
		
		this.point.delete();
		this.size.delete();
		
		this.hide();

		ConnectableCollection.getInstance().removeBox(this);
	}

	public void anchorDeleted(Anchor sender) {
		// .
		// sender.removeAnchorVectorEventListener(this);
		this.anchors.remove(sender);
	}

	public boolean hasAnchor(Anchor anchor) {
		for (int i = 0; i < this.anchors.size(); i++) {
			if (anchor == this.anchors.get(i))
				return true;
		}

		return false;
	}

	protected int getPosX(int x) {
		int px = getRootPanel().getAbsoluteLeft();
		if ((px + x) < DefaultGFX.getInstsnce().getAbsoluteLeft())
			return DefaultGFX.getInstsnce().getAbsoluteLeft() - px;

		if ((px + x + this.getOffsetWidth()) > (DefaultGFX.getInstsnce()
				.getAbsoluteLeft() + DefaultGFX.getInstsnce().getOffsetWidth()))
			return DefaultGFX.getInstsnce().getAbsoluteLeft()
					+ DefaultGFX.getInstsnce().getOffsetWidth()
					- this.getOffsetWidth() - px;

		return x;
	}

	protected int getPosY(int y) {
		int py = getRootPanel().getAbsoluteTop();
		if ((py + y) < DefaultGFX.getInstsnce().getAbsoluteTop())
			return DefaultGFX.getInstsnce().getAbsoluteTop() - py;

		if ((py + y + this.getOffsetHeight()) > (DefaultGFX.getInstsnce()
				.getAbsoluteTop() + DefaultGFX.getInstsnce().getOffsetHeight()))
			return DefaultGFX.getInstsnce().getAbsoluteTop()
					+ DefaultGFX.getInstsnce().getOffsetHeight()
					- this.getOffsetHeight() - py;

		return y;
	}

	public void makeTopmost() {
		ConnectableCollection.getInstance().makeBoxTopmost(this);
	}

	protected void onStartDrag() {
		this.makeTopmost();
		super.onStartDrag();
	}

	protected void onResize() {
		this.updateConnections();
		super.onResize();
	}

}