package com.ly.web.voice;

import java.io.File;
import java.io.FileInputStream;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;


/**
 * Created by yongliu on 8/4/16.
 *
 * @author   <a href="mailto:yong.liu@ozstrategy.com">Yong Liu</a>
 * @version  08/04/2016 15:02
 */
public class VoicePlayer {
  //~ Static fields/initializers ---------------------------------------------------------------------------------------

  private static final String DEFAULT_VOICE = "/voices/clock.wav";

  //~ Instance fields --------------------------------------------------------------------------------------------------

  private Logger logger = LoggerFactory.getLogger(getClass());

  private ContinuousAudioDataStream loopAudioData = null;

  private String voiceFile = null;

  //~ Constructors -----------------------------------------------------------------------------------------------------

  /**
   * Creates a new VoicePlayer object.
   */
  public VoicePlayer() {
    URL voiceUrl = getClass().getResource(DEFAULT_VOICE);

    if (voiceUrl != null) {
      voiceFile = voiceUrl.getPath();
    } else {
      logger.error("Not found voice file:" + DEFAULT_VOICE);
    }
  }

  /**
   * Creates a new VoicePlayer object.
   *
   * @param  voiceFile  String
   */
  public VoicePlayer(String voiceFile) {
    Assert.notNull(voiceFile);
    this.voiceFile = voiceFile;
  }

  //~ Methods ----------------------------------------------------------------------------------------------------------

  /**
   * playLoop.
   */
  public void playLoop() {
    logger.info("Play voice: [" + voiceFile + "]");

    try {
      File soundFile = new File(voiceFile);

      if (soundFile.exists()) {
        FileInputStream audioInputStream = new FileInputStream(soundFile);
        AudioStream     audioStream      = new AudioStream(audioInputStream);
        AudioData       audioData        = audioStream.getData();
        loopAudioData = new ContinuousAudioDataStream(audioData);
        AudioPlayer.player.start(loopAudioData);
      } else {
        logger.info("The file:'" + voiceFile + "' not found");
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  //~ ------------------------------------------------------------------------------------------------------------------

  /**
   * stopLoop.
   */
  public void stopLoop() {
    logger.info("Stop play voice: [" + voiceFile + "]");

    if (loopAudioData != null) {
      AudioPlayer.player.stop(loopAudioData);
    }
  }


// public static void main(String[] args) {
//
// VoicePlayer playSounds = new VoicePlayer();
// playSounds.playLoop();
// }

} // end class VoicePlayer
