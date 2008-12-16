package net.pleso.odbui.client.widgets.dijit;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHTML;

public class DojoRichTextEditor extends FocusWidget implements HasHTML {
	
	private String elementId = null;
	
	private JavaScriptObject dojoWidget = null;
	
	public void setHeight(String height) {
		super.setHeight(height);
		if (dojoWidget != null){
			nativeSetHeight(dojoWidget, height);
		}
	}
	
	private native void nativeSetHeight(JavaScriptObject dojoWidget, String value)
	/*-{
	 	dojoWidget.height = value;
	 	dojoWidget.iframe.style.height= value;
	}-*/;

	public void setWidth(String width) {
		super.setWidth(width);
		if (dojoWidget != null){
			nativeSetWidth(dojoWidget, width);
		}
	}
	
	private native void nativeSetWidth(JavaScriptObject dojoWidget, String value)
	/*-{
	 	dojoWidget.width = value;
	}-*/;

	private String createUniqueId() {
		return "RichTextEditor_" + System.identityHashCode(this);
	}

	public DojoRichTextEditor(Element element) {
		if (element == null)
			throw new IllegalArgumentException("Input element cant be null.");
		
		elementId = createUniqueId();
		setElement(element);
		DOM.setElementAttribute(this.getElement(), "id", elementId);
		
		if (DOM.getElementById(elementId) == null) {
			throw new IllegalArgumentException("Input element was not added into DOM. Can't wrapp it with Dojo Editor.");
		}
		dojoWidget = dojoParse(elementId);
		setHeight("");
	}
	
	private static native JavaScriptObject dojoParse(String elementId) /*-{
		  return new $wnd.dijit.Editor({}, $wnd.dojo.byId(elementId));
	}-*/;

	public String getHTML() {
		if (dojoWidget == null) {
			return DOM.getInnerHTML(this.getElement());
		} else {
			return nativeGetValue(dojoWidget);
		}
	}

	public void setHTML(String html) {
		if (dojoWidget == null) {
			DOM.setInnerHTML(this.getElement(), html);
		} else {
			nativeSetValue(dojoWidget, html);
		}
	}
	
	private native void nativeSetValue(JavaScriptObject dojoWidget, String value)
	/*-{
	 	dojoWidget.setValue(value);
	}-*/;
	
	private native String nativeGetValue(JavaScriptObject dojoWidget)
	/*-{
	 	return dojoWidget.getValue(false);
	}-*/;


	public String getText() {
		return getHTML();
	}

	public void setText(String text) {
		setHTML(text);
	}

}
