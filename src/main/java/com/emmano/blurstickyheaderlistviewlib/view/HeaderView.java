package com.emmano.blurstickyheaderlistviewlib.view;


import com.emmano.blurstickyheaderlistviewlib.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;

import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by emmanuelortiguela on 9/11/14.
 */
public class HeaderView extends RelativeLayout implements Callback {

    //TODO make it work with RoboGuice
    private ImageView nonBlurredImageView;

    private ImageView blurredImageView;

    private TextView dynamicTitle;

    private TextView titleTextView;

    private Picasso picasso;

    public HeaderView(Context context) {
        super(context);
        init();
        onFinishInflate();
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.list_view_header, this);
        nonBlurredImageView = (ImageView) findViewById(R.id.list_header_nonBlurredImageView);
        blurredImageView = (ImageView) findViewById(R.id.list_header_blurredImageView);
        titleTextView = (TextView) findViewById(R.id.list_view_header_title);
        dynamicTitle = new TextView(getContext());
        picasso = Picasso.with(getContext());
    }


    public void loadHeaderImage(String imageURL, int placeholderResourceId, boolean loggingEnabled,
            Integer... imageDimens
    ) {
        if (loggingEnabled) {
            picasso.setLoggingEnabled(loggingEnabled);
        }

        if (imageDimens.length == 2) {
            picasso.load(imageURL)
                    .placeholder(placeholderResourceId)
                    .resize(imageDimens[0], imageDimens[1])
                    .centerInside()
                    .into(nonBlurredImageView, this);
        } else {
            picasso.load(imageURL)
                    .placeholder(placeholderResourceId)
                    .into(nonBlurredImageView, this);
        }

    }

    public void loadHeaderImage(RequestCreator picasso
    ) {
        picasso.into(nonBlurredImageView, this);
    }


    public void setHeaderParallax(int parallaxMultiplier) {
        blurredImageView.setTop(-getTop() / parallaxMultiplier);
        nonBlurredImageView.setTop(-getTop() / parallaxMultiplier);
    }

    @Override
    public void onSuccess() {
        final Bitmap photo = createBlurImage();
        blurredImageView.setImageBitmap(photo);
    }

    @Override
    public void onError() {

    }

    private Bitmap createBlurImage() {
        final Bitmap photo = ((BitmapDrawable) nonBlurredImageView.getDrawable())
                .getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        final RenderScript rs = RenderScript.create(getContext());
        final Allocation input = Allocation
                .createFromBitmap(rs, photo, Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur
                .create(rs, Element.U8_4(rs));
        script.setInput(input);
        script.setRadius(25f);
        script.forEach(output);
        output.copyTo(photo);
        return photo;
    }

    public boolean blurredViewAtTop(int height) {
        return blurredImageView.getTop() - height <= 0;
    }

    public void configureStickyTitle(int height) {
        dynamicTitle.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        titleTextView.getHeight()));
        dynamicTitle.setBackgroundResource(R.drawable.title_gradient);
        dynamicTitle.setText(titleTextView.getText());
        dynamicTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        dynamicTitle.setTextColor(Color.parseColor("#FFFFFFFF"));
        final ViewGroup parent = (ViewGroup) getParent().getParent();
        if (parent != null) {
            parent.addView(dynamicTitle);
        }
        dynamicTitle.setY(height);
    }

    public void setAlpha(float alpha) {
        blurredImageView.setAlpha(alpha);
    }

    public void setTitleText(String title) {
        titleTextView.setText(title);
    }

    public int getTitleHeight() {
        return titleTextView.getHeight();
    }

    public void setTitleVisibility(boolean titleVisibility) {
        if (!titleVisibility) {
            titleTextView.setVisibility(View.INVISIBLE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
        }
    }

    public void removeTitle() {
        ((ViewGroup) getParent().getParent()).removeView(dynamicTitle);
    }

}
