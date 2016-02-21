package domain.no.movierecommendation;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by David on 12/26/2015.
 */
public class MovieJSONSerializer {
    private Context mContext;
    private String mFilename;

    public MovieJSONSerializer(Context c,String f){
        mContext=c;
        mFilename=f;
    }

    public void saveMovies(ArrayList<Movie> movies) throws JSONException,IOException{
        JSONArray array=new JSONArray();
        for(Movie m:movies){
            array.put(m.toJSON());
        }

        Writer writer=null;
        try{
            OutputStream out=mContext.openFileOutput(mFilename,Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }

    public ArrayList<Movie> loadMovies() throws JSONException,IOException{
        ArrayList<Movie>  movies=new ArrayList<>();

        BufferedReader reader=null;

        try{
            InputStream in=mContext.openFileInput(mFilename);
            reader=new BufferedReader(new InputStreamReader(in));

            StringBuilder jsonString=new StringBuilder();
            String line=null;

            while ((line=reader.readLine())!=null){
                jsonString.append(line);
            }

            JSONArray array=(JSONArray) new JSONTokener(jsonString.toString()).nextValue();

            for(int i=0;i<array.length();i++){
                movies.add(new Movie(array.getJSONObject(i)));
            }
        }catch (FileNotFoundException ex){
            //when starting fresh
        }finally{
            if(reader!=null) reader.close();

        }

        return movies;
    }
}
