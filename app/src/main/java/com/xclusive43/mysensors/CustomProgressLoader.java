package com.xclusive43.mysensors;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CustomProgressLoader extends View {

    private Paint paint;
    private int progress; // Progress value (0-100)
    private int circleRadius; // Radius of the circle
    private int circleStrokeWidth; // Width of the circle stroke
    private int circleColor; // Color of the circle
    private int progressColor; // Color of the progress arc

    private ValueAnimator animator;

    public CustomProgressLoader(Context context) {
        super(context);
        init(null);
    }

    public CustomProgressLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomProgressLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);

        // Default values
        circleRadius = 100;
        circleStrokeWidth = 10;
        circleColor = Color.LTGRAY;
        progressColor = Color.BLUE;

        // Load attributes if set
        if (attrs != null) {
            // TODO: Load custom attributes from XML if needed
        }

        // Initialize animator
        animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(1000); // Animation duration in milliseconds
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progress = (int) animation.getAnimatedValue();
                invalidate(); // Redraw the view
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Draw the background circle
        paint.setColor(circleColor);
        paint.setStrokeWidth(circleStrokeWidth);
        canvas.drawCircle(centerX, centerY, circleRadius, paint);

        // Draw the progress arc
        paint.setColor(progressColor);
        float sweepAngle = 360f * progress / 100;
        canvas.drawArc(centerX - circleRadius, centerY - circleRadius,
                centerX + circleRadius, centerY + circleRadius,
                -90, sweepAngle, false, paint);
    }

    // Start animation
    public void startAnimation() {
        animator.start();
    }

    // Stop animation
    public void stopAnimation() {
        animator.cancel();
    }

    // Setters for progress and colors
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // Redraw the view
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }
}
