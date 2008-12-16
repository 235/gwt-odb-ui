package net.pleso.odbui.client.widgets.base.fly_buttons;

import com.google.gwt.user.client.Timer;

public class FlyButtonsHideTimer extends Timer {

	private boolean running = false;
	
	public void run() {
		if (this.running) {
			FlyButtonsManager.hideFlyButtons();
			this.running = false;
		}
	}
	
	public void schedule() {
		if (!this.running) {
			// Це таймаут по якому зникають кнопки з ноди, після того як з неї зійшов курсор.
			this.schedule(500);
			this.running = true;
		}
	}

	public void cancel() {
		if (this.running) {
			super.cancel();
			this.running = false;
		}
	}
	
}
