package com.muslimlife.app.view.message;

import com.muslimlife.app.data.model.GetAnswerRes;
import com.muslimlife.app.data.model.NotificationRes;

public interface AnswerView {
    void chooseAnswer(GetAnswerRes getAnswerRes);
    void chooseNotification(NotificationRes notificationRes);
}
