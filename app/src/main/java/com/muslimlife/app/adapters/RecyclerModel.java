package com.muslimlife.app.adapters;

import com.muslimlife.app.data.model.DiaspoarResponce;
import com.muslimlife.app.data.model.ImamResMain;
import com.muslimlife.app.data.model.OtherEntity;

import java.util.List;

public class RecyclerModel {

    private int type;

    private OtherEntity worship;

    private List<RecyclerModel> models;

    private DiaspoarResponce diaspoarRes;

    private ImamResMain imamRes;

    public RecyclerModel(int type){
        this.type = type;
    }

    public RecyclerModel(int type, OtherEntity worship){
        this.type = type;
        this.worship = worship;
    }

    public RecyclerModel(int type, List<RecyclerModel> models){
        this.type = type;
        this.models = models;
    }

    public RecyclerModel(int type, ImamResMain imamRes){
        this.imamRes=imamRes;
        this.type=type;
    }

    public RecyclerModel(int type, DiaspoarResponce diaspoarRes){
        this.diaspoarRes=diaspoarRes;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public OtherEntity getWorship() {
        return worship;
    }

    public List<RecyclerModel> getModels() {
        return models;
    }

    public ImamResMain getImamRes() {
        return imamRes;
    }

    public DiaspoarResponce getDiaspoarRes() {
        return diaspoarRes;
    }
}
