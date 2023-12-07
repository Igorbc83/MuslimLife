package com.muslimlife.app.view.main.listeners;

import com.muslimlife.app.adapters.RecyclerModel;
import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.ImamResMain;

import java.util.List;

public interface MainRecyclerListener {

    default void initMainRecycler(List<RecyclerModel> models){};

    default void selectWorship(int type){}
    default void selectImam(ImamResMain imamResMain){}
    default void selectDiaspoar(DiaspoarResponce diaspoarResponce){}

    default void selectMap(int type){ }

    default void selectBarakat(){ }

    default void selectMigrants(){ }

    default void launchBottomDialog(String type){};

    default void selectAzkars(int type){}

    default void selectChatAI(){ }
}
