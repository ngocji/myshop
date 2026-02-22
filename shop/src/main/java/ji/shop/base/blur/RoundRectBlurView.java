package ji.shop.base.blur;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import ji.shop.R;

/**
 * Created by vincent on 2017/11/22.
 */

public class RoundRectBlurView extends RealtimeBlurView {
    private Paint mPaint;
    private RectF mRectF;
    private float mAllRadius;
    private float mTopLeftRadius, mTopRightRadius, mBottomLeftRadius, mBottomRightRadius;
    private Bitmap mRoundBitmap;
    private Canvas mTmpCanvas;

    private Path path;

    private Paint mStrokePaint;
    private boolean mEnabledStroke = true;

    public RoundRectBlurView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRectF = new RectF();

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStrokeWidth(context.getResources().getDimension(R.dimen._1dp));
        mStrokePaint.setStyle(Paint.Style.STROKE);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundRectBlurView);
        mAllRadius = a.getDimension(R.styleable.RoundRectBlurView_allRadius, 0);
        mTopLeftRadius = a.getDimension(R.styleable.RoundRectBlurView_topLeftRadius, 0);
        mTopRightRadius = a.getDimension(R.styleable.RoundRectBlurView_topRightRadius, 0);
        mBottomLeftRadius = a.getDimension(R.styleable.RoundRectBlurView_bottomLeftRadius, 0);
        mBottomRightRadius = a.getDimension(R.styleable.RoundRectBlurView_bottomRightRadius, 0);

        mEnabledStroke = a.getBoolean(R.styleable.RoundRectBlurView_enabledStroke, true);

        a.recycle();
    }

    @Override
    protected void drawBlurredBitmap(Canvas canvas, Bitmap blurredBitmap, int overlayColor) {
        super.drawBlurredBitmap(canvas, blurredBitmap, overlayColor);
        // 制造一个和View一样大小的带圆角的bitmap，圆角之外的部分为透明色
        // 此bitmap用来将从父类传进来的canvas裁切为圆角
        mRectF.right = getWidth();
        mRectF.bottom = getHeight();
        if (mRoundBitmap == null) {
            mRoundBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (mTmpCanvas == null) {
            mTmpCanvas = new Canvas(mRoundBitmap);
        }
        if (mAllRadius != 0) {
            mTmpCanvas.drawRoundRect(mRectF, mAllRadius, mAllRadius, mPaint);
        } else {
            if (path == null) {
                path = new Path();
                float[] radii = new float[]{
                        mTopLeftRadius, mTopLeftRadius,  // top-left
                        mTopRightRadius, mTopRightRadius,  // top-right
                        mBottomRightRadius, mBottomRightRadius,              // bottom-right
                        mBottomLeftRadius, mBottomLeftRadius               // bottom-left
                };

                path.addRoundRect(mRectF, radii, Path.Direction.CW);
            }
            mTmpCanvas.drawPath(path, mPaint);
        }

        // 对父类传来的canvas进行圆角裁切
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(mRoundBitmap, 0, 0, mPaint);

        if (mEnabledStroke) {
            if (mStrokePaint.getShader() == null) {
                initStrokeShader();
            }
            if (mAllRadius != 0) {
                canvas.drawRoundRect(mRectF, mAllRadius, mAllRadius, mStrokePaint);
            } else {
                canvas.drawPath(path, mStrokePaint);
            }
        }

//        if (blurredBitmap != null) {
//            mRectF.right = getWidth();
//            mRectF.bottom = getHeight();
//
//            mPaint.reset();
//            mPaint.setAntiAlias(true);
//            BitmapShader shader = new BitmapShader(blurredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//            Matrix matrix = new Matrix();
//            matrix.postScale(mRectF.width() / blurredBitmap.getWidth(), mRectF.height() / blurredBitmap.getHeight());
//            shader.setLocalMatrix(matrix);
//            mPaint.setShader(shader);
//            canvas.drawRoundRect(mRectF, mXRadius, mYRadius, mPaint);
//
//            mPaint.reset();
//            mPaint.setAntiAlias(true);
//            mPaint.setColor(overlayColor);
//            canvas.drawRoundRect(mRectF, mXRadius, mYRadius, mPaint);
//        }
    }

    private void initStrokeShader() {
        mStrokePaint.setShader(
                new LinearGradient(
                        0f, 0f,
                        0f, getHeight(),
                        new int[]{
                                Color.parseColor("#66FFFFFF"),
                                Color.parseColor("#0DFFFFFF"),
                                Color.parseColor("#1AFFFFFF")
                        },
                        new float[]{0f, 0.5f, 1f},
                        Shader.TileMode.CLAMP
                )
        );
    }

    public void setRadius(float radius) {
        this.mAllRadius = radius;
        invalidate();
    }

    public void setRadius(float topLeftRadius, float topRightRadius, float bottomLeftRadius, float bottomRightRadius) {
        this.mTopLeftRadius = topLeftRadius;
        this.mTopRightRadius = topRightRadius;
        this.mBottomLeftRadius = bottomLeftRadius;
        this.mBottomRightRadius = bottomRightRadius;
        this.path = null;
        invalidate();
    }
}
