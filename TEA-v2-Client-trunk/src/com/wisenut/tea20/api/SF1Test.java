package com.wisenut.tea20.api;

import java.util.ArrayList;
import java.util.Scanner;

import QueryAPI530.Search;

public class SF1Test {
	public static final String SEARCH_IP = "211.39.140.51";
	public static final int SEARCH_PORT = 7000;
	public static final int SEARCH_TIMEOUT = 20000;
	
	public SF1Test(){
		
	}
	
	public ArrayList<String> search(String query, int listNo, int isDebug){
		boolean debug = false;
		if(isDebug != 0) debug = true;
		
		ArrayList<String> docidList = new ArrayList<String>();
		
		Search search = new Search();
		
		String collection = "article";
		String sort = "RANK/DESC,UID/DESC";
		
		int pageNum = 0;
		
		String documentFields = "DOCID,TITLE,CONTENT_PLAIN";
		String searchFields = "DOCID,TITLE,CONTENT_PLAIN,KEYWORD";
		
		int ret = 0;
		
		ret = search.w3SetCodePage("UTF-8");
		ret = search.w3SetQueryLog(1);
		ret = search.w3SetCommonQuery(query, 0);
		
		String[] collectionArr = collection.split(",");
		for(String col : collectionArr){
			if(debug) System.out.println(" - collection : " + col);			
			ret = search.w3AddCollection(col);
			
			if(debug) System.out.println(" - ranking : basic, rpf, 10000");
			ret = search.w3SetRanking(col, "basic", "rpf", 10000);
			
			if(debug) System.out.println(" - highlight : 1,1");
			ret = search.w3SetHighlight(col, 1, 1);
			
			if(debug) System.out.println(" - sort : " + sort);
			ret = search.w3SetSortField(col, sort);
			
			if(debug) System.out.println(" - query analyzer : 1,1,1,1");
			ret = search.w3SetQueryAnalyzer(col, 1, 1, 1, 1);
			
			if(debug) System.out.println(" - search fields : " + searchFields);
			ret = search.w3SetSearchField(col, searchFields);
			
			if(debug) System.out.println(" - document fields : " + documentFields);
			ret = search.w3SetDocumentField(col, documentFields);
			
			if(debug) System.out.println(" - page info : " + pageNum + ", " + listNo);
			ret = search.w3SetPageInfo(col, pageNum, listNo);
		}
		
		if(debug) System.out.println(" - search ip : " + SEARCH_IP);
		if(debug) System.out.println(" - search port : " + SEARCH_PORT);
		if(debug) System.out.println(" - search timeout : " + SEARCH_TIMEOUT);
		ret = search.w3ConnectServer(SEARCH_IP, SEARCH_PORT, SEARCH_TIMEOUT);
		
		ret = search.w3ReceiveSearchQueryResult(0);
		if(ret != 0) {
            System.out.println(search.w3GetErrorInfo() + " (Error Code : " + search.w3GetError() + " )");
            return null;
        }
		
		int totalResultCount = 0;
		for(String col : collectionArr){
			totalResultCount += search.w3GetResultTotalCount(col);
		}
		
		System.out.println("############################################# ");
		System.out.println("### Query : " + query);
		System.out.println("### Total Result Count : " + totalResultCount);
		System.out.println("############################################# ");
		int count = search.w3GetResultCount(collection);
		for(int i=0; i<count; i++){
			String docid = search.w3GetField(collection, "DOCID", i);
			docid += "$!$" + search.w3GetField(collection, "TITLE", i).trim();
			docid += "$!$" + search.w3GetField(collection, "CONTENT_PLAIN", i).trim();
			docidList.add(docid);
		}
		
		return docidList;
	}
	
	public ArrayList<String> searchDoc(String query, int listNo, int isDebug){
		boolean debug = false;
		if(isDebug != 0) debug = true;
		
		ArrayList<String> docidList = new ArrayList<String>();
		
		Search search = new Search();
		
		String collection = "article";
		String sort = "RANK/DESC,UID/DESC";
		
		int pageNum = 0;
		
		String documentFields = "DOCID,TITLE,CONTENT_PLAIN";
		String searchFields = "TITLE";
		
		int ret = 0;
		
		ret = search.w3SetCodePage("UTF-8");
		ret = search.w3SetQueryLog(1);
		//ret = search.w3SetCommonQuery("", 0);
		
		String[] collectionArr = collection.split(",");
		for(String col : collectionArr){
			if(debug) System.out.println(" - collection : " + col);			
			ret = search.w3AddCollection(col);
			
			if(debug) System.out.println(" - ranking : basic, rpf, 10000");
			ret = search.w3SetRanking(col, "basic", "rpf", 10000);
			
			if(debug) System.out.println(" - highlight : 1,1");
			ret = search.w3SetHighlight(col, 1, 1);
			
			if(debug) System.out.println(" - sort : " + sort);
			ret = search.w3SetSortField(col, sort);
			
			if(debug) System.out.println(" - query analyzer : 1,1,1,1");
			ret = search.w3SetQueryAnalyzer(col, 1, 1, 1, 1);
			
			if(debug) System.out.println(" - search fields : " + searchFields);
			ret = search.w3AddSearchField(col, searchFields);
			
			if(debug) System.out.println(" - document fields : " + documentFields);
			ret = search.w3SetDocumentField(col, documentFields);
			
			if(debug) System.out.println(" - page info : " + pageNum + ", " + listNo);
			ret = search.w3SetPageInfo(col, pageNum, listNo);
			
			ret = search.w3SetPrefixQuery(col, "<DOCID:contains:"+query+">", 0);
		}
		
		if(debug) System.out.println(" - search ip : " + SEARCH_IP);
		if(debug) System.out.println(" - search port : " + SEARCH_PORT);
		if(debug) System.out.println(" - search timeout : " + SEARCH_TIMEOUT);
		ret = search.w3ConnectServer(SEARCH_IP, SEARCH_PORT, SEARCH_TIMEOUT);
		
		ret = search.w3ReceiveSearchQueryResult(0);
		if(ret != 0) {
            System.out.println(search.w3GetErrorInfo() + " (Error Code : " + search.w3GetError() + " )");
            return null;
        }
		
		int totalResultCount = 0;
		for(String col : collectionArr){
			totalResultCount += search.w3GetResultTotalCount(col);
		}
		
		if(debug)System.out.println("############################################# ");
		if(debug)System.out.println("### Query : " + query);
		if(debug)System.out.println("### Total Result Count : " + totalResultCount);
		if(debug)System.out.println("############################################# ");
		int count = search.w3GetResultCount(collection);
		for(int i=0; i<count; i++){
			//String docid = search.w3GetField(collection, "DOCID", i);
			//docid += "$!$" + search.w3GetField(collection, "TITLE", i).trim();
			//docid += "$!$" + search.w3GetField(collection, "CONTENT_PLAIN", i).trim();
			String docid = search.w3GetField(collection, "CONTENT_PLAIN", i);
			docidList.add(docid);
		}
		
		return docidList;
	}
	
	public static void main(String[] args){
		SF1Test test = new SF1Test();
		
		String query = "article_DI20160414104995";
		
		ArrayList<String> resultList = test.searchDoc(query, 10, 1);
		for(String docid: resultList){	
			System.out.println("### DOCID : " + docid);
		}
	}
	
}
