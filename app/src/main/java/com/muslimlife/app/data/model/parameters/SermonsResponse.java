package com.muslimlife.app.data.model.parameters;

public class SermonsResponse {


    public int type;
    public String name, title,audio_title, text, image, audio,audio_text, video, created, time_video,time_audio, date, titel_video;



    public SermonsResponse(int type, String name, String title, String audio_title, String text, String image, String audio, String audio_text, String video, String created, String time_video, String time_audio, String date, String titel_video) {
        this.type = type;
        this.name = name;
        this.title = title;
        this.audio_title = audio_title;
        this.text = text;
        this.image = image;
        this.audio = audio;
        this.audio_text = audio_text;
        this.video = video;
        this.created = created;
        this.time_video = time_video;
        this.time_audio = time_audio;
        this.date = date;
        this.titel_video = titel_video;
    }



}
