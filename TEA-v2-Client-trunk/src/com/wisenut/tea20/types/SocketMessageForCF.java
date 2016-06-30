package com.wisenut.tea20.types;

import java.util.HashMap;
import java.util.Map;

import com.wisenut.tea20.tools.*;

/**
 * Wrapper for Socket Message.
 * (inner class)
 * 
 * @author hkseo@wisenut.co.kr
 */
public class SocketMessageForCF {
	public static final String CDATA_HEAD = "<![CDATA[";
	public static final String CDATA_TAIL = "]]>";

	private String[] fields = null;
	private Map<String, String> fieldValueMap = null;

	private String type = "";
	private String name = "";

	private boolean isResponse = false;
	private String errorCode = "";
	private String errorMessage = "";

	public SocketMessageForCF(boolean isResponse) {
		this.isResponse = isResponse;
		this.fields = new String[0];
		this.fieldValueMap = new HashMap<String, String>();
	}

	public SocketMessageForCF(String[] params) {
		this.fields = params;
		this.fieldValueMap = new HashMap<String, String>();
		initFields();
	}

	public SocketMessageForCF( String commandType, String collectionId, String[] fields) {
		this.isResponse = false;
		this.fieldValueMap = new HashMap<String, String>();
		this.type = commandType;
		this.name = collectionId;
		setFields(fields);
	}

	public SocketMessageForCF(String commandType, String reqType, String reqId,
			String[] fields, String errorCode, String errorMessage) {
		this.isResponse = true;
		this.fieldValueMap = new HashMap<String, String>();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		setFields(fields);
	}
	
	private void initFields() {
		if (null == fields) {
			return;
		}

		for (int i = 0; i < fields.length; i++) {
			if (fieldValueMap.containsKey(fields[i])) {
				fieldValueMap.remove(fields[i]);
			}
			fieldValueMap.put(fields[i], "");
		}
	}

	public void setFields(String[] fields) {
		this.fields = fields;
		initFields();
	}

	public String[] getFields() {
		return fields;
	}

	public void setValue(String field, String value) {
		if (null == field || null == value || null == fieldValueMap || null == fieldValueMap.get(field)) {
			return;
		}

		fieldValueMap.remove(field);
		fieldValueMap.put(field, value);
	}

	public String getValue(String field) {
		if (null == field || null == fieldValueMap || null == fieldValueMap.get(field)) {
			return "";
		}

		return fieldValueMap.get(field);
	}

	private String getModifiedString(String str) {
		String toReturn = str;

		toReturn = StringTool.replace(str, "&", "&amp;");
		toReturn = StringTool.replace(toReturn, "<", "&lt;");
		toReturn = StringTool.replace(toReturn, ">", "&gt;");

		return toReturn;
	}

	private String getXmlNodeString(String str) {
		if (null == str || 0 == str.length()) {
			return "";
		}

		StringBuffer toReturn = new StringBuffer();
		int idx = 0;
		int posHead = str.indexOf(CDATA_HEAD, idx);
		int posTail = 0;

		while (posHead >= idx) {
			posTail = str.indexOf(CDATA_TAIL, idx);
			// no CDATA end tag, so breaks
			if (posTail < 0) {
				break;
			}

			if (posHead != idx) {
				toReturn.append(getModifiedString(str.substring(idx, posHead)));
			}
			idx = posTail + CDATA_TAIL.length();
			toReturn.append(str.substring(posHead, idx));
			posHead = str.indexOf(CDATA_HEAD, idx);
		}

		if (idx < str.length()) {
			toReturn.append(getModifiedString(str.substring(idx)));
		}

		return toReturn.toString();
	}

	private void addNodeString(String field, String value, StringBuffer target) {
		if (null == target || null == value) {
			return;
		}

		if (null == value || 0 == value.length()) {
			target.append('<');
			target.append(field);
			target.append(" />\n");
		} else {
			target.append('<');
			target.append(field);
			target.append('>');
			target.append(getXmlNodeString(value));
			target.append("</");
			target.append(field);
			target.append(">\n");
		}
	}

	public String toString() {
		StringBuffer toReturn = new StringBuffer("");
		if (isResponse) {
			toReturn.append("<response>\n");
		} else {
			toReturn.append("<request>\n");
		}

		toReturn.append('\t');
		addNodeString("type", type, toReturn);

		toReturn.append('\t');
		addNodeString("name", name, toReturn);

		for (int i = 0; i < fields.length; i++) {
			toReturn.append('\t');
			addNodeString(fields[i], fieldValueMap.get(fields[i]), toReturn);
		}

		if (isResponse) {
			toReturn.append("\t");
			if( errorMessage.isEmpty() == false )
			{
				addNodeString("error", errorMessage, toReturn);
				toReturn.append("\t</error>\n");
			}			
			toReturn.append("</response>\n");
		} else {
			toReturn.append("</request>\n");
		}

		return toReturn.toString();
	}

	public String getCommand() {
		return type;
	}

	public void setCommand(String command) {
		this.type = command;
	}

	public String getRequestType() {
		return name;
	}

	public void setRequestType(String requestType) {
		this.name = requestType;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String code) {
		this.errorCode = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}
}
