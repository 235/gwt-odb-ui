package net.pleso.odbui.client.widgets.textpaste;

import java.util.ArrayList;

import net.pleso.odbui.client.rdf_ds.RDFDataSet;
import net.pleso.odbui.client.widgets.custom.Node;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.XMLParser;

/**
 * Цей клас парсить HTML і візуалізує його у виді графа
 * 
 * @author Scater
 */
public class TextPasteParser {
	
	public TextPasteParser(RDFDataSet dataSet){
		this.dataSet = dataSet;
	}
	
	private RDFDataSet dataSet;
	
	public void parseAndShow(String html){
		//  XML doc has only single root element
		html = addRootDiv(html);
		html = html.replaceAll("<H2>", "<h2>").replaceAll("</H2>", "</h2>").replaceAll("<P>", "<p>").replaceAll("</P>", "</p>");
		Document document = XMLParser.parse(html); 
		
		Node previous_h_node = null;
		Node first_h_node = null;
		ArrayList alonePNodes = new ArrayList();
		
		for (int i = 0; i < document.getDocumentElement().getChildNodes().getLength(); i++){
			// h element
			if (document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("h2")){
				Node new_h_node = createH2Node(document.getDocumentElement().getChildNodes().item(i));
				if (previous_h_node != null){
					new_h_node.createConnectionWith(previous_h_node);
				} else {
					first_h_node = new_h_node;
				}
				previous_h_node = new_h_node;
			}
			
			// p element
			if (document.getDocumentElement().getChildNodes().item(i).getNodeName().equals("p")){
				Node new_p_node = createPNode(document.getDocumentElement().getChildNodes().item(i));
				if (previous_h_node != null){
					new_p_node.createConnectionWith(previous_h_node);
				} else {
					alonePNodes.add(new_p_node);
				}
			}
		}
		
		if (first_h_node != null) {
			for (int i = 0; i < alonePNodes.size(); i++){
				((Node) alonePNodes.get(i)).createConnectionWith(previous_h_node);
			}
		}
	}
	
	private Node createH2Node(com.google.gwt.xml.client.Node h2_node){
		return createGraphNode(h2_node.getFirstChild().getNodeValue(), "");
	}

	private Node createPNode(com.google.gwt.xml.client.Node p_node){
		String first_3_words = "new node";
		for (int j = 0; j < p_node.getChildNodes().getLength(); j++) {
			if (!p_node.getChildNodes().item(j).hasChildNodes()){
				String value = p_node.getChildNodes().item(j).toString();
				String[] f_3_words = value.trim().split(" ", 4);
				for (int k = 0; k < f_3_words.length; k++){
					if (k == 0)
						first_3_words = "";
					if (k < 3)
						first_3_words = first_3_words + f_3_words[k] + " ";
				}
			}
		}
		
		return createGraphNode(first_3_words + "...", p_node.getChildNodes().toString());
	}
	
	private String addRootDiv(String html){
		return "<div>" + html + "</div>";
	}
	
	private Node createGraphNode(String header, String body){
		Node newNode = Node.createCustomBox(dataSet);
		newNode.setText(header);
		newNode.setContent(body);
		newNode.show();
		newNode.storeState();
		return newNode;
	}
}
