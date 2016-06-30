package com.wisenut.tea20.tools;

import java.util.List;
import java.util.ArrayList;

import com.wisenut.tea20.types.*;

public class MessageParserForCF
{
	private String content_="";
	private boolean isResponse_ = true;
	
	public MessageParserForCF(String content, boolean isResponse)
	{
		content_ = content;
		isResponse_ = isResponse;
	}
	
	public SocketMessageForCF parse()
	{
		SocketMessageForCF toReturn = new SocketMessageForCF(isResponse_);
		
		String command = getConvertedString(getNodeString(content_, "type"));
		String reqType = getConvertedString(getNodeString(content_, "name"));
		//String reqId = getConvertedString(getNodeString(content_, "request_id"));
		
		toReturn.setCommand(command);
		toReturn.setRequestType(reqType);
		
		List<Pair<String>> fieldValues = new ArrayList< Pair<String> > ();
		try
		{
			//System.out.println(content_);
			fieldValues.add(new Pair<String>("status", getConvertedString(getNodeString(content_, "status"))));
			fieldValues.add(new Pair<String>("category", getConvertedString(getNodeString(content_, "category")))); 			
		}
		catch(Exception e)
		{
			toReturn.setErrorCode("APIL-XXXX");
			toReturn.setErrorMessage("Error during response parsing: " + e.getMessage());
			
			return toReturn;
		}
		
		String [] fields = new String [fieldValues.size()];
		for (int i=0; i<fields.length; i++)
		{
			fields[i] = fieldValues.get(i).key();
		}
		toReturn.setFields(fields);
		for (int i=0; i<fields.length; i++)
		{
			Pair<String> item = fieldValues.get(i);
			toReturn.setValue(item.key(), item.value());
		}
		
		if (isResponse_)
		{
			String errorContent = getNodeString(content_, "error");
			String errCode = getConvertedString(getNodeString(errorContent, "code"));
			String errMessage = getConvertedString(getNodeString(errorContent, "message"));
			toReturn.setErrorCode(errCode);
			toReturn.setErrorMessage(errMessage);
		}
		
		return toReturn;
	}
	
	private String getConvertedString(String str)
	{
		String toReturn = StringTool.replace(str, "&lt;", "<");
		toReturn = StringTool.replace(toReturn, "&gt;", ">");
		toReturn = StringTool.replace(toReturn, "&amp;", "&");
		
		return toReturn;
	}
	
	private String getConvertedXmlNodeString(String str)
	{
		if (null == str || 0 == str.length())
			return "";
		
		StringBuffer toReturn = new StringBuffer();
		int idx = 0;
		int posHead = str.indexOf(SocketMessage.CDATA_HEAD, idx);
		int posTail = 0;
		
		while (posHead >= idx)
		{
			posTail = str.indexOf(SocketMessage.CDATA_TAIL, idx);
			if (posTail < 0) // no CDATA end tag, so breaks
				break;
			
			if (posHead != idx)
			{
				toReturn.append(getConvertedString(str.substring(idx, posHead)));
			}
			idx = posTail + SocketMessage.CDATA_TAIL.length();
			toReturn.append(str.substring(posHead+SocketMessage.CDATA_HEAD.length(), posTail));
			
			posHead = str.indexOf(SocketMessage.CDATA_HEAD, idx);
		}
		
		if (idx < str.length())
		{
			toReturn.append(getConvertedString(str.substring(idx)));
		}
		return toReturn.toString();
	}
	
	private String getNodeString(String str, String field)
	{
		String header = "<"+field+">"; 
		String tail = "</"+field+">";
		String specialCase = "<" + field + " /";
		
		if (str.indexOf(specialCase) >= 0)
			return "";
		
		int startPos = str.indexOf(header);
		if (0 <= startPos)
		{
			startPos += header.length();
		}
		else
		{
			return "";
		}
		int endPos = str.indexOf(tail);
		if (endPos < startPos)
			return "";
		
		return str.substring(startPos, endPos);
	}
	/*
	private List< Pair<String> > getFieldValueString(String str) //throws Exception
	{
		List< Pair<Double> > toReturn = new ArrayList< Pair<Double> > ();
		String header = "<" + field + " ";
		String tail = "</" + field + ">";
		int startPos = 0;
		int endPos = 0;
		int checkedPos = 0;
		while (checkedPos < str.length())
		{
			startPos = str.indexOf(header, checkedPos);
			if (startPos < 0)
				break;
			checkedPos = startPos + header.length();
			int quotPos1 = str.indexOf('"', checkedPos);
			int quotPos2 = str.indexOf('"', quotPos1+1);
			String name = str.substring(quotPos1+1, quotPos2);
			String value = "";
			
			checkedPos = quotPos2 + 1;
			
			int nextPos1 = str.indexOf('/', checkedPos);
			int nextPos2 = str.indexOf('>', checkedPos);
			
			if ((nextPos1 > 0) && (nextPos1 < nextPos2))
			{
				checkedPos = nextPos2 + 1;
			}
			else
			{
				startPos = nextPos2 + 1;
				endPos = str.indexOf(tail, startPos);
				if (endPos < startPos)
					break;
				value = getConvertedXmlNodeString(str.substring(startPos, endPos));
				checkedPos = endPos + tail.length();
			}
			toReturn.add(new Pair<String> (name, value));
		}
		return toReturn;
	}
	*/
}
