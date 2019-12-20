import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread implements Runnable {

    private Socket client = null;
    public ServerThread(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        try{
            PrintStream out = new PrintStream(client.getOutputStream());
            BufferedReader buf = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String requestHead = "";
            while(true){
                String str =  buf.readLine();
                if((str == null || "".equals(str)) && (!requestHead.equals("") || requestHead != null)){
                    break;
                }else{
                    requestHead += str+"\r\n";
                }
            }
            System.out.println(requestHead);
            HashMap<String, String> heads = RequestParser.parse(requestHead);
            String body = "";
            for (int i = 0; i < Integer.parseInt(heads.get("CONTENT-LENGTH")); i++) {
                body += (char) buf.read();
            }
            heads.put("/body", body);
            String response = "";
            if (heads.get("/method").equals("POST")) {
                response = handlePost(heads);
            } else if (heads.get("/method").equals("GET")) {
                response = handleGet(heads);
            }
            out.println(wrapInHTTP(response));
            System.out.println(response);
            out.close();
            System.out.println(heads.get("/body"));
            client.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String handlePost(HashMap<String, String> requestMap) {
        HashMap<String, String> infoMap = RequestParser.parseFormInfo(requestMap.get("/body"));
        String body = "";
        body += "Received!!<br />";
        body += "Name: " + infoMap.get("NAME") + "<br />";
        body += "Message: " + infoMap.get("MESSAGE") + "<br />";
        new Record(infoMap.get("NAME"), infoMap.get("MESSAGE"));
        return wrapInHTML(body);
    }

    public String handleGet(HashMap<String, String> requestMap) {
        return Record.getJSON();
    }

    public static String wrapInHTML(String body) {
        return "<html><head><meta charset=\"utf-8\"> \n</head><body>" + body + "</body></html>";
    }

    public static String wrapInHTTP(String response) {
        return "HTTP/1.0 200 OK\r\nAccess-Control-Allow-Origin: *\r\nContent-Type:text/html\r\ncontent-length:" + response.getBytes().length + "\r\n\r\n" + response;
    }
}