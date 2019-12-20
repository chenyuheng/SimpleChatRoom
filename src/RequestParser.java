import java.net.URLDecoder;
import java.util.HashMap;

public class RequestParser {
    /**
     * Parse HTTP request head.
     * @param request request message (decoded) without body (if any).
     * @return General parameters in request header can be found by its key in uppercase.
     * Specially, "/method", "/url", "/version", (not in headers) should be found by the key with slash ahead.
     * The HashMap returns null if the corresponding key not exist.
     */
    public static HashMap<String, String> parse(String request) {
        //System.out.println("request: "+request);
        HashMap<String, String> map = new HashMap<>();
        String[] headers = request.split("\r\n");
        String[] startLineTokens = headers[0].split(" ");
        map.put("/method", startLineTokens[0]);
        map.put("/url", startLineTokens[1]);
        map.put("/version", startLineTokens[2]);
        for (int i = 1; i < headers.length; i++) {
            String[] key_value = headers[i].split(": ");
            map.put(key_value[0].toUpperCase(), key_value[1]);
        }
        if (!map.containsKey("CONTENT-LENGTH")) {
            map.put("CONTENT-LENGTH", "0");
        }
        return map;
    }

    public static HashMap<String, String> parseFormInfo(String formInfo) {
        String[] items = formInfo.split("&");
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0 ; i < items.length; i++) {
            String[] kv = items[i].split("=", 2);
            map.put(kv[0].toUpperCase(), URLDecoder.decode(kv[1]));
        }
        return map;
    }
}
