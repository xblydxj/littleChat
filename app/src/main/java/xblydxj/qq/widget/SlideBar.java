package xblydxj.qq.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;

import xblydxj.qq.R;
import xblydxj.qq.adapter.ContactRecyclerAdapter;


public class SlideBar extends ListView {

    private static final String TAG = SlideBar.class.getSimpleName();
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private String[] letters = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };
    private int mGap;
    private CardView mFloatHeader;
    private TextView mFloatHeaderText;
    private RecyclerView mContactRecycler;
    private int mChangeTextColor;
    private int mIndex = -1;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //设置背景
                setBackgroundResource(R.drawable.slidebar_bg);
                //显示float_toast
                //定位ListView
            case MotionEvent.ACTION_MOVE:
                //设置背景
                showHeaderAndScroll(ev);

                //更新显示float_toast
                break;
            case MotionEvent.ACTION_UP:
                //修改背景
                mIndex = -1;
//                mPaint.setColor(Color.parseColor("#9c9c9c"));
                setBackgroundColor(Color.TRANSPARENT);
                if (mFloatHeader != null) {
                    mFloatHeader.setVisibility(GONE);
                }
                //隐藏float_toast
                break;
        }
        return true;
    }


    private void showHeaderAndScroll(MotionEvent ev) {
        if (mFloatHeader == null) {
            ViewGroup parent = (ViewGroup) getParent().getParent();
            ViewGroup gradParent = (ViewGroup) parent.getParent();
            mFloatHeader = (CardView) parent.findViewById(R.id.contact_center_tip_card);
            mContactRecycler = (RecyclerView) gradParent.findViewById(R.id.contact_recycler);
            mFloatHeaderText = (TextView) mFloatHeader.findViewById(R.id.contact_center_tip);
        }
        mFloatHeader.setVisibility(VISIBLE);
        String letter = getFloatIndex(ev);
        mFloatHeaderText.setText(letter);
        ContactRecyclerAdapter adapter = (ContactRecyclerAdapter) mContactRecycler.getAdapter();
        if (adapter == null) {
            return;
        }

        String[] sections = adapter.getSections();
        int sectionIndex = -1;
        for (int i = 0; i < sections.length; i++) {
            if (letter.equals(sections[i])) {
                sectionIndex = i;
                break;
            }
        }
        if (sectionIndex != -1) {
            int position = adapter.getPositionForSection(sectionIndex);
            mContactRecycler.smoothScrollToPosition(position);
        }
    }


    private String getFloatIndex(MotionEvent ev) {
        float currentY = ev.getY();
        mIndex = (int) (currentY / mGap);
        if (mIndex < 0) {
            mIndex = 0;
        } else if (mIndex > letters.length - 1) {
            mIndex = letters.length - 1;
        }
        return letters[mIndex];
    }

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 10));
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#9c9c9c"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mGap = (mHeight - 5) / letters.length;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < letters.length; i++) {
            if (mIndex==i) {
                mPaint.setColor(Color.parseColor("#FFFFFF"));
            } else {
                mPaint.setColor(Color.parseColor("#9c9c9c"));
            }
            canvas.drawText(letters[i], mWidth / 2, mGap * (i + 1), mPaint);
        }
    }
}
