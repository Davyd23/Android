package domain.no.movierecommendation;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by David on 12/26/2015.
 */
public class MovieTheater {
    private static MovieTheater mMovieTheater;
    private Context mAppContext;
    private ArrayList<Movie> mMovies;
    private MovieJSONSerializer mSerializer;

    private static final String mFilename="movies.json";

    private MovieTheater(Context context){
        mAppContext=context;

        mSerializer=new MovieJSONSerializer(mAppContext,mFilename);

        try{
            mMovies=mSerializer.loadMovies();
        }catch (Exception ex){
            mMovies=new ArrayList<>();
        }
    }

    public static MovieTheater get(Context c){
        if(mMovieTheater==null){
            mMovieTheater=new MovieTheater(c.getApplicationContext());
        }
        return mMovieTheater;
    }

    public void addMovie(Movie m){
        mMovies.add(m);
    }

    public ArrayList<Movie> getMovies(){
        return mMovies;
    }

    public Movie getMovie(UUID id){
        for(Movie m:mMovies){
            if(m.getId().equals(id)){
                return m;
            }
        }
        return null;
    }

    public boolean exist(String title){
        for(Movie m:mMovies){
            if(m.getTitle().equals(title)){
                return true;
            }
        }
        return false;
    }

    public void saveMovies(){
        try{
            mSerializer.saveMovies(mMovies);
        }catch (Exception ex){}
    }
}
