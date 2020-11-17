package org.thoughtcrime.redphone.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

/**
 * Utilities for manipulating device audio configuration
 *
 * @author Stuart O. Anderson
 */
public class AudioUtils {
  private static final String TAG = AudioUtils.class.getName();
  public static void enableDefaultRouting(Context context) {
    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    am.setSpeakerphoneOn(false);
    am.setBluetoothScoOn(false);
    Log.d(TAG, "Set default audio routing");
  }

  public static void enableSpeakerphoneRouting(Context context) {
    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    am.setSpeakerphoneOn(true);
    Log.d(TAG, "Set speakerphone audio routing");
  }

  public static void enableBluetoothRouting(Context context) {
    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    am.startBluetoothSco();
    am.setBluetoothScoOn(true);
  }

  public static void resetConfiguration(Context context) {
    enableDefaultRouting(context);
  }

  public static enum AudioMode {
    DEFAULT,
    HEADSET,
    SPEAKER,
  }

  public static AudioMode getCurrentAudioMode(Context context) {
    AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    if (am.isBluetoothScoOn()) {
      return AudioMode.HEADSET;
    } else if (am.isSpeakerphoneOn()) {
      return AudioMode.SPEAKER;
    } else {
      return AudioMode.DEFAULT;
    }
  }

  public static String getScoUpdateAction() {
    if (Build.VERSION.SDK_INT >= 14) {
      return AudioManager.ACTION_SCO_AUDIO_STATE_UPDATED;
    } else {
      return AudioManager.ACTION_SCO_AUDIO_STATE_CHANGED;
    }
  }
}
