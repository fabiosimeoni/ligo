package org.ligo.nodes.binders;

import static org.ligo.nodes.model.impl.Nodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ligo.binders.Binder;
import org.ligo.core.data.Provided;
import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.Node;

public class JSonBinders {
	
	public static final Binder<JSONObject,Provided> JSON_BINDER = new Binder<JSONObject,Provided>() {
		@Override
		public Provided bind(JSONObject jso) {
			return convert(jso);
		}
	};
	
	// to handle top-level json arrays
	public static final Binder<JSONArray,Provided> JSON_ARRAYBINDER = new Binder<JSONArray,Provided>() {
		@Override
		public Provided bind(JSONArray jsa) {
			return convert(jsa);
		}		
	};
	
	
	// dispatch method
	private static Node convert(Object o) {

		if (o instanceof JSONObject)
			return convert((JSONObject)o);
		else if (o instanceof JSONArray)  // what to do with arrays without a contextual name? 
			return convert((JSONArray)o, "value");
		else // primitives
			return l(o);
	}
	
	
	private static Node convert(JSONObject jso) {	
		List<Edge> edges = new ArrayList<Edge>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> names = jso.keys();
		while(names.hasNext()) {
			String name = names.next();
			try {
				Object value = jso.get(name);
				if (value instanceof JSONArray) { // hoist all underlying values one level up
					JSONArray jsa = (JSONArray)value;
					for (int i=0; i<jsa.length(); i++)
						edges.add(e(new QName(name),convert(jsa.get(i))));
				} else  // just attach values here
					edges.add(e(new QName(name),convert(jso.get(name))));
			} catch (JSONException e) {
				// can't happen as we iterate over keys
				e.printStackTrace();
			}
		}
		
		return n(edges.toArray(new Edge[0]));
	}	
	
	// only used for no-context arrays (others are done in-situ in JSONObject converter
	private static Node convert(JSONArray jsa, String label) {
		List<Edge> edges = new ArrayList<Edge>();
		
		for (int i=0; i<jsa.length(); i++)
			try {
				edges.add(e(new QName(label),convert(jsa.get(i))));
			} catch (JSONException e) {
				// can't happen
				e.printStackTrace();
			}
			
		return n(edges.toArray(new Edge[0]));
	}
}
