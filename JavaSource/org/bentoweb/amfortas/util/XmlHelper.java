package org.bentoweb.amfortas.util;

/*
 * @deprecated
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.resolver.tools.CatalogResolver;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlHelper {
	public static Document createDom(InputSource in, boolean validate)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		factory = DocumentBuilderFactory.newInstance();
		factory = DocumentBuilderFactory.newInstance();
		factory.setAttribute(
				"http://apache.org/xml/features/validation/schema", validate);
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new LogErrorHandler());
		docBuilder.setEntityResolver(new CatalogResolver());
		return docBuilder.parse(in);
	}

	public static InputSource convertElement2InputStream(Document doc)
			throws IOException {
		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		XMLSerializer serializer = new XMLSerializer();
		InputSource in = new InputSource();
		File tmp = new File("tmp");
		serializer.setOutputCharStream(new FileWriter(tmp));
		serializer.serialize(doc);
		in.setByteStream(new FileInputStream(tmp));
		return in;
	}
	

	public XmlHelper() throws ParserConfigurationException {
		DocumentBuilderFactory factory = null;
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new LogErrorHandler());
	}

	public static Document getDocumentFromUrl(URL url) throws SAXException,
			IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = null;
		StringBuffer s = new StringBuffer();
		URLConnection site = url.openConnection();
		InputStream input = site.getInputStream();
		BufferedInputStream in = new BufferedInputStream(input);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null)
			s.append(line + "\n");
		// System.out.println(s.toString());
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);// TODO://make true
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new LogErrorHandler());
		// JaxpParser saxx = new JaxpParser();
		final InputSource is = new InputSource(new StringReader(s.toString()));
		return docBuilder.parse(is);
	}

	public List getTextListFromXPath(URL url, String xpath, String defValue)
			throws SAXException, IOException, ParserConfigurationException {
		StringBuffer s = new StringBuffer();
		URLConnection site = url.openConnection();
		InputStream input = site.getInputStream();
		BufferedInputStream in = new BufferedInputStream(input);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = reader.readLine()) != null)
			s.append(line + "\n");
		final InputSource is = new InputSource(new StringReader(s.toString()));
		return this.getXPathListAsTexts(is, xpath, defValue);
	}

	private List getXPathListAsTexts(InputSource is, String xpath,
			String defValue) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory factory = null;
		factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		docBuilder.setErrorHandler(new LogErrorHandler());
		Document doc = docBuilder.parse(is);
		Element docRoot = doc.getDocumentElement();
		return getXPathListAsTexts(docRoot, xpath);
	}

	public String getTextFromXPath(URL url, String xpath, String defValue)
			throws SAXException, IOException, ParserConfigurationException {
		// StringBuffer s = new StringBuffer();
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		/*
		 * BufferedWriter out = new BufferedWriter(new
		 * OutputStreamWriter(conn.getOutputStream())); out.flush();
		 * out.close();
		 * 
		 * BufferedReader in = new BufferedReader(new
		 * InputStreamReader(conn.getInputStream().)); String line; while ((line =
		 * in.readLine()) != null) { s.append(line); }
		 */
		return this.getTextFromXPath(conn.getInputStream().toString(), xpath,
				defValue);
	}

	public String getTextFromXPath(String docString, String xpath,
			String defValue) throws SAXException, IOException,
			ParserConfigurationException {
		DocumentBuilderFactory factory = null;
		DocumentBuilder docBuilder = null;
		Document doc = docBuilder.parse(docString);
		Element docRoot = doc.getDocumentElement();
		return getXPathText(docRoot, xpath, defValue);
	}

	public String getTextFromXPath(Document doc, String xpath, String defValue) {
		Element docRoot = doc.getDocumentElement();
		return getXPathText(docRoot, xpath, defValue);
	}

	public static String getXPathText(Element docRoot, String xPath,
			String defValue) {
		String text = null;
		try {
			text = XPathAPI.selectNodeList(docRoot, xPath).item(0)
					.getTextContent();
		} catch (Exception e) {
			if (defValue != null)
				text = defValue;
		}
		return text;
	}

	private List getXPathListAsTexts(Element docRoot, String xPath) {
		ArrayList res = new ArrayList();
		try {
			NodeList l = XPathAPI.selectNodeList(docRoot, xPath);
			for (int i = 0; i < l.getLength(); i++)
				res.add(l.item(i).getTextContent());
		} catch (Exception e) {

		}
		return (List) res;
	}
	
	public static String elementToString(Element element)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		OutputFormat of = new OutputFormat("XML","UTF-8",true);
		of.setIndent(1);
		of.setIndenting(true);
		XMLSerializer serializer = new XMLSerializer();
		serializer.setOutputByteStream(out);
		serializer.setOutputFormat(of);
		try {
			serializer.asDOMSerializer();
		} catch (IOException e1) {
			//e1.printStackTrace();
		}
		try {
			serializer.serialize(element);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}
}
