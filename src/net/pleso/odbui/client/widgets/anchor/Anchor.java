package net.pleso.odbui.client.widgets.anchor;

import net.pleso.odbui.client.widgets.point.GFXPoint;
import net.pleso.odbui.client.widgets.point.Point;
import net.pleso.odbui.client.widgets.point.PointEventListener;

public class Anchor implements PointEventListener {
	
	private boolean deleted = false;
	// ����� �������� �� ������� �����. ���������� ��������� ����. ����������� ���.
	private GFXPoint anchorPoint;
	// ����� ��� ������� �������� �������� - ����������� ��� �������. ���������� ��������� ����. ����������� ���.
	private GFXPoint anchorDirectionPoint;
	// �����, ��� ������� �������� ������. ������������ ����. ����������� ����.
	private GFXPoint anchorVectorPoint;
	
	private AnchorEventListenerCollection anchorVectorEventListeners;

	public Anchor() {
		this.anchorPoint = new GFXPoint();
		this.anchorDirectionPoint = new GFXPoint();
		
		this.anchorPoint.addPointEventListener(this);
	}

	public GFXPoint getAnchorPoint() {
		return this.anchorPoint;
	}

	public GFXPoint getAnchorVectorPoint() {
		return this.anchorVectorPoint;
	}
	
	public GFXPoint getAnchorDirectionPoint() {
		return this.anchorDirectionPoint;
	}

	public void setAnchorVectorPoint(GFXPoint vectorPoint) {
		if (this.anchorVectorPoint != vectorPoint) {
			if (this.anchorVectorPoint != null)
				this.anchorVectorPoint.removePointEventListener(this);
				
			this.anchorVectorPoint = vectorPoint;
			
			if (this.anchorVectorPoint != null)
				this.anchorVectorPoint.addPointEventListener(this);
		}
	}
	
	private void doVectorPointChanged() {
		if (this.anchorVectorEventListeners != null)
			this.anchorVectorEventListeners.fireAnchorVectorPointChanged(this);
	}
	
	public void addAnchorVectorEventListener(AnchorEventListener listener) {
		if (this.anchorVectorEventListeners == null) {
			this.anchorVectorEventListeners = new AnchorEventListenerCollection();
		}
		this.anchorVectorEventListeners.add(listener);
	}
	
	public void removeAnchorVectorEventListener(AnchorEventListener listener) {
		if (this.anchorVectorEventListeners != null) {
			this.anchorVectorEventListeners.remove(listener);
		}
	}

	public void onPointChange(Point p) {
		this.doVectorPointChanged();
	}

	public void onPointDelete(Point p) {
		if (p == this.anchorPoint) {
			delete();
		}
	}
	
	public void delete() {
		if (!this.deleted) {
			this.deleted = true;
			
			this.anchorPoint.delete();
			this.anchorDirectionPoint.delete();
			this.anchorVectorPoint.delete();
			
			if (this.anchorVectorEventListeners != null)
				this.anchorVectorEventListeners.fireAnchorDeleted(this);
		}
	}
	
	public boolean isDeleted() {
		return deleted;
	}
}
