package net.pleso.odbui.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hp.hpl.jena.graph.GraphEvents;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;

public class ClientJSONParser {
	
	public void parse(Model model, String inputJSON) throws JSONException {
		JSONObject jsonRoot = null;

		model.notifyEvent(GraphEvents.startRead);
		try {
			jsonRoot = new JSONObject(inputJSON);
			JSONArray items = jsonRoot.getJSONObject("results").getJSONArray(
					"bindings");
			JSONObject item = null;
			for (int i = 0; i < items.length(); i++) {
				item = items.getJSONObject(i);

				String subject = item.getJSONObject("s").getString("value");
				String predicate = item.getJSONObject("p").getString("value");
				String object = item.getJSONObject("o").getString("value");

				String subject_type = item.getJSONObject("s").getString("type");
				String predicate_type = item.getJSONObject("p").getString(
						"type");
				String o_type = item.getJSONObject("o").getString("type");

				String xml_lang = null;
				if (!item.getJSONObject("o").isNull("xml:lang"))
					xml_lang = item.getJSONObject("o").getString("xml:lang");

				if (!predicate_type.equals("uri")) {
					throw new IllegalArgumentException(
							"Predicate type must be 'uri'. Unknown type: "
									+ predicate_type);
				}

				model.add((Resource) createRDFNode(model, subject,
						subject_type, xml_lang), model
						.createProperty(predicate), createRDFNode(model,
						object, o_type, xml_lang));
			}

		} finally {
			model.notifyEvent(GraphEvents.finishRead);
		}
	}
	
	private RDFNode createRDFNode(Model model, String data, String type,
			String xml_lang) throws IllegalArgumentException {
		if (type.equals("uri")) {
			return model.createResource(data);
		} else if (type.equals("bnode")) {
			return model.createResource(new AnonId(data));
		} else if (type.equals("literal")) {
			if (xml_lang == null)
				return model.createLiteral(data);
			else
				return model.createLiteral(data, xml_lang);
		} else
			throw new IllegalArgumentException("Unknown type: " + type);
	}

}
