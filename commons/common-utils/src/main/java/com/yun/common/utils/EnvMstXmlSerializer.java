package com.yun.common.utils;


import net.sf.json.xml.XMLSerializer;

public class EnvMstXmlSerializer {
    private static final String START = "<xml>";
    private static final String END = "</xml>";

    public static String xml2Json(String xml) {
        StringBuilder stringBuilder = new StringBuilder();
        if (xml.indexOf("xml") == -1) {
            stringBuilder.append(START);
            stringBuilder.append(xml);
            stringBuilder.append(END);
        } else {
            stringBuilder.append(xml);
        }
        XMLSerializer xmlSerializer = new XMLSerializer();
        return xmlSerializer.read(stringBuilder.toString()).toString();
    }
}
