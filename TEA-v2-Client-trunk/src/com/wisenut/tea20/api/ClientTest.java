package com.wisenut.tea20.api;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import QueryAPI530.Search;

import com.wisenut.tea20.api.TeaClient;
import com.wisenut.tea20.tools.Tools;
import com.wisenut.tea20.types.DocumentInfo;
import com.wisenut.tea20.types.Pair;

public class ClientTest {

    static TeaClient teaClient;
    
    public static final String TEA_IP = "10.0.10.135";
    public static final int TEA_PORT = 11000;
    
	public static final String SEARCH_IP = "211.39.140.51";
	public static final int SEARCH_PORT = 7000;
	public static final int SEARCH_TIMEOUT = 20000;
	
    public static void main(String[] args) {
    	test_getSimilarDoc();
    	//test_getSimilarDoc2();
    	//test_getSimilarDoc3();
    	//test_getSimilarDocWithContent("media");
    	//test_getNer();
    	
    	System.exit(0);
    }
    
   	public static void test_extractKeywords()
	{
   		System.out.println( "=========== test_extractKeywords() ===========");
		String content = "[나도원의 \'대중음악을 보다\'] \'디자인 서울\'서 \'광화문 연가\'가 나올 수 있을까  그 동네에서 피어난 노래들  \"떠나보낸 사람에서 떠난 사람이 된 작곡가\" 이영훈의 1주기를 맞아 <광화문 연가>(민음사 펴냄)가 출간되었다. 고인이 남긴 글과 노랫말로 채워진 이 종이뭉치는 \"한 남자의 정답 없는 삶에 대한 고백이고 훌륭한 음악가의 해설이며 대중음악의 귀한 사료\"이다. 노래가 태어난 사연을 따라가며 손끝을 옮기다보면 익히 알려진 시청 앞과 덕수궁을 지나 대학로까지 따라가게 된다. 책에는 \"라일락 꽃향기 맡으면 잊을 수 없는 기억\"을 떠올린 \'가로수 그늘 아래 서면\'과 \'이 아침엔\' 등이 태어난 곳이 혜화동이라고 적혀 있다.    ▲<광화문 연가>(민음사 펴냄) ⓒ프레시안 중학교 시절의 단짝 친구가 제 살던 곳이라며 데리고 갔던 혜화동은 후에 주말마다 차 없는 거리로 조성되는 대학로와 자유로운 공연이 펼쳐지던 마로니에 공원을 껴안은 해방구가 되었다. 일찍이 젊음과 낭만의 거리로 불렸던 이 동네를 권인하 중심의 프로젝트 그룹 \'마로니에\'는 \'동숭로에서\'로 예찬하면서 지금의 홍대거리와 같았던 당시의 공기를 담아두었다. 1990년대 후반에는 콜라나 맥주 하나를 시켜놓고 마음껏 음악을 들을 수 있는 곳들이 골목 여기저기에 들어앉았다. 지금은 그 자리를 게임방과 고기집이 차지했지만, 후미진 골목에는 짝짝이 양말을 신은 친구와 같은 정감이 조금은 남아 있다.   잿빛이라는 단어를 처음 보았을 때 어릴 적 뒷마당에서 본 족제비가 생각난 탓에 한동안 남들과 다른 색을 떠올렸다. 동요 \'검은 고양이 네로\'가 어딘지 모르게 스산하게 들리는 이유 역시 어릴 적에 읽은 에드거 앨런 포우의 <검은 고양이>와 겹쳐지기 때문이다. 이처럼 사소하지만 남들과 다른 주름을 누구나 가지고 있고, 동네에 대한 이미지도 그렇다. 그래서 누군가 특별히 내뱉는 동네이름과 그 동네에서 피어난 노래에서는 \'영광의 탈출 메인테마\'가 장중히 흐르는 \'주말의 명화\'나 \'아랑후에스 협주곡\'이 깔리는 \'토요명화\'의 시작영상처럼 아련함이 스며 나온다.   재주소년은 \'명륜동\'에서 \"잡은 손을 놓지 않고 … 골목을 누비던 밤을\" 추억한다. 별과 달이 있기에 밤하늘을 보는 것처럼 도시는 손금 같은 골목 덕에 발길을 불러 모은다. 으레 무슨 슈퍼마켓의 간판과 마주치고 성가시게 구는 사나운 개의 참견을 받기도 하지만, 오래된 장독대와 리코더 소리를 만날 수 있다. 행여 막다른 길에 다다르더라도 돌아 나오면 그만이다. 으슥한 길에선 공포를 외면하려 콧노래를 부르기도 하는데, 재주소년은 노래 속에 노래를 심어 놓았다. \"귓가엔 폴의 노래가 맴돌았지.\" \'폴\'은 루시드 폴(Lucid Fall)이 아닐까 마음대로 생각해볼 수 있고, 재미있게도 그는 명륜동과 잇닿은 \'삼청동\'을 노래했다. 더 올라가면 밝은 미소의 싱어송라이터 시와가 \'길상사에서\'로 노래한 나이 어린 사찰이 숨어 있다.  현재의 노래들이야말로 훗날의 풍속도   명륜동을 떠나 창덕궁과 창경궁 돌담을 끼고 돌면 고즈넉함을 넘어 쓸쓸하기까지 한 종묘와의 사이길이다. 가루눈 날리던 밤에 걸었던 그 길은 유조차가 아니라 유모차를 밀고 나온 이들까지 터무니없는 대접을 받아야 했던 곳으로 이어진다. 동십자각과 안국역 사이를 차들은 무심히 지나고, 인사동 초입은 인파로 분주하다. 백현진의 \'아구탕에서 나온 네 명\'은 영화 <내 생애 가장 아름다운 일주일>의 현장인 인사동과 탑골공원에 안성철 씨와 앤더슨을 데려다 놓는다. <정글스토리>가 스케치했듯이 머리 긴 총각들의 일터인 \'낙원삘딩\'이 지친 기색으로 내려다보고 있는 곳이다. 홍상수가 영화를 빌려 소설을 썼다면 어긋남을 성찰한 백현진은 음악을 빌려 의뭉스러운 수필을 썼다.   아무 때나 울리는 경적과 기사님들의 불친절이 당연시되던 시절에 비하면 도심은 변했다. 얼마 전까지 그냥 다닐 수 있었던 공간에 대한 권리라도 주장하려는 듯한 태연스러운 무단횡단은 드물어졌다. 하지만 아직도 \"종로에는 사과나무\"가 없고 \"을지로엔 감나무\"가 없다. \"거리마다 푸른 꿈이 넘실거리는\" \'서울\' 찬가는 사진 위에 매직펜으로 마음대로 써넣은 글씨와 같았을 뿐이다. 그보다는 안치환의 \'오늘도 미국 대사관 앞엔\', \"산다는 것이 얼마나 위대한가를, 비참한 우리 가난한 사랑을 위하여\"라고 목 놓아 불렀던 천지인의 \'청계천8가\'가 그 당시를 잘 그려냈다. 이 현재의 노래들이야말로 훗날에는 풍속도가 될 것이다.   ▲지난해 성탄절을 앞두고 화려하게 단장한 홍대입구 걷고 싶은 거리. 자본이 들어온 후 문화는 사라졌다는 비판이 많다. ⓒ뉴시스 이 길은 멀리 신촌까지 닿아 있다. 어느 날, 페럿 한 마리가 옛 철길을 사이에 둔 신촌과 홍대를 잇는 골목길을 가로지르고 있었다. 족제비과의 이 작은 동물은 애완용으로 키워지던 중에 가출했거나 버려졌을 것이다. 접점의 기능을 하고는 있지만 신촌과 홍대는 변했거나 버려졌고, 클럽의 이미지도 달라졌다. 댄스클럽과 댄스음악에 대한 비하가 아니라 소비양상을 말함이다. 홍대 앞 \'문화의 거리\'에서 음악공연이 민원으로 중단되는 허탈한 풍경을 목격하기도 했다. 너무나 익숙한 풍경을 보고 있자니 오히려 어느 먼 이국의 카페에 앉아 있는 것처럼 낯설기만 했다. 그래도 홍대 앞 자취방들에선 노래들이 나지막이 태어나고 있으며, 날이 풀리자 버스커스(거리음악인)들은 \"거리로 나와 사람 속으로\" 들어가고 있다.   범인 검거에 공을 세웠다고 일상적인 감시에 대한 동의서를 얻은 것이 아님에도 서울은 CCTV로 가득하다. 모든 기록이 수집되고 잠재적 범죄자로 대접받아야 하지만, 도시는 또 다른 환경이 되었고, 자연보다 도시에 어울리는 뉴에이지 음악은 흔해졌다. 그렇다고 서울이 모든 동네노래의 주인공이 될 자격을 갖고 있진 않다. 어떤 음식의 이름에 로망을 갖듯이 도시와 거리의 이름에도 환상이 만들어지지만, 문학과 영화가 상해와 뉴욕을 즐겨 그리는 것은 현재의 위상과도 관련이 있다. 20세기 초반의 경성을 매력적으로 다루려는 시도들이 아직까진 성공적이지 못한 이유는 서울의 현재와 무관치 않다. 지역 음악 신(Scene)이 활성화되지 못한 것은 재화와 자원의 서울 편중, 즉 사회구조적 문제와 연결되어 있기도 하다.  노래를 찾아 담아둘 마음의 공터를 위한 자리   그래서 바깥, 아니 저마다의 중심에서 태어난 이름과 노래들이 귀하다. 영국 어디쯤에서 살고 있을 법한 청년들의 음악을 하는 밴드 \'미내리\'의 이름은 충주시 엄정면의 산골마을 미내리에서 가져왔다. 손병휘의 리코더 연주까지 들을 수 있는 문진오의 \'내 고향 장작골\'은 애잔하고, 제주도 음악인인 \'B동 301호\'는 \'겨울새벽, 탑동에서\'를 연주함으로써 자기 동네를 새겨둔다. 김현철의 \'춘천 가는 기차\'처럼 지역과 지역을 잇는 수단이 아니라 그 자체로 공간성을 갖는 길도 음악의 사랑을 받고 있다. 멜로우이어(Mellowyear)는 [The Vane]을 7번 국도를 따라가는 장면들로 채웠고, 너무 짧은 곡들이라며 손사래 칠 수도 있지만 조동익은 하나뮤직 음악인들의 모음음반인 [바다]에 7번국도와 속초를 뿌려 놓았다.  사실은 딱따구리 소리보다 시멘트를 쪼아대는 굴착기 소음이 잦은 도시의 골목 대신 숲길을 더 자주 찾는다. 숲 속 나뭇가지 위에서 벌어지는 찌르레기 떼의 소란스러운 숨바꼭질에 정신을 팔고, 알토 오카리나 소리 비슷한 멧비둘기의 점잖은 수다와 때 이른 매미소리 같은 곤줄박이의 지껄임을 엿듣곤 한다. 이맘때에는 산타클로스의 어린 시절 웃음소리 같은 검은등뻐꾸기와 꾀꼬리의 울음이 한창이다. 밤에는 산을 타고 저수지로 흘러나오는 소쩍새와 호랑지빠귀의 휘파람을 듣는 때가 많다. 그렇게 한참을 바위 위에 새 모양으로 쪼그리고 앉아서 나도 좀 끼워달라는 양 새소리를 흉내내가며 염탐한다.   그래도 도시의 골목은 아련한 초상이다. 지도보기를 좋아했던 사람들이 모두 멀리 떠나지는 않지만, 어떤 사람들은 대단할 것 없는 사연을 안고 구부러진 시간의 골목을 따라 흘러가버렸다. 저녁 냄새가 나고, 사라져 가는 것들의 대변자인 전신주가 남아 있고, 계절이 바뀜에 따라 봄을 기다리며 겨울을 버틴 목련과 담뿍 피는 개나리 그리고 라일락의 향이 가까워졌다 다시 멀어지곤 한다. 같은 세상에 살고 있으나 타인일 수밖에 없는 어느 가족의 소곤거림과 먼저 죽은 이들이 공기 속에 흩어 보낸 숨이 있다. 이런 공기를 전해줄 노래를 찾아 담아둘 마음의 공터를 위해 자리를 비워두는 것이 필요하다. 마음 자체가 소리 없는 악기이기는 하지만, 갑자기 얻은 꽃을 꽂아놓을 수 있는 꽃병 하나쯤은 있어야 하지 않을까.   ▲경남 통영시 동피랑 마을. 벽화가 유명세를 타면서 관광코스가 됐다. 골목길은 관광상품 가치가 사라지면 없어져야 할 대상이 됐다. ⓒ뉴시스  *언급되지 않은 좋은 동네노래 몇 곡은 \'시청앞 지하철역에서 우린 만났었지\'에 있습니다. (☞ 바로가기 : \"시청 앞 지하철역에서 우린 다시 만났었지\")";
	       	
	    	List<Pair<Integer>> keywordList = extractKeywords( content );
	        
	        //test code ( extract keywords from Document )
	        for (int i = 0; i < keywordList.size(); i++) {
	 			Pair<Integer> item = keywordList.get(i);
	 			if (null == item || item.value().intValue() < 40 ) {
	 				continue;
	 			}
	 			System.out.print( item.key() + "^" + item.value() + ",");
	 		}
	        System.out.println("");
	
	}
   	public static void test_getSimilarDoc()
    {
   		System.out.println( "=========== test_getSimilarDoc1() ===========");
    	//String content = "(새벽 03시부터 사용 가능합니다)      인공지능 컴퓨터가  바둑 경기에서 처음으로 프로 기사를 이겼습니다.    세계적 학술지 네이처는 구글의 자회사인 구글 딥마인드가 개발한 컴퓨터 바둑 프로그램 '알파고'가 유럽 바둑 챔피언이자 중국 프로 바둑 기사인 판후이 2단과 다섯 차례 대국에서 모두 이겼다고 발표했습니다.    바둑은 체스와는 달리 탐색 공간이 광범위한 데다 한 수의 위치를 평가하기 어려워 인공지능이 도전하기 어려운 영역으로 여겨졌습니다.    '알파고'는 수의 위치를 평가하는 '가치 네트워크'와 움직임을 선택하는 '정책 네트워크'를 사용하도록 개발됐고 실제 바둑 경기 등을 통해 학습했다고 네이처는 설명했습니다.    네이처는 인공지능도 인간 수준의 능력에 도달할 수 있다는 희망을 제시했다고 평가했습니다.    알파고는 오는 3월 서울에서 바둑 세계 챔피언 이세돌과 맞대결을 벌일 예정입니다.#####";
    	String content = "    정부가 비용을 지원하는 자궁경부암 검진 나이가 기존 30살에서 20살로 낮춰지고, 간암 검진 주기도 1년에서 6개월로 단축됩니다.     보건복지부는  암관리법 시행령 일부 개정안에 따라 의견 수렴을 거쳐, 늦어도 다음달 안에 시행된다고 밝혔습니다.     정부는 자궁경부암이 20살부터 >나타나면서 만 12살 이하 국가 예방접종에 자궁경부암 접종도 포함했고, 간암의 조기 검진의 중요성을 반영했다고 밝혔습니다.     한편, 올해부터는 암과  심장병 등 4대 중증질환의 치료 목적인 초음파 검사와 수면 내시경에도  건강보험 혜택이 이뤄집니다.@@@";
    	
    	System.out.println( content.length() );
    	ArrayList<String> docIdListForFiltering =  new ArrayList<String>();
    	String prefix = "A";
    	
    	//get similar document list by model  		
		List<Pair<Double>> similarDocumentList = getSimilarDoc( content, docIdListForFiltering, prefix );     
        //System.out.println( "get similar doc of whole documents" );
    	for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 		}    
    }
   	
    public static void test_getSimilarDoc2()
    {
    	System.out.println( "=========== test_getSimilarDoc2() ===========");
    	String content = "(새벽 03시부터 사용 가능합니다)      인공지능 컴퓨터가  바둑 경기에서 처음으로 프로 기사를 이겼습니다.    세계적 학술지 네이처는 구글의 자회사인 구글 딥마인드가 개발한 컴퓨터 바둑 프로그램 '알파고'가 유럽 바둑 챔피언이자 중국 프로 바둑 기사인 판후이 2단과 다섯 차례 대국에서 모두 이겼다고 발표했습니다.    바둑은 체스와는 달리 탐색 공간이 광범위한 데다 한 수의 위치를 평가하기 어려워 인공지능이 도전하기 어려운 영역으로 여겨졌습니다.    '알파고'는 수의 위치를 평가하는 '가치 네트워크'와 움직임을 선택하는 '정책 네트워크'를 사용하도록 개발됐고 실제 바둑 경기 등을 통해 학습했다고 네이처는 설명했습니다.    네이처는 인공지능도 인간 수준의 능력에 도달할 수 있다는 희망을 제시했다고 평가했습니다.    알파고는 오는 3월 서울에서 바둑 세계 챔피언 이세돌과 맞대결을 벌일 예정입니다.#####";
    	  
    	ArrayList<String> docIdListForFiltering =  new ArrayList<String>();
    	docIdListForFiltering.add( "article_DI20160421415532" );
    	docIdListForFiltering.add( "article_DI20160421415421" );
    	docIdListForFiltering.add( "article_DI20160421415567" );
    	docIdListForFiltering.add( "article_DI20160421415463" );
    	//resultList.add( "article_DI20160421415572" );
    	
    	String prefix = "";    	
    			    					
    	//get similar document list by model  		
		List<Pair<Double>> similarDocumentList = getSimilarDoc( content, docIdListForFiltering, prefix );     
        //System.out.println( "get similar doc of whole documents" );
    	for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 		}    
    }
    
    public static void test_getSimilarDoc3()
    {
    	System.out.println( "=========== test_getSimilarDoc3() ===========");
    	String content = "(새벽 03시부터 사용 가능합니다)      인공지능 컴퓨터가  바둑 경기에서 처음으로 프로 기사를 이겼습니다.    세계적 학술지 네이처는 구글의 자회사인 구글 딥마인드가 개발한 컴퓨터 바둑 프로그램 '알파고'가 유럽 바둑 챔피언이자 중국 프로 바둑 기사인 판후이 2단과 다섯 차례 대국에서 모두 이겼다고 발표했습니다.    바둑은 체스와는 달리 탐색 공간이 광범위한 데다 한 수의 위치를 평가하기 어려워 인공지능이 도전하기 어려운 영역으로 여겨졌습니다.    '알파고'는 수의 위치를 평가하는 '가치 네트워크'와 움직임을 선택하는 '정책 네트워크'를 사용하도록 개발됐고 실제 바둑 경기 등을 통해 학습했다고 네이처는 설명했습니다.    네이처는 인공지능도 인간 수준의 능력에 도달할 수 있다는 희망을 제시했다고 평가했습니다.    알파고는 오는 3월 서울에서 바둑 세계 챔피언 이세돌과 맞대결을 벌일 예정입니다.#####";
    	
  	   	ArrayList<String> docIdListForFiltering =  new ArrayList<String>();
    	String prefix = "i";
    	
    	//get similar document list by model  		
		List<Pair<Double>> similarDocumentList = getSimilarDoc( content, docIdListForFiltering, prefix );     
        //System.out.println( "get similar doc of whole documents" );
    	for (int i = 0; i < similarDocumentList.size(); i++) {
 			Pair<Double> item = similarDocumentList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 		}    

    }
    
    
    
    public static void test_getSimilarDocWithContent(String type)
    {
    	System.out.println( "=========== test_getSimilarDocWithContent() ===========");
    	//String content = "(새벽 03시부터 사용 가능합니다)      인공지능 컴퓨터가  바둑 경기에서 처음으로 프로 기사를 이겼습니다.    세계적 학술지 네이처는 구글의 자회사인 구글 딥마인드가 개발한 컴퓨터 바둑 프로그램 '알파고'가 유럽 바둑 챔피언이자 중국 프로 바둑 기사인 판후이 2단과 다섯 차례 대국에서 모두 이겼다고 발표했습니다.    바둑은 체스와는 달리 탐색 공간이 광범위한 데다 한 수의 위치를 평가하기 어려워 인공지능이 도전하기 어려운 영역으로 여겨졌습니다.    '알파고'는 수의 위치를 평가하는 '가치 네트워크'와 움직임을 선택하는 '정책 네트워크'를 사용하도록 개발됐고 실제 바둑 경기 등을 통해 학습했다고 네이처는 설명했습니다.    네이처는 인공지능도 인간 수준의 능력에 도달할 수 있다는 희망을 제시했다고 평가했습니다.    알파고는 오는 3월 서울에서 바둑 세계 챔피언 이세돌과 맞대결을 벌일 예정입니다.#####";
    	String content = "(베이루트 AFP=연합뉴스)    5년동안 내전에 시달리는 시리아에서 지난 한 해(2015년) 5만5천여 명이 숨진것으로 추정되고 있습니다.   영국의 '시리아인권관측소'는 희생자 5만5천여 명 가운데 30% 정도인1만 3천여 명이 민간인이었고, 어린이도 2천5백여 명 포함됐다고 밝혔습니다.   내전이 시작된 지난 2011년부터 지금까지 시리아에서 숨진 사람들은 민간인 7만 6천여 명을 포함해 모두 26만 명인 것으로 집계됐습니다.###";
    	
    	//get similar document list by model  		
  	   	ArrayList<String> docIdListForFiltering =  new ArrayList<String>();
  	   	
    	String prefix = ""; // none
		if("image".equals(type)){
			prefix = "I";
		}else if("video".equals(type)){
			prefix = "V";
		}else if("article".equals(type) || "media".equals(type)){
			prefix = "A";
		}
    	
		Map<String,Map<String,String>> resultMap = getSimilarDocWithContent( "media", "TITLE", content, "10", docIdListForFiltering, prefix); 
		
        // how to print similarDocumentList and similarDocumentContentList
    	Iterator<String> iter = resultMap.keySet().iterator();
    	while(iter.hasNext()){
    		String docid = iter.next();
    		System.out.println(docid + " >>>>");
    		Map<String,String> contents = resultMap.get(docid);
    		String[] fields = Tools.joinExclude("TITLE", "/", ":").split(":");
    		
    		for(String field: fields){    			
    			System.out.println(field + " : " + contents.get(field));
    		}
    				
    	}

    }
    
    public static void test_getNer()
    {
    	System.out.println( "=========== test_getNer() ===========");
    	String content = "경기도의회 여야가 누리과정예산안에 합의를 이루지 못하고 처리시한인 지난해 12월 31일을 넘기면서, 경기도와 경기도교육청이 사상 첫 준예산 사태를 맞았습니다.  준예산은 지방자치법에 따라 회계연도가 시작되는 1월 1일까지 예산안이 의결되지 못할 경우, 전년도 예산에 준해 법정 경비만 집행하는 것입니다.  이에 따라이달부터 경기도내 유치원생 19만 8천명과 어린이집 15만6천여 명 등 35만 명이 넘는 유아에 대한 누리과정 지원이 중단됩니다.  유치원과 어린이집 모두 누리과정 예산이 전혀반영되지 않은 곳은 서울·광주·전남에 이어 경기도가 4번째입니다.   앞서 경기도의회 다수당인 더불어민주당은 누리과정 예산을중앙정부가 책임져야 한다며 예산안에 누리과정 예산을 편성하지 않았고, 새누리당은 도교육청 예산으로 6개월분을 우선 편성해야 한다고맞서며 의장석을 점거하는 등 몸싸움이 벌어졌고 의원 4명이 병원으로 실려갔습니다.  예산안 처리가 무산되자 남경필 경기도지사는 입장자료를 내고, 빠른 시간 안에 도의회 임시회를 열어 예산안을 처리해달라고 요청했습니다.  이재정 경기도 교육감은 준예산 사태를 유발한 모든 책임은 대통령과 정부 당국에 있다며 대통령이 결단해달라고 촉구했습니다.///";
  	   	ArrayList<String> docIdListForFiltering =  new ArrayList<String>();
    	String prefix = ""; // none
    	//get similar document list by model  		
		List<Pair<Integer>> nerList = extractNer( content, docIdListForFiltering, prefix );     
        //System.out.println( "get similar doc of whole documents" );
    	for (int i = 0; i < nerList.size(); i++) {
 			Pair<Integer> item = nerList.get(i);
 			if (null == item) {
 				continue;
 			}
 			System.out.println( item.key() + "^" + item.value() );
 		}    

    }
	

    public static List<Pair<Integer>> extractKeywords( String content ) {
    	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);       
        String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.extractKeywordsForPlainText("kbs", query, "TERMS" );
    }
    
    public static List<Pair<Double>> getSimilarDoc( String content, String prefix  ) {
   	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.getSimilarDoc( "kbs", query, "10", prefix, "20160101000000", "20160630000000" );
    }
    
    public static List<Pair<Double>> getSimilarDoc( String content, ArrayList<String> docListForFiltering, String prefix ) {
      	
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	String query = "CONTENT_PLAIN" + "$!$" + content;
         
        return teaClient.getSimilarDoc( "media", query, "10", docListForFiltering, prefix , "20160101000000", "20160630000000" );
    }
    
    //public static List<Pair<Double>> getSimilarDocWithContent( String collection, String fieldToDisplay, String content, String pageSize, List<Pair<Double>> similarDocumentList, List<Pair<String>> similarDocumentContentList, ArrayList<String> docListForFiltering, String prefix ) {
    public static Map<String,Map<String,String>> getSimilarDocWithContent( String collection, String fieldToDisplay, String content, String pageSize, ArrayList<String> docListForFiltering, String prefix ) {
   	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	String query = "CONTENT_PLAIN" + "$!$" + content;
    	
    	System.out.println("- collection : " + collection);
    	System.out.println("- query : " + query);
    	System.out.println("- fieldToDisplay : " + fieldToDisplay);
    	System.out.println("- pageSize : " + pageSize);
    	System.out.println("- docListForFiltering.size() : " + docListForFiltering.size());
    	System.out.println("- prefix : " + prefix);
         
        return teaClient.getSimilarDocWithContent( collection, query, fieldToDisplay, pageSize, docListForFiltering, prefix , "20160101000000", "20160630000000" );
    }
           
    public static List<Pair<Integer>> extractNer( String content, ArrayList<String> searchResultList, String prefix ) {
      	 
    	teaClient = new TeaClient(TEA_IP, TEA_PORT);
    	String query = "CONTENT_PLAIN" + "$!$" + content;
    	
    	return teaClient.extractNerForPlainText("media", query,"10", searchResultList, prefix, "20160101000000", "20160630000000"  );
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
