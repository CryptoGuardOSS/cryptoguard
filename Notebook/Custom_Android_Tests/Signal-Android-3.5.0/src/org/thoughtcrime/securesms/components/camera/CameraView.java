/***
 Copyright (c) 2013-2014 CommonsWare, LLC
 Portions Copyright (C) 2007 The Android Open Source Project

 Licensed under the Apache License, Version 2.0 (the "License"); you may
 not use this file except in compliance with the License. You may obtain
 a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.thoughtcrime.securesms.components.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.FrameLayout;

import java.io.IOException;
import java.util.List;

import org.thoughtcrime.securesms.ApplicationContext;
import org.thoughtcrime.securesms.util.BitmapUtil;
import org.thoughtcrime.securesms.util.TextSecurePreferences;
import org.thoughtcrime.securesms.util.Util;
import org.whispersystems.jobqueue.Job;
import org.whispersystems.jobqueue.JobParameters;
import org.whispersystems.libaxolotl.util.guava.Optional;

@SuppressWarnings("deprecation")
public class CameraView extends FrameLayout {
  private static final String TAG = CameraView.class.getSimpleName();

  private final CameraSurfaceView   surface;
  private final OnOrientationChange onOrientationChange;

  private @NonNull volatile Optional<Camera> camera   = Optional.absent();
  private          volatile int              cameraId = CameraInfo.CAMERA_FACING_BACK;

  private           boolean            started;
  private @Nullable CameraViewListener listener;
  private           int                displayOrientation     = -1;
  private           int                outputOrientation      = -1;

  public CameraView(Context context) {
    this(context, null);
  }

  public CameraView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public CameraView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setBackgroundColor(Color.BLACK);

    if (isMultiCamera()) cameraId = TextSecurePreferences.getDirectCaptureCameraId(context);

    surface             = new CameraSurfaceView(getContext());
    onOrientationChange = new OnOrientationChange(context.getApplicationContext());
    addView(surface);
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void onResume() {
    if (started) return;
    started = true;
    Log.w(TAG, "onResume() queued");
    enqueueTask(new SerialAsyncTask<Camera>() {
      @Override
      protected @Nullable Camera onRunBackground() {
        try {
          return Camera.open(cameraId);
        } catch (Exception e) {
          Log.w(TAG, e);
          return null;
        }
      }

      @Override
      protected void onPostMain(@Nullable Camera camera) {
        if (camera == null) {
          Log.w(TAG, "tried to open camera but got null");
          if (listener != null) listener.onCameraFail();
          return;
        }

        CameraView.this.camera = Optional.of(camera);
        try {
          if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
            onOrientationChange.enable();
          }
          onCameraReady();
          synchronized (CameraView.this) {
            CameraView.this.notifyAll();
          }
          requestLayout();
          invalidate();
          Log.w(TAG, "onResume() completed");
        } catch (RuntimeException e) {
          Log.w(TAG, "exception when starting camera preview", e);
          onPause();
        }
      }
    });
  }

  public void onPause() {
    if (!started) return;
    started = false;
    Log.w(TAG, "onPause() queued");

    enqueueTask(new SerialAsyncTask<Void>() {
      private Optional<Camera> cameraToDestroy;
      @Override protected void onPreMain() {
        cameraToDestroy = camera;
        camera = Optional.absent();
      }

      @Override protected Void onRunBackground() {
        if (cameraToDestroy.isPresent()) {
          try {
            stopPreview();
            cameraToDestroy.get().release();
            Log.w(TAG, "released old camera instance");
          } catch (Exception e) {
            Log.w(TAG, e);
          }
        }
        return null;
      }

      @Override protected void onPostMain(Void avoid) {
        onOrientationChange.disable();
        displayOrientation = -1;
        outputOrientation = -1;
        Log.w(TAG, "onPause() completed");
      }
    });
  }

  public boolean isStarted() {
    return started;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0 && camera.isPresent()) {
      final Size preferredPreviewSize = CameraUtils.getPreferredPreviewSize(displayOrientation,
                                                                            getMeasuredWidth(),
                                                                            getMeasuredHeight(),
                                                                            camera.get());
      final Parameters parameters = camera.get().getParameters();
      if (preferredPreviewSize != null && !parameters.getPreviewSize().equals(preferredPreviewSize)) {
        Log.w(TAG, "setting preview size to " + preferredPreviewSize.width + "x" + preferredPreviewSize.height);
        stopPreview();
        parameters.setPreviewSize(preferredPreviewSize.width, preferredPreviewSize.height);
        camera.get().setParameters(parameters);
        requestLayout();
        startPreview();
      }
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    final int  width         = r - l;
    final int  height        = b - t;
    final int  previewWidth;
    final int  previewHeight;

    if (camera.isPresent()) {
      final Size previewSize = camera.get().getParameters().getPreviewSize();
      if (displayOrientation == 90 || displayOrientation == 270) {
        previewWidth  = previewSize.height;
        previewHeight = previewSize.width;
      } else {
        previewWidth  = previewSize.width;
        previewHeight = previewSize.height;
      }
    } else {
      previewWidth  = width;
      previewHeight = height;
    }

    if (previewHeight == 0 || previewWidth == 0) {
      Log.w(TAG, "skipping layout due to zero-width/height preview size");
      return;
    }
    Log.w(TAG, "layout " + width + "x" + height + ", target " + previewWidth + "x" + previewHeight);

    if (width * previewHeight > height * previewWidth) {
      final int scaledChildHeight = previewHeight * width / previewWidth;
      surface.layout(0, (height - scaledChildHeight) / 2, width, (height + scaledChildHeight) / 2);
    } else {
      final int scaledChildWidth = previewWidth * height / previewHeight;
      surface.layout((width - scaledChildWidth) / 2, 0, (width + scaledChildWidth) / 2, height);
    }
  }

  public void setListener(@Nullable CameraViewListener listener) {
    this.listener = listener;
  }

  public boolean isMultiCamera() {
    return Camera.getNumberOfCameras() > 1;
  }

  public boolean isRearCamera() {
    return cameraId == CameraInfo.CAMERA_FACING_BACK;
  }

  public void flipCamera() {
    if (Camera.getNumberOfCameras() > 1) {
      cameraId = cameraId == CameraInfo.CAMERA_FACING_BACK
                 ? CameraInfo.CAMERA_FACING_FRONT
                 : CameraInfo.CAMERA_FACING_BACK;
      onPause();
      onResume();
      TextSecurePreferences.setDirectCaptureCameraId(getContext(), cameraId);
    }
  }

  @TargetApi(14)
  private void onCameraReady() {
    if (!camera.isPresent()) return;

    final Parameters parameters = camera.get().getParameters();

    if (VERSION.SDK_INT >= 14) {
      parameters.setRecordingHint(true);
      final List<String> focusModes = parameters.getSupportedFocusModes();
      if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
        parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
      } else if (focusModes.contains(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
        parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
      }
    }

    displayOrientation = CameraUtils.getCameraDisplayOrientation(getActivity(), getCameraInfo());
    camera.get().setDisplayOrientation(displayOrientation);
    camera.get().setParameters(parameters);

    enqueueTask(new PostInitializationTask<Void>() {
      @Override protected void onPostMain(Void avoid) {
        if (camera.isPresent()) {
          try {
            camera.get().setPreviewDisplay(surface.getHolder());
            requestLayout();
          } catch (Exception e) {
            Log.w(TAG, e);
          }
        }
      }
    });
  }

  private void startPreview() {
    if (camera.isPresent()) {
      try {
        camera.get().startPreview();
      } catch (Exception e) {
        Log.w(TAG, e);
      }
    }
  }

  private void stopPreview() {
    if (camera.isPresent()) {
      try {
        camera.get().stopPreview();
      } catch (Exception e) {
        Log.w(TAG, e);
      }
    }
  }

  public int getCameraPictureOrientation() {
    if (getActivity().getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
      outputOrientation = getCameraPictureRotation(getActivity().getWindowManager()
                                                                .getDefaultDisplay()
                                                                .getOrientation());
    } else if (getCameraInfo().facing == CameraInfo.CAMERA_FACING_FRONT) {
      outputOrientation = (360 - displayOrientation) % 360;
    } else {
      outputOrientation = displayOrientation;
    }

    return outputOrientation;
  }

  private @NonNull CameraInfo getCameraInfo() {
    final CameraInfo info = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, info);
    return info;
  }

  // XXX this sucks
  private Activity getActivity() {
    return (Activity)getContext();
  }

  public int getCameraPictureRotation(int orientation) {
    final CameraInfo info = getCameraInfo();
    final int        rotation;

    orientation = (orientation + 45) / 90 * 90;

    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
      rotation = (info.orientation - orientation + 360) % 360;
    } else {
      rotation = (info.orientation + orientation) % 360;
    }

    return rotation;
  }

  private class OnOrientationChange extends OrientationEventListener {
    public OnOrientationChange(Context context) {
      super(context);
      disable();
    }

    @Override
    public void onOrientationChanged(int orientation) {
      if (camera.isPresent() && orientation != ORIENTATION_UNKNOWN) {
        int newOutputOrientation = getCameraPictureRotation(orientation);

        if (newOutputOrientation != outputOrientation) {
          outputOrientation = newOutputOrientation;

          Camera.Parameters params = camera.get().getParameters();

          params.setRotation(outputOrientation);

          try {
            camera.get().setParameters(params);
          }
          catch (Exception e) {
            Log.e(TAG, "Exception updating camera parameters in orientation change", e);
          }
        }
      }
    }
  }

  public void takePicture(final Rect previewRect) {
    if (!camera.isPresent() || camera.get().getParameters() == null) {
      Log.w(TAG, "camera not in capture-ready state");
      return;
    }

    camera.get().setOneShotPreviewCallback(new Camera.PreviewCallback() {
      @Override
      public void onPreviewFrame(byte[] data, final Camera camera) {
        final int  rotation     = getCameraPictureOrientation();
        final Size previewSize  = camera.getParameters().getPreviewSize();
        final Rect croppingRect = getCroppedRect(previewSize, previewRect, rotation);

        Log.w(TAG, "previewSize: " + previewSize.width + "x" + previewSize.height);
        Log.w(TAG, "data bytes: " + data.length);
        Log.w(TAG, "previewFormat: " + camera.getParameters().getPreviewFormat());
        Log.w(TAG, "croppingRect: " + croppingRect.toString());
        Log.w(TAG, "rotation: " + rotation);
        new CaptureTask(previewSize, rotation, croppingRect).execute(data);
      }
    });
  }

  private Rect getCroppedRect(Size cameraPreviewSize, Rect visibleRect, int rotation) {
    final int previewWidth  = cameraPreviewSize.width;
    final int previewHeight = cameraPreviewSize.height;

    if (rotation % 180 > 0) rotateRect(visibleRect);

    float scale = (float) previewWidth / visibleRect.width();
    if (visibleRect.height() * scale > previewHeight) {
      scale = (float) previewHeight / visibleRect.height();
    }
    final float newWidth  = visibleRect.width()  * scale;
    final float newHeight = visibleRect.height() * scale;
    final float centerX   = (VERSION.SDK_INT < 14) ? previewWidth - newWidth / 2 : previewWidth / 2;
    final float centerY   = previewHeight / 2;

    visibleRect.set((int) (centerX - newWidth  / 2),
                    (int) (centerY - newHeight / 2),
                    (int) (centerX + newWidth  / 2),
                    (int) (centerY + newHeight / 2));

    if (rotation % 180 > 0) rotateRect(visibleRect);
    return visibleRect;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void rotateRect(Rect rect) {
    rect.set(rect.top, rect.left, rect.bottom, rect.right);
  }

  private void enqueueTask(SerialAsyncTask job) {
    ApplicationContext.getInstance(getContext()).getJobManager().add(job);
  }

  private static abstract class SerialAsyncTask<Result> extends Job {

    public SerialAsyncTask() {
      super(JobParameters.newBuilder().withGroupId(CameraView.class.getSimpleName()).create());
    }

    @Override public void onAdded() {}

    @Override public final void onRun() {
      try {
        onWait();
        Util.runOnMainSync(new Runnable() {
          @Override public void run() {
            onPreMain();
          }
        });

        final Result result = onRunBackground();

        Util.runOnMainSync(new Runnable() {
          @Override public void run() {
            onPostMain(result);
          }
        });
      } catch (PreconditionsNotMetException e) {
        Log.w(TAG, "skipping task, preconditions not met in onWait()");
      }
    }

    @Override public boolean onShouldRetry(Exception e) {
      return false;
    }

    @Override public void onCanceled() { }

    protected void onWait() throws PreconditionsNotMetException {}
    protected void onPreMain() {}
    protected Result onRunBackground() { return null; }
    protected void onPostMain(Result result) {}
  }

  private abstract class PostInitializationTask<Result> extends SerialAsyncTask<Result> {
    @Override protected void onWait() throws PreconditionsNotMetException {
      synchronized (CameraView.this) {
        if (!camera.isPresent()) {
          throw new PreconditionsNotMetException();
        }
        while (getMeasuredHeight() <= 0 || getMeasuredWidth() <= 0 || !surface.isReady()) {
          Log.w(TAG, String.format("waiting. surface ready? %s", surface.isReady()));
          Util.wait(CameraView.this, 0);
        }
      }
    }
  }

  private class CaptureTask extends AsyncTask<byte[], Void, byte[]> {
    private final Size previewSize;
    private final int  rotation;
    private final Rect croppingRect;

    public CaptureTask(Size previewSize, int rotation, Rect croppingRect) {
      this.previewSize  = previewSize;
      this.rotation     = rotation;
      this.croppingRect = croppingRect;
    }

    @Override
    protected byte[] doInBackground(byte[]... params) {
      final byte[] data = params[0];
      try {
        return BitmapUtil.createFromNV21(data,
                                         previewSize.width,
                                         previewSize.height,
                                         rotation,
                                         croppingRect);
      } catch (IOException e) {
        Log.w(TAG, e);
        return null;
      }
    }

    @Override
    protected void onPostExecute(byte[] imageBytes) {
      if (imageBytes != null && listener != null) listener.onImageCapture(imageBytes);
    }
  }

  private static class PreconditionsNotMetException extends Exception {}

  public interface CameraViewListener {
    void onImageCapture(@NonNull final byte[] imageBytes);
    void onCameraFail();
  }
}
