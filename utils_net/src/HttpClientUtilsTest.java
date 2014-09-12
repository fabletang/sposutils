import org.junit.Before;
import org.junit.Test;

public class HttpClientUtilsTest {
    HttpBytes httpBytes;
   @Before
   public void init(){
      httpBytes=new HttpBytes();
      httpBytes.setReqUrl("http://127.0.0.1:6666/myApp");
   }
   @Test
   public void testHttpGet() throws Exception {
     httpBytes=HttpClientUtil.sendGetReq(httpBytes);
     System.out.println(httpBytes);
   }
    @Test
    public void testHttpPost() throws Exception {
        httpBytes=HttpClientUtil.sendPostReq(httpBytes);
        System.out.println(httpBytes);
    }
}