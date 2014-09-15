BlurStickyHeaderListView
========================

###What is BlurStickyHeaderListView?

It is a custom `ListView` with a header that displays pictures from an URL. It then adds a nice blur/parallax effect to the downloaded picture. It also provides the option of a sticky title. Here is a [video](https://vid.me/bHJ) of it in action.

###How do I use the thing?

Add `compile 'me.emmano:blurstickyheaderlistview:0.1.4'` to the `dependencies{}` in your build.gradle. If you do not aleady have `jcenter()` added to your project, do so by adding the following to build.gradle:

    repositories {
        jcenter()
    }
You also need to add renderscript to your project. If using android studio add the following to `defaultConfig{}` inside build.gradle:

    defaultConfig {
        //Other config...
        renderscriptTargetApi 19
        renderscriptSupportMode true
    }
`BlurStickyHeaderListView` is pretty straight forward to use. You have two alternatives:

1. Using BlurListFragment.
2. Using BlurListView.


Regardless of which option you use the API is the same. Here is a snippet for your reference.
``` java

import com.emmano.blurstickyheaderlistviewlib.fragment.BlurListFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import java.util.Arrays;


public class MyActivity extends FragmentActivity {

    public static final String DUMMY_TEXT =
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore "
                    + "magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
                    + " Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur."
                    + " Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (savedInstanceState == null) {
            final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Arrays.asList(DUMMY_TEXT, DUMMY_TEXT));
            
          final BlurListFragment blurListFragment = new BlurListFragment();
            //Set up the BlurListFragment before you call FragmentTransaction.commit() methods called after commit() will do nothing.
            blurListFragment.controlActionBar(true);
            blurListFragment.setEnableLoggigng(true);
            blurListFragment.loadHeaderImage("http://someimage",R.drawable.ic_launcher);
            blurListFragment.setBlurHeaderListAdapter(listAdapter);
            blurListFragment.shouldTitleStick(true);
            getSupportFragmentManager().beginTransaction().add(R.id.container, blurListFragment,BlurListFragment.class.getSimpleName()).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
```
###Limitations

1. In order for this library to work, you need to set your `ActionBar` to overlay mode. Go [here](https://developer.android.com/training/basics/actionbar/overlaying.html#EnableOverlay) for steps on how to set your `ActionBar` to overlay mode. 
