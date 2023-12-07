package com.muslimlife.app.view.adapters;

public interface OnItemSelectListener {
    public void onItemSelect(int position);

    default void callPhone(String phone){}
    default  void sendEmail(String email){}

    default  void openWebsite(String website){}
}
