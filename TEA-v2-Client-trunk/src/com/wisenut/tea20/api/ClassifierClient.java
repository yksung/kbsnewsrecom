package com.wisenut.tea20.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.wisenut.tea20.tools.MessageHandler;
import com.wisenut.tea20.tools.StringTool;
import com.wisenut.tea20.tools.Tools;
import com.wisenut.tea20.types.Pair;
import com.wisenut.tea20.types.SocketMessage;
import com.wisenut.tea20.tools.MessageHandlerForCF;
import com.wisenut.tea20.types.SocketMessageForCF;

import QueryAPI530.Search;

public class ClassifierClient {
	public static final String DEFAULT_IP = "211.39.140.51";
	public static final int DEFAULT_PORT = 11111;
	public static final int TIMEOUT = 20000;
	
    /**
     * upper limit for content size
     */
    private final int MAX_CONTENT_SIZE = 2 * 1024 * 1024;
    
    /**
     * initial interval for socket connection retry
     */
    private int initConnectionInterval = 0;

    /**
     * lower limit for socket connection retry
     */
    private int INIT_CONNECTION_INTERVAL = 10;

    /**
     * maximum interval for socket connection retry
     */
    private int maxConnectionInterval = 0;

    /**
     * upper limit for maxConnectionInterval_
     */
    private final int MAX_CONNECTION_INTERVAL = 2000;

    /**
     * maximum connection retries
     */
    private int maxConnectionRetry = 0;

    /**
     * upper limit for maxConnectionRetry_
     */
    private final int MAX_CONNECTION_RETRY = 2000;

    /**
     * recent error code
     */
    private String recentErrorCode = "";

    /**
     * recent error code from server
     */
    private String recentServerErrorCode = "";

    /**
     * recent error message
     */
    private String recentErrorMessage = "";

    /**
     * recent error message from server
     */
    private String recentServerErrorMessage = "";
    
    /**
     * flag for whether to display messages in the console
     */
    private boolean consoleLog = true;
    
    /**
     * maximum wait time for receiving results from listener
     */
    private int waitTimeout = 0;
    
    /**
     * IP for listener.
     */
    private String serverIp = DEFAULT_IP;
    /**
     * port for listener.
     */
    private int serverPort = DEFAULT_PORT;
	
    public ClassifierClient(){
	}
    
	public ClassifierClient(String ip, int port){
		serverIp = ip;
		serverPort = port;
	}
    /**
     * Method for actual socket communication. (internal use)
     *
     * @param request wrapper object for (request) socket message
     * @return wrapper object for response message
     */
    private SocketMessageForCF handleMessage(SocketMessageForCF request) {
    	SocketMessageForCF toReturn = null;
        MessageHandlerForCF handler = null;

        if (0 != maxConnectionInterval || 0 != maxConnectionRetry) {
            handler = new MessageHandlerForCF(serverIp, serverPort, initConnectionInterval,
                    maxConnectionInterval, maxConnectionRetry, waitTimeout);
        } else {
            handler = new MessageHandlerForCF(serverIp, serverPort);
        }
        toReturn = handler.getResponse(request);
        /*
        recentResponse = toReturn;

        if (!"".equals(toReturn.getErrorCode())) {
            setError(toReturn.getErrorCode(), toReturn.getErrorMessage());
            recentServerErrorCode = toReturn.getErrorCode();
            recentServerErrorMessage = toReturn.getErrorMessage();
        } else {
            setError("", "");
        }
        */
        return toReturn;
    }

	
	public List<Pair<Double>> predictDocument(String collectionId, String content, String item_delimiter, String weight_delimiter ){
		//boolean debug = false;
		//if(isDebug != 0) debug = true;
		
		List<Pair<Double>> toReturn = new ArrayList<Pair<Double>>();
        if (null == collectionId || null == content || 0 == collectionId.length() || 0 == content.length()) {
            setError("APIL_0200", "argument's not valid.");
            return toReturn;
        }

        if (content.length() > MAX_CONTENT_SIZE) {
            setError("APIL_0153", "content size cannot exceed " + MAX_CONTENT_SIZE + " characters: " + content.length());
            return toReturn;
        }
        
        String[] paramFields = {"doc_content","item_delimiter", "weight_delimiter"};
        SocketMessageForCF request = new SocketMessageForCF("l_classify_document", collectionId, paramFields);
        request.setValue("doc_content", content);
        request.setValue("item_delimiter", item_delimiter);
        request.setValue("weight_delimiter", weight_delimiter);
       
        SocketMessageForCF response = handleMessage(request);
        if (!isSuccessful(response)) {
            if ("".equals(response.getErrorCode())) {
                setError("APIL_0271", "category classification wasn't successful: coll_id=" + collectionId);
            } else {
                wrapError("APIL_0271", "category classification wasn't successful: coll_id=" + collectionId);
            }
        } else {
            //public static List< Pair<String> > getPairListStr(String obj, String itemDelimiter, String valueDelimiter)
            String categoryString = response.getValue("category").trim();
            toReturn = Tools.getPairListDouble(categoryString, item_delimiter, weight_delimiter);
        }
        return toReturn;
	}
	
	/**
     * Set recent error code / message (internal use)
     *
     * @param code    error code to set
     * @param message error message to set
     */
    private void setError(String code, String message) {
        recentErrorCode = code;
        recentErrorMessage = message;
        recentServerErrorCode = "";
        recentServerErrorMessage = "";

        if (consoleLog && !"".equals(message)) {
            System.out.println("[E!:" + code + "] " + message);
        }
    }

    /**
     * Modify error code / message using messages from system (internal use)
     *
     * @param code    error code to modify
     * @param message error message to modify
     */
    private void wrapError(String code, String message) {
        recentErrorCode = code;

		/* 
		 *	<error>
		 *		<code>TEAL_8453</code>
		 *		<message>[request_id=201203082] Failed to create a process module by wrong value of a parameter(=topic_id)</message>
		 *	</error>
		 *
		 *	<error>
		 *		<code>TEAL_4410</code>
		 *		<message>Topic ID <1000> does not exist.</message>
		 *	</error> 
		*/
        if ("TEAL_8453".equals(recentServerErrorCode)) {
            recentErrorCode = "APIL_0155";
            recentErrorMessage = message + ": argument's not valid (server-side): " + recentServerErrorMessage;
        } else if ("TEAL_4410".equals(recentServerErrorCode)) {
            recentErrorCode = "APIL_0161";
            recentErrorMessage = message + ": topic ID's not exist";
        } else {
            recentErrorMessage = message + " DUE TO [" + recentServerErrorCode + ": " + recentServerErrorMessage + "]";
        }

        recentServerErrorMessage = "";

        if (consoleLog) {
            System.out.println("[E!:" + recentErrorCode + "] " + recentErrorMessage);
        }
    }

    /**
     * Check if response's successful. (internal use)
     *
     * @param response wrapper object for response socket message
     * @return true if successful
     */
    private boolean isSuccessful(SocketMessageForCF response) {
        if ("success".equals(response.getValue("status"))) {
            return true;
        } else {
            return false;
        }
    }

	public static void main(String[] args){
		ClassifierClient test = new ClassifierClient();
		
		// query = getMainKeywordsInfo();
		String query = "시리아:100^천여:86^시리아인권관측소:79^내전:77^민간인:66^베이루트:59^연합뉴스:59^희생자:51^어린이:51^추정:39^영국:39^백여:39^시작:39^사람:39^집계:39";
		System.out.print("Please input query : " + query + "\n" );
			
		List<Pair<Double>> resultList = test.predictDocument( "KBS", query, "^", ":");
		for (int i = 0; i < resultList.size(); i++) {
 			Pair<Double> item = resultList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 		}        	
	}
}	

