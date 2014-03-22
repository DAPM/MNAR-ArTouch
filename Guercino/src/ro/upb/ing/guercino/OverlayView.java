package ro.upb.ing.guercino;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class OverlayView extends View {
	private Paint mPaint;
	private Path[] polyPath = {new Path(), new Path(), new Path()};
    private BlurMaskFilter mBlur = new BlurMaskFilter(400, BlurMaskFilter.Blur.NORMAL); 
    RectF rectF = new RectF();
    Region r = new Region();

	private SparseArray<PointF> mActivePointers;

	public OverlayView(Context context) {
		super(context);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    // set painter color to a color you like
	    mPaint.setColor(Color.BLUE);
	    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    mActivePointers = new SparseArray<PointF>();
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    // set painter color to a color you like
	    mPaint.setColor(Color.BLUE);
	    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    mActivePointers = new SparseArray<PointF>();
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    // set painter color to a color you like
	    mPaint.setColor(Color.BLUE);
	    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
	    mActivePointers = new SparseArray<PointF>();
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		// get pointer index from the event object
	    int pointerIndex = event.getActionIndex();

	    // get pointer ID
	    int pointerId = event.getPointerId(pointerIndex);

	    // get masked (not specific to a pointer) action
	    int maskedAction = event.getActionMasked();

	    switch (maskedAction) {

	    case MotionEvent.ACTION_DOWN:
	    case MotionEvent.ACTION_POINTER_DOWN: {
	      // We have a new pointer. Lets add it to the list of pointers

	      PointF f = new PointF();
	      f.x = event.getX(pointerIndex);
	      f.y = event.getY(pointerIndex);
	      mActivePointers.put(pointerId, f);
	      break;
	    }
	    case MotionEvent.ACTION_MOVE: { // a pointer was moved
	      for (int size = event.getPointerCount(), i = 0; i < size; i++) {
	        PointF point = mActivePointers.get(event.getPointerId(i));
	        if (point != null) {
	          point.x = event.getX(i);
	          point.y = event.getY(i);
	        }
	      }
	      break;
	    }
	    case MotionEvent.ACTION_UP:
	    case MotionEvent.ACTION_POINTER_UP:
	    case MotionEvent.ACTION_CANCEL: {
	      mActivePointers.remove(pointerId);
	      break;
	    }
	    }
		
		this.postInvalidate();
		return true;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mPaint.setMaskFilter(null);
		
		//find center point
		int x = -1, y = -1;
		
		// draw all pointers
		for (int size = mActivePointers.size(), i = 0; i < size; i++) {
			PointF point = mActivePointers.valueAt(i);
			if(point != null){
				x+=(int)point.x;
				y+=(int)point.y;
			}
		}
		
		if (x >= 0 && y >= 0){
			x = (int)(x/mActivePointers.size());
			y = (int)(y/mActivePointers.size());
			mPaint.setColor(Color.argb(75, 0, 0, 100));		
		    canvas.drawCircle(x, y, 50, mPaint);
		}
		
		
		//draw zones
	    mPaint.setMaskFilter(mBlur);
		mPaint.setColor(Color.argb(80, 128, 128, 128));
	    mPaint.setStyle(Style.FILL);

	    //Zone 1 - Angel
	    // path
	    polyPath[0].moveTo(0, 0);
	    polyPath[0].lineTo(canvas.getWidth(), 0);
	    polyPath[0].lineTo(canvas.getWidth(), (int)(canvas.getHeight()*0.45));
	    polyPath[0].lineTo((int)(canvas.getWidth()*0.42), (int)(canvas.getHeight()*0.45));
	    polyPath[0].lineTo(0, (int)(canvas.getHeight()*0.10));
	    polyPath[0].close();
	    
	    polyPath[0].computeBounds(rectF, true);
	    r.setPath(polyPath[0], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
	    if(! r.contains(x, y)){
	    // draw
	    canvas.drawPath(polyPath[0], mPaint);
	    }
	    
		//Zone 2 - St. Francisc
	    // path
	    polyPath[1].moveTo(canvas.getWidth(), canvas.getHeight());
	    polyPath[1].lineTo(canvas.getWidth(), (int)(canvas.getHeight()*0.45));
	    polyPath[1].lineTo((int)(canvas.getWidth()*0.42), (int)(canvas.getHeight()*0.45));
	    polyPath[1].lineTo((int)(canvas.getWidth()*0.42), canvas.getHeight());
	    polyPath[1].close();

	    polyPath[1].computeBounds(rectF, true);
	    r.setPath(polyPath[1], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
	    if(! r.contains(x, y)){

	    // draw
	    canvas.drawPath(polyPath[1], mPaint);	
	    }
	    
		//Zone 3 - St. Benedict
	    // path
	    polyPath[2].moveTo(0, canvas.getHeight());
	    polyPath[2].lineTo((int)(canvas.getWidth()*0.42), canvas.getHeight());
	    polyPath[2].lineTo((int)(canvas.getWidth()*0.42), (int)(canvas.getHeight()*0.45));
	    polyPath[2].lineTo(0, (int)(canvas.getHeight()*0.10));
	    polyPath[2].close();
	    
	    polyPath[2].computeBounds(rectF, true);
	    r.setPath(polyPath[2], new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
	    if(! r.contains(x, y)){
	    // draw
	    canvas.drawPath(polyPath[2], mPaint);
	    }
	}
	
}
