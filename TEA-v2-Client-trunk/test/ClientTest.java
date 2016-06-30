import com.wisenut.tea20.api.TeaClient;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientTest {

    static TeaClient teaClient;

    @BeforeClass
    public static void setupClass() {
        String ip = "61.82.137.86";
        int port = 11000;
        teaClient = new TeaClient(ip, port);
    }

    @Test
    public void historyListTest() {
        //int count = teaClient.selectHistoryInfoTotalCount("server01", "sample_terms");
        int count = teaClient.searchTopicCount("sample_terms", "금융");
        System.err.println(count);
    }
}
