package com.emmano.blurstickyheaderlistviewlib.view;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by emmanuelortiguela on 9/12/14.
 */
public class BlurListView extends ListView implements AbsListView.OnScrollListener {

    private HeaderView headerView;

    private boolean isTitleSticking = false;

    private boolean shouldTitleStick = false;

    private ActionBar actionbar;

    public void shouldTitleStick(boolean shouldTitleStick) {
        this.shouldTitleStick = shouldTitleStick;
    }


    public BlurListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public void controlActionbar(ActionBar actionBar) {
        this.actionbar = actionBar;
    }


    public void setTitle(String title) {
        headerView.setTitleText(title);
    }

    private void init() {
        createHeaderView();
        super.addHeaderView(headerView);
        setOnScrollListener(this);
    }

    private final void createHeaderView() {
        headerView = new HeaderView(getContext());
        headerView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount,
            int totalItemCount) {

        float alpha = (float) -headerView.getTop() / (
                headerView.getBottom());
        if (alpha > 1) {
            alpha = 1;
        }
        headerView.setAlpha(alpha);

        if (shouldTitleStick) {
            if (view.getChildAt(1) != null) {
                final int offset = view.getChildAt(1).getTop() - headerView.getTitleHeight();
                if (offset <= 0) {
                    if (!isTitleSticking) {
                        isTitleSticking = true;
                        configureStickyTitle();
                        headerView.setTitleVisibility(false);
                    }
                } else if (offset > 0
                        && getChildAt(getFirstVisiblePosition()) == headerView) {
                    if (isTitleSticking) {
                        headerView.removeTitle();
                    }
                    isTitleSticking = false;
                    headerView.setTitleVisibility(true);
                }
            }
        }
        setParallax();
    }

    private void setParallax() {
        headerView.setHeaderParallax(2);
        if (actionbar != null) {
            if (headerView.blurredViewAtTop()) {
                actionbar.show();
            } else {
                actionbar.hide();
            }
        }
    }

    private void configureStickyTitle() {
        headerView.configureStickyTitle();
    }

    @Override
    public void addHeaderView(View v) {
        throw new UnsupportedOperationException("No custom headers allowed.");
    }

    public void loadHeaderImage(String imageUrl, Integer placeHolderResourceId, boolean enableLogging, 
            Integer... imageDimns) {
        headerView.loadHeaderImage(imageUrl, placeHolderResourceId,enableLogging, imageDimns);
    }
}
