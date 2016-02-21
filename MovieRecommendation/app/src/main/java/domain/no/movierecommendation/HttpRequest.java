package domain.no.movierecommendation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;



public class HttpRequest {


    public ArrayList<String> getUrl(String url) throws IOException{
        return getUrlBytes(url);
    }
    public ArrayList<String> getUrlBytes(String urlStr) throws IOException{
        URL url=new URL(urlStr);
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        ArrayList<String> page=new ArrayList<>();


        try{
            InputStream is=conn.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));


            String line=null;

            while((line=br.readLine())!=null){
                page.add(line);
            }
            is.close();
            return page;
        }finally {
            conn.disconnect();
        }
    }
}
