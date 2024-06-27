package com.xclusive43.mysensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MazeView extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private float width, height;
    private List<Ball> balls;
    private List<CircleBorder> circleBorders;
    private Random random;

    public MazeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        paint = new Paint();
        random = new Random();
        balls = new ArrayList<>();
        circleBorders = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        width = getWidth();
        height = getHeight();
        initializeBalls();
        initializeCircleBorders();
        new Thread(() -> {
            while (true) {
                Canvas canvas = getHolder().lockCanvas();
                if (canvas != null) {
                    drawMaze(canvas);
                    getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }).start();
    }

    private void initializeBalls() {
        for (int i = 0; i < 5; i++) {
            float startX = random.nextFloat() * width;
            float startY = random.nextFloat() * height;
            balls.add(new Ball(startX, startY, 20, getRandomColor()));
        }
    }

    private void initializeCircleBorders() {
        // Add circular borders around the edges of the view
        circleBorders.add(new CircleBorder(width / 2, height / 2, width / 2, Color.RED));
        circleBorders.add(new CircleBorder(width / 2, height / 2, width / 3, Color.GREEN));
        circleBorders.add(new CircleBorder(width / 2, height / 2, width / 4, Color.BLUE));
        circleBorders.add(new CircleBorder(width / 2, height / 2, width / 5, Color.YELLOW));
        circleBorders.add(new CircleBorder(width / 2, height / 2, width / 6, Color.CYAN));
    }

    private void drawMaze(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        // Draw circular borders
        for (CircleBorder circleBorder : circleBorders) {
            paint.setColor(circleBorder.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            canvas.drawCircle(circleBorder.centerX, circleBorder.centerY, circleBorder.radius, paint);
        }

        // Draw balls
        for (Ball ball : balls) {
            paint.setColor(ball.color);
            canvas.drawCircle(ball.x, ball.y, ball.radius, paint);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public void updateBallPosition(float dx, float dy) {
        for (Ball ball : balls) {
            ball.x += dx;
            ball.y += dy;

            // Check collision with circular borders
            for (CircleBorder circleBorder : circleBorders) {
                float dxBorder = ball.x - circleBorder.centerX;
                float dyBorder = ball.y - circleBorder.centerY;
                float distance = (float) Math.sqrt(dxBorder * dxBorder + dyBorder * dyBorder);
                if (distance <= circleBorder.radius) {
                    ball.color = circleBorder.color;
                }
            }

            // Handle screen edge collisions
            if (ball.x - ball.radius < 0 || ball.x + ball.radius > width ||
                    ball.y - ball.radius < 0 || ball.y + ball.radius > height) {
                ball.color = getRandomColor();
                if (ball.x - ball.radius < 0) ball.x = ball.radius;
                if (ball.x + ball.radius > width) ball.x = width - ball.radius;
                if (ball.y - ball.radius < 0) ball.y = ball.radius;
                if (ball.y + ball.radius > height) ball.y = height - ball.radius;
            }
        }
    }

    private int getRandomColor() {
        return Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    private static class Ball {
        float x, y, radius;
        int color;

        Ball(float x, float y, float radius, int color) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.color = color;
        }
    }

    private static class CircleBorder {
        float centerX, centerY, radius;
        int color;

        CircleBorder(float centerX, float centerY, float radius, int color) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.radius = radius;
            this.color = color;
        }
    }
}
