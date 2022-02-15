package com.yalantis.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.R;
import com.yalantis.ucrop.callback.ChangeImageViewTypeListener;
import com.yalantis.ucrop.callback.CropBoundsChangeListener;
import com.yalantis.ucrop.callback.OverlayViewChangeListener;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class UCropView extends FrameLayout {

    private GestureCropImageView mGestureCropImageView;
    private ImageView mCenterImageView, mVagueImageView;
    private FrameLayout mFlImageViewCrop;
    private final OverlayView mViewOverlay;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.ucrop_view, this, true);
        mGestureCropImageView = findViewById(R.id.image_view_crop);
        mVagueImageView = findViewById(R.id.iv_vague_image);
        mViewOverlay = findViewById(R.id.view_overlay);
        mFlImageViewCrop = findViewById(R.id.fl_image_view_crop);
        mCenterImageView = findViewById(R.id.top_image);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();


        setListenersToViews();
    }

    private void setListenersToViews() {
        mGestureCropImageView.setChangeImageViewTypeListener(new ChangeImageViewTypeListener() {
            @Override
            public void onChangeImageViewType(boolean changed) {
                if (changed) {
//                    getmFlImageViewCrop().getLayoutParams().width = (int) mViewOverlay.getCropViewRect().width();
                    mCenterImageView.getLayoutParams().width = (int) mViewOverlay.getCropViewRect().width() + 10;
                    mVagueImageView.getLayoutParams().width = (int) mViewOverlay.getCropViewRect().width() + 10;
//                    getmFlImageViewCrop().getLayoutParams().height = (int) mViewOverlay.getCropViewRect().height();
                    mCenterImageView.getLayoutParams().height = (int) mViewOverlay.getCropViewRect().height() + 10;
                    mVagueImageView.getLayoutParams().height = (int) mViewOverlay.getCropViewRect().height() + 10;
                    Glide.with(UCropView.this).load(mGestureCropImageView.getImageInputUri())
                            .override((int) mViewOverlay.getCropViewRect().width() + 10,
                                    (int) mViewOverlay.getCropViewRect().height() + 10).
                            into(mCenterImageView);
                    Glide.with(UCropView.this).load(mGestureCropImageView.getImageInputUri())
                            .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 3)))
                            .into(mVagueImageView);
                } else {
//                    getmFlImageViewCrop().getLayoutParams().width = LayoutParams.MATCH_PARENT;
//                    getmFlImageViewCrop().getLayoutParams().height = LayoutParams.MATCH_PARENT;
                    mVagueImageView.setImageDrawable(null);
                    mCenterImageView.setImageDrawable(null);
                }
            }
        });
        mGestureCropImageView.setCropBoundsChangeListener(new CropBoundsChangeListener() {
            @Override
            public void onCropAspectRatioChanged(float cropRatio) {
                mViewOverlay.setTargetAspectRatio(cropRatio);
            }
        });
        mViewOverlay.setOverlayViewChangeListener(new OverlayViewChangeListener() {
            @Override
            public void onCropRectUpdated(RectF cropRect) {

                mCenterImageView.getLayoutParams().width = (int) cropRect.width();
                mCenterImageView.getLayoutParams().height = (int) cropRect.height();
                mGestureCropImageView.setCropRect(cropRect);
            }

            @Override
            public void postTranslate(float deltaX, float deltaY) {
                mGestureCropImageView.postTranslate(deltaX, deltaY);
            }
        });
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

    /**
     * Method for reset state for UCropImageView such as rotation, scale, translation.
     * Be careful: this method recreate UCropImageView instance and reattach it to layout.
     */
    public void resetCropImageView() {
        removeView(mGestureCropImageView);
        mGestureCropImageView = new GestureCropImageView(getContext());
        setListenersToViews();
        mGestureCropImageView.setCropRect(getOverlayView().getCropViewRect());
        addView(mGestureCropImageView, 0);
    }

    public ImageView getmCenterImageView() {
        return mCenterImageView;
    }

    public FrameLayout getmFlImageViewCrop() {
        return mFlImageViewCrop;
    }
}