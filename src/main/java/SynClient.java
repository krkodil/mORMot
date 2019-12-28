import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

class SynException extends Exception {
    public SynException(int errorCode, String errorText) {
        super(String.format("Server side error: %d %s", errorCode, errorText));
    }
}

public class SynClient {

    private String user;
    private String root;
    private RESTClient http;

    private long sessionID;
    private String passwordHashHexa = "";
    private String sessionIDHexa8 =  "";
    private long sessionPrivateKey  = 0;
    private long sessionTickCountOffset = 0;
    private long serverTimeStampOffset;
    private long lastSessionTickCount = 0;

    public static int HTTP_OK = 200;
    public static int HTTP_FORBIDDEN = 403;

    public SynClient(String host, int port, String root, boolean ssl){
        this.root = root;
        String baseUrl = (ssl?"https":"http") + "://" + host + ":" + port + "/";
        this.http =  new RESTClient(baseUrl);
    }

    public long getTimestamp() {
        return this.nowAsMormotTime() + this.serverTimeStampOffset;
    }
    public boolean login(String user, String password) throws Exception {
        this.user = user;
        this.passwordHashHexa = SHA256("salt" + password);
        this.sessionTickCountOffset = System.currentTimeMillis();
        return doAuth();
    }

    public JSONObject call(String function, JSONArray data) throws Exception {
        JSONObject response = http.doPost(signUrl(root + "/" + function), data);
        if (http.statusCode == HTTP_FORBIDDEN) {
            if (doAuth()) {
                response = http.doPost(signUrl(root +  "/" + function), data);
            }
        }

        if (http.statusCode != HTTP_OK)
            throw new SynException(response.getInt("errorCode"),
                                   response.getString("errorText") );

        return response;
    }

    public void logout() throws Exception {
        String url = signUrl(root + "/auth?UserName="+ this.user + "&Session=" + this.sessionID);
        String response = http.doGet(url);
        if (http.statusCode != HTTP_OK) {
            JSONObject json = new JSONObject(response);
            throw new SynException(json.getInt("errorCode"),
                    json.getString("errorText"));
        }
    }

    private  String byteToHex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }

    private String SHA256(String nonce) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = nonce.getBytes();
            md.update(bytes);
            return byteToHex(md.digest());
        }catch (java.security.NoSuchAlgorithmException err) {
            err.printStackTrace();
        }
        return "";
    }

    private String padL(String s, int count) {
        return String.format("%"+count+"s", s).replace(' ','0');
    }

    private String binaryString(int i, int digits) {
        return padL(Integer.toBinaryString(i), digits);
    }

    private long nowAsMormotTime() {
        Calendar cal = Calendar.getInstance();
        String clientTime = binaryString( cal.get(Calendar.YEAR), 13);
        clientTime += binaryString( cal.get(Calendar.MONTH), 4);
        clientTime += binaryString( cal.get(Calendar.DAY_OF_MONTH)-1, 5);

        clientTime += binaryString( cal.get(Calendar.HOUR_OF_DAY), 5);
        clientTime += binaryString( cal.get(Calendar.MINUTE), 6);
        clientTime += binaryString( cal.get(Calendar.SECOND), 6);

        return Long.valueOf(clientTime, 2);
    }

    private void gotTimestamp(String timestamp) {
        serverTimeStampOffset = Long.parseLong(timestamp) - nowAsMormotTime();
    }

    private String calcClientNonce () {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String clientNonce = df.format(new Date());
        return SHA256(clientNonce);
    }

    private long  gotSession(String sessionKey) {
        int i = sessionKey.indexOf('+');
        sessionID = Long.valueOf(sessionKey.substring(0, i));
        sessionIDHexa8 = padL(Long.toHexString(sessionID), 8);

        long r = CRC32.calculate(sessionKey, 0);
        sessionPrivateKey = CRC32.calculate(passwordHashHexa, r);

        return sessionPrivateKey;
    }

    private boolean doAuth() throws Exception {
        String timeStamp =  http.doGet(root + "/TimeStamp");

        gotTimestamp(timeStamp);

        String response = http.doGet(root + "/auth?UserName="+this.user);
        JSONObject json = new JSONObject(response);
        if (http.statusCode != HTTP_OK)
            throw new SynException(json.getInt("errorCode"), json.getString("errorText") );

        String token = json.getString("result");
        String nonce =  calcClientNonce();
        response =  http.doGet(root +"/auth"+
                 "?UserName="+ user +
                 "&Password="+ SHA256(root + token + nonce + user + passwordHashHexa)+
                 "&ClientNonce=" + nonce);

        json = new JSONObject(response);
        if (http.statusCode != HTTP_OK)
           throw new SynException(json.getInt("errorCode"), json.getString("errorText") );

        return gotSession(json.getString("result")) > 0;
    }

    private String signUrl(String url) {
        long ticks = System.currentTimeMillis() - sessionTickCountOffset;

        if(lastSessionTickCount == ticks)
            ticks += 1;

        lastSessionTickCount = ticks;

        String nonce = padL(Long.toHexString(ticks), 8);
        if (nonce.length() > 8)
            nonce = nonce.substring(nonce.length() - 8);

        long key = CRC32.calculate(url, CRC32.calculate(nonce, sessionPrivateKey));
        String sesionKey = padL(Long.toHexString(key), 8);

        url += url.contains("?") ? "&":"?";
        return url + "session_signature=" + sessionIDHexa8 + nonce + sesionKey;
    }

}