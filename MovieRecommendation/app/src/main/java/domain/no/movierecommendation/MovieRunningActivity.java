package domain.no.movierecommendation;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by David on 12/27/2015.
 */
public class MovieRunningActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        UUID id=(UUID)getIntent().getSerializableExtra(NotSeenListFragment.EXTRA_ID);

        return MovieRunningFragment.newInstance(id);
    }
}
