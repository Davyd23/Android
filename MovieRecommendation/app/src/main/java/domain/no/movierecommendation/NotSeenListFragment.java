package domain.no.movierecommendation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


public class NotSeenListFragment extends ListFragment implements MovieRunningFragment.MoviesCallback {
    ArrayList<Movie> titluri;

    public static final String EXTRA_ID="domain.no.movierecommendation.ExtTraId";

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        getActivity().setTitle("Movies list");

        titluri=MovieTheater.get(getActivity()).getMovies();

        Collections.sort(titluri);

        NotSeenAdapter adapter=new NotSeenAdapter(titluri);
        setListAdapter(adapter);
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState){
        super.onCreateView(inflater,parent,savedInstanceState);

        View v=inflater.inflate(R.layout.empty_list,parent,false);

        Button addButton=(Button) v.findViewById(R.id.empty_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie m=new Movie("");
                MovieTheater.get(getActivity()).addMovie(m);

                Intent i=new Intent(getActivity(),MovieRunningActivity.class);
                i.putExtra(EXTRA_ID,m.getId());

                startActivity(i);
            }
        });

        Button updateButton=(Button) v.findViewById(R.id.empty_list_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchItemsTask().execute();
            }
        });

        return v;
    }

    @Override
    public void UpdateList() {
        updateUI();
    }

    private class NotSeenAdapter extends ArrayAdapter<Movie>{
        public NotSeenAdapter(ArrayList<Movie> movies){
            super(getActivity(),0,movies);
        }
        public View getView(int position ,View convertView,ViewGroup parent){
            if(convertView==null){
                convertView=getActivity().getLayoutInflater().inflate(R.layout.list_item_movie,null);
            }
            Movie movie=getItem(position);

            TextView tv=(TextView)convertView.findViewById(R.id.movie_list_item_titleTextView);
            tv.setText(movie.getTitle());

            CheckBox checkBox=(CheckBox)convertView.findViewById(R.id.movie_list_item_solvedCheckBox);
            checkBox.setChecked(movie.isSeen());

            return convertView;
        }
    }


    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String url="https://docs.google.com/spreadsheets/d/1R67RMa0x15kqrq5tLTl_gIgFHWcOdyCfbKz_jaXzHJc/edit#gid=0";
            HttpRequest req=new HttpRequest();

            try{
                ArrayList<String> web=req.getUrl(url);

                    for(int i=0;i<web.size();i++){
                    String[] parts=(web.get(i)).split("px; left: -1px;\">");

                    if(parts.length!=1){
                        for(int j=1;j<parts.length;j++){

                            String[] extra_split=parts[j].split("</div>");

                            extra_split[0]= Jsoup.parse(extra_split[0]).text(); //pt htlm
                            if((MovieTheater.get(getActivity()).exist(extra_split[0])!=true)){
                                MovieTheater.get(getActivity()).addMovie(new Movie(extra_split[0]));
                            }
                        }
                    }
                }

            }catch(Exception ex){}
            return null;
        }
        @Override
        public void onPostExecute(Void v){
            updateUI();
        }
    }

    public void onListItemClick(ListView l,View v,int position,long id){
        Movie m=((NotSeenAdapter)getListAdapter()).getItem(position);

        Intent i=new Intent(getActivity(),MovieRunningActivity.class);
        i.putExtra(EXTRA_ID,m.getId());

        startActivity(i);
    }

    public void updateUI(){
        ((NotSeenAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menu_item_refresh_list:
                new FetchItemsTask().execute();
                break;
            case R.id.menu_item_add:
                Movie movie=new Movie("");
                MovieTheater.get(getActivity()).addMovie(movie);

                Intent i=new Intent(getActivity(),MovieRunningActivity.class);
                i.putExtra(EXTRA_ID,movie.getId());
                startActivity(i);
                break;
        }
        return true;
    }
    @Override
    public void onPause(){
        MovieTheater.get(getActivity()).saveMovies();
        super.onPause();
    }
}

