package com.muslimlife.app.view.koran.listeners;

public interface KoranPageListener {

    default void selectPage(int position){}
    default void rollPage(int position){}
    default void stateChange(boolean stateFull){}
    default void ayahSelect(int surah, int ayah){}
    default void lineTranslate(){}
    default void linePlay(){}
    default void selectReader(){}
    default void lineShare(){}
    default void showSubDialog(){}
}
