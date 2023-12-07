package com.muslimlife.app.data.model;

import java.util.ArrayList;

public class PushTime {
    private final ArrayList<String> timing;
    private final String timezone;

    public PushTime(ArrayList<String> timing, String timezone) {
        this.timing = timing;
        this.timezone = timezone;
    }
}
