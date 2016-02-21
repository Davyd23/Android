package domain.no.movierecommendation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.UUID;

/**
 * Created by David on 12/27/2015.
 */
public class MovieRunningFragment extends Fragment {

    private Movie mMovie;
    EditText mTitle;
    CheckBox mChecked;
    EditText mExtraInfo;
    MoviesCallback mCallback;

    public static final String EXTRA_ID="domain.no.movierecommendation.EXTRA_ID";

    private MovieRunningFragment(){};

    public static Fragment newInstance(UUID id){
        MovieRunningFragment fragment=new MovieRunningFragment();

        Bundle args=new Bundle();
        args.putSerializable(EXTRA_ID,id);

        fragment.setArguments(args);

        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        setHasOptionsMenu(true);

        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

        mCallback=new MoviesCallback() {
            @Override
            public void UpdateList() {

            }
        };

    }

    public View onCreateView(LayoutInflater inflater,ViewGroup parent, Bundle savedInstanceState){

        View v=inflater.inflate(R.layout.movie_view,parent,false);

        mMovie=MovieTheater.get(getActivity()).getMovie((UUID) getArguments().getSerializable(EXTRA_ID));

        mTitle=(EditText)v.findViewById(R.id.movie_view_title);
        if(!(mMovie.getTitle().equals(""))){
            mTitle.setText(mMovie.getTitle());
        }
        mTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMovie.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mChecked=(CheckBox)v.findViewById(R.id.movie_view_checkBox);
        mChecked.setChecked(mMovie.isSeen());
        mChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMovie.setSeen(isChecked);
            }
        });

        mExtraInfo=(EditText)v.findViewById(R.id.movie_view_extraInfo);
        if(!(mMovie.getExtraInfo().equals(""))){
            mExtraInfo.setText(mMovie.getExtraInfo());
        }
        mExtraInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMovie.setExtraInfo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){
            case android.R.id.home :

                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    interface MoviesCallback{
        public void UpdateList();
    }

    public void onPause(){
        mCallback.UpdateList();

        super.onPause();
    }
}
