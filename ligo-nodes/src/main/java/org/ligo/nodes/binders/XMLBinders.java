/**
 * 
 */
package org.ligo.nodes.binders;

import static org.ligo.core.utils.Constants.*;
import static org.ligo.nodes.model.impl.Nodes.*;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ligo.core.binders.api.Binder;
import org.ligo.data.LigoData;
import org.ligo.nodes.model.api.Edge;
import org.ligo.nodes.model.api.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Fabio Simeoni
 *
 */
public class XMLBinders {

	private static final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	static {
		factory.setNamespaceAware(true);
	}
	public static final Binder<Element,LigoData> DOM = new Binder<Element,LigoData>() {
		@Override
		public LigoData bind(Element e) {
			return fromElementRec(e);
		}
	};
	
	public static final Binder<Reader,LigoData> XREADER = new Binder<Reader,LigoData>() {
		@Override
		public LigoData bind(Reader r) {
			try {
				return XSOURCE.bind(new InputSource(r));
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	public static final Binder<InputSource,LigoData> XSOURCE = new Binder<InputSource,LigoData>() {
		@Override
		public LigoData bind(InputSource s) {
			try {
				return fromElementRec(factory.newDocumentBuilder().parse(s).getDocumentElement());
			}
			catch(Exception e) {
				throw new RuntimeException(e);
			}
		}
	};
	
	/**
	 * Transforms a DOM tree into a tree of {@link Node}s.
	 * @param e the {@link Element} at the root of the tree.
	 * @return the tree of {@link Node}s.
	 * @throws Exception if the DOM tree could not transformed.
	 */
	private static Node fromElementRec(Element e) {
		
		List<Edge> edges = new ArrayList<Edge>();
		
		//parse attributes
		NamedNodeMap map =  e.getAttributes();
		for (int i=0; i<map.getLength();i++) {
				String prefix = map.item(i).getPrefix(); 
				if (prefix!=null && prefix.equals("xmlns")) 
					continue;
				else
					edges.add(e(new QName(map.item(i).getNamespaceURI(),map.item(i).getLocalName()),map.item(i).getNodeValue()));
		}
		
		NodeList children = e.getChildNodes();
		if (children.getLength()==1 && children.item(0).getNodeType()==3) {
			Object value = l(children.item(0).getNodeValue());
			if (edges.size()==0)
				return l(children.item(0).getNodeValue());
			else
				edges.add(e(NONAME,value));
		}		
		else {
			
			for (int i=0; i<children.getLength(); i++)
				if (children.item(i).getNodeType()==1) {
					Element child = (Element) children.item(i);
					QName name = new QName(child.getNamespaceURI(),child.getLocalName());
					edges.add(e(name,fromElementRec(child)));
				}
			
		}
			
		
		
		return n(edges.toArray(new Edge[0]));
		
	}

}
