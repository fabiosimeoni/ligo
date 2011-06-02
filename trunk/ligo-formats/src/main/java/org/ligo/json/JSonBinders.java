package org.ligo.json;

import static org.codehaus.jackson.JsonToken.*;
import static org.ligo.core.Constants.*;
import static org.ligo.core.data.impl.DataBuilders.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ligo.binders.Binder;
import org.ligo.core.data.LigoData;
import org.ligo.core.data.LigoObject;
import org.ligo.core.data.impl.NamedData;

public class JSonBinders {
	
	public static final Binder<JSONObject,LigoData> JSON_OBJECT = new Binder<JSONObject,LigoData>() {
		@Override
		public LigoData bind(JSONObject jso) {
			return convert(jso);
		}
	};
	
	// to handle top-level json arrays
	public static final Binder<JSONArray,LigoData> JSON_ARRAY = new Binder<JSONArray,LigoData>() {
		@Override
		public LigoData bind(JSONArray jsa) {
			return convert(jsa);
		}		
	};
	
	
	// dispatch method
	private static LigoData convert(Object o) {

		if (o instanceof JSONObject)
			return convert((JSONObject)o);
		else if (o instanceof JSONArray)  // what to do with arrays without a contextual name? 
			return convert((JSONArray)o, NONAME);
		else // primitives
			return v(o);
	}
	
	
	private static LigoData convert(JSONObject jso) {	
		List<NamedData> data = new ArrayList<NamedData>();
		
		@SuppressWarnings("unchecked")
		Iterator<String> names = jso.keys();
		while(names.hasNext()) {
			String name = names.next();
			try {
				Object value = jso.get(name);
				if (value instanceof JSONArray) { // hoist all underlying values one level up
					JSONArray jsa = (JSONArray)value;
					for (int i=0; i<jsa.length(); i++)
						data.add(n(name,convert(jsa.get(i))));
				} else  // just attach values here
					data.add(n(name,convert(jso.get(name))));
			} catch (JSONException e) {
				// can't happen as we iterate over keys
				e.printStackTrace();
			}
		}
		
		return o(data.toArray(new NamedData[0]));
	}	
	
	// only used for no-context arrays (others are done in-situ in JSONObject converter
	private static LigoObject convert(JSONArray jsa, String name) {
		List<NamedData> data = new ArrayList<NamedData>();
		
		for (int i=0; i<jsa.length(); i++)
			try {
				data.add(n(name,convert(jsa.get(i))));
			} catch (JSONException e) {
				// can't happen
				e.printStackTrace();
			}
			
		return o(data.toArray(new NamedData[0]));
	}
	
	
	////////////////////JACKSON-BASED
	
	
		public static final Binder<Reader,LigoData> JSON_READER = new Binder<Reader,LigoData>() {
			@Override
			public LigoData bind(Reader r) {
				try {
					return parse(jsonFactory.createJsonParser(r));
				}
				catch(Exception e) {
					throw new RuntimeException(e);
				}
				
			}
		};
	
		private static final JsonFactory jsonFactory = new JsonFactory();
		
		public static LigoData parse(JsonParser parser)  {
			
			try {
			     parser.nextToken();
			     return parseRec(parser);
			} 
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		private static LigoData parseRec(JsonParser parser) throws Exception  {
		
			JsonToken token = parser.getCurrentToken();
				
			if(token==null) 
				return null;
			
			List<NamedData> data = new ArrayList<NamedData>();
			
			switch(token) {
				case START_OBJECT:
					while (parser.nextValue()!=END_OBJECT) 
						data.add(n(parser.getCurrentName(),parseRec(parser)));
					break;
				case START_ARRAY: 
					while (parser.nextToken()!=END_ARRAY)
						data.add(n(NONAME,parseRec(parser)));
					break;
				default:
					return v(parser.getText());
			 }
			 return o(data.toArray(new NamedData[0]));
			
		
		}



}
