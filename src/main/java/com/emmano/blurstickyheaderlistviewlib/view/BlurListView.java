package com.emmano.blurstickyheaderlistviewlib.view;

import com.squareup.picasso.RequestCreator;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
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

    private ActionBar actionBar;

    private ColorDrawable color;

    private int actionBarHeight;

    public void shouldTitleStick(boolean shouldTitleStick) {
        this.shouldTitleStick = shouldTitleStick;
    }

    public BlurListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void controlActionbar(ActionBar actionBar) {

        this.actionBar = actionBar;
    }

    public void setTitle(String title) {
        headerView.setTitleText(title);
    }

    private void init() {
        if (!windowActionBarOverlay()) {
            throw new IllegalStateException(
                    "Your ActionBar has to be set to overlay mode to use this library");
        }
        actionBarHeight = getActionBarHeight();
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

        float alpha = (float) -headerView.getTop() * 2 / (
                headerView.getBottom());
        if (alpha > 1) {
            alpha = 1;
        }

        setActionBarState(alpha);
        headerView.setAlpha(alpha);

        shouldStickMode(view);
        setParallax();

    }

    private void shouldStickMode(AbsListView view) {
        if (shouldTitleStick && view.getChildAt(1) != null) {
            final int titleHeight = view.getChildAt(1).getTop() - headerView.getTitleHeight();
            final int offset = color != null ? titleHeight - actionBarHeight
                    : titleHeight;
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

    private void setActionBarState(float alpha) {
        if (actionBar != null) {
            if (color != null) {
                int alpha255 = (int) (alpha * 255);
                color.setAlpha(alpha255);
                actionBar.setBackgroundDrawable(color);
            }
            if (headerView.blurredViewAtTop(getActionBarHeight())) {
                actionBar.show();
            } else if (actionBar.isShowing() && color == null) {

                actionBar.hide();
            }

        }
    }

    public void setActionBarColor(String color) {
        if (actionBar == null) {
            throw new IllegalStateException(
                    "You haven't given the library control of the ActionBar. Call either controlActionBar() or setControlActionBar()");
        }
        this.color = new ColorDrawable(Color.parseColor(color));
    }

    private void setParallax() {
        headerView.setHeaderParallax(2);

    }

    final private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        int actionBarHeight = TypedValue
                .complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return actionBarHeight;
    }

    private boolean windowActionBarOverlay() {
        TypedValue attributeValue = new TypedValue();
        final Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(android.R.attr.actionBarStyle, attributeValue, true);

        TypedArray a = theme.obtainStyledAttributes(attributeValue.data,
                new int[]{android.R.attr.windowActionBarOverlay});
        boolean isOverlaid = a.getBoolean(0, false);
        a.recycle();

        return isOverlaid;
    }

    private void configureStickyTitle() {
        int dynamicTitleHeight = actionBar != null && !actionBar.isShowing() ? 0
                : getActionBarHeight();
        headerView.configureStickyTitle(dynamicTitleHeight);

    }

    @Override
    public void addHeaderView(View v) {
        throw new UnsupportedOperationException("No custom headers allowed.");
    }

    public void loadHeaderImage(String imageUrl, Integer placeHolderResourceId,
            boolean enableLogging,
            Integer... imageDimns) {
        headerView.loadHeaderImage(imageUrl, placeHolderResourceId, enableLogging, imageDimns);
    }

    public void loadHeaderImage(RequestCreator picassoCreator) {
        headerView.loadHeaderImage(picassoCreator);
    }

}
