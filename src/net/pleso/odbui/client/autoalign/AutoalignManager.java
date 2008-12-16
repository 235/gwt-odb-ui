package net.pleso.odbui.client.autoalign;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import net.pleso.odbui.client.widgets.connectable.ConnectableBox;
import net.pleso.odbui.client.widgets.connectable.ConnectableCollection;
import net.pleso.odbui.client.widgets.connectable.ConnectablesEventListener;
import net.pleso.odbui.client.widgets.connectable.Connector;
import net.pleso.odbui.client.widgets.connectable.ConnectableCollection.ConnectableBoxPair;
import net.pleso.odbui.client.widgets.gfx.DefaultGFX;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.IncrementalCommand;

public class AutoalignManager implements IncrementalCommand,
		ConnectablesEventListener {

	private AutoalignedNode[] nodes;
	private AutoalignedNode[][] connections;

	private int area_width;
	private int area_height;

	private Date lastTime;

	private HashMap mapping = new HashMap();

	private boolean running = false;

	private FinishAutoalignListener finishAutoalignListener = null;

	public FinishAutoalignListener getFinishAutoalignListener() {
		return finishAutoalignListener;
	}

	public void setFinishAutoalignListener(
			FinishAutoalignListener finishAutoalignListener) {
		this.finishAutoalignListener = finishAutoalignListener;
	}

	public AutoalignManager(ConnectableCollection connectableCollection) {
		this.area_width = DefaultGFX.getInstsnce().getOffsetWidth();
		this.area_height = DefaultGFX.getInstsnce().getOffsetHeight();

		ArrayList boxes = connectableCollection.getBoxes();
		this.nodes = new AutoalignedNode[boxes.size()];
		for (int i = 0; i < boxes.size(); i++) {
			this.nodes[i] = new AutoalignedNode((ConnectableBox) boxes.get(i),
					this.area_width, this.area_height);
			this.mapping.put(boxes.get(i), this.nodes[i]);
		}

		ArrayList pairs = connectableCollection.getBoxPairs();
		this.connections = new AutoalignedNode[pairs.size()][2];
		for (int i = 0; i < pairs.size(); i++) {
			this.connections[i][0] = (AutoalignedNode) mapping
					.get(((ConnectableBoxPair) pairs.get(i)).getBox1());
			this.connections[i][1] = (AutoalignedNode) mapping
					.get(((ConnectableBoxPair) pairs.get(i)).getBox2());
		}

		connectableCollection.addConnectablesEventListener(this);
	}

	public void start() {
		this.running = true;
		this.forceStop = false;
		this.lastTime = new Date();

		DeferredCommand.addCommand(this);
	}

	private boolean forceStop;

	public void stop() {
		this.forceStop = true;
	}

	private boolean processIteration() {
		this.applyConnectionForce();
		this.applyOutForce();
		this.applyWallForces();
		this.applyResistanceForce();
		boolean rest = this.isSystemInRest();
		this.moveNodes(AutoalignConsts.dt, true);
		
		double v = getMaxVelocity() * AutoalignConsts.dt;
		
		if (!rest) {
			if (v == 0)
				AutoalignConsts.dt = 0.2;
			else
			if (v < AutoalignConsts.min_velocity) {
				double k = v / AutoalignConsts.min_velocity;
				AutoalignConsts.dt = AutoalignConsts.dt / k;
//				GWT.log("dt="+AutoalignConsts.dt+" max_v="+v, null);
//				GWT.log("UP", null);
			} else
			while (v > AutoalignConsts.max_velocity) {
				double k = v / AutoalignConsts.max_velocity;
				AutoalignConsts.dt = AutoalignConsts.dt / k;
//				GWT.log("dt="+AutoalignConsts.dt+" max_v="+v, null);
//				GWT.log("DOWN", null);
				this.moveNodes(AutoalignConsts.dt, true);
				v = getMaxVelocity() * AutoalignConsts.dt;
//				GWT.log("dt="+AutoalignConsts.dt+" max_v="+v, null);
			}
			
			
		} else
			GWT.log("======================================== fin ===", null);
			
		Date currentTime = new Date();
		boolean draw = (currentTime.getTime() - this.lastTime.getTime()) > AutoalignConsts.redraw_interval;
		if (draw) {
			this.lastTime = currentTime;
			this.updateNodes();
			GWT.log("dt="+AutoalignConsts.dt+" max_v="+v, null);
		}

		return rest;
	}

	private boolean isSystemInRest() {
		for (int i = 0; i < this.nodes.length; i++) {
			if (this.nodes[i].getF().length() > AutoalignConsts.min_force)
				return false;
		}
		return true;
	}
	
	private double getMaxVelocity() {
		double maxV = 0;
		for (int i = 0; i < this.nodes.length; i++) {
			double v = this.nodes[i].getV().length();
			if (v > maxV)
				maxV = v;
		}
		return maxV;
	}

	private void applyOutForce() {
		Vector v = new Vector();
		for (int i = 0; i < this.nodes.length - 1; i++)
			for (int j = i + 1; j < this.nodes.length; j++) {
				Vector.minus(v, this.nodes[i].getC(), this.nodes[j].getC());
				if (v.x == 0 && v.y == 0) {
					v.x = 1;
					v.y = 1;
				}
				double length = v.length();
				double d = (this.nodes[i].getR() + this.nodes[j].getR())
						- length;
				if (d > 0) {
					// F = k * x
					v.multiply(AutoalignConsts.k_out * d);
					v.devide(length);

					this.nodes[i].addForce(v);
					v.multiply(-1);
					this.nodes[j].addForce(v);
				}
			}
	}

	private void applyResistanceForce() {
		Vector v = new Vector();
		for (int i = 0; i < this.nodes.length; i++) {
			Vector.multiply(v, this.nodes[i].getV(),
					-AutoalignConsts.velocity_resistance);
			this.nodes[i].addForce(v);
		}
	}

	private void applyConnectionForce() {
		Vector v = new Vector();
		for (int i = 0; i < this.connections.length; i++) {
			Vector.minus(v, this.connections[i][0].getC(),
					this.connections[i][1].getC());

			v.multiply(AutoalignConsts.k_connection);

			this.connections[i][1].addForce(v);
			v.multiply(-1);
			this.connections[i][0].addForce(v);
		}
	}

	private void applyWallForces() {
		Vector n = new Vector(0, 1);
		Vector s = new Vector(0, -1);
		Vector w = new Vector(1, 0);
		Vector e = new Vector(-1, 0);
		Vector v = new Vector();
		for (int i = 0; i < this.nodes.length; i++) {
			Vector c = this.nodes[i].getC();
			double r = this.nodes[i].getR();
			// w
			if (c.x < r) {
				Vector.multiply(v, w, AutoalignConsts.k_out * (r - c.x));
				this.nodes[i].addForce(v);
			}
			// n
			if (c.y < r) {
				Vector.multiply(v, n, AutoalignConsts.k_out * (r - c.y));
				this.nodes[i].addForce(v);
			}
			// e
			if (c.x > this.area_width - r) {
				Vector.multiply(v, e, AutoalignConsts.k_out
						* (c.x - (this.area_width - r)));
				this.nodes[i].addForce(v);
			}
			// s
			if (c.y > this.area_height - r) {
				Vector.multiply(v, s, AutoalignConsts.k_out
						* (c.y - (this.area_height - r)));
				this.nodes[i].addForce(v);
			}

		}
	}

	private void moveNodes(double dt, boolean reserve) {
		for (int i = 0; i < this.nodes.length; i++) {
			if (reserve)
				this.nodes[i].reserveValues();
			else
				this.nodes[i].restoreValues();
			this.nodes[i].doMove(dt);
		}
	}

	private void updateNodes() {
		for (int i = 0; i < this.nodes.length; i++) {
			this.nodes[i].updateBox();
		}
	}

	public boolean execute() {
		boolean abort = this.forceStop || processIteration();
		if (abort) {
			updateNodes();
			this.running = false;
			if (finishAutoalignListener != null)
				finishAutoalignListener.onFinish();
		}
		return !abort;
	}

	public int getArea_width() {
		return area_width;
	}

	public int getArea_height() {
		return area_height;
	}

	public boolean isRunning() {
		return running;
	}

	public void boxAdded(ConnectableBox box) {
		AutoalignedNode[] new_nodes = new AutoalignedNode[this.nodes.length + 1];
		for (int i = 0; i < this.nodes.length; i++)
			new_nodes[i] = this.nodes[i];
		new_nodes[this.nodes.length] = new AutoalignedNode(box,
				this.area_width, this.area_height);
		this.mapping.put(box, new_nodes[this.nodes.length]);

		this.nodes = new_nodes;
	}

	public void boxRemoved(ConnectableBox box) {
		AutoalignedNode node = (AutoalignedNode) this.mapping.get(box);
		AutoalignedNode[] new_nodes = new AutoalignedNode[this.nodes.length - 1];
		int shift = 0;
		for (int i = 0; i < this.nodes.length; i++)
			if (this.nodes[i] == node)
				shift = 1;
			else
				new_nodes[i - shift] = this.nodes[i];
		this.nodes = new_nodes;
		this.mapping.remove(box);
	}

	public void connectionAdded(Connector connector) {
		AutoalignedNode[][] new_connections = new AutoalignedNode[this.connections.length + 1][2];
		for (int i = 0; i < this.connections.length; i++)
			for (int j = 0; j < 2; j++) {
				new_connections[i][j] = this.connections[i][j];
			}
		new_connections[this.connections.length][0] = (AutoalignedNode) mapping
				.get(connector.getBox1());
		new_connections[this.connections.length][1] = (AutoalignedNode) mapping
				.get(connector.getBox2());

		this.connections = new_connections;
	}

	public void connectionRemoved(Connector connector) {
		AutoalignedNode[][] new_connections = new AutoalignedNode[this.connections.length - 1][2];
		
		AutoalignedNode node1 = (AutoalignedNode) this.mapping.get(connector
				.getBox1());
		AutoalignedNode node2 = (AutoalignedNode) this.mapping.get(connector
				.getBox2());

		int shift = 0;

		for (int i = 0; i < this.connections.length; i++)
			if (this.connections[i][0] == node1
					&& this.connections[i][1] == node2)
				shift = 1;
			else
				for (int j = 0; j < 2; j++) {
					new_connections[i - shift][j] = this.connections[i][j];
				}
		
		this.connections = new_connections;
	}
}
