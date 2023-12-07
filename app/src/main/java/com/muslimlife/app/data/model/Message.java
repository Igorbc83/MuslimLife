package com.muslimlife.app.data.model;

public class Message {

    //TODO: Тестовые данные - удалить
    public String[]titel={"Тема 1", "Тема 2","Тема 3"};
    public String[]message={"С другой стороны реализация намеченных плановых заданий требуют от нас анализа дальнейших направлений развития.",
                    "И хитрили они, и хитрил Аллах, а Аллах — лучший из хитрецов.",
                    "Он сотворил смерть и жизнь для того, чтобы испытать вас, кто будет лучшим в делах (в работе, труде; занятиях, поступках) [материализуя мысли, размышления и слова в конкретные продуманные и перспективные дела, поступки, имея при этом высокую квалификацию и подходя ко всему ответственно]. Он [Господь миров] — Всемогущ и Всепрощающ."};
    public boolean[]indicator={true,false,true};
    public String[]date={};


    public String[] getDate() { return date; }

    public void setDate(String[] date) {this.date = date; }
    public String[] getTitel() {
        return titel;
    }

    public void setTitel(String[] titel) {
        this.titel = titel;
    }

    public String[] getMessage() {
        return message;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public boolean[] getIndicator() {
        return indicator;
    }

    public void setIndicator(boolean[] indicator) {
        this.indicator = indicator;
    }



}
