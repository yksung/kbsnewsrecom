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
            	System.out.println(response);
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
		String query = "음악:100^한류:93^이유:59^영화:57^뮤지션:54^한국:52^산업:52^장르:48^비틀즈:46^일본:46^키즈:42^예술문화:40^대중예술:40^한류스타:40^시장:36^예술:36^부각:36^교류:36^캐릭터:35^콘텐츠:35^뉴시스:35^드라마:35^시스템:35^역량:33^전문:33^문화:33^의식:33^세계:33^동남아시아:32^라크리모사:32^애니메이션:32^가수:29^생명:29^모델:29^대중음악:29^대중스타:29^문화산업:29^할리우드:29^티켓가격:29^지속가능:29^홍콩영화:29^내부교류:29^동방신기:29^아티스트:29^위험부담:29^계약문제:29^판타지:28^수용자:28^동네:27^미국:27^반지:27^제왕:27^강점:27^공연:27^기억:27^영향:27^기획:27^결과:27^의미:27^존중:27^확산:27^추억:27^생산:27^연속성:25^트렌드:25^젊은이:25^음악성:25^생명력:25^예술인:25^자신감:25^기획사:25^마니아:25^시드니오페라하우스:23^재벌:22^수용편중현상:19^예능프로그램:19^소득불규칙성:19^메이저리거들:19^안전지상주의:19^프로그레시브:19^일렉트로니카:19^소설:19^서구:19^재즈:19^생각:19^포크:19^이전:19^발휘:19^비용:19^입장:19^인력:19^활동:19^다수:19^소비:19^착각:19^구조:19^편승:19^이름:19^나라:19^정책:19^배출:19^야구:19^잡지:19^세월:19^규모:19^과정:19^위기:19^시작:19^무관:19^연주:19^성공:19^변화:19^소품:19^후반:19^감독:19^기세:19^외국:19^복지:19^제한:19^경쟁:19^작품:19^물음:19^한계:19^시대:19^인기:19^외부:19^옛날이야기:18^오스트리아:18^중년여성층:18^표현수단인:18^이데올로기:18^이탈리아어:18^문화향수권:18^문화콘텐츠:18^국가브랜드:18^이스트우드:18^매니지먼트:18^장르다양성:18^여성주의자:18^반기업정서:18^음악애호가:18^대학가요제:18^제작시스템:18^엘리트체육:18^궁중암투로:18^다큐멘터리:18^흥행영화:16^야구관중:16^연쇄효과:16^브라운관:16^지지기류:16^단편영화:16^상영공간:16^프로듀서:16^향유계층:16^벼락스타:16^장르스타:16^사회권력:16^경제효과:16^한국영화:16^공공영역:16^마니아급:16^개인스타:16^영화인력:16^문화의식:16^공연투어:16^주류음악:16^경연대회:16^행사돌기:16^아시아인:16^댄스가수:16^중소기업:16^사람인양:16^에이전시:16^일방진출:16^음악스타:16^인종문제:16^외부교류:16^편도티켓:16^장르성은:16^패권의식:16^할아버지:16^브레이크:16^문화사업:16^해외진출:16^사전준비:16^경쟁사회:16^장편영화:16^에피소드:16^동아시아:16^장르음악:16^점검대상:16^이란영화:16^자기복제:16^몬테레이:16^페스티벌:16^전쟁영화:16^홍콩감독:16^성냥개비:16^영웅본색:16^단기성과:16^대형회사:16^국제스타:16^활동방식:16^음악여정:16^문화배경:16^작가주의:16^표지모델:16^노동환경:16^관객시대:16^사회복지:16^소외계층:16^수익분배:16^상품가치:16^생활체육:16^노동시간:16^전제조건:16^행동양식:16^관계유지:16^beatles:15^new:15^음악:100^한류:93^이유:59^영화:57^뮤지션:54^한국:52^산업:52^장르:48^비틀즈:46^일본:46^키즈:42^예술문화:40^대중예술:40^한류스타:40^시장:36^예술:36^부각:36^교류:36^캐릭터:35^콘텐츠:35^뉴시스:35^드라마:35^시스템:35^역량:33^전문:33^문화:33^의식:33^세계:33^동남아시아:32^라크리모사:32^애니메이션:32^가수:29^생명:29^모델:29^대중음악:29^대중스타:29^문화산업:29^할리우드:29^티켓가격:29^지속가능:29^홍콩영화:29^내부교류:29^동방신기:29^아티스트:29^위험부담:29^계약문제:29^판타지:28^수용자:28^동네:27^미국:27^반지:27^제왕:27^강점:27^공연:27^기억:27^영향:27^기획:27^결과:27^의미:27^존중:27^확산:27^추억:27^생산:27^연속성:25^트렌드:25^젊은이:25^음악성:25^생명력:25^예술인:25^자신감:25^기획사:25^마니아:25^시드니오페라하우스:23^재벌:22^수용편중현상:19^예능프로그램:19^소득불규칙성:19^메이저리거들:19^안전지상주의:19^프로그레시브:19^일렉트로니카:19^소설:19^서구:19^재즈:19^생각:19^포크:19^이전:19^발휘:19^비용:19^입장:19^인력:19^활동:19^다수:19^소비:19^착각:19^구조:19^편승:19^이름:19^나라:19^정책:19^배출:19^야구:19^잡지:19^세월:19^규모:19^과정:19^위기:19^시작:19^무관:19^연주:19^성공:19^변화:19^소품:19^후반:19^감독:19^기세:19^외국:19^복지:19^제한:19^경쟁:19^작품:19^물음:19^한계:19^시대:19^인기:19^외부:19^옛날이야기:18^오스트리아:18^중년여성층:18^표현수단인:18^이데올로기:18^이탈리아어:18^문화향수권:18^문화콘텐츠:18^국가브랜드:18^이스트우드:18^매니지먼트:18^장르다양성:18^여성주의자:18^반기업정서:18^음악애호가:18^대학가요제:18^제작시스템:18^엘리트체육:18^궁중암투로:18^다큐멘터리:18^흥행영화:16^야구관중:16^연쇄효과:16^브라운관:16^지지기류:16^단편영화:16^상영공간:16^프로듀서:16^향유계층:16^벼락스타:16^장르스타:16^사회권력:16^경제효과:16^한국영화:16^공공영역:16^마니아급:16^개인스타:16^영화인력:16^문화의식:16^공연투어:16^주류음악:16^경연대회:16^행사돌기:16^아시아인:16^댄스가수:16^중소기업:16^사람인양:16^에이전시:16^일방진출:16^음악스타:16^인종문제:16^외부교류:16^편도티켓:16^장르성은:16^패권의식:16^할아버지:16^브레이크:16^문화사업:16^해외진출:16^사전준비:16^경쟁사회:16^장편영화:16^에피소드:16^동아시아:16^장르음악:16^점검대상:16^이란영화:16^자기복제:16^몬테레이:16^페스티벌:16^전쟁영화:16^홍콩감독:16^성냥개비:16^영웅본색:16^단기성과:16^대형회사:16^국제스타:16^활동방식:16^음악여정:16^문화배경:16^작가주의:16^표지모델:16^노동환경:16^관객시대:16^사회복지:16^소외계층:16^수익분배:16^상품가치:16^생활체육:16^노동시간:16^전제조건:16^행동양식:16^관계유지:16^beatles:15^new:15^음악:100^한류:93^이유:59^영화:57^뮤지션:54^한국:52^산업:52^장르:48^비틀즈:46^일본:46^키즈:42^예술문화:40^대중예술:40^한류스타:40^시장:36^예술:36^부각:36^교류:36^캐릭터:35^콘텐츠:35^뉴시스:35^드라마:35^시스템:35^역량:33^전문:33^문화:33^의식:33^세계:33^동남아시아:32^라크리모사:32^애니메이션:32^가수:29^생명:29^모델:29^대중음악:29^대중스타:29^문화산업:29^할리우드:29^티켓가격:29^지속가능:29^홍콩영화:29^내부교류:29^동방신기:29^아티스트:29^위험부담:29^계약문제:29^판타지:28^수용자:28^동네:27^미국:27^반지:27^제왕:27^강점:27^공연:27^기억:27^영향:27^기획:27^결과:27^의미:27^존중:27^확산:27^추억:27^생산:27^연속성:25^트렌드:25^젊은이:25^음악성:25^생명력:25^예술인:25^자신감:25^기획사:25^마니아:25^시드니오페라하우스:23^재벌:22^수용편중현상:19^예능프로그램:19^소득불규칙성:19^메이저리거들:19^안전지상주의:19^프로그레시브:19^일렉트로니카:19^소설:19^서구:19^재즈:19^생각:19^포크:19^이전:19^발휘:19^비용:19^입장:19^인력:19^활동:19^다수:19^소비:19^착각:19^구조:19^편승:19^이름:19^나라:19^정책:19^배출:19^야구:19^잡지:19^세월:19^규모:19^과정:19^위기:19^시작:19^무관:19^연주:19^성공:19^변화:19^소품:19^후반:19^감독:19^기세:19^외국:19^복지:19^제한:19^경쟁:19^작품:19^물음:19^한계:19^시대:19^인기:19^외부:19^옛날이야기:18^오스트리아:18^중년여성층:18^표현수단인:18^이데올로기:18^이탈리아어:18^문화향수권:18^문화콘텐츠:18^국가브랜드:18^이스트우드:18^매니지먼트:18^장르다양성:18^여성주의자:18^반기업정서:18^음악애호가:18^대학가요제:18^제작시스템:18^엘리트체육:18^궁중암투로:18^다큐멘터리:18^흥행영화:16^야구관중:16^연쇄효과:16^브라운관:16^지지기류:16^단편영화:16^상영공간:16^프로듀서:16^향유계층:16^벼락스타:16^장르스타:16^사회권력:16^경제효과:16^한국영화:16^공공영역:16^마니아급:16^개인스타:16^영화인력:16^문화의식:16^공연투어:16^주류음악:16^경연대회:16^행사돌기:16^아시아인:16^댄스가수:16^중소기업:16^사람인양:16^에이전시:16^일방진출:16^음악스타:16^인종문제:16^외부교류:16^편도티켓:16^장르성은:16^패권의식:16^할아버지:16^브레이크:16^문화사업:16^해외진출:16^사전준비:16^경쟁사회:16^장편영화:16^에피소드:16^동아시아:16^장르음악:16^점검대상:16^이란영화:16^자기복제:16^몬테레이:16^페스티벌:16^전쟁영화:16^홍콩감독:16^성냥개비:16^영웅본색:16^단기성과:16^대형회사:16^국제스타:16^활동방식:16^음악여정:16^문화배경:16^작가주의:16^표지모델:16^노동환경:16^관객시대:16^사회복지:16^소외계층:16^수익분배:16^상품가치:16^생활체육:16^노동시간:16^전제조건:16^행동양식:16^관계유지:16^beatles:15^new:15";
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

