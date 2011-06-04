package org.ligo.json;

import static org.codehaus.jackson.JsonToken.*;
import static org.ligo.core.utils.Constants.*;
import static org.ligo.data.impl.DataBuilders.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.ligo.core.binders.api.Binder;
import org.ligo.data.LigoData;
import org.ligo.data.impl.NamedData;

public class JSonBinders {
	
	
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
