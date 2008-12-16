package net.pleso.odbui.client.widgets.base;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SearchBar extends Composite {
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private DockPanel searchBox = new DockPanel();
	private VerticalPanel resultsPanel = new VerticalPanel();
	private ScrollPanel resultsScrollPanel = new ScrollPanel();
	
	private TextBox searchTextBox = new TextBox();
	private PushButton searchButton = new PushButton();
	private ListBox searchComboBox = new ListBox();
	
	public SearchBar(){
		// search box
		searchButton.setStyleName("searchbar-search-button");
		searchBox.add(searchTextBox, DockPanel.WEST);
		searchBox.add(searchButton, DockPanel.EAST);
		searchBox.add(searchComboBox, DockPanel.EAST);
		
		searchBox.setCellWidth(searchTextBox, "100%");
		
		searchBox.setCellVerticalAlignment(searchButton, DockPanel.ALIGN_MIDDLE);
		searchBox.setCellVerticalAlignment(searchTextBox, DockPanel.ALIGN_MIDDLE);
		searchBox.setCellVerticalAlignment(searchComboBox, DockPanel.ALIGN_MIDDLE);
		
		searchComboBox.addItem("Web");
		searchComboBox.addItem("Site");
		searchComboBox.addItem("My maps");
		searchComboBox.addItem("This map");
		searchComboBox.setVisibleItemCount(1);
		
		searchBox.setWidth("100%");
		searchTextBox.setStyleName("searchbar-textbox");
		searchTextBox.setText("semantic");
		searchBox.setStyleName("searchbar-searchbox");
		
		// res panel
		resultsScrollPanel.add(resultsPanel);
		// main
		mainPanel.add(searchBox);
		mainPanel.add(resultsScrollPanel);
		resultsScrollPanel.setWidth("100%");
		resultsScrollPanel.setHeight("286px");
		initWidget(mainPanel);
		setStyleName("searchbar");
		
		setWidth("100%");
		
		formatResults();
	}

	private void formatResults(){
		addResult("","<cetner><i>Results are just for illustration purpopses</i></center>","#");
		addResult(
				"<b>Semantics</b> - Wikipedia, the free encyclopedia", 
				"Semantics (Greek, which studies the practical use of signs by agents or communities of interpretation within particular circumstances and contexts. ...",
				"http://en.wikipedia.org/wiki/Semantics");
		
		addResult(
				"W3C <b>Semantics</b> Web Activity", 
				"The Semantic Web provides a common framework that allows data to be shared and reused across application, enterprise, and community boundaries. ...",
				"http://www.w3.org/2001/sw/");
		
		addResult(
				"<b>Semantics</b>, HTML, XHTML, and Structure", 
				"Semantics, HTML, XHTML, and Structure. Helpful basics of HTML article on using the correct HTML markup for the task. Good HTML structure is based on logic, ...",
				"http://brainstormsandraves.com/articles/semantics/structure/");
		
		addResult(
				"Welcome to WWW.SEMANTICWEB.ORG", 
				"Welcome to WWW.SEMANTICWEB.ORG. Click here to enter http://www.ontoworld.org/. | Domain Name Registration and Domain Name Forwarding by mydomain.com ...",
				"http://www.semanticweb.org");
	}
	
	private void addResult(String header, String text, String link){
		if (resultsPanel.getWidgetCount() > 0)
			resultsPanel.add(new HTML("<br/>"));
		resultsPanel.add(new Hyperlink(header, true, link));
		
		HTML txt = new HTML();
		txt.setHTML(text);
		txt.setStyleName("searchbar-result-text");
		
		resultsPanel.add(txt);
	}
}
