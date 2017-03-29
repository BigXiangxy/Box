package com.box.lib.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import java.io.File;

public class VoicePlayer {
    private static boolean isPlaying = false;

    private MediaPlayer mediaPlayer = null;
    private String filePath = "";
    private VoiceCallBack voiceCallBack;

    private VoicePlayer() {

    }

    public String getFilePath() {
        return TextUtils.isEmpty(filePath) ? "nullStr" : filePath;
    }

    private static class SingletonHolder {
        private static final VoicePlayer INSTANCE = new VoicePlayer();
    }

    public static VoicePlayer getInstance() {
        return SingletonHolder.INSTANCE;
    }


    public void stopPlayVoice() {
        if (voiceCallBack != null)
            voiceCallBack.onCompletion();
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
            isPlaying = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean playVoice(Context context, String filePath, final VoiceCallBack voiceCallBack) {
        if (isPlaying) {
            stopPlayVoice();
            if (this.filePath.equals(filePath)) {
                VoicePlayer.this.filePath = "";
                return false;
            }
        }
        this.filePath = filePath;
        this.voiceCallBack = voiceCallBack;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) return false;
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mediaPlayer = new MediaPlayer();
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                    stopPlayVoice();
                    VoicePlayer.this.filePath = "";
                }
            });
            mediaPlayer.start();
            isPlaying = true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public interface VoiceCallBack {
        void onCompletion();
    }
}
