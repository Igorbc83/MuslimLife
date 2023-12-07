package com.muslimlife.app.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import com.muslimlife.app.R;

public class WorkTimeUtils {


    public static  String getWorkTime(String[] schedule, Context context){
        String[] days = { context.getString(R.string.pn), context.getString(R.string.vt), context.getString(R.string.sr),
                context.getString(R.string.cht), context.getString(R.string.pt), context.getString(R.string.sb), context.getString(R.string.vs) };

        Map<String, String> res = new HashMap<>();

        String startDay = days[0], endDay = days[0],scheduleItem = schedule[0];

        for (int i = 1; i < 7; i++)
            if (scheduleItem!=null && !scheduleItem.equals(schedule[i]))
            {
                if (startDay.equals(endDay))
                    res.put(endDay, scheduleItem);

                else
                    res.put(startDay+"-"+endDay, scheduleItem);

                scheduleItem = schedule[i];
                endDay = days[i];
                startDay = days[i];
            }
            else
                endDay = days[i];

        if (startDay.equals(endDay))
            res.put(endDay, scheduleItem);
        else
            res.put(startDay+"-"+endDay, scheduleItem);

        StringBuilder output = new StringBuilder();

        for (String key : res.keySet()) {
            output.append(key).append(": ").append(res.get(key)).append('\n');
        }


        return output.toString();
    }

}
