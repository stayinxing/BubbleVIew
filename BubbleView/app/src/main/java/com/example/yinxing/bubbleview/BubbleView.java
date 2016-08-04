package com.example.yinxing.bubbleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BubbleView extends RelativeLayout {

    private List<Drawable> drawableList = new ArrayList<Drawable>();

    private int riseDuration = 3000;

    private int bottomPadding = 0;
    private int originsOffset = 20;

    private float maxScale = 1.0f;
    private float minScale = 1.0f;

    public BubbleView(Context context) {
        super(context);
    }

    public BubbleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BubbleView setDrawableList(List<Drawable> drawableList) {
        this.drawableList = drawableList;
        return this;
    }

    public BubbleView setDefaultDrawableList() {
        List<Drawable> drawableList = new ArrayList<Drawable>();
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love1));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_glasses));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love2));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_good));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love3));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_kele));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love4));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_donuts));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love5));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_sandwich));
        drawableList.add(getResources().getDrawable(R.drawable.live_ic_love6));

        setDrawableList(drawableList);
        return this;
    }

    public void startAnimation(final int rankWidth, final int rankHeight) {
        bubbleAnimation(rankWidth, rankHeight);

    }

    public void startAnimation(final int rankWidth, final int rankHeight, int count) {
        for (int i = 0; i < count; i++) {
            bubbleAnimation(rankWidth, rankHeight);
        }

    }


    private void bubbleAnimation(int rankWidth, int rankHeight) {
        rankHeight -= dip2px(getContext(), bottomPadding);
        int seed = (int) (Math.random() * 3);
        switch (seed) {
            case 0:
                rankWidth -= originsOffset;
                break;
            case 1:
                rankWidth += originsOffset;
                break;
            case 2:
                rankHeight -= originsOffset;
                break;
        }

        int heartDrawableIndex;
        heartDrawableIndex = (int) (drawableList.size() * Math.random());
        ImageView tempImageView = new ImageView(getContext());
        tempImageView.setImageDrawable(drawableList.get(heartDrawableIndex));
        addView(tempImageView);

        ObjectAnimator riseAlphaAnimator = ObjectAnimator.ofFloat(tempImageView, "alpha", 1.0f, 0.0f);
        riseAlphaAnimator.setDuration(riseDuration);

        ObjectAnimator riseScaleXAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleX", minScale, maxScale);
        riseScaleXAnimator.setDuration(riseDuration);

        ObjectAnimator riseScaleYAnimator = ObjectAnimator.ofFloat(tempImageView, "scaleY", minScale, maxScale);
        riseScaleYAnimator.setDuration(riseDuration);

        ValueAnimator valueAnimator = getBesselAnimator(tempImageView, rankWidth, rankHeight);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(valueAnimator).with(riseAlphaAnimator).with(riseScaleXAnimator).with(riseScaleYAnimator);
        animatorSet.start();
    }

    private ValueAnimator getBesselAnimator(final ImageView imageView, int rankWidth, int rankHeight) {
        float point0[] = new float[2];
        point0[0] = rankWidth / 2;
        point0[1] = rankHeight;

        float point1[] = new float[2];
        point1[0] = (float) ((rankWidth) * (0.10)) + (float) (Math.random() * (rankWidth) * (0.8));
        point1[1] = (float) (rankHeight - Math.random() * rankHeight * (0.5));

        float point2[] = new float[2];
        point2[0] = (float) (Math.random() * rankWidth);
        point2[1] = (float) (Math.random() * (rankHeight - point1[1]));

        float point3[] = new float[2];
        point3[0] = (float) (Math.random() * rankWidth);
        point3[1] = 0;

        BesselEvaluator besselEvaluator = new BesselEvaluator(point1, point2);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(besselEvaluator, point0, point3);
        valueAnimator.setDuration(riseDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] currentPosition = new float[2];
                currentPosition = (float[]) animation.getAnimatedValue();
                imageView.setTranslationX(currentPosition[0]);
                imageView.setTranslationY(currentPosition[1]);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                removeView(imageView);
                imageView.setImageDrawable(null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return valueAnimator;
    }

    public class BesselEvaluator implements TypeEvaluator<float[]> {
        private float point1[] = new float[2], point2[] = new float[2];

        public BesselEvaluator(float[] point1, float[] point2) {
            this.point1 = point1;
            this.point2 = point2;
        }

        @Override
        public float[] evaluate(float fraction, float[] point0, float[] point3) {
            float[] currentPosition = new float[2];
            currentPosition[0] = point0[0] * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + point1[0] * 3 * fraction * (1 - fraction) * (1 - fraction)
                    + point2[0] * 3 * (1 - fraction) * fraction * fraction
                    + point3[0] * fraction * fraction * fraction;
            currentPosition[1] = point0[1] * (1 - fraction) * (1 - fraction) * (1 - fraction)
                    + point1[1] * 3 * fraction * (1 - fraction) * (1 - fraction)
                    + point2[1] * 3 * (1 - fraction) * fraction * fraction
                    + point3[1] * fraction * fraction * fraction;
            return currentPosition;
        }
    }


    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
