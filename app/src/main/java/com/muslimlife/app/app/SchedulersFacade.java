package com.muslimlife.app.app;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class SchedulersFacade {

    @Inject
    public SchedulersFacade() {

    }

    /**
     * Пул потоков для выполнения "долгих" операций. Рекомендуется использовать для запросов
     * на сервер либо других потенциально долгих операций.
     *
     * @return пул потоков
     */
    public Scheduler io() {
        return Schedulers.io();
    }

    /**
     * Пул потков для "тяжелых" локальных вычислений. Рекомендуется использовать
     * для математических расчетов либо других операций.
     *
     * @return пул потоков
     */
    public Scheduler computation() {
        return Schedulers.computation();
    }

    /**
     * Возвращает планировщик для основного потока Андроид системы. Необходим чтобы общаться
     * с пользовательским интерфесом системы в основном потоке.
     *
     * @return планировщик андроид
     */
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
