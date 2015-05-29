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
        if (urlParts[0].equals("")){
            for (int i = 0; i < urlParts.length-1; i++){
                urlParts[i] = urlParts[i+1];
            }
            urlParts[urlParts.length-1] = "";
        }
        return urlParts;
    }
}