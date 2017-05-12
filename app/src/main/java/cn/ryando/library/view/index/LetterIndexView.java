package cn.ryando.library.view.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class LetterIndexView extends View {

    private OnTouchingLetterChangedListener listener;

    private String[] letters = null;

    private Paint mPaint;

    private float offset;

    private boolean hit;

    private int normalColor;

    private int touchColor;

    private Drawable hintDrawable;

//    private int stringArrayId = R.array.letter_list;

    // TODO: 2017/5/12 set your own letter array
//    private int stringArrayId = R.array.letter_list2;

    private int mTextSize;

    public LetterIndexView(Context paramContext) {
        this(paramContext, null);
    }

    public LetterIndexView(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public LetterIndexView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.mPaint = new Paint();
        this.offset = 0.0F;
        this.hit = false;
        this.normalColor = Color.GRAY;
        this.touchColor = Color.WHITE;

//        hintDrawable = paramContext.getResources().getDrawable(R.drawable.nim_contact_letter_view_hit_point);
//        hintDrawable.setBounds(0, 0, hintDrawable.getIntrinsicWidth(), hintDrawable.getIntrinsicHeight());
        //// TODO: 2017/5/12  init your own drawable here
        hintDrawable = new ColorDrawable(Color.RED);
        // TODO: 2017/5/12  设置你的drawable的大小
        hintDrawable.setBounds(0, 0, 30,30);

        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(normalColor);

//        letters = paramContext.getResources().getStringArray(stringArrayId);
        // TODO: 2017/5/12 set your own letters
        letters = new String[] {
                "#", "A", "B", "C","D","E", "F", "G", "H","I","J", "K", "L", "L","M","N", "O", "P", "Q","R","S", "T", "U", "V","W"
        };
        mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, paramContext.getResources().getDisplayMetrics());
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.listener = onTouchingLetterChangedListener;
    }

    public void setLetters(String[] letters) {
        this.letters = letters;
    }

    public void setNormalColor(int color) {
        this.normalColor = color;
        mPaint.setColor(normalColor);
    }

    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            hit = true;
//            setBackgroundColor(getResources().getColor(R.color.contact_letter_idx_bg));
            // TODO: 2017/5/12 set your own hit background color
            setBackgroundColor(Color.BLUE);
            mPaint.setColor(touchColor);
            onHit(event.getY());
            break;
        case MotionEvent.ACTION_MOVE:
            onHit(event.getY());
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL:
            onCancel();
            break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int abheight = MeasureSpec.getSize(heightMeasureSpec);

        int letterHeight = (int) (mTextSize * 6f / 5f);

        int height = letterHeight * letters.length;

        setMeasuredDimension(width, Math.min(abheight, height));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float letterHeight = ((float)getHeight()) / letters.length;
        float width = getWidth();
        float textSize = letterHeight * 5 / 6;
        mPaint.setTextSize(textSize);
        for (int i = 0; i < letters.length; ++i) {
            float halfWidth = width / 2;
            float letterPosY = letterHeight * i + textSize;
            canvas.drawText(letters[i], halfWidth, letterPosY, mPaint);
        }
        if (hit) {
            int halfWidth = getWidth() / 2;
            int halfDrawWidth = hintDrawable.getIntrinsicWidth() / 2;
            float translateX = halfWidth - halfDrawWidth;
            float halfDrawHeight = hintDrawable.getIntrinsicHeight() / 2;
            float translateY = offset - halfDrawHeight;
            canvas.save();
            canvas.translate(translateX, translateY);
            hintDrawable.draw(canvas);
            canvas.restore();
        }
    }

    private void onHit(float offset) {
        this.offset = offset;
        if (hit && listener != null) {
            int index = (int) (offset / getHeight() * letters.length);
            index = Math.max(index, 0);
            index = Math.min(index, letters.length - 1);
            String str = letters[index];
            listener.onHit(str);
        }
    }

    private void onCancel() {
        hit = false;
        setBackgroundColor(Color.TRANSPARENT);
        mPaint.setColor(this.normalColor);
        refreshDrawableState();

        if (listener != null) {
            listener.onCancel();
        }
    }

    public interface OnTouchingLetterChangedListener {
        void onHit(String letter);

        void onCancel();
    }

}
