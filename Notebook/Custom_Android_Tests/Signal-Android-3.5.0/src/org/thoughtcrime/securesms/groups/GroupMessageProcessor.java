package org.thoughtcrime.securesms.groups;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.protobuf.ByteString;

import org.thoughtcrime.securesms.ApplicationContext;
import org.thoughtcrime.securesms.crypto.MasterSecretUnion;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.EncryptingSmsDatabase;
import org.thoughtcrime.securesms.database.GroupDatabase;
import org.thoughtcrime.securesms.database.MmsDatabase;
import org.thoughtcrime.securesms.jobs.AvatarDownloadJob;
import org.thoughtcrime.securesms.mms.OutgoingGroupMediaMessage;
import org.thoughtcrime.securesms.notifications.MessageNotifier;
import org.thoughtcrime.securesms.recipients.RecipientFactory;
import org.thoughtcrime.securesms.recipients.Recipients;
import org.thoughtcrime.securesms.sms.IncomingGroupMessage;
import org.thoughtcrime.securesms.sms.IncomingTextMessage;
import org.thoughtcrime.securesms.util.Base64;
import org.thoughtcrime.securesms.util.GroupUtil;
import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.messages.TextSecureAttachment;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;
import org.whispersystems.textsecure.api.messages.TextSecureGroup;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ws.com.google.android.mms.MmsException;

import static org.thoughtcrime.securesms.database.GroupDatabase.GroupRecord;
import static org.whispersystems.textsecure.internal.push.TextSecureProtos.AttachmentPointer;
import static org.whispersystems.textsecure.internal.push.TextSecureProtos.GroupContext;

public class GroupMessageProcessor {

  private static final String TAG = GroupMessageProcessor.class.getSimpleName();

  public static void process(@NonNull Context context,
                             @NonNull MasterSecretUnion masterSecret,
                             @NonNull TextSecureEnvelope envelope,
                             @NonNull TextSecureDataMessage message,
                             boolean outgoing)
  {
    if (!message.getGroupInfo().isPresent() || message.getGroupInfo().get().getGroupId() == null) {
      Log.w(TAG, "Received group message with no id! Ignoring...");
      return;
    }

    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    TextSecureGroup group    = message.getGroupInfo().get();
    byte[]        id       = group.getGroupId();
    GroupRecord   record   = database.getGroup(id);

    if (record != null && group.getType() == TextSecureGroup.Type.UPDATE) {
      handleGroupUpdate(context, masterSecret, envelope, group, record, outgoing);
    } else if (record == null && group.getType() == TextSecureGroup.Type.UPDATE) {
      handleGroupCreate(context, masterSecret, envelope, group, outgoing);
    } else if (record != null && group.getType() == TextSecureGroup.Type.QUIT) {
      handleGroupLeave(context, masterSecret, envelope, group, record, outgoing);
    } else {
      Log.w(TAG, "Received unknown type, ignoring...");
    }
  }

  private static void handleGroupCreate(@NonNull Context context,
                                        @NonNull MasterSecretUnion masterSecret,
                                        @NonNull TextSecureEnvelope envelope,
                                        @NonNull TextSecureGroup group,
                                        boolean outgoing)
  {
    GroupDatabase        database = DatabaseFactory.getGroupDatabase(context);
    byte[]               id       = group.getGroupId();
    GroupContext.Builder builder  = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    TextSecureAttachment avatar = group.getAvatar().orNull();

    database.create(id, group.getName().orNull(), group.getMembers().orNull(),
                    avatar != null && avatar.isPointer() ? avatar.asPointer() : null,
                    envelope.getRelay());

    storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
  }

  private static void handleGroupUpdate(@NonNull Context context,
                                        @NonNull MasterSecretUnion masterSecret,
                                        @NonNull TextSecureEnvelope envelope,
                                        @NonNull TextSecureGroup group,
                                        @NonNull GroupRecord groupRecord,
                                        boolean outgoing)
  {

    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    byte[]        id       = group.getGroupId();

    Set<String> recordMembers = new HashSet<>(groupRecord.getMembers());
    Set<String> messageMembers = new HashSet<>(group.getMembers().get());

    Set<String> addedMembers = new HashSet<>(messageMembers);
    addedMembers.removeAll(recordMembers);

    Set<String> missingMembers = new HashSet<>(recordMembers);
    missingMembers.removeAll(messageMembers);

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.UPDATE);

    if (addedMembers.size() > 0) {
      Set<String> unionMembers = new HashSet<>(recordMembers);
      unionMembers.addAll(messageMembers);
      database.updateMembers(id, new LinkedList<>(unionMembers));

      builder.clearMembers().addAllMembers(addedMembers);
    } else {
      builder.clearMembers();
    }

    if (missingMembers.size() > 0) {
      // TODO We should tell added and missing about each-other.
    }

    if (group.getName().isPresent() || group.getAvatar().isPresent()) {
      TextSecureAttachment avatar = group.getAvatar().orNull();
      database.update(id, group.getName().orNull(), avatar != null ? avatar.asPointer() : null);
    }

    if (group.getName().isPresent() && group.getName().get().equals(groupRecord.getTitle())) {
      builder.clearName();
    }

    if (!groupRecord.isActive()) database.setActive(id, true);

    storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
  }

  private static void handleGroupLeave(@NonNull Context            context,
                                       @NonNull MasterSecretUnion  masterSecret,
                                       @NonNull TextSecureEnvelope envelope,
                                       @NonNull TextSecureGroup    group,
                                       @NonNull GroupRecord        record,
                                       boolean  outgoing)
  {
    GroupDatabase database = DatabaseFactory.getGroupDatabase(context);
    byte[]        id       = group.getGroupId();
    List<String>  members  = record.getMembers();

    GroupContext.Builder builder = createGroupContext(group);
    builder.setType(GroupContext.Type.QUIT);

    if (members.contains(envelope.getSource())) {
      database.remove(id, envelope.getSource());
      if (outgoing) database.setActive(id, false);

      storeMessage(context, masterSecret, envelope, group, builder.build(), outgoing);
    }
  }


  private static void storeMessage(@NonNull Context context,
                                   @NonNull MasterSecretUnion masterSecret,
                                   @NonNull TextSecureEnvelope envelope,
                                   @NonNull TextSecureGroup group,
                                   @NonNull GroupContext storage,
                                   boolean  outgoing)
  {
    if (group.getAvatar().isPresent()) {
      ApplicationContext.getInstance(context).getJobManager()
                        .add(new AvatarDownloadJob(context, group.getGroupId()));
    }

    try {
      if (outgoing) {
        MmsDatabase               mmsDatabase     = DatabaseFactory.getMmsDatabase(context);
        Recipients                recipients      = RecipientFactory.getRecipientsFromString(context, GroupUtil.getEncodedId(group.getGroupId()), false);
        OutgoingGroupMediaMessage outgoingMessage = new OutgoingGroupMediaMessage(recipients, storage, null, envelope.getTimestamp());
        long                      threadId        = DatabaseFactory.getThreadDatabase(context).getThreadIdFor(recipients);
        long                      messageId       = mmsDatabase.insertMessageOutbox(masterSecret, outgoingMessage, threadId, false);

        mmsDatabase.markAsSent(messageId);
      } else {
        EncryptingSmsDatabase smsDatabase  = DatabaseFactory.getEncryptingSmsDatabase(context);
        String                body         = Base64.encodeBytes(storage.toByteArray());
        IncomingTextMessage   incoming     = new IncomingTextMessage(envelope.getSource(), envelope.getSourceDevice(), envelope.getTimestamp(), body, Optional.of(group));
        IncomingGroupMessage  groupMessage = new IncomingGroupMessage(incoming, storage, body);

        Pair<Long, Long> messageAndThreadId = smsDatabase.insertMessageInbox(masterSecret, groupMessage);
        MessageNotifier.updateNotification(context, masterSecret.getMasterSecret().orNull(), messageAndThreadId.second);
      }
    } catch (MmsException e) {
      Log.w(TAG, e);
    }
  }

  private static GroupContext.Builder createGroupContext(TextSecureGroup group) {
    GroupContext.Builder builder = GroupContext.newBuilder();
    builder.setId(ByteString.copyFrom(group.getGroupId()));

    if (group.getAvatar().isPresent() && group.getAvatar().get().isPointer()) {
      builder.setAvatar(AttachmentPointer.newBuilder()
                                         .setId(group.getAvatar().get().asPointer().getId())
                                         .setKey(ByteString.copyFrom(group.getAvatar().get().asPointer().getKey()))
                                         .setContentType(group.getAvatar().get().getContentType()));
    }

    if (group.getName().isPresent()) {
      builder.setName(group.getName().get());
    }

    if (group.getMembers().isPresent()) {
      builder.addAllMembers(group.getMembers().get());
    }

    return builder;
  }

}
