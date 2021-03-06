package kr.co.wisenut.perftest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import kr.co.wisenut.common.WiseSearchWorker;
import kr.co.wisenut.common.WiseTeaWorker;
import kr.co.wisenut.util.StringUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

/**
 * 샘플 클래스
 * @author dosuser
 *
 */
public class RecommendTest extends AbstractJavaSamplerClient {
	private static final Logger LOG = LoggingManager.getLoggerForClass();

    /**
     * A mask to be applied to the current time in order to add a semi-random
     * component to the sleep time.
     */
    /** The label to store in the sample result. */
    private String label;
	
	/** The default value of the Label parameter. */
    // private static final String LABEL_DEFAULT = "JavaTest";
    /** The name used to store the Label parameter. */
    private static final String LABEL_NAME = "Label";

    /** The response message to store in the sample result. */
    private String responseMessage;

    /** The default value of the ResponseMessage parameter. */
    private static final String RESPONSE_MESSAGE_DEFAULT = "";

    /** The name used to store the ResponseMessage parameter. */
    private static final String RESPONSE_MESSAGE_NAME = "ResponseMessage";

    /** The response code to be stored in the sample result. */
    private String responseCode;

    /** The default value of the ResponseCode parameter. */
    private static final String RESPONSE_CODE_DEFAULT = "";

    /** The name used to store the ResponseCode parameter. */
    private static final String RESPONSE_CODE_NAME = "ResponseCode";

    /** The sampler data (shown as Request Data in the Tree display). */
    private String samplerData;

    /** The default value of the SamplerData parameter. */
    private static final String SAMPLER_DATA_DEFAULT = "";

    /** The name used to store the SamplerData parameter. */
    private static final String SAMPLER_DATA_NAME = "SamplerData";

    /** Holds the result data (shown as Response Data in the Tree display). */
    private String resultData;

    /** The default value of the ResultData parameter. */
    private static final String RESULT_DATA_DEFAULT = "";

    /** The name used to store the ResultData parameter. */
    private static final String RESULT_DATA_NAME = "ResultData";
	
    /* 추천엔진 관련 파라미터 변수 */
    private String SF_IP;
    private int SF_PORT;
	private String TEA_IP;
	private int TEA_PORT;
	
	private String SF_COLLECTION;
	private String TEA_COLLECTION;
	private String PREFIX;
	private String START_DATE;
	private String END_DATE;
	private String DOCUMENT_FIELDS;
	
	private String CONTENTS;
	private String PAGE_SIZE;
	private String KEYWORD;
	
	/*
     * Utility method to set up all the values
     */
    private void setupValues(JavaSamplerContext context) {
    	
    	responseMessage = context.getParameter(RESPONSE_MESSAGE_NAME, RESPONSE_MESSAGE_DEFAULT);

        responseCode = context.getParameter(RESPONSE_CODE_NAME, RESPONSE_CODE_DEFAULT);

        label = context.getParameter(LABEL_NAME, "");
        if (label.length() == 0) {
            label = context.getParameter(TestElement.NAME); // default to name of element
        }

        samplerData = context.getParameter(SAMPLER_DATA_NAME, SAMPLER_DATA_DEFAULT);

        resultData = context.getParameter(RESULT_DATA_NAME, RESULT_DATA_DEFAULT);

    	TEA_IP = context.getParameter("tea_ip");
    	TEA_PORT = context.getIntParameter("tea_port");
    	TEA_COLLECTION = context.getParameter("tea_collection");

    	SF_IP = context.getParameter("sf_ip");
    	SF_PORT = context.getIntParameter("sf_port");
    	SF_COLLECTION = context.getParameter("sf_collection");
    	
    	PREFIX = context.getParameter("prefix");
    	START_DATE = context.getParameter("startdate");
    	END_DATE = context.getParameter("enddate");
    	
    	DOCUMENT_FIELDS = context.getParameter("documentFields");
    	
    	CONTENTS = context.getParameter("contents");
    	
    	PAGE_SIZE = context.getParameter("pageSize");
    	
    	KEYWORD = context.getParameter("keyword");
    }
    
    @Override
    public void setupTest(JavaSamplerContext context) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(whoAmI() + "\tsetupTest()");
            listParameters(context);
        }
    }
    
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("tea_ip", 			"10.0.10.135");
        params.addArgument("tea_port",			String.valueOf(11000));
        params.addArgument("tea_collection",	"media");
        params.addArgument("sf_ip",				"10.0.10.135");
        params.addArgument("sf_port",			String.valueOf(7000));
        params.addArgument("sf_collection",		"article");
        params.addArgument("prefix",			"A");
        params.addArgument("startdate",			"");
        params.addArgument("enddate",			"");
        params.addArgument("documentFields",	"TITLE");
        params.addArgument("contents",			"${content}");
        params.addArgument("pageSize",			"10");
        params.addArgument("keyword",			"");
        return params;
    }

	@Override
	public SampleResult runTest(JavaSamplerContext context) {
		setupValues(context);
		
		SampleResult results = new SampleResult();
		
		results.setResponseCode(responseCode);
        results.setResponseMessage(responseMessage);
        results.setSampleLabel(label);

        if (samplerData != null && samplerData.length() > 0) {
            results.setSamplerData(samplerData);
        }

        if (resultData != null && resultData.length() > 0) {
            results.setResponseData(resultData, null);
            results.setDataType(SampleResult.TEXT);
        }
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> runTest start");
		try {
			// Record sample start time.
			results.sampleStart();
			
			/******************************** TEST START *************************************/ 
			WiseTeaWorker teaWorker = new WiseTeaWorker(TEA_IP, TEA_PORT, TEA_COLLECTION);
			WiseSearchWorker searchWorker = new WiseSearchWorker(SF_IP, SF_PORT);
			List<Map<String,String>> resultList = new ArrayList<Map<String,String>>(); 
			int totalResultCount = 0;
			
			ArrayList<String> docidList = new ArrayList<String>();
			if(StringUtils.isNotEmpty(KEYWORD)){
				String filterQuery = searchWorker.makeFilterQuery("UPD_DTIME", START_DATE, END_DATE);
				
				searchWorker.search(SF_COLLECTION, KEYWORD, "", 0, 10000, filterQuery, "", "", "DOCID");
				docidList = searchWorker.getDocidList();
				
				LOG.info("Searched Docid List size() : " + docidList.size());
			}
			
			// 기사 길이에 따라 모델, 모델+sf1을 구분해서 가져옴.
			String type = "";
			if(TEA_COLLECTION.equals("media")){
				type = "article";
			}else{
				type = TEA_COLLECTION;
			}
    		resultList = teaWorker.getRecommendedContents(type, CONTENTS, PAGE_SIZE, docidList, DOCUMENT_FIELDS, PREFIX, START_DATE, END_DATE);
    		LOG.info("Recommended List size() : " + resultList.size());
    		
        	totalResultCount = teaWorker.getTotalRecommendedMediaCount();

			// DOCID Search에 대한 결과는 한 개이므로 첫번째 결과만 가져와서 add.
	        StringBuffer resultSb = new StringBuffer();
	 		
	 		for(Map<String,String> map: resultList){
	 			Iterator<String> iter = map.keySet().iterator();
	 			while(iter.hasNext()){
	 				String field = iter.next();
	 				resultSb.append("- "+field+" : " + map.get(field) + "\n");
	 			}				
	 			resultSb.append("\n");	 				
			}
				
	 		results.setResponseData(resultSb.toString(), null);
			results.setSamplerData(CONTENTS);
			results.setBodySize(resultSb.toString().getBytes().length);
			results.setSuccessful(true);
			results.setResponseMessage("OK (TotalResultCount : " + totalResultCount);
			results.setResponseCodeOK();
			/******************************** TEST END *************************************/
		}catch (Exception e) {
			results.setSuccessful(false);
			results.setSamplerData(CONTENTS);
			results.setBodySize(-1);
			results.setSuccessful(false);
			results.setResponseMessage(e.getMessage() + "\n" + StringUtil.getSStackTraceElement(e.getStackTrace()));
		} finally {
			results.sampleEnd();
		}
		
		if (LOG.isDebugEnabled()) {
            LOG.debug(whoAmI() + "\trunTest()" + "\tTime:\t" + results.getTime());
            listParameters(context);
        }

		System.out.println("runTest finish >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		return results;
	}
	

    /**
     * Dump a list of the parameters in this context to the debug log.
     * Should only be called if debug is enabled.
     *
     * @param context
     *            the context which contains the initialization parameters.
     */
    private void listParameters(JavaSamplerContext context) {
        Iterator<String> argsIt = context.getParameterNamesIterator();
        while (argsIt.hasNext()) {
            String name = argsIt.next();
            LOG.debug(name + "=" + context.getParameter(name));
        }
    }


    /**
     * Generate a String identifier of this test for debugging purposes.
     *
     * @return a String identifier for this test instance
     */
    private String whoAmI() {
        StringBuilder sb = new StringBuilder();
        sb.append(Thread.currentThread().toString());
        sb.append("@");
        sb.append(Integer.toHexString(hashCode()));
        return sb.toString();
    }
}