package com.wisenut.tea20.api;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import QueryAPI530.Search;

import com.wisenut.tea20.api.TeaClient;
import com.wisenut.tea20.tools.Tools;
import com.wisenut.tea20.types.DocumentInfo;
import com.wisenut.tea20.types.Pair;

public class ClientTest {

    static TeaClient teaClient;
    
    public static final String TEA_IP = "211.39.140.51";
    public static final int TEA_PORT = 11000;
    
	public static final String SEARCH_IP = "211.39.140.52";
	public static final int SEARCH_PORT = 7000;
	public static final int SEARCH_TIMEOUT = 20000;
	
    public static void main(String[] args) {
    	
    	String content;	
    	String printStr;
    	String similarContent;
    	
    	
    	String fileName = "AtoJ_test.txt";
    	//String fileName = "text.txt";
    	try {
    		
    		BufferedReader in = new BufferedReader(new FileReader(fileName)) ;
    		FileOutputStream output = new FileOutputStream("result.txt") ;
    		
    		
    		while ( ( content = in.readLine() ) != null )
    		{
    
    			similarContent = content;
    	//content = "겨울채비 하세요... 이번 주 맑고 포근 윤우현 기자 whyoon@jbnews.com 11월4째주 충북지방은 대체로 고기압의 영향을 받아 맑겠으며, 기온도 평년보다 조금 높아 포근한 한 주가 될 것으로 전망된다.청주기상대는 20일 충북지방은 고기압의 영향을 받아 맑은 날씨를 보이겠으며, 아침최저 청주2도, 충주 영하1도 등 영하2~영상2도의 분포를 보이겠고, 낮 최고기온은 청주 16도, 충주 14도 등 14~16도로 포근할 것이라고 예보했다. 한편 11월 셋째 휴일인 19일 충북지역은 구름이 낀 흐린 날씨를 보인 가운데 유명산 등에는 다소 한산한 모습을 보였다. 가을 단풍이 모두 떨어진 월악산국립공원 입장객은 2천명으로 지난 주의 절반에 그쳤고 속리산에는 4천명의 등산객이 산행을 즐겼으나 평소보다 적은 수준이었다. 대통령 옛 별장인 청원 청남대에도 휴일 평균 관람객의 50%를 밑도는 2천명만 입장해 초겨울 대청호의 풍광을 감상했다. 청주 상당산성, 청원 문의문화재 단지 등 도내 주요 유원지에서는 두툼한 옷을 입은 가족단위 행락객이 등산, 산책, 외식 등을 하며 휴일의 여유를 즐겼다. 청주 도심 극장가는 대입 수능을 마친 고3학생 등으로 북적거렸고 도시와 농촌가정에서는 김장김치를 담그는 손길이 분주했다.";
    	//content = "규모 6.5의 강진이 강타한 구마모토 일대에서는 지진으로 주택과 건물이 수십 채가 무너지고 화재가 잇따르면서 대규모 인명 피해가 발생했다. 일본 NHK 방송은 15일 오전 9시 현재 지진으로 인해 붕괴된 건물에 깔리거나 화재 때문에 9명임 숨지고 950여 명이 부상을 당해 부근 병원에서 치료를 받고 있다고 보도했다. 부상자 가운데 50여 명은 중상이어서 인명 피해는 더 늘어날 것으로 예상된다.      이번 지진으로 만 6천여 가구에 전기도 끊겼다. 피해 지역 주민 4만 5천여 명은 주택 붕괴 위험을 피해 옥외 주차장과 체육관 등에 모여 뜬눈으로 밤을 지새웠다. 규슈 일부 지역은 휴대전화 등의 통신이 원활하지 않은 상황이며 현지 가스 회사는 화재 피해를 줄이기 위해 가스 공급을 차단했으며 수도가 끊긴 곳도 속출했다.";
    	//content = "  일본에서는 어제저녁, 사쿠라지마 화산이 분화했습니다.   일본은 활화산들이 잇따라 분화하면서 일명 `불의 고리'가 요동치는 것은 아닌지 불안감이 커지고 있습니다.   사쿠라지마 분화 현장을 박재우 특파원이 다녀왔습니다.   (어제 사쿠라지마 분화)   번개가 치는 듯한 `화산뢰`와 함께 시뻘건 마그마를 쏟아냈던 사쿠라지마.   (경비행기)   비행기로 현장을 찾았습니다.     분화구에서는 여전히 하얀 연기가 쉴새없이 뿜어져나오고   마그마와 함께 쏟아진 화산재는 사쿠라지마를 온통 잿빛으로 물들였습니다.   박재우(사쿠라지마) 지금 저희 취재진은 사쿠라지마 화산 정상 부근을 날고 있습니다.분화 당시 쏟아져 나온 돌들이 약 2km떨어진 산 아래쪽 민가 가까이까지 날아갔습니다.    산 아래 민가를 찾았습니다.     집집마다 화산재가 수북이 쌓여 있습니다.   주민들은 화산분출로 해마다 피난을 가야하는 형편입니다   사카모토/사쿠라지마 주민 가고시마 시내에 여동생이 있어서 거기에 피난을 갑니다.     일본 기상청은 현지 피해 실태 조사에 나섰습니다.   다카하시/가고시마 지방기상대 기상대로서는 확실하게 관측을 해서 이상한 점을 놓치지 않도록 하겠습니다.     50km 떨어진 센다이 원전에는 별다른 영향이 없었습니다.     하지만 80여 개의 활화산이 있는 일본 열도는 잇단 화산 분화와 지진에 늘 불안합니다.   사쿠라지마에서 KBS 뉴스 박재웁니다.";
    			
    	//extract keywords from Document
        //List<Pair<Integer>> keywordList = extractKeywords( content );
       
    	
        List<Pair<Integer>> keywordList = extractKeywords( content );
         //test code ( extract keywords from Document )
         
        /*
 		for (int i = 0; i < keywordList.size(); i++) {
 			Pair<Integer> item = keywordList.get(i);
 			if (null == item) {
 				continue;
 			}
 			
 			printStr = item.key() + "^" + item.value() + " ";
 			//output.write( printStr.getBytes() ); 			
 			System.out.println( item.key() + "^" + item.value() );
 			
 		}*/
 		
        // start code : get named entity        
    	List<Pair<Double>> similarDocumentList = getSimilarDoc( content );     
        for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			} 			
 			/* to do code
 			// String similarContent += getContentByDocid( item.key() ); */
        }            	
                
 		List<Pair<Integer>> nerList = extractNer( content, similarContent );
 		for (int i = 0; i < nerList.size(); i++) {
 			Pair<Integer> item = nerList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 			
 		} // end code : get named entity
 		
        
         //make query for SF1 
        StringBuffer query = new StringBuffer();
      
		printStr = "\nsearch keywords\n";
		/*
        for (int i = 0; i < keywordList.size(); i++) {
         	Pair<Integer> item = keywordList.get(i);
 			if (null == item) {
 				continue;
 			}
 			
 			if( item.value() > 50 && !item.key().contains(" ") )
 			{
 			 			
 			if( query.length() != 0 )
 				query.append("|");
 			
 			query.append(item.key());
 			
			System.out.println( item.key() + "^" + item.value() );
			output.write( (item.key() + "|").getBytes()  );
 			}
 	
 		}*/     
        
        //String tempStr = "네트워크^50|바둑|컴퓨터|인공|지능|네이처|기사|챔피언|구글|알파|위치|경기|사용|개발|프로그램|세계적|맞대결|자회사|마인드|이세돌|움직임|광범위|새벽|학술|유럽|중국|대국|발표|체스|탐색|공간|도전|영역|수의|가치|예정|선택|정책|학습|수준|능력|도달|희망|제시|서울|세계|승리";
        //tempStr = "가스^367|인터넷^363|화재^359|지진^359|기사^281|점검^281|구마모토^273|지역^273|부상^273|붕괴^273|인명^273|건물^273|주택^273|대규모^238|규모^183|강진^183|강타^183|일대^183|일본^183|방송^183|부근^183|병원^183|치료^183|중상^183|예상^183|가구^183|전기^183|주민^183|옥외^183|차단^183|속출^183|현지^183|공급^183|상황^183|원활^183|통신^183|전화^183|휴대^183|규슈^183|뜬눈^183|회사^183|체육^183|주차^183|이기형^124";
  
        //StringBuffer query = new StringBuffer();
        query.append(content);
   
        int cnt = 0;
        
        /*
        // get document list for query by SF1          
    	SF1Test test = new SF1Test();
		ArrayList<String> resultList = test.search(query.toString(), 100, 1);
		System.out.println( "get sf1 search result" ); 		
		printStr = "\nget sf1 search result\n";
		output.write( printStr.getBytes() );
		for(String docid: resultList){	
			//output.write( docid + "|");
			if( cnt++ < 12 )
			{
			printStr = docid + "\n";
			output.write( printStr.getBytes() );
			}
			System.out.println( docid );
		}		
		if( resultList.isEmpty() )
		{
			printStr = "NONE\n";
			output.write( printStr.getBytes() );		
		}*/
		
        
        //get similar document list by model  	
        /*	
		List<Pair<Double>> similarDocumentList = getSimilarDoc( content );     
        System.out.println( "get similar doc of whole documents" );
        printStr = "\nget similar doc of whole documents\n";
    	output.write( printStr.getBytes() );
        for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			}
 			
 			printStr = item.key() + "\t" + item.value() + "\n";
 			output.write( printStr.getBytes() );
 			System.out.println( item.key() + "^" + item.value() );
 		} */       	
    	
   	
        /*
    	 //get similar document list by model + SF1
		similarDocumentList = getSimilarDoc( content, resultList ); 
		System.out.println( "get similar doc of sf1 results" );
		printStr = "\nget similar doc of sf1 results\n";
		output.write( printStr.getBytes() );
		for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			}
 			printStr = item.key() + "\t" + item.value() + "\n";
 			output.write( printStr.getBytes() );
 			System.out.println( item.key() + "^" + item.value() );
 		}*/
		
    		}
    		
    		} catch (IOException e) {
        		System.err.println(e) ;
        		System.exit(1); 
        	}
    	
    	System.out.println( "finish" );
    }
    
    public static List<Pair<Integer>> extractKeywords( String content ) {
    	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);       
        String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.extractKeywordsForPlainText("kbs", query, "TERMS_KMA" );
    }
    
    public static List<Pair<Integer>> extractNer( String content, String similarContent ) {
   	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);       
       
        return teaClient.extractNerForPlainText("kbs", content, similarContent );
    }
    
    
    public static List<Pair<Double>> getSimilarDoc( String content ) {
   	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	// not yet
    	String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.getSimilarDoc( "kbs", query, "10", "");
    }
    
    public static List<Pair<Double>> getSimilarDoc( String content, ArrayList<String> resultList ) {
      	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	// not yet
    	String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.getSimilarDoc( "kbs", query, "10", resultList, "");
    }
    
    
   
    public ArrayList<String> search(String query, int listNo, int isDebug){
		boolean debug = false;
		if(isDebug != 0) debug = true;
		
		ArrayList<String> docidList = new ArrayList<String>();
		
		Search search = new Search();
		
		String collection = "article";
		String sort = "RANK/DESC,UID/DESC";
		
		int pageNum = 0;
		
		String documentFields = "DOCID";
		String searchFields = "Subject,Contents";
		
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
			docidList.add(docid);
		}
		
		return docidList;
	}
}
