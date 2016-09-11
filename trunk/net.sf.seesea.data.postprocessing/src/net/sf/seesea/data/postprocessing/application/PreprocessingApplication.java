/**
Copyright (c) 2013-2015, Jens Kübler
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
 * Neither the name of the <organization> nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package net.sf.seesea.data.postprocessing.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import net.sf.seesea.data.postprocessing.DataPostprocessingActivator;
import net.sf.seesea.data.postprocessing.database.IUploadedData2Contours;

/**
 * This class controls all aspects of the application's execution
 */
public class PreprocessingApplication implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@Override
	public Object start(IApplicationContext context) throws InvalidSyntaxException, SQLException, XMLStreamException {

//		ServiceReference<ConfigurationAdmin> serviceReference = DataPostprocessingActivator.getContext()
//				.getServiceReference(ConfigurationAdmin.class);
//		ConfigurationAdmin configurationAdmin = DataPostprocessingActivator.getContext().getService(serviceReference);
//
//		Map<Object, Object> arguments = context.getArguments();
//		String[] args = (String[]) arguments.get("application.args"); //$NON-NLS-1$
//		String configFile = null;
//		if (args.length > 0) {
//			configFile = args[0];
//		} else {
//			configFile = "config.xml"; //$NON-NLS-1$
//		}
//		try {
//			File file = new File(configFile);
//			System.out.println(file.getAbsolutePath());
//
//			XMLInputFactory xmlif = XMLInputFactory.newInstance();
//			XMLEventReader xmlr = xmlif.createXMLEventReader(configFile, new FileInputStream(file));
////			xmlif.setEventAllocator(new XMLEventAllocatorImpl());
//			
//			List<XMLConfig> configs = new ArrayList<XMLConfig>();
//			XMLConfig xmlConfig = null;
//			boolean singleton = true;
//
//			while (xmlr.hasNext()) {
//				XMLEvent event = xmlr.nextEvent();
//				// Get all "Book" elements as XMLEvent object
//				if (event.isStartElement()) {
//					StartElement startElement = event.asStartElement();
//					if (startElement.getName().getLocalPart() == ("singleton")) {
//						singleton = true;
//					}
//					if (startElement.getName().getLocalPart() == ("multiton")) {
//						singleton = false;
//						for (Iterator iterator = startElement.getAttributes(); iterator.hasNext();) {
//							Attribute attribute = (Attribute) iterator.next();
//							if("id".equals(attribute.getName().getLocalPart())) {
//								xmlConfig.setMultitonID(attribute.getValue());
//							}
//							
//						}
//					}
//					if (startElement.getName().getLocalPart() == ("config")) {
//						xmlConfig = new XMLConfig();
//						configs.add(xmlConfig);
//						for (Iterator iterator = startElement.getAttributes(); iterator.hasNext();) {
//							Attribute attribute = (Attribute) iterator.next();
//							if("persistentIdentifier".equals(attribute.getName().getLocalPart())) {
//								xmlConfig.setConfigID(attribute.getValue());
//							}
//							
//						}
//					}
//					if (startElement.getName().getLocalPart() == ("property")) {
//						for (Iterator iterator = startElement.getAttributes(); iterator.hasNext();) {
//							Attribute attribute = (Attribute) iterator.next();
//							if("key".equals(attribute.getName().getLocalPart())) {
//								Dictionary<String, Object> properties2 = xmlConfig.getProperties();
//								String key = attribute.getValue();
//								String value = xmlr.nextEvent().asCharacters().getData();
//								Object object = properties2.get(key);
//								if(object instanceof Collection) {
//									Collection<Object> collection = (Collection<Object>) object;
//									collection.add(value);
//								} else if(object != null) {
//									List<Object> list = new ArrayList<Object>();
//									list.add(object);
//									list.add(value);
//									properties2.put(key, list);
//								} else {
//									properties2.put(key, value);
//								}
//							}
//							
//						}
//					}
//
//				} else if(event.isEndElement()) {
//					EndElement endElement = event.asEndElement();
//					if(endElement.getName().getLocalPart() == "config") {
//						if(singleton) {
//							Configuration configuration = configurationAdmin.getConfiguration(xmlConfig.getConfigID());
//							configuration.update(xmlConfig.getProperties());
//							configs.clear();
//						} else {
//							for (XMLConfig xmlConfig2 : configs) {
//								Configuration[] configurations = configurationAdmin.listConfigurations(xmlConfig2.getMultitonId() + "=" + xmlConfig.getProperties().get(xmlConfig2.getMultitonId()));
//								if(configurations.length == 0) {
//									Configuration configuration = configurationAdmin.createFactoryConfiguration(xmlConfig2.getConfigID());
//									configuration.update(xmlConfig2.getProperties());
//								} else {
//									for (Configuration configuration : configurations) {
//										configuration.update(xmlConfig2.getProperties());
//									}
//								}
//							}
//							configs.clear();
//						}
//					}
//				}
//			}

//			long time = System.currentTimeMillis();
//			while((time < System.currentTimeMillis() + 1200000)) {
				
				ServiceReference<IUploadedData2Contours> serviceReference2 = DataPostprocessingActivator.getContext()
						.getServiceReference(IUploadedData2Contours.class);
				IUploadedData2Contours uploadedData2Contours = DataPostprocessingActivator.getContext()
						.getService(serviceReference2);
				uploadedData2Contours.processData();
//			}
		
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		// nothing to do
	}

}
