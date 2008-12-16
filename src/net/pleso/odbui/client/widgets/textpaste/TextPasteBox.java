package net.pleso.odbui.client.widgets.textpaste;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TextPasteBox extends DialogBox implements ClickListener {

	private Button btnPaste = new Button("Process");

	private Button btnClose = new Button("Close");

	private VerticalPanel panel = new VerticalPanel();

	private HorizontalPanel buttonPanel = new HorizontalPanel();
	
	private RichTextArea textArea = new RichTextArea();

	public TextPasteBox(String caption, FormResultListener formResultListener) {
		this.formResultListener = formResultListener;
		setText(caption);

		this.btnPaste.addClickListener(this);
		this.buttonPanel.add(this.btnPaste);
		this.buttonPanel.setWidth("100%");

		this.btnClose.addClickListener(this);
		this.buttonPanel.add(this.btnClose);
		
		this.textArea.setWidth("100%");
		this.textArea.setHeight("100%");
		
		// for testing
		this.textArea.setHTML(DOM.getInnerHTML(RootPanel.get("paste-text-testing-content").getElement()));
		this.panel.add(this.textArea);
		this.panel.add(this.buttonPanel);
		this.panel.setCellHeight(this.buttonPanel, "20px");

		this.setWidth("100%");
		this.panel.setWidth("100%");
		this.panel.setHeight("100%");
		this.setWidget(this.panel);
		
		this.setStyleName("textPasteBox");
	}

	public void onClick(Widget sender) {
		if (sender == btnClose)
			this.hide();
		if (sender == btnPaste){
			if (formResultListener != null)
				formResultListener.onSubmit(textArea.getHTML());
			this.hide();
		}
			
	}
	
	public void show() {
		super.show();
		center();
	}
	
	private FormResultListener formResultListener;

	public FormResultListener getFormResultListener() {
		return formResultListener;
	}

	public void setFormResultListener(FormResultListener formResultListener) {
		this.formResultListener = formResultListener;
	}
}
