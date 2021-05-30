package com.yun.common.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @ClassName XmlUtils
 * @Description 关于xml解析的工具
 * @Auther wu_xufeng
 * @Date 2020/12/17
 * @Version 1.0
 */
public class XmlUtils {

    /*======================【1】======================*/

    /**
     * <p>
     * 1.DOM方式
     * </p>
     * 用Element方式
     */
    public static void element(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Element element = (Element) list.item(i);
            NodeList childNodes = element.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    //获取节点
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    //获取节点值
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    public static void node(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            NodeList childNodes = node.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    System.out.print(childNodes.item(j).getNodeName() + ":");
                    System.out.println(childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
        }
    }

    /*======================【2】======================*/

    /**
     * @Author: cxx
     * SAX解析DOM
     * 一行一行  Handler
     * startElement
     * endElement
     * @Date: 2018/5/29 20:03
     */
    class SAXDemoHandel extends DefaultHandler {
        //遍历xml文件开始标签
        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            System.out.println("sax解析开始");
        }

        //遍历xml文件结束标签
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            System.out.println("sax解析结束");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals("student")) {
                System.out.println("============开始遍历student=============");
                //System.out.println(attributes.getValue("rollno"));
            } else if (!qName.equals("student") && !qName.equals("class")) {
                System.out.print("节点名称:" + qName + "----");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals("student")) {
                System.out.println(qName + "遍历结束");
                System.out.println("============结束遍历student=============");
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            String value = new String(ch, start, length).trim();
            if (!value.equals("")) {
                System.out.println(value);
            }
        }
    }


    public static void main(String[] args) {

        /*======================【1】======================*/

        //1.创建DocumentBuilderFactory对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //2.创建DocumentBuilder对象
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document d = builder.parse("src/main/resources/demo.xml");
            NodeList sList = d.getElementsByTagName("student");
            //element(sList);
            node(sList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*======================【2】======================*/

        //1.或去SAXParserFactory实例
//        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2.获取SAXparser实例
//        SAXParser saxParser = factory.newSAXParser();
        //创建Handel对象
//        SAXDemoHandel handel = new SAXDemoHandel();
//        saxParser.parse("src/main/resources/demo.xml", handel);

        /*======================【3】======================*/

        /*======================【4】======================*/
    }
}
