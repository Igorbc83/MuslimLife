package com.muslimlife.app.view.tasbih;

public class TasbihControl {

    int round;
    int currentCount;

    TasbihListener tasbihListener;

    public TasbihControl(TasbihListener tasbihListener){
        this.tasbihListener = tasbihListener;
        this.round = 0;
        this.currentCount = 0;

        update();
    }

    public TasbihControl(TasbihListener tasbihListener, int round, int currentCount){
        this.tasbihListener = tasbihListener;
        this.round = round;
        this.currentCount = currentCount;

        update();
    }

    public void onBead(boolean state) {
        if (state) {
            currentCount += 1;

            if (currentCount > 33)
                nextRound();
        } else {
            currentCount -= 1;
            if (currentCount < 1)
                prevRound();
        }

        update();
    }

    private void nextRound() {
        round += 1;
        if (round > 2) {
            round = 0;
            currentCount = 0;
        }else
            currentCount = 1;
    }

    private void prevRound() {
        round -= 1;

        if (round < 0) {
            round = 0;
            currentCount = 0;
        } else
            currentCount = 33;
    }

    private void update(){tasbihListener.update(round, currentCount);}

    public void reset(){
        round = 0;
        currentCount = 0;

        update();
    }
}
