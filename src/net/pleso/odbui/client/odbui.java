package net.pleso.odbui.client;

import java.util.HashMap;

import net.pleso.odbui.client.autoalign.AutoalignManager;
import net.pleso.odbui.client.autoalign.ConstantsEditor;
import net.pleso.odbui.client.autoalign.FinishAutoalignListener;
import net.pleso.odbui.client.rdf_ds.RDFObject;
import net.pleso.odbui.client.rdf_ds.RDFPredicate;
import net.pleso.odbui.client.rdf_ds.RDFResultSet;
import net.pleso.odbui.client.rdf_ds.RDFTriplet;
import net.pleso.odbui.client.widgets.base.PanScrollPanel;
import net.pleso.odbui.client.widgets.base.PopupPanel;
import net.pleso.odbui.client.widgets.base.SideBar;
import net.pleso.odbui.client.widgets.connectable.ConnectableCollection;
import net.pleso.odbui.client.widgets.connectable.PanNavigator;
import net.pleso.odbui.client.widgets.custom.Node;
import net.pleso.odbui.client.widgets.custom.Relation;
import net.pleso.odbui.client.widgets.gfx.DefaultGFX;
import net.pleso.odbui.client.widgets.gfx.GFX;
import net.pleso.odbui.client.widgets.textpaste.FormResultListener;
import net.pleso.odbui.client.widgets.textpaste.TextPasteBox;
import net.pleso.odbui.client.widgets.textpaste.TextPasteParser;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class odbui implements EntryPoint, ClickListener {

	private final PushButton buttonLoad = new PushButton("Load");
	private final PushButton buttonSave = new PushButton("Save");
	private final PushButton resetData = new PushButton("Reset");
	private final PushButton btnStart = new PushButton("Start autoalign");
	private final PushButton btnStop = new PushButton("Stop autoalign");
	private final PushButton buttonShowTextPasteBox = new PushButton(
			"Paste text");
	private final PushButton btnAddNode = new PushButton("Add node");
	private final PushButton btnDumpRDF = new PushButton("Dump RDF");

	private final HorizontalPanel toolbarPanel = new HorizontalPanel();
	private final VerticalPanel toolbarLeftPanel = new VerticalPanel();
	private final VerticalPanel toolbarCenterPanel = new VerticalPanel();

	private final ConstantsEditor constantsEditor = new ConstantsEditor();

	private final Label statusLabel = new Label();

	private static odbui instance;

	private RDFResultSet rs = new RDFResultSet();
	private AutoalignManager autoalignManager = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Не виходить з яваскрипта викликати інстанс метод. Поки юзаємо статику
		// через попу.
		instance = this;
		subscribeLoad();
	}

	/**
	 * Subscribing to dojo OnLoad event.
	 */
	private native void subscribeLoad() /*-{
	     $wnd.dojo.addOnLoad(@net.pleso.odbui.client.odbui::doDojoLoad());
		}-*/;

	/**
	 * Method called after dojo loaded.
	 */
	public static void doDojoLoad() {
		instance.realOnLoad();
	}

	public void realOnLoad() {

		// Це коренева панель в якій буде вся краса. Її розмір визначає видимий
		// розмір краси (зараз задається в хмтл).
		RootPanel root = RootPanel.get("gfx_holder");

		// Це додаткова панель для організації скрола.
		PanScrollPanel sp = new PanScrollPanel();
		sp.setWidth("100%");
		sp.setHeight("100%");
		sp.setAlwaysShowScrollBars(true);
		DOM.setStyleAttribute(sp.getElement(), "position", "absolute");
		root.add(sp);

		// Це дів в якому безпосередньо буде лежати жфікс.
		FlowPanel gfx_root = new FlowPanel();
		// Це розміри діва в якому буде жфікс. Їх можна завадати якими хоч.
		gfx_root.setWidth("1024px");
		gfx_root.setHeight("700px");
		sp.add(gfx_root);

		// Цей дів покладеться в середину попереднього, і в нього будуть
		// кластись діви нод.
		FlowPanel popup_root = new FlowPanel();
		PopupPanel.setCustomRootPanel(popup_root);
		gfx_root.add(popup_root);

		DefaultGFX.setInstsnce(new GFX(gfx_root));

		new PanNavigator(RootPanel.get("gfx_navigator"), ConnectableCollection
				.getInstance(), sp, DefaultGFX.getInstsnce(), 150);

		btnAddNode.addClickListener(this);
		buttonLoad.addClickListener(this);
		buttonSave.addClickListener(this);
		resetData.addClickListener(this);
		buttonShowTextPasteBox.addClickListener(this);
		this.btnStop.addClickListener(this);
		this.btnStart.addClickListener(this);
		btnDumpRDF.addClickListener(this);

		toolbarPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);

		statusLabel.setStyleName("statusLabel");

		btnAddNode.setStyleName("addnode-button");
		buttonLoad.setStyleName("load-button");
		buttonSave.setStyleName("save-button");
		resetData.setStyleName("reset-button");
		btnStart.setStyleName("start-autoalign-button");
		btnStop.setStyleName("stop-autoalign-button");
		btnDumpRDF.setStyleName("dump-rdf-button");
		buttonShowTextPasteBox.setStyleName("paste-text-button");

		toolbarLeftPanel.add(buttonLoad);
		toolbarLeftPanel.add(buttonSave);
		toolbarLeftPanel.add(resetData);

		toolbarCenterPanel.add(btnDumpRDF);
		toolbarCenterPanel.add(btnStart);
		// toolbarCenterPanel.add(btnStop);
		toolbarCenterPanel.add(btnAddNode);
		toolbarCenterPanel.add(buttonShowTextPasteBox);

		toolbarPanel.add(toolbarLeftPanel);
		toolbarPanel.add(toolbarCenterPanel);

		String html = DOM.getInnerHTML(RootPanel.get("helptext").getElement());
		DOM.setInnerHTML(RootPanel.get("helptext").getElement(), "");
		SimplePanel panel = new SimplePanel();
		DOM.setInnerHTML(panel.getElement(), html);

		toolbarPanel.add(panel);
		RootPanel.get("toolbar").add(toolbarPanel);

		//RootPanel.get("toolbar").add(this.constantsEditor);
		
		SideBar sideBar = new SideBar(buttonShowTextPasteBox);
		sideBar.setHeight(RootPanel.get("sidebar_holder").getOffsetHeight()
				+ "px");
		sideBar.setWidth("100%");
		RootPanel.get("sidebar_holder").add(sideBar);
		
		doGetDataClick();
	}

	private int newNodeCounter = 0;

	public void onClick(Widget sender) {
		if (sender == buttonLoad) {
			doGetDataClick();
		} else if (sender == buttonSave) {
			if (rs != null) {
				sendDataset(rs.toString());
			} else {
				Window.alert("Please, load data first.");
			}
		} else if (sender == resetData) {
			resetData();
		} else if (sender == buttonShowTextPasteBox) {
			TextPasteBox newItem = new TextPasteBox("Paste your content here", 
					new FormResultListener() {

						public void onSubmit(String html) {
							ConnectableCollection.getInstance().deleteAllBoxes();
							TextPasteParser pr = new TextPasteParser(rs.getDataSet());
							pr.parseAndShow(html);
							startAutoalign();
						}
			});
			newItem.show();
		} else if (sender == btnStop) {
			this.stopAutoalign();
		} else if (sender == btnStart) {
			this.startAutoalign();
		} else if (sender == btnAddNode) {
			Node newNode = Node.createCustomBox(rs.getDataSet());
			newNode.setText("new node " + newNodeCounter++);
			newNode.show();
			newNode.storeState();
			startAutoalign();
		}
		if (sender == btnDumpRDF) {
			String moduleRelativeURL = GWT.getModuleBaseURL() + "dumpdata";
			Window.open(moduleRelativeURL, "_blank", "");
		}
	}

	private void doGetDataClick() {
		showStatus("Loading...");

		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, GWT
				.getModuleBaseURL()
				+ "getdata");
		// for known IE bug:
		// http://en.wikipedia.org/wiki/XMLHTTP#Microsoft_Internet_Explorer_cache_issues
		request.setHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
		try {
			request.sendRequest(null, new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
					RootPanel.get("status").remove(statusLabel);
				}

				public void onResponseReceived(Request request,
						Response response) {
					JSONValue json = JSONParser.parse(response.getText());

					ConnectableCollection.getInstance().deleteAllBoxes();

					rs = new RDFResultSet(json.isObject());
					drawGraph();
					hideStatus();
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
		;
	}

	private void drawGraph() {
		HashMap boxes = new HashMap();
		RDFObject[] objects = this.rs.getDataSet().getObjects();
		RDFPredicate related = this.rs.getDataSet().smartGetRDFPredicate(
				"http://pleso.net/schemas/odbui#related");
		RDFPredicate title = this.rs.getDataSet().smartGetRDFPredicate(
				"http://pleso.net/schemas/odbui#title");
		RDFPredicate coordinates = this.rs.getDataSet().smartGetRDFPredicate(
				"http://pleso.net/schemas/odbui#coordinates");
		RDFPredicate content = this.rs.getDataSet().smartGetRDFPredicate(
				"http://pleso.net/schemas/odbui#content");
		RDFPredicate dimensions = this.rs.getDataSet().smartGetRDFPredicate(
				"http://pleso.net/schemas/odbui#dimensions");

		RDFPredicate[] predicates = new RDFPredicate[] { title, coordinates,
				content, dimensions };
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].containsPredicates(predicates)) {
				Node box = new Node(this.rs.getDataSet(), objects[i]);
				box.show();

				boxes.put(objects[i].getValue(), box);
			}
		}

		for (int i = 0; i < objects.length; i++) {
			if (objects[i].containsPredicates(predicates)) {
				RDFTriplet[] ro = objects[i].getMultiTriplets(related);
				Node cb = (Node) boxes.get(objects[i].getValue());
				for (int j = 0; j < ro.length; j++) {
					Node box2 = (Node) boxes.get(ro[j].getObject().getValue());
					if (!ConnectableCollection.getInstance()
							.isConnectableBoxPairExists(cb, box2))
						Relation.Create(this.rs.getDataSet(), ro[j], cb, box2);
				}
			}
		}
	}

	private void sendDataset(String data) {
		GWT.log(data, null);
		showStatus("Saving...");

		StringBuffer postData = new StringBuffer();
		postData.append(URL.encode("data")).append("=").append(
				URL.encodeComponent(data));

		RequestBuilder request2 = new RequestBuilder(RequestBuilder.POST, GWT
				.getModuleBaseURL()
				+ "savedata");
		request2.setHeader("Content-type", "application/x-www-form-urlencoded");

		// for known IE bug:
		// http://en.wikipedia.org/wiki/XMLHTTP#Microsoft_Internet_Explorer_cache_issues
		request2.setHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");

		try {
			request2.sendRequest(postData.toString(), new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
					hideStatus();
				}

				public void onResponseReceived(Request request,
						Response response) {
					hideStatus();
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

	public void stopAutoalign() {
		if (this.autoalignManager != null) {
			this.autoalignManager.stop();
		}
	}

	private void switchAutoalignButoon() {

		if (autoalignManager != null && autoalignManager.isRunning()) {
			this.toolbarCenterPanel.remove(btnStart);
			this.toolbarCenterPanel.insert(btnStop, 1);
		} else {
			this.toolbarCenterPanel.remove(btnStop);
			this.toolbarCenterPanel.insert(btnStart, 1);
		}
	}

	public void startAutoalign() {
		stopAutoalign();

		if (this.autoalignManager == null) {
			this.autoalignManager = new AutoalignManager(ConnectableCollection
					.getInstance());

			this.autoalignManager
					.setFinishAutoalignListener(finishAutoalignListenerImpl);
		}
		
		this.autoalignManager.start();

		this.switchAutoalignButoon();
	}

	private void showStatus(String status) {
		statusLabel.setText(status);
		RootPanel.get("loading-place").add(statusLabel);
	}

	private void hideStatus() {
		RootPanel.get("loading-place").remove(statusLabel);
	}

	private void resetData() {
		showStatus("Reseting...");

		RequestBuilder request2 = new RequestBuilder(RequestBuilder.GET, GWT
				.getModuleBaseURL()
				+ "fileloader");

		// for known IE bug:
		// http://en.wikipedia.org/wiki/XMLHTTP#Microsoft_Internet_Explorer_cache_issues
		request2.setHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");

		try {
			request2.sendRequest("", new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					Window.alert(exception.getMessage());
					hideStatus();
				}

				public void onResponseReceived(Request request,
						Response response) {
					hideStatus();
					doGetDataClick();
				}
			});
		} catch (RequestException e) {
			Window.alert(e.getMessage());
			e.printStackTrace();
		}
	}

	private class FinishAutoalignListenerImpl implements
			FinishAutoalignListener {

		public void onFinish() {
			switchAutoalignButoon();
		}
	}

	private FinishAutoalignListenerImpl finishAutoalignListenerImpl = new FinishAutoalignListenerImpl();
}
