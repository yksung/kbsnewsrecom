package kr.co.wisenut.perftest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import kr.co.wisenut.common.WiseSearchWorker;
import kr.co.wisenut.util.StringUtil;

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
public class SearchTest extends AbstractJavaSamplerClient {
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
	private String IP;
	private int PORT;
	private String COLLECTION;
	
	private String QUERY;
	private String PAGESIZE;
	private String PAGE;
	private String SEARCH_FIELDS;
	private String DOCUMENT_FIELDS;
	
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

        IP = context.getParameter("ip", "10.113.38.22");
        PORT = context.getIntParameter("port", 7000);
        COLLECTION = context.getParameter("collection", "article");
    	
    	QUERY = context.getParameter("query", "");
    	PAGESIZE = context.getParameter("pageSize", "10");
    	PAGE = context.getParameter("page", "0");
    	SEARCH_FIELDS = context.getParameter("searchFields", "TITLE,CONTENT_PLAIN");
    	DOCUMENT_FIELDS = context.getParameter("documentFields", "DOCID,TITLE");
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
        params.addArgument("ip", "10.113.38.22");
        params.addArgument("port", "7000");
        params.addArgument("collection", "article");
        params.addArgument("query", "");
        params.addArgument("pageSize", "10");
        params.addArgument("page", "1");
        params.addArgument("searchFields", "TITLE,CONTENT_PLAIN");
        params.addArgument("documentFields", "DOCID,TITLE");
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
			WiseSearchWorker searchWorker = new WiseSearchWorker(IP, PORT);
			
			String sort = "", filterQuery = "", prefixQuery = "";
			searchWorker.search(COLLECTION, QUERY, sort, Integer.parseInt(PAGE), Integer.parseInt(PAGESIZE), filterQuery, prefixQuery, SEARCH_FIELDS, DOCUMENT_FIELDS);
			ArrayList<HashMap<String,String>> resultList = searchWorker.getResultList();
			
			// 카테고리 추천 결과를 세팅.
			StringBuffer resultSb = new StringBuffer();
			for(int cnt=0; cnt<resultList.size(); cnt++){
				resultSb.append("- NO : " + (cnt+1) + "\n");

				HashMap<String,String> item = resultList.get(cnt);
				Iterator<String> iter = item.keySet().iterator();
				while(iter.hasNext()){
					String key = iter.next();
					resultSb.append("- "+ key +" : " + item.get(key) + "\n");
				}
				
				resultSb.append("\n\n");
			}
			
			results.setResponseData(resultSb.toString(), null);
			results.setSamplerData(QUERY);
			results.setBodySize(resultSb.toString().getBytes().length);
			results.setSuccessful(true);
			results.setResponseMessage("OK");
			results.setResponseCodeOK();
			/******************************** TEST END *************************************/
			
			results.setSuccessful(true);
		}catch (Exception e) {
			results.setSuccessful(false);
			results.setSamplerData(QUERY);
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