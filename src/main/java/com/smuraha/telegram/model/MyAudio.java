package com.smuraha.telegram.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyAudio extends BaseEntity{

    private String fileId;
    private int duration;
    private String performer;
    private String title;
    private String searchTitle;
    private int fileSize;

    public MyAudio(byte[] audioFile, org.telegram.telegrambots.meta.api.objects.Audio audio) {
        this.audio = audioFile;
        fileId = audio.getFileId();
        duration = audio.getDuration();
        performer = audio.getPerformer();
        title = audio.getTitle();
        fileSize= audio.getFileSize();
    }

    public MyAudio(int id, int duration, String performer, String title) {
        super(id);
        this.duration = duration;
        this.performer = performer;
        this.title = title;
    }

    private byte[] audio;
}
