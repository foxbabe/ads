package com.sztouyun.advertisingsystem.model.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by wenfeng on 2017/10/19.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayInfo {
    private String playsize;
    private String playDuration;
    private String playTime;

    public String getPlaysize() {
        return playsize;
    }

    public void setPlaysize(String playsize) {
        this.playsize = playsize;
    }

    public String getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(String playDuration) {
        this.playDuration = playDuration;
    }

    public String getPlayTime() {
        return playTime;
    }

    public void setPlayTime(String playTime) {
        this.playTime = playTime;
    }
}
