package net.pleso.odbui.client.autoalign;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ConstantsEditor extends Composite {

	private Grid panel = new Grid(2, 10);
	
	private TextBox bk_connection = new TextBox();
	private Label lk_connection = new Label("k_conn");
	private TextBox bk_out = new TextBox();
	private Label lk_out = new Label("k_out");
	private TextBox bvelocity_resistance = new TextBox();
	private Label lvelocity_resistance = new Label("vel_res");
	private TextBox bmin_force = new TextBox();
	private Label lmin_force = new Label("min_force");
	private TextBox bmin_velocity = new TextBox();
	private Label min_velocity = new Label("min_vel");
	private TextBox bmax_velocity = new TextBox();
	private Label max_velocity = new Label("max_vel");
	private TextBox bdt = new TextBox();
	private Label ldt = new Label("dt");
	private TextBox bredraw_interval = new TextBox();
	private Label lredraw_interval = new Label("redraw_int");
	private TextBox bk_r = new TextBox();
	private Label lk_r = new Label("k_r");
	
	private Button btnUpdate = new Button("update");
	
	public ConstantsEditor() {
		this.panel.setWidget(0, 0, lk_connection);
		this.panel.setWidget(1, 0, bk_connection);
		bk_connection.setWidth("100px");
		
		this.panel.setWidget(0, 1, lk_out);
		this.panel.setWidget(1, 1, bk_out);
		bk_out.setWidth("100px");
		
		this.panel.setWidget(0, 2, lvelocity_resistance);
		this.panel.setWidget(1, 2, bvelocity_resistance);
		bvelocity_resistance.setWidth("100px");
		
		this.panel.setWidget(0, 3, lmin_force);
		this.panel.setWidget(1, 3, bmin_force);
		bmin_force.setWidth("100px");
		
		this.panel.setWidget(0, 4, min_velocity);
		this.panel.setWidget(1, 4, bmin_velocity);
		bmin_velocity.setWidth("100px");
		
		this.panel.setWidget(0, 5, max_velocity);
		this.panel.setWidget(1, 5, bmax_velocity);
		bmax_velocity.setWidth("100px");
		
		this.panel.setWidget(0, 6, ldt);
		this.panel.setWidget(1, 6, bdt);
		bdt.setWidth("100px");
		
		this.panel.setWidget(0, 7, lredraw_interval);
		this.panel.setWidget(1, 7, bredraw_interval);
		bredraw_interval.setWidth("100px");
		
		this.panel.setWidget(0, 8, lk_r);
		this.panel.setWidget(1, 8, bk_r);
		bk_r.setWidth("100px");
		
		this.panel.setWidget(1, 9, btnUpdate);
		this.btnUpdate.addClickListener(new ClickListener() {
		      public void onClick(Widget sender) {
		    	  updateConsts();
		        }
		      });
		
		initWidget(this.panel);
		
		readConsts();
	}
	
	private void readConsts() {
		bk_connection.setText(String.valueOf(AutoalignConsts.k_connection));
		bk_out.setText(String.valueOf(AutoalignConsts.k_out));
		bvelocity_resistance.setText(String.valueOf(AutoalignConsts.velocity_resistance));
		bmin_force.setText(String.valueOf(AutoalignConsts.min_force));
		bmin_velocity.setText(String.valueOf(AutoalignConsts.min_velocity));
		bmax_velocity.setText(String.valueOf(AutoalignConsts.max_velocity));
		bdt.setText(String.valueOf(AutoalignConsts.dt));
		bredraw_interval.setText(String.valueOf(AutoalignConsts.redraw_interval));
		bk_r.setText(String.valueOf(AutoalignConsts.k_r));
	}
	
	private void updateConsts() {
		AutoalignConsts.k_connection = Double.parseDouble(bk_connection.getText());
		AutoalignConsts.k_out = Double.parseDouble(bk_out.getText());
		AutoalignConsts.velocity_resistance = Double.parseDouble(bvelocity_resistance.getText());
		AutoalignConsts.min_force = Double.parseDouble(bmin_force.getText());
		AutoalignConsts.min_velocity = Double.parseDouble(bmin_velocity.getText());
		AutoalignConsts.max_velocity = Double.parseDouble(bmax_velocity.getText());
		AutoalignConsts.dt = Double.parseDouble(bdt.getText());
		AutoalignConsts.redraw_interval = Integer.parseInt(bredraw_interval.getText());
		AutoalignConsts.k_r = Double.parseDouble(bk_r.getText());
	}
}
