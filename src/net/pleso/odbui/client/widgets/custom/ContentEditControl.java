package net.pleso.odbui.client.widgets.custom;

import net.pleso.odbui.client.widgets.richtext.RichTextEditor;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;

public class ContentEditControl extends Composite {

	private FlowPanel panel;
	private TextBox tbTitle;
	private SimplePanel tbTitleContainer;
	private RichTextEditor taContent;
	
	private HorizontalPanel buttonsPanel;
	private PushButton btnSave;
	private PushButton btnCancel;
	
	private int tbTitleContainerHeight = 28;
	
	public ContentEditControl() {
		this.panel = new FlowPanel();
		this.tbTitle = new TextBox();
		this.buttonsPanel = new HorizontalPanel();
		initButtonsPanel();
		this.tbTitleContainer = new SimplePanel();
		this.tbTitleContainer.setHeight(tbTitleContainerHeight + "px");
		this.tbTitleContainer.add(tbTitle);
		this.taContent = new RichTextEditor();
		this.taContent.setWidth("100%");
		
		this.tbTitle.setStyleName("textbox-title");
		
		this.panel.add(this.tbTitleContainer);
		this.panel.add(this.taContent);
		this.panel.add(this.buttonsPanel);
		
		initWidget(this.panel);
		
		setStyleName("content-edit-control");
		setWidth("100%");
		setHeight("100%");
	}
	
	public void setContent(String title, String content) {
		this.tbTitle.setText(title);
		this.taContent.setHTML(content);
	}
	
	public String getTitleText() {
		return this.tbTitle.getText();
	}
	
	public String getContentHTML() {
		return this.taContent.getHTML();
	}	

	public void refreshHeight(int newFixHeight){
		int h = newFixHeight - tbTitleContainerHeight - buttonsPanel.getOffsetHeight();
		if (h < 0) {
			h = 0;
		}
		this.taContent.setHeight(h + "px");
	}
	
	public void setRichTextEditorFocus(boolean focused){
		taContent.setFocus(focused);
	}

	private void initButtonsPanel(){
		btnSave = new PushButton("Save");
		btnSave.setStyleName("content-edit-control-save-button");
		
		btnCancel = new PushButton("Cancel");
		btnCancel.setStyleName("content-edit-control-cancel-button");
		
		buttonsPanel.setWidth("100%");
		buttonsPanel.setStyleName("content-edit-control-buttons-panel");
		buttonsPanel.setVerticalAlignment(HorizontalPanel.ALIGN_MIDDLE);
		buttonsPanel.add(btnSave);
		buttonsPanel.add(btnCancel);
		buttonsPanel.setCellWidth(btnCancel, "96px");
		buttonsPanel.setCellHorizontalAlignment(btnSave, HorizontalPanel.ALIGN_RIGHT);
		buttonsPanel.setCellHorizontalAlignment(btnCancel, HorizontalPanel.ALIGN_RIGHT);
	}

	public PushButton getBtnSave() {
		return btnSave;
	}

	public PushButton getBtnCancel() {
		return btnCancel;
	}
}
