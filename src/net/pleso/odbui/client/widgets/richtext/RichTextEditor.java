package net.pleso.odbui.client.widgets.richtext;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RichTextEditor extends Composite implements HasHTML {
	
	private RichTextArea area = new RichTextArea();
	private RichTextToolbar tb = new RichTextToolbar(area);
	private VerticalPanel p = new VerticalPanel();
	
	public RichTextEditor() {
		p.add(tb);
		p.add(area);
		p.setCellHeight(area, "100%");

		area.setWidth("100%");
		area.setHeight("100%");
		tb.setWidth("100%");
		p.setWidth("100%");
		
		initWidget(p);
		
		setStyleName("richTextEditor");
		setWidth("100%");
	}

	public String getHTML() {
		return area.getHTML();
	}

	public void setHTML(String html) {
		area.setHTML(html);
	}

	public String getText() {
		return area.getText();
	}

	public void setText(String text) {
		area.setText(text);
	}
	
	public void setFocus(boolean focused){
		area.setFocus(focused);
	}

}
