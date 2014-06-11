package org.lucasr.twowayview;

import android.graphics.Rect;

import org.lucasr.twowayview.TWView.Orientation;

class TWLayoutState {
    private boolean mIsVertical;
    private Rect[] mRects;

    public TWLayoutState(Orientation orientation, int count) {
        setOrientation(orientation);
        mRects = new Rect[count];
        for (int i = 0; i < count; i++) {
            mRects[i] = new Rect();
        }
    }

    public void setOrientation(Orientation orientation) {
        mIsVertical = (orientation == Orientation.VERTICAL);
    }

    public void offset(int offset) {
        for (int i = 0; i < mRects.length; i++) {
            offset(i, offset);
        }
    }

    public void offset(int index, int offset) {
        final Rect rect = mRects[index];
        rect.offset(mIsVertical ? 0 : offset,
                mIsVertical ? offset : 0);
    }

    public int getCount() {
        return mRects.length;
    }

    public Rect get(int index) {
        return mRects[index];
    }

    public void set(int index, int l, int t, int r, int b) {
        final Rect rect = mRects[index];
        rect.left = l;
        rect.top = t;
        rect.right = r;
        rect.bottom = b;
    }

    public void increaseWidthBy(int index, int addedWidth) {
        final Rect rect = mRects[index];
        rect.right += addedWidth;
    }

    public void increaseHeightBy(int index, int addedHeight) {
        final Rect rect = mRects[index];
        rect.bottom += addedHeight;
    }

    public void reduceWidthBy(int index, int removedWidth) {
        final Rect rect = mRects[index];
        rect.right -= Math.min(rect.right - rect.left, removedWidth);
    }

    public void reduceHeightBy(int index, int removedHeight) {
        final Rect rect = mRects[index];
        rect.bottom -= Math.min(rect.bottom - rect.top, removedHeight);
    }

    public int getOuterStartEdge() {
        // TODO: make this a lot more performant/efficient
        int outerState = Integer.MAX_VALUE;
        for (int i = 0; i < mRects.length; i++) {
            final Rect rect = mRects[i];
            outerState = Math.min(outerState, mIsVertical ? rect.top : rect.left);
        }

        return outerState;
    }

    public int getInnerStartEdge() {
        int innerStart = 0;
        for (int i = 0; i < mRects.length; i++) {
            final Rect rect = mRects[i];
            innerStart = Math.max(innerStart, mIsVertical ? rect.top : rect.left);
        }

        return innerStart;
    }

    public int getInnerEndEdge() {
        int innerEnd = Integer.MAX_VALUE;
        for (int i = 0; i < mRects.length; i++) {
            final Rect rect = mRects[i];
            innerEnd = Math.min(innerEnd, mIsVertical ? rect.bottom : rect.right);
        }

        return innerEnd;
    }

    public int getOuterEndEdge() {
        int outerEnd = 0;
        for (int i = 0; i < mRects.length; i++) {
            final Rect rect = mRects[i];
            outerEnd = Math.max(outerEnd, mIsVertical ? rect.bottom : rect.right);
        }

        return outerEnd;
    }

    public boolean intersects(int from, int l, int t, int r, int b) {
        for (int i = from; i < mRects.length; i++) {
            if (mRects[i].intersects(l, t, r, b)) {
                return true;
            }
        }

        return false;
    }
}
