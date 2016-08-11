package kr.co.wisenut.common;

import java.util.List;

import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

import com.wisenut.tea20.api.ClassifierClient;
import com.wisenut.tea20.types.Pair;

public class WiseClassifierWorker {
	
	private static final Logger LOGGER = LoggingManager.getLoggerForClass();

	public static String cfIP;
	public static int cfPort;
	public static String collectionId;
	
	public WiseClassifierWorker(String ip, int port, String collection) throws Exception{
		cfIP = ip;
		cfPort = port;
		
		collectionId = collection;
	}
	
	public List<Pair<Double>> getRecommendedCategoryPair(String keywords) throws Exception{
		ClassifierClient cfClient = new ClassifierClient(cfIP, cfPort);
					
		List<Pair<Double>> resultList = cfClient.predictDocument( collectionId, keywords, "^", ":");
		
		if(cfClient.hasError()){
			LOGGER.error("[WiseTeaWorker>getMainKeywordsPair][" + cfClient.getErrorCode()+"] " + cfClient.getErrorMessage());
			throw new Exception("[" + cfClient.getErrorCode()+"] " + cfClient.getErrorMessage());
		}
		
		return resultList;
	}
}
