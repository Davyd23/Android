package domain.no.movierecommendation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by David on 12/26/2015.
 */
public class Movie implements Comparable<Movie>{
    public static final String JSON_TITLE="domain.no.movierecommendation.title";
    public static final String JSON_SEEN="domain.no.movierecommendation.seen";
    public static final String JSON_EXTRA_INFO="domain.no.movierecommendation.extra_info";
    public static final String JSON_ID="domain.no.movierecommendation.id";
    String mTitle;
    boolean mSeen;
    String mExtraInfo;
    private UUID mId;


    public Movie(String title){
        mTitle=title;
        mSeen=false;
        mExtraInfo="";
        mId=UUID.randomUUID();
    }
    public Movie(JSONObject json) throws JSONException{
        mTitle=json.getString(JSON_TITLE);
        mSeen=json.getBoolean(JSON_SEEN);
        mExtraInfo=json.getString(JSON_EXTRA_INFO);
        mId=UUID.fromString(json.getString(JSON_ID));
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isSeen() {
        return mSeen;
    }

    public void setSeen(boolean mSeen) {
        this.mSeen = mSeen;
    }

    public String getExtraInfo() {
        return mExtraInfo;
    }

    public void setExtraInfo(String mExtraInfo) {
        this.mExtraInfo = mExtraInfo;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID mId) {
        this.mId = mId;
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json=new JSONObject();

        json.put(JSON_TITLE,mTitle);
        json.put(JSON_SEEN,mSeen);
        json.put(JSON_EXTRA_INFO,mExtraInfo);
        json.put(JSON_ID,mId.toString());

        return json;
    }

    @Override
    public int compareTo(Movie another) {
        return (this.mTitle).compareTo(another.getTitle());
    }
}
