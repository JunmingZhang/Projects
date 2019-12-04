package fall2018.csc207_project.SlidingTileGame;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * the GridView of Gesture detect
 */
public class GestureDetectGridView extends GridView {
    /**
     * minimum distance of swip
     */
    public static final int SWIPE_MIN_DISTANCE = 100;

    /**
     * maximum path of swipe
     */
    public static final int SWIPE_MAX_OFF_PATH = 100;

    /**
     * threshold velocity of wipe
     */
    public static final int SWIPE_THRESHOLD_VELOCITY = 100;

    /**
     * detector of gesture
     */
    private GestureDetector gDetector;

    /**
     * controller of movement
     */
    private MovementController mController;

    /**
     * whether is confirmed
     */
    private boolean mFlingConfirmed = false;

    /**
     * coordinates of Touch
     */
    private float mTouchX;
    private float mTouchY;

    /**
     * Game of Sliding Tile
     */
    private SlidingTileGame slidingTileGame;

    /**
     * construct gridView of detect gesture
     *
     * @param context game activity
     */
    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    /**
     * construct gridView of gesture detect
     *
     * @param context game activity
     * @param attrs   attribute
     */
    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * construct gridView of gesture detect
     *
     * @param context      game activity
     * @param attrs        attribute
     * @param defStyleAttr defStyleAttr
     */
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * init the game center
     *
     * @param context game activity
     */
    private void init(final Context context) {
        mController = new MovementController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                mController.processTapMovement(context, position, true);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    public void setGame(SlidingTileGame slidingTileGame) {
        this.slidingTileGame = slidingTileGame;
        mController.setGame(slidingTileGame);
    }

    public MovementController getMController() {
        return mController;
    }
}
