package org.thoughtcrime.securesms.sms;

import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.recipients.Recipients;

public class OutgoingEncryptedMessage extends OutgoingTextMessage {

  public OutgoingEncryptedMessage(Recipients recipients, String body) {
    super(recipients, body);
  }

  private OutgoingEncryptedMessage(OutgoingEncryptedMessage base, String body) {
    super(base, body);
  }

  @Override
  public boolean isSecureMessage() {
    return true;
  }

  @Override
  public OutgoingTextMessage withBody(String body) {
    return new OutgoingEncryptedMessage(this, body);
  }
}
