package test.com.tencent.xinge;

import com.tencent.xinge.Message;
import com.tencent.xinge.Style;
import com.tencent.xinge.XingeApp;
import org.json.JSONObject;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Administrator on 2016-03-07.
 */
public class TencentXGTest {
    private final String secretKey = "81be06f0241cd62e750418ecdaebf65f";
    private final long accessId = 2100185400L;
    private final String tokenId = "f82fcac5acfbfc7f422482ba95e72e3a7d9c5db0";

    public static final int STATUS_OK = 0;
    private final XingeApp app = new XingeApp(accessId, secretKey);

    @Test
    public void pushAMessageSilently() throws Exception {
        JSONObject response = app.pushAllDevice(XingeApp.DEVICE_ALL, aSilentMessage());

        assertMessageHasBeenSent(response);
    }

    @Test
    public void pushANotificationMessage() throws Exception {
        JSONObject response = app.pushAllDevice(XingeApp.DEVICE_ALL, aNotificationMessage());

        assertMessageHasBeenSent(response);
    }

    @Test
    public void pushAStyledNotificationMessage() throws Exception {
        Message message = aNotificationMessage();
        //set message style,like:lights,vibrate,ring,.etc
        message.setStyle(new Style(1, 1, 1, 0, 1, 1, 0, 1));

        JSONObject response = app.pushAllDevice(XingeApp.DEVICE_ALL, message);

        assertMessageHasBeenSent(response);
    }

    @Test
    public void pushMessageToMultiDevicesViaTokens() throws Exception {
        //create message
        JSONObject result = app.createMultipush(aNotificationMessage());

        //fetch push id
        int pushId = result.getJSONObject("result").getInt("push_id");

        //push message to devices via tokens
        JSONObject response = app.pushDeviceListMultiple(pushId, asList(tokenId));
        assertMessageHasBeenSent(response);
    }

    private Message aSilentMessage() {
        return new Message() {{
            setTitle("common message");
            setContent("get started tencent xinge push service");
            setType(TYPE_MESSAGE);
        }};
    }

    private Message aNotificationMessage() {
        return new Message() {{
            setTitle("notification");
            setContent("get started tencent xinge push service");
            setType(TYPE_NOTIFICATION);
        }};
    }

    private void assertMessageHasBeenSent(JSONObject response) {
        assertThat(response.getInt("ret_code"), equalTo(STATUS_OK));
    }
}
