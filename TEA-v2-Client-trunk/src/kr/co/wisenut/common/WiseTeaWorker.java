package kr.co.wisenut.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.wisenut.tea20.api.TeaClient;
import com.wisenut.tea20.types.Pair;

public class WiseTeaWorker {

	private static final Logger LOGGER = LoggingManager.getLoggerForClass();
	
	public static String teaIP;
	public static int teaPort;
	public static String collectionId;
	public static String searchField;
	
	private int totalRecommendedMediaCount;
	private int totalKeywordsCount;
	
	public WiseTeaWorker(String ip, int port, String collection) throws Exception{
		teaIP = ip;
		teaPort = port;
		
		collectionId = collection;
		searchField = "CONTENT_PLAIN";
	}
	
	public List<Pair<Integer>> getMainKeywordsPair(String article) throws Exception {
		TeaClient teaClient = new TeaClient(teaIP, teaPort);
		
		article = searchField + "$!$" + article;
		List<Pair<Integer>> keywordList = teaClient.extractKeywordsForPlainText(collectionId, article, "TERMS");
		
		if(teaClient.hasError()){
			LOGGER.error("[WiseTeaWorker>getMainKeywordsPair][" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
			throw new Exception("[" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
		}
		
		totalKeywordsCount = keywordList.size();
		LOGGER.info("extractKeywordsForPlainText results in " + keywordList.size() + " keywords.");
		
		return keywordList;
	}
	
	public List<Pair<Integer>> getNerPair(String collection, String article, String topN, ArrayList<String> filteringDocidList, String prefix, String startDate, String endDate) throws Exception{
		TeaClient teaClient = new TeaClient(teaIP, teaPort);
		
		article = searchField + "$!$" + article;
		
		List<Pair<Integer>> result = teaClient.extractNerForPlainText(collection, article, topN, filteringDocidList, prefix, startDate, endDate );
		if(teaClient.hasError()){
			LOGGER.error("[WiseTeaWorker>getNerPair][" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
			throw new Exception("[" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
		}

		return result;
	}
	
	public List<Pair<Double>> getRecommendedContentsPair(String type, String article, ArrayList<String> searchResultList, String startDate, String endDate) throws Exception {
		TeaClient teaClient = new TeaClient(teaIP, teaPort);
		
		article = searchField + "$!$" + article;
		
		String prefix = "";
		if(type != null && type.length()>1){
			prefix = type.substring(0,1).toUpperCase();
		}
		List<Pair<Double>> documentList = teaClient.getSimilarDoc( type, article, "100", searchResultList, prefix, startDate, endDate );
		
		totalRecommendedMediaCount = documentList.size();
		LOGGER.info("getSimilarDocSf1 results in " + documentList.size() + " documents.");
		
		if(teaClient.hasError()){
			LOGGER.error("[WiseTeaWorker>getRecommendedContentsPair][" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
			throw new Exception("[" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
		}
		
		return documentList;
	}
	
	public List<Map<String,String>> getRecommendedContents(String type, String article, String pageSize, ArrayList<String> searchResultList, String fieldToDisplay, String prefix, String startDate, String endDate) throws Exception {
		TeaClient teaClient = new TeaClient(teaIP, teaPort);
		
		article = searchField + "$!$" + article;
		
		List<Map<String,String>> documentList = teaClient.getSimilarDocWithContent(collectionId, article, fieldToDisplay, pageSize, searchResultList, prefix, startDate, endDate);
		
		totalRecommendedMediaCount = documentList.size();
		LOGGER.info("getSimilarDocSf1 results in " + documentList.size() + " documents.");
		LOGGER.info(String.format("- collectionId : %s\n - type : %s\n - article : %s\n - pageSize : %s\n - searchResultList.size(): %s\n - fieldToDisplay : %s\n - prefix : %s\n - startDate : %s\n - endDate : %s", collectionId, type, article, pageSize, searchResultList.size(), fieldToDisplay, prefix, startDate, endDate ));
		
		if(teaClient.hasError()){
			LOGGER.error("[WiseTeaWorker>getRecommendedContents][" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
			throw new Exception("[" + teaClient.getErrorCode()+"] " + teaClient.getErrorMessage());
		}
		
		return documentList;
	}
	
	public int getTotalRecommendedMediaCount(){
		return totalRecommendedMediaCount;
	}
	
	public int getTotalKeywordsCount(){
		return totalKeywordsCount;
	}
}
