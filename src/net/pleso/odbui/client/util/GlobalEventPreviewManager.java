package net.pleso.odbui.client.util;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventPreview;

public class GlobalEventPreviewManager implements EventPreview {

	private static GlobalEventPreviewManager instance = new GlobalEventPreviewManager();

	private ArrayList eventPreviews = new ArrayList();

	private GlobalEventPreviewManager() {
		DOM.addEventPreview(this);
	}

	public void pop() {
		DOM.removeEventPreview(this);
		DOM.addEventPreview(this);
	}

	public boolean onEventPreview(Event event) {
		boolean result = true;
		ArrayList copy = (ArrayList) this.eventPreviews.clone();
		for (int i = 0; i < copy.size(); i++) {
			EventPreview eventPreview = (EventPreview) copy.get(i);
			if (this.eventPreviews.contains(eventPreview))
				if (!eventPreview.onEventPreview(event))
					result = false;
		}
		return result;
	}

	public void addEventPreview(EventPreview eventPreview) {
		if (!this.eventPreviews.contains(eventPreview))
			this.eventPreviews.add(eventPreview);
	}

	public void removeEventPreview(EventPreview eventPreview) {
		this.eventPreviews.remove(eventPreview);
	}

	public static GlobalEventPreviewManager getInstance() {
		return instance;
	}
}
