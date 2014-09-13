package com.emmano.blurstickyheaderlistviewlib.fragment;

import com.emmano.blurstickyheaderlistviewlib.R;
import com.emmano.blurstickyheaderlistviewlib.view.BlurListView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;


/**
 * Created by emmanuelortiguela on 9/11/14.
 */
public class BlurListFragment extends Fragment {


    private BlurListView blurList;

    private Long articleId;

    private boolean shouldControl;

    private boolean shouldStick;

    private String imageUrl;

    private String title;

    private Integer placeholderResourceId;

    private Integer[] picDimens;

    private ListAdapter listAdapter;

    private boolean enableLoggigng;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.blur_list_view_layout, container, false);
        blurList = (BlurListView) root.findViewById(R.id.blur_list);
        blurFragmentConfig();
        return root;
    }

    private void blurFragmentConfig() {
        if (shouldStick) {
            blurList.shouldTitleStick(true);
        }
        if (shouldControl) {
            blurList.controlActionbar(getActivity().getActionBar());
        }

        if (title != null) {
            blurList.setTitle(title);
        }
        if (imageUrl != null) {
            blurList.loadHeaderImage(imageUrl, placeholderResourceId, enableLoggigng,
                    picDimens);
        }

        if (listAdapter != null) {
            blurList.setAdapter(listAdapter);
        } else {
            throw new IllegalStateException(
                    "Adapter not set. Did you forget to call setBlurHeaderListAdapter() before FragmentTransaction.commit()?");
        }
    }


    public void controlActionBar(boolean shouldControl) {
        this.shouldControl = shouldControl;

    }

    public void shouldTitleStick(boolean shouldStick) {
        this.shouldStick = shouldStick;
    }


    public void loadHeaderImage(String imageUrl, Integer placeholderResourceId,
            Integer... picDimens) {
        this.imageUrl = imageUrl;
        this.placeholderResourceId = placeholderResourceId;
        this.picDimens = picDimens;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBlurHeaderListAdapter(ListAdapter listAdapter) {
        this.listAdapter = listAdapter;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public void setEnableLoggigng(boolean enableLoggigng) {
        this.enableLoggigng = enableLoggigng;
    }
}
