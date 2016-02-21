package domain.no.movierecommendation;

import android.support.v4.app.Fragment;

public class NotSeenListActivity extends SingleFragmentActivity{


    @Override
    protected Fragment createFragment() {
        return new NotSeenListFragment();
    }
}
