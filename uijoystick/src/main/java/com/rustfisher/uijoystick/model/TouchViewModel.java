package com.rustfisher.uijoystick.model;


public class TouchViewModel {
    /**
     * 背景图片资源ID
     */
    private int bgResId;

    /**
     * 触摸图 - 例如一个圆球  资源ID
     */
    private int touchBmpResId;

    /**
     * 整个View的半径
     */
    private float wholeViewRadius;

    /**
     * 背景圆到view边界的px
     */
    private int circleBgGapPx;

    /**
     * 指示当前触摸点与圆心相对方向的图片ID
     */
    private int directionPicResId;

    /**
     * 是否显示指示图片
     */
    private boolean showDirectionPic = false;

    public TouchViewModel(int bgResId, int touchPicResId, float wholeViewRadius) {
        this.bgResId = bgResId;
        this.touchBmpResId = touchPicResId;
        this.wholeViewRadius = wholeViewRadius;
    }

    public boolean isShowDirectionPic() {
        return showDirectionPic;
    }

    public int getDirectionPicResId() {
        return directionPicResId;
    }

    public void setDirectionPicResId(int directionPicResId) {
        this.directionPicResId = directionPicResId;
        showDirectionPic = true;
    }

    public int getCircleBgGapPx() {
        return circleBgGapPx;
    }

    public void setCircleBgGapPx(int px) {
        this.circleBgGapPx = px;
    }

    public int getBgResId() {
        return bgResId;
    }

    public int getTouchBmpResId() {
        return touchBmpResId;
    }

    public float getWholeViewRadius() {
        return wholeViewRadius;
    }

}
