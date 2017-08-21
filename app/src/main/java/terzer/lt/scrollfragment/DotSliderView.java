package terzer.lt.scrollfragment;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DotSliderView extends View {

    private static Map<DotColor, Paint> dotColorMap = new HashMap<>();
    private Paint outlinePaint;
    private Paint textPaint;

    private List<ColoredDotButton> dotsUpper = Collections.emptyList();
    private List<ColoredDotButton> dotsLower = Collections.emptyList();

    private double dragMultiplier = 2;

    private int buttonSize = 0;

    private int visibleRange = 0;
    private int maxWidth = 0;
    private int maxProgress = 0;
    private int progress = 0;

    private boolean dragMoved = false;
    private float dragPrevious = 0;

    static {
        for (DotColor color : DotColor.values()) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(color.color);
            dotColorMap.put(color, paint);
        }
    }

    public DotSliderView(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.outlinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.outlinePaint.setColor(0xff000000);
        this.outlinePaint.setStyle(Paint.Style.STROKE);
        this.outlinePaint.setStrokeWidth(3);

        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(0xff000000);
        this.textPaint.setTextAlign(Paint.Align.CENTER);
        this.textPaint.setTextSize(50);
    }

    public void setDots(List<ColoredDot> dots) {
        // Padalinti sąrašą į viršutinį ir apatinį sąrašą
        // Viršutinis sąrašas turi būti ilgesnis
        // Perpiešti view
        int half = (int) Math.ceil(dots.size()/(double)2);
        this.dotsUpper = mapButtons(dots.subList(0, half));
        this.dotsLower = mapButtons(dots.subList(half, dots.size()));
        invalidate();
    }

    private List<ColoredDotButton> mapButtons(List<ColoredDot> dots) {
        List<ColoredDotButton> result = new ArrayList<>();
        for (ColoredDot dot : dots)
            result.add(new ColoredDotButton(dot));
        return result;
    }

    private void onClick(MotionEvent event) {
        // Randa paspaustą tašką (jei toks yra) ir paleidžia handler'į
        for (ColoredDotButton button : dotsUpper) {
            if (isOverButton(button, event.getX(), event.getY())) {
                button.getDot().getHandler().onAction(button.getDot());
                return;
            }
        }

        for (ColoredDotButton button : dotsLower) {
            if (isOverButton(button, event.getX(), event.getY())) {
                button.getDot().getHandler().onAction(button.getDot());
                return;
            }
        }
    }

    private boolean isOverButton(ColoredDotButton button, float posX, float posY) {
        float deltaX = button.getPosX() - posX;
        float deltaY = button.getPosY() - posY;
        return Math.sqrt(deltaX*deltaX + deltaY*deltaY) < buttonSize;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            dragPrevious = event.getX();
            dragMoved = false;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // Pridėti progresą, kai stumia pirštu į šoną
            progress += (dragPrevious - event.getX()) * dragMultiplier;
            dragPrevious = event.getX();
            dragMoved = true;
            invalidate(); // Priverstinis perpiešimas
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN && !dragMoved) {
            onClick(event);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight();

        int sliceHeight = height / 3;
        int miniDotSize = sliceHeight/4;
        int bigDotsSize = (int) (sliceHeight*0.8);

        maxWidth = bigDotsSize*dotsUpper.size();
        visibleRange = (int) (visibleDots(canvas.getWidth(), bigDotsSize) * miniDotSize);
        maxProgress = Math.max(0, maxWidth - canvas.getWidth());
        progress = Math.max(0, Math.min(maxProgress, progress));

        // Nupiešti viršutinius taškus (mygtukus), taškų minimap, apatinius taškus (mygtukus)
        drawDotsList(canvas, -progress, 0, bigDotsSize, dotsUpper);
        drawMiniMap(canvas, 0, sliceHeight, sliceHeight, miniDotSize, bigDotsSize);
        drawDotsList(canvas, -progress, sliceHeight*2, bigDotsSize, dotsLower);
    }

    private void drawMiniMap(Canvas canvas, int offsetX, int offsetY, int height, int dotSize, int largeDotSize) {
        // taškelių minimap komponentai turi pajudėti jeigu jeigu yra per daug item'ų
        double percentProgress = maxProgress == 0 ? 0 : progress/(double)maxProgress;
        double leeway = Math.max(0, dotSize*dotsUpper.size() - canvas.getWidth());
        int leewayOffset = (int) (leeway*percentProgress);

        // Nupiešti taškelių minimap komponentus - apatinius ir viršutinius taškus
        int newOffsetY = offsetY + dotSize;
        drawMiniDotsList(canvas, offsetX - leewayOffset, newOffsetY, dotSize, dotsUpper);
        drawMiniDotsList(canvas, offsetX - leewayOffset, newOffsetY + dotSize, dotSize, dotsLower);

        // Nupiešti taškelių minimap view langą
        int offset = (int) (progress/(double)largeDotSize*dotSize) - leewayOffset;
        canvas.drawRect(
            offset + offsetX,
            offsetY,
            offset + offsetX + visibleRange,
            offsetY + height,
            outlinePaint
        );
    }

    private double visibleDots(int canvasWidth, int dotWidth) {
        return (double)canvasWidth/(double)dotWidth;
    }

    private void drawMiniDotsList(Canvas canvas, int offsetX, int offsetY, int height, List<ColoredDotButton> dots) {
        int offset = 0;
        int radius = (int) (height/2 * 0.7);
        int halfHeight = height/2;

        for (ColoredDotButton dot : dots) {
            // Jei taškas bus nematomas, jo nepiešti
            if (offsetX + offset > canvas.getWidth() || offsetX + offset + height < 0) {
                offset += height;
                continue;
            }

            Paint paint = dotColorMap.get(dot.getDot().getColor());
            canvas.drawCircle(offsetX + offset + halfHeight, offsetY + halfHeight, radius, paint);
            canvas.drawCircle(offsetX + offset + halfHeight, offsetY + halfHeight, radius, outlinePaint);
            offset += height;
        }
    }

    private void drawDotsList(Canvas canvas, int offsetX, int offsetY, int height, List<ColoredDotButton> dots) {
        int offset = 0;
        int radius = (int) (height/2 * 0.7);
        int halfHeight = height/2;
        int textOffset = halfHeight - radius;

        buttonSize = radius; // išsaugoti apskaičiuotą mygtuko dydi paspaudimo event'ui

        for (ColoredDotButton dot : dots) {
            int posX = offsetX + offset + halfHeight;
            int posY = offsetY + halfHeight;
            dot.setPos(posX, posY);

            // Jei taškas bus nematomas, jo nepiešti
            if (offsetX + offset > canvas.getWidth() || offsetX + offset + height < 0) {
                offset += height;
                continue;
            }

            Paint paint = dotColorMap.get(dot.getDot().getColor());
            canvas.drawCircle(posX, posY, radius, paint);
            canvas.drawCircle(posX, posY, radius, outlinePaint);

            canvas.drawText(String.valueOf(dot.getDot().getNumber()),
                offsetX + offset + halfHeight,
                offsetY + height + textOffset, textPaint);
            offset += height;
        }
    }
}
