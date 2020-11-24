package org.thoughtcrime.securesms.components;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.thoughtcrime.securesms.R;
import org.thoughtcrime.securesms.database.AttachmentDatabase;
import org.thoughtcrime.securesms.events.PartProgressEvent;
import org.thoughtcrime.securesms.mms.Slide;
import org.thoughtcrime.securesms.util.Util;
import org.thoughtcrime.securesms.util.ViewUtil;

import de.greenrobot.event.EventBus;

public class TransferControlView extends FrameLayout {
  private static final int TRANSITION_MS = 300;

  @Nullable private Slide slide;
  @Nullable private View  current;

  private final ProgressWheel progressWheel;
  private final TextView      downloadDetails;
  private final int           contractedWidth;
  private final int           expandedWidth;

  public TransferControlView(Context context) {
    this(context, null);
  }

  public TransferControlView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TransferControlView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    inflate(context, R.layout.transfer_controls_view, this);

    final Drawable background = ContextCompat.getDrawable(context, R.drawable.transfer_controls_background);
    if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) {
      background.setColorFilter(0x66ffffff, Mode.MULTIPLY);
    }
    setLongClickable(false);
    ViewUtil.setBackground(this, background);
    setVisibility(GONE);

    this.progressWheel   = ViewUtil.findById(this, R.id.progress_wheel);
    this.downloadDetails = ViewUtil.findById(this, R.id.download_details);
    this.contractedWidth = getResources().getDimensionPixelSize(R.dimen.transfer_controls_contracted_width);
    this.expandedWidth   = getResources().getDimensionPixelSize(R.dimen.transfer_controls_expanded_width);
  }

  @Override
  public void setFocusable(boolean focusable) {
    super.setFocusable(focusable);
    downloadDetails.setFocusable(focusable);
  }

  @Override
  public void setClickable(boolean clickable) {
    super.setClickable(clickable);
    downloadDetails.setClickable(clickable);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().registerSticky(this);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    EventBus.getDefault().unregister(this);
  }

  public void setSlide(final @NonNull Slide slide) {
    this.slide = slide;
    if (slide.getTransferState() == AttachmentDatabase.TRANSFER_PROGRESS_STARTED) {
      showProgressSpinner();
    } else if (slide.isPendingDownload()) {
      downloadDetails.setText(slide.getContentDescription());
      display(downloadDetails);
    } else {
      display(null);
    }
  }

  public void showProgressSpinner() {
    progressWheel.spin();
    display(progressWheel);
  }

  public void setDownloadClickListener(final @Nullable OnClickListener listener) {
    downloadDetails.setOnClickListener(listener);
  }

  public void clear() {
    clearAnimation();
    setVisibility(GONE);
    if (current != null) {
      current.clearAnimation();
      current.setVisibility(GONE);
    }
    current = null;
    slide   = null;
  }

  private void display(@Nullable final View view) {
    final int sourceWidth = current == downloadDetails ? expandedWidth : contractedWidth;
    final int targetWidth = view    == downloadDetails ? expandedWidth : contractedWidth;

    if (current == view || current == null) {
      ViewGroup.LayoutParams layoutParams = getLayoutParams();
      layoutParams.width = targetWidth;
      setLayoutParams(layoutParams);
    } else {
      ViewUtil.fadeOut(current, TRANSITION_MS);
      Animator anim = getWidthAnimator(sourceWidth, targetWidth);
      anim.start();
    }

    if (view == null) {
      ViewUtil.fadeOut(this, TRANSITION_MS);
    } else {
      ViewUtil.fadeIn(this, TRANSITION_MS);
      ViewUtil.fadeIn(view, TRANSITION_MS);
    }

    current = view;
  }

  private Animator getWidthAnimator(final int from, final int to) {
    final ValueAnimator anim = ValueAnimator.ofInt(from, to);
    anim.addUpdateListener(new AnimatorUpdateListener() {
      @Override
      public void onAnimationUpdate(ValueAnimator animation) {
        final int val = (Integer)animation.getAnimatedValue();
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = val;
        setLayoutParams(layoutParams);
      }
    });
    anim.setInterpolator(new FastOutSlowInInterpolator());
    anim.setDuration(TRANSITION_MS);
    return anim;
  }

  @SuppressWarnings("unused")
  public void onEventAsync(final PartProgressEvent event) {
    if (this.slide != null && event.attachment.equals(this.slide.asAttachment())) {
      Util.runOnMain(new Runnable() {
        @Override
        public void run() {
          progressWheel.setInstantProgress(((float)event.progress) / event.total);
        }
      });
    }
  }
}
