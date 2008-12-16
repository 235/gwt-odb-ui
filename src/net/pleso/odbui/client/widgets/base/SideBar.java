package net.pleso.odbui.client.widgets.base;

import java.util.ArrayList;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SideBar extends StackPanel {
	
	private class SideBarHeader extends Composite {
		
		PushButton openCloseButton = new PushButton();
		HTML headerItem = new HTML();
		
		private DockPanel panel = new DockPanel();
		
		public SideBarHeader(String headerText){
			headerItem.setStylePrimaryName("sidebar-header-text");
			headerItem.setText(headerText);
			
			setButtonStatus(false);
			
			panel.add(headerItem, DockPanel.WEST);
			panel.add(openCloseButton, DockPanel.EAST);
			initWidget(panel);
			
			panel.setVerticalAlignment(DockPanel.ALIGN_MIDDLE);
			panel.setWidth("100%");
			panel.setCellWidth(headerItem, "100%");
		}
		
		public void setButtonStatus(boolean isOpen){
			if (isOpen) {
				openCloseButton.setStyleName("sidebar-button-minus");
			} else
				openCloseButton.setStyleName("sidebar-button-plus");
		}
	}

	private ArrayList headers = new ArrayList();
	
	private SearchBar searchBar = new SearchBar();
	
	public SideBar(PushButton buttonShowTextPasteBox){
		super();
		
		headers.add(getNewHeader("Search (demo only)"));
		headers.add(getNewHeader("Text to graph"));
		headers.add(getNewHeader("Add node or connection"));
		
		// format "add node or connection"
		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.setHeight("308px");
		scrollPanel.setWidth("249px");
		HTML addNode = new HTML();
		scrollPanel.add(addNode);
		addNode.setStyleName("how-to-add-node");
		addNode.setHTML(DOM.getInnerHTML(RootPanel.get("how-to-add-node").getElement()));
		
		// format "text to graph"
		VerticalPanel text2graphPanel = new VerticalPanel();
		text2graphPanel.add(buttonShowTextPasteBox);
		text2graphPanel.setCellHorizontalAlignment(buttonShowTextPasteBox, VerticalPanel.ALIGN_CENTER);
		HTML textTograph = new HTML();
		text2graphPanel.add(textTograph);
		textTograph.setStyleName("text-to-graph");
		textTograph.setHTML(DOM.getInnerHTML(RootPanel.get("text-to-graph").getElement()));
		
		this.add(searchBar, (SideBarHeader) headers.get(0));
		this.add(text2graphPanel, (SideBarHeader) headers.get(1));
		this.add(scrollPanel, (SideBarHeader) headers.get(2));
	}
	
	public void showStack(int index) {
		super.showStack(index);
		for (int i = 0; i < headers.size(); i++){
			((SideBarHeader) headers.get(i)).setButtonStatus(i == index);
		}
	}

	private SideBarHeader getNewHeader(String headerText){
		SideBarHeader item = new SideBarHeader(headerText);
		item.setStylePrimaryName("sidebar-header");
		item.setWidth("100%");
		return item;
	}
	
	public void add(Widget w, Widget headerWidget) {		
		add(w);

		int index = getWidgetCount() - 1;

		if (index >= getWidgetCount()) {
			return;
		}

		Element body = DOM.getChild(this.getElement(), 0);
		
		Element td = DOM.getChild(DOM.getChild(body, index * 2), 0);
		DOM.appendChild(td, headerWidget.getElement());
	 }
}
