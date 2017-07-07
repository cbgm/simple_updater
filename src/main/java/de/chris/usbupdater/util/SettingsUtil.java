package de.chris.usbupdater.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import de.chris.usbupdater.model.PathDetail;
import de.chris.usbupdater.model.Settings;
import de.chris.usbupdater.model.UpdateInfo;

public class SettingsUtil {

	
	public static Settings loadSetttings() throws Exception {
		XMLInputFactory factory = XMLInputFactory.newInstance();
		File file = new File("settings.xml");
        XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(file));
        
        PathDetail pathDetail = null;
        UpdateInfo updateInfo = null;
        Settings settings = Settings.getInstance(false);
        String text = "";

        while (reader.hasNext()) {
        	int Event = reader.next();
        	
        	switch (Event) {
				
        		case XMLStreamConstants.START_ELEMENT: {
			
        			if ("Settings".equals(reader.getLocalName())) {
						settings.setAutoCheck(Boolean.parseBoolean(reader.getAttributeValue(0)));
						settings.setAutoStart(Boolean.parseBoolean(reader.getAttributeValue(1)));
						settings.setIntervalTime(Integer.parseInt(reader.getAttributeValue(2)));
					}
					
					if ("UpdateInfo".equals(reader.getLocalName())) {
						updateInfo = new UpdateInfo();
						updateInfo.setKey(reader.getAttributeValue(1));
						updateInfo.setConfigName(reader.getAttributeValue(0));
					}
					
					if ("PathInfo".equals(reader.getLocalName())) {
			            pathDetail = new PathDetail();
					}
					break;
				}
				
        		case XMLStreamConstants.CHARACTERS: {
					text = reader.getText().trim();
					break;
				}
        		
        		case XMLStreamConstants.END_ELEMENT: {
        			
        			switch (reader.getLocalName()) {
			        	case "UpdateInfo": {
			        		settings.getUpdateInfos().add(updateInfo);
			        		break;
			        	}
			        	
			        	case "LocalPath": {
			        		pathDetail.setLocalPath(text);
			        		break;
			        	}
			        	
			        	case "ExternalPath": {
			        		pathDetail.setExternalPath(text);
			        		break;
			        	}
			        	
			        	case "PathInfo": {
			        		updateInfo.getPathsDetails().add(pathDetail);
			        		break;
			        	}
        			}
			        break;
        		}
			 }
        }
		return settings;
	}
	
	public static void saveSettings(final Settings settings) {
		DocumentBuilderFactory icFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder icBuilder;
        
        try {
            icBuilder = icFactory.newDocumentBuilder();
            Document doc = icBuilder.newDocument();
            Element settingsElement = doc.createElement("Settings");
        	settingsElement.setAttribute("interval", settings.getIntervalTime().toString());
        	settingsElement.setAttribute("autostart", String.valueOf(settings.isAutoStart()));
        	settingsElement.setAttribute("autocheck", String.valueOf(settings.isAutoCheck()));
            doc.appendChild(settingsElement);
            //Element updateInfosElement = doc.createElement("UpdateInfos");
            //settingsElement.appendChild(updateInfosElement);
 
            for (UpdateInfo updateItem : settings.getUpdateInfos()) {
            	settingsElement.appendChild(getUpdateItem(doc, updateItem));
            } 
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            File settingsFile = new File("settings.xml");
            FileOutputStream fileOutputStream = new FileOutputStream(settingsFile);
            StreamResult streamResult = new StreamResult(fileOutputStream);
            transformer.transform(source, streamResult);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    private static Node getUpdateItem(final Document doc, final UpdateInfo updateItem) {
    	Element updateInfoElement = doc.createElement("UpdateInfo");
    	updateInfoElement.setAttribute("description", String.valueOf(updateItem.getConfigName()));
    	updateInfoElement.setAttribute("key", String.valueOf(updateItem.getKey()));
    	//Element pathInfosElement = doc.createElement("PathInfos");
    	//updateInfoElement.appendChild(pathInfosElement);
    	
    	for (PathDetail pathItem : updateItem.getPathsDetails()) {
    		updateInfoElement.appendChild(getPathItemElement(doc, pathItem));
    	}
    	
		return updateInfoElement;
	}
 
    private static Node getPathItemElement(final Document doc, final PathDetail pathDetail) {
    	Element pathInfoElement = doc.createElement("PathInfo");
    	pathInfoElement.appendChild(getPathItemElements(doc, "LocalPath", pathDetail.getLocalPath()));
    	pathInfoElement.appendChild(getPathItemElements(doc, "ExternalPath", pathDetail.getExternalPath()));
        return pathInfoElement;
    
	}
    
    private static Node getPathItemElements(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
}
