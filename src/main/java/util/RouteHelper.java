package util;


public class RouteHelper {

    public static String[] urlParse(String url){
        String[] urlBuf = url.split("/");
        String[] urlParts;
        if (urlBuf.length < 4){
            urlParts = new String[4];
            System.arraycopy(urlBuf,0,urlParts,0,urlBuf.length);
            urlParts[3] = "index";
        } else {
            urlParts = urlBuf;
        }
        return urlParts;
    }
}