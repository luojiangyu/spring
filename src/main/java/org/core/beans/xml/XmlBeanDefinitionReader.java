package org.core.beans.xml;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.core.beans.AbstractBeanDefinitionReader;
import org.core.beans.BeanDefinition;
import org.core.beans.BeanReference;
import org.core.beans.PropertyValue;
import org.core.beans.PropertyValues;
import org.core.beans.factory.support.BeanDefinitionRegistry;
import org.core.beans.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
	public static final String BEAN = "bean";
	public static final String ID = "id";
	public static final String CLASS = "class";
	public static final String PROPERTY = "property";
	public static final String REF = "ref";
	public static final String NAME = "name";
	public static final String VALUE = "value";
	public static final String SCOPE = "scope";
	public static final String LIST = "list";
	public static final String SET = "set";

	public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
		super(beanDefinitionRegistry);
	}

	public int loadBeanDefinitions(Resource resource)   {
		// TODO Auto-generated method stub
		InputStream inputStream = resource.getInputStream();
		int result = 0;
		try{
			result=doLoadBeanDefinitions(inputStream);
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;

	}

	public int loadBeanDefinitions(Resource... resources) {
		int result = 0;
		for (Resource resource : resources) {
			result += loadBeanDefinitions(resource);
		}
		return result;
	}

	public int doLoadBeanDefinitions(InputStream inputStream) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(inputStream);
		return registerBeanDefinition(doc);
	}

	public int registerBeanDefinition(Document doc) {
		Element root = doc.getDocumentElement();
		return parseBeanDefinition(root);
	}

	public int parseBeanDefinition(Element root) {
		NodeList nodeList = root.getChildNodes();
		int count = 0;
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node temp = nodeList.item(i);
			if (temp instanceof Element && BEAN.equals(temp.getNodeName())) {
				count++;
				processBeanDefinition((Element) temp);
			}
		}
		return count;
	}

	public void processBeanDefinition(Element temp) {
		try {
			String id = temp.getAttribute(ID);
			String clazz = temp.getAttribute(CLASS);
			String scope = "singleton";
			if (temp.hasAttribute(SCOPE)) {
				scope = temp.getAttribute(SCOPE);
			}
			BeanDefinition beanDefinition = new BeanDefinition();
			beanDefinition.setClassName(clazz);
			beanDefinition.setScope(scope);
			parsePropertyElements(temp, beanDefinition);
			// BeanDefinition 注册
			getBeanDefinitionRegistry().registryBeanDefinition(id, beanDefinition);
		} catch (Exception e) {

		}
	}

	public void parsePropertyElements(Element element, BeanDefinition beanDefinition) {
		NodeList nodeList = element.getChildNodes();
		PropertyValues pvs = new PropertyValues();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node item = nodeList.item(i);
			if (item instanceof Element) {
				String nodeName = item.getNodeName();
				if (PROPERTY.equals(nodeName)) {
					PropertyValue pv = new PropertyValue();
					String name = ((Element) item).getAttribute(NAME);
					pv.setName(name);
					if (item.hasChildNodes()) {
						/*
						 * //java自带DOM 下标0被占用（比如
						 * <bean id="user"><property name="name" value="ljy"></bean>，下标0指的是<bean id="user"></bean>之间的空白）
						 */
						Node firstNode = item.getChildNodes().item(1);
						if(LIST.equals(firstNode.getNodeName())){
							parsePropertyList((Element) firstNode, pv);
						}else if(SET.equals(firstNode.getNodeName())){
							parsePropertySet((Element) firstNode, pv);
						}
                        
					} else {

						if (((Element) item).hasAttribute(VALUE)) {
							pv.setValue(((Element) item).getAttribute(VALUE));
						} else if (((Element) item).hasAttribute(REF)) {
							BeanReference beanRerence = new BeanReference();
							beanRerence.setType("object");
							beanRerence.setMappingBean(new String[] { ((Element) item).getAttribute(REF) });
							beanRerence.setMappingType(new String[] { "ref" });
							pv.setValue(beanRerence);
						}

					}
					pvs.addPropertyValue(pv);
				}
			}
		}
		beanDefinition.setPvs(pvs);
		beanDefinition.setStatus("0");
	}
	protected void parsePropertyList(Element root,PropertyValue pv){
		parsePropertCollection(root, pv, LIST);
	}
	public void parsePropertySet(Element root,PropertyValue pv){
		parsePropertCollection(root, pv, SET);
	}
	protected void parsePropertCollection(Element root,PropertyValue pv,String type){
		NodeList childs=root.getChildNodes();
		BeanReference beanReference = new BeanReference();
		beanReference.setType(type);
		String [] mappingBean=new String[childs.getLength()];
		String [] mappingType=new String[childs.getLength()];
		int len=0;
		for(int i=0;i<childs.getLength();i++){
			Node item=childs.item(i);
			if(item instanceof Element){
				if(REF.equals(item.getNodeName())){
					mappingType[len]=REF;
					mappingBean[len++]=((Element) item).getAttribute(BEAN);
				}else if(VALUE.equals(item.getNodeName())){
					mappingType[len]=VALUE;
					mappingBean[len++]=item.getFirstChild().getNodeValue();
				}
			}
		}
		beanReference.setMappingType(mappingType);
		beanReference.setMappingBean(mappingBean);
		pv.setValue(beanReference);
	}


}
