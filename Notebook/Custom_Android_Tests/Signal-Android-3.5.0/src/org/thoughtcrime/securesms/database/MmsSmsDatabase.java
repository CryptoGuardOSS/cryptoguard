/**
 * Copyright (C) 2011 Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thoughtcrime.securesms.database;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.model.MessageRecord;
import org.whispersystems.libaxolotl.util.guava.Optional;

import java.util.HashSet;
import java.util.Set;

public class MmsSmsDatabase extends Database {

  private static final String TAG = MmsSmsDatabase.class.getSimpleName();

  public static final String TRANSPORT     = "transport_type";
  public static final String MMS_TRANSPORT = "mms";
  public static final String SMS_TRANSPORT = "sms";

  private static final String[] PROJECTION = {MmsSmsColumns.ID, SmsDatabase.BODY, SmsDatabase.TYPE,
                                              MmsSmsColumns.THREAD_ID,
                                              SmsDatabase.ADDRESS, SmsDatabase.ADDRESS_DEVICE_ID, SmsDatabase.SUBJECT,
                                              MmsSmsColumns.NORMALIZED_DATE_SENT,
                                              MmsSmsColumns.NORMALIZED_DATE_RECEIVED,
                                              MmsDatabase.MESSAGE_TYPE, MmsDatabase.MESSAGE_BOX,
                                              SmsDatabase.STATUS, MmsDatabase.PART_COUNT,
                                              MmsDatabase.CONTENT_LOCATION, MmsDatabase.TRANSACTION_ID,
                                              MmsDatabase.MESSAGE_SIZE, MmsDatabase.EXPIRY,
                                              MmsDatabase.STATUS, MmsSmsColumns.RECEIPT_COUNT,
                                              MmsSmsColumns.MISMATCHED_IDENTITIES,
                                              MmsDatabase.NETWORK_FAILURE, TRANSPORT,
                                              AttachmentDatabase.ATTACHMENT_ID_ALIAS,
                                              AttachmentDatabase.UNIQUE_ID,
                                              AttachmentDatabase.MMS_ID,
                                              AttachmentDatabase.SIZE,
                                              AttachmentDatabase.DATA,
                                              AttachmentDatabase.CONTENT_TYPE,
                                              AttachmentDatabase.CONTENT_LOCATION,
                                              AttachmentDatabase.CONTENT_DISPOSITION,
                                              AttachmentDatabase.NAME,
                                              AttachmentDatabase.TRANSFER_STATE};

  public MmsSmsDatabase(Context context, SQLiteOpenHelper databaseHelper) {
    super(context, databaseHelper);
  }

  public Cursor getConversation(long threadId, long limit) {
    String order     = MmsSmsColumns.NORMALIZED_DATE_RECEIVED + " DESC";
    String selection = MmsSmsColumns.THREAD_ID + " = " + threadId;

    Cursor cursor = queryTables(PROJECTION, selection, order, limit > 0 ? String.valueOf(limit) : null);
    setNotifyConverationListeners(cursor, threadId);

    return cursor;
  }

  public Cursor getConversation(long threadId) {
    return getConversation(threadId, 0);
  }

  public Cursor getIdentityConflictMessagesForThread(long threadId) {
    String order           = MmsSmsColumns.NORMALIZED_DATE_RECEIVED + " ASC";
    String selection       = MmsSmsColumns.THREAD_ID + " = " + threadId + " AND " + MmsSmsColumns.MISMATCHED_IDENTITIES + " IS NOT NULL";

    Cursor cursor = queryTables(PROJECTION, selection, order, null);
    setNotifyConverationListeners(cursor, threadId);

    return cursor;
  }

  public Cursor getConversationSnippet(long threadId) {
    String order     = MmsSmsColumns.NORMALIZED_DATE_RECEIVED + " DESC";
    String selection = MmsSmsColumns.THREAD_ID + " = " + threadId;

    return  queryTables(PROJECTION, selection, order, "1");
  }

  public Cursor getUnread() {
    String order           = MmsSmsColumns.NORMALIZED_DATE_RECEIVED + " ASC";
    String selection       = MmsSmsColumns.READ + " = 0";

    return queryTables(PROJECTION, selection, order, null);
  }

  public int getConversationCount(long threadId) {
    int count = DatabaseFactory.getSmsDatabase(context).getMessageCountForThread(threadId);
    count    += DatabaseFactory.getMmsDatabase(context).getMessageCountForThread(threadId);

    return count;
  }

  public void incrementDeliveryReceiptCount(String address, long timestamp) {
    DatabaseFactory.getSmsDatabase(context).incrementDeliveryReceiptCount(address, timestamp);
    DatabaseFactory.getMmsDatabase(context).incrementDeliveryReceiptCount(address, timestamp);
  }

  private Cursor queryTables(String[] projection, String selection, String order, String limit) {
    String[] mmsProjection = {MmsDatabase.DATE_SENT + " AS " + MmsSmsColumns.NORMALIZED_DATE_SENT,
                              MmsDatabase.DATE_RECEIVED + " AS " + MmsSmsColumns.NORMALIZED_DATE_RECEIVED,
                              MmsDatabase.TABLE_NAME + "." + MmsDatabase.ID + " AS " + MmsSmsColumns.ID,
                              AttachmentDatabase.TABLE_NAME + "." + AttachmentDatabase.ROW_ID + " AS " + AttachmentDatabase.ATTACHMENT_ID_ALIAS,
                              SmsDatabase.BODY, MmsSmsColumns.READ, MmsSmsColumns.THREAD_ID,
                              SmsDatabase.TYPE, SmsDatabase.ADDRESS, SmsDatabase.ADDRESS_DEVICE_ID, SmsDatabase.SUBJECT, MmsDatabase.MESSAGE_TYPE,
                              MmsDatabase.MESSAGE_BOX, SmsDatabase.STATUS, MmsDatabase.PART_COUNT,
                              MmsDatabase.CONTENT_LOCATION, MmsDatabase.TRANSACTION_ID,
                              MmsDatabase.MESSAGE_SIZE, MmsDatabase.EXPIRY, MmsDatabase.STATUS,
                              MmsSmsColumns.RECEIPT_COUNT, MmsSmsColumns.MISMATCHED_IDENTITIES,
                              MmsDatabase.NETWORK_FAILURE, TRANSPORT,
                              AttachmentDatabase.UNIQUE_ID,
                              AttachmentDatabase.MMS_ID,
                              AttachmentDatabase.SIZE,
                              AttachmentDatabase.DATA,
                              AttachmentDatabase.CONTENT_TYPE,
                              AttachmentDatabase.CONTENT_LOCATION,
                              AttachmentDatabase.CONTENT_DISPOSITION,
                              AttachmentDatabase.NAME,
                              AttachmentDatabase.TRANSFER_STATE};

    String[] smsProjection = {SmsDatabase.DATE_SENT + " AS " + MmsSmsColumns.NORMALIZED_DATE_SENT,
                              SmsDatabase.DATE_RECEIVED + " AS " + MmsSmsColumns.NORMALIZED_DATE_RECEIVED,
                              MmsSmsColumns.ID,  "NULL AS " + AttachmentDatabase.ATTACHMENT_ID_ALIAS,
                              SmsDatabase.BODY, MmsSmsColumns.READ, MmsSmsColumns.THREAD_ID,
                              SmsDatabase.TYPE, SmsDatabase.ADDRESS, SmsDatabase.ADDRESS_DEVICE_ID, SmsDatabase.SUBJECT, MmsDatabase.MESSAGE_TYPE,
                              MmsDatabase.MESSAGE_BOX, SmsDatabase.STATUS, MmsDatabase.PART_COUNT,
                              MmsDatabase.CONTENT_LOCATION, MmsDatabase.TRANSACTION_ID,
                              MmsDatabase.MESSAGE_SIZE, MmsDatabase.EXPIRY, MmsDatabase.STATUS,
                              MmsSmsColumns.RECEIPT_COUNT, MmsSmsColumns.MISMATCHED_IDENTITIES,
                              MmsDatabase.NETWORK_FAILURE, TRANSPORT,
                              AttachmentDatabase.UNIQUE_ID,
                              AttachmentDatabase.MMS_ID,
                              AttachmentDatabase.SIZE,
                              AttachmentDatabase.DATA,
                              AttachmentDatabase.CONTENT_TYPE,
                              AttachmentDatabase.CONTENT_LOCATION,
                              AttachmentDatabase.CONTENT_DISPOSITION,
                              AttachmentDatabase.NAME,
                              AttachmentDatabase.TRANSFER_STATE};

    SQLiteQueryBuilder mmsQueryBuilder = new SQLiteQueryBuilder();
    SQLiteQueryBuilder smsQueryBuilder = new SQLiteQueryBuilder();

    mmsQueryBuilder.setDistinct(true);
    smsQueryBuilder.setDistinct(true);

    smsQueryBuilder.setTables(SmsDatabase.TABLE_NAME);
    mmsQueryBuilder.setTables(MmsDatabase.TABLE_NAME + " LEFT OUTER JOIN " +
                              AttachmentDatabase.TABLE_NAME +
                              " ON " + AttachmentDatabase.TABLE_NAME + "." + AttachmentDatabase.ROW_ID + " = " +
                                  " (SELECT " + AttachmentDatabase.TABLE_NAME + "." + AttachmentDatabase.ROW_ID +
                                  " FROM " + AttachmentDatabase.TABLE_NAME + " WHERE " +
                                  AttachmentDatabase.TABLE_NAME + "." + AttachmentDatabase.MMS_ID + " = " +
                                  MmsDatabase.TABLE_NAME + "." + MmsDatabase.ID + " LIMIT 1)");


    Set<String> mmsColumnsPresent = new HashSet<>();
    mmsColumnsPresent.add(MmsSmsColumns.ID);
    mmsColumnsPresent.add(MmsSmsColumns.READ);
    mmsColumnsPresent.add(MmsSmsColumns.THREAD_ID);
    mmsColumnsPresent.add(MmsSmsColumns.BODY);
    mmsColumnsPresent.add(MmsSmsColumns.ADDRESS);
    mmsColumnsPresent.add(MmsSmsColumns.ADDRESS_DEVICE_ID);
    mmsColumnsPresent.add(MmsSmsColumns.RECEIPT_COUNT);
    mmsColumnsPresent.add(MmsSmsColumns.MISMATCHED_IDENTITIES);
    mmsColumnsPresent.add(MmsDatabase.MESSAGE_TYPE);
    mmsColumnsPresent.add(MmsDatabase.MESSAGE_BOX);
    mmsColumnsPresent.add(MmsDatabase.DATE_SENT);
    mmsColumnsPresent.add(MmsDatabase.DATE_RECEIVED);
    mmsColumnsPresent.add(MmsDatabase.PART_COUNT);
    mmsColumnsPresent.add(MmsDatabase.CONTENT_LOCATION);
    mmsColumnsPresent.add(MmsDatabase.TRANSACTION_ID);
    mmsColumnsPresent.add(MmsDatabase.MESSAGE_SIZE);
    mmsColumnsPresent.add(MmsDatabase.EXPIRY);
    mmsColumnsPresent.add(MmsDatabase.STATUS);
    mmsColumnsPresent.add(MmsDatabase.NETWORK_FAILURE);

    mmsColumnsPresent.add(AttachmentDatabase.ROW_ID);
    mmsColumnsPresent.add(AttachmentDatabase.UNIQUE_ID);
    mmsColumnsPresent.add(AttachmentDatabase.SIZE);
    mmsColumnsPresent.add(AttachmentDatabase.CONTENT_TYPE);
    mmsColumnsPresent.add(AttachmentDatabase.CONTENT_LOCATION);
    mmsColumnsPresent.add(AttachmentDatabase.CONTENT_DISPOSITION);
    mmsColumnsPresent.add(AttachmentDatabase.NAME);
    mmsColumnsPresent.add(AttachmentDatabase.TRANSFER_STATE);

    Set<String> smsColumnsPresent = new HashSet<>();
    smsColumnsPresent.add(MmsSmsColumns.ID);
    smsColumnsPresent.add(MmsSmsColumns.BODY);
    smsColumnsPresent.add(MmsSmsColumns.ADDRESS);
    smsColumnsPresent.add(MmsSmsColumns.ADDRESS_DEVICE_ID);
    smsColumnsPresent.add(MmsSmsColumns.READ);
    smsColumnsPresent.add(MmsSmsColumns.THREAD_ID);
    smsColumnsPresent.add(MmsSmsColumns.RECEIPT_COUNT);
    smsColumnsPresent.add(MmsSmsColumns.MISMATCHED_IDENTITIES);
    smsColumnsPresent.add(SmsDatabase.TYPE);
    smsColumnsPresent.add(SmsDatabase.SUBJECT);
    smsColumnsPresent.add(SmsDatabase.DATE_SENT);
    smsColumnsPresent.add(SmsDatabase.DATE_RECEIVED);
    smsColumnsPresent.add(SmsDatabase.STATUS);

    String mmsSubQuery = mmsQueryBuilder.buildUnionSubQuery(TRANSPORT, mmsProjection, mmsColumnsPresent, 3, MMS_TRANSPORT, selection, null, null, null);
    String smsSubQuery = smsQueryBuilder.buildUnionSubQuery(TRANSPORT, smsProjection, smsColumnsPresent, 3, SMS_TRANSPORT, selection, null, null, null);

    SQLiteQueryBuilder unionQueryBuilder = new SQLiteQueryBuilder();
    String unionQuery = unionQueryBuilder.buildUnionQuery(new String[] {smsSubQuery, mmsSubQuery}, order, limit);

    SQLiteQueryBuilder outerQueryBuilder = new SQLiteQueryBuilder();
    outerQueryBuilder.setTables("(" + unionQuery + ")");

    String query      = outerQueryBuilder.buildQuery(projection, null, null, null, null, null, null);

    Log.w("MmsSmsDatabase", "Executing query: " + query);
    SQLiteDatabase db = databaseHelper.getReadableDatabase();
    return db.rawQuery(query, null);
  }

  public Reader readerFor(@NonNull Cursor cursor, @Nullable MasterSecret masterSecret) {
    return new Reader(cursor, masterSecret);
  }

  public Reader readerFor(@NonNull Cursor cursor) {
    return new Reader(cursor);
  }

  public class Reader {

    private final Cursor                       cursor;
    private final Optional<MasterSecret>       masterSecret;
    private       EncryptingSmsDatabase.Reader smsReader;
    private       MmsDatabase.Reader           mmsReader;

    public Reader(Cursor cursor, @Nullable MasterSecret masterSecret) {
      this.cursor       = cursor;
      this.masterSecret = Optional.fromNullable(masterSecret);
    }

    public Reader(Cursor cursor) {
      this(cursor, null);
    }

    private EncryptingSmsDatabase.Reader getSmsReader() {
      if (smsReader == null) {
        if (masterSecret.isPresent()) smsReader = DatabaseFactory.getEncryptingSmsDatabase(context).readerFor(masterSecret.get(), cursor);
        else                          smsReader = DatabaseFactory.getSmsDatabase(context).readerFor(cursor);
      }

      return smsReader;
    }

    private MmsDatabase.Reader getMmsReader() {
      if (mmsReader == null) {
        mmsReader = DatabaseFactory.getMmsDatabase(context).readerFor(masterSecret.orNull(), cursor);
      }

      return mmsReader;
    }

    public MessageRecord getNext() {
      if (cursor == null || !cursor.moveToNext())
        return null;

      return getCurrent();
    }

    public MessageRecord getCurrent() {
      String type = cursor.getString(cursor.getColumnIndexOrThrow(TRANSPORT));

      if      (MmsSmsDatabase.MMS_TRANSPORT.equals(type)) return getMmsReader().getCurrent();
      else if (MmsSmsDatabase.SMS_TRANSPORT.equals(type)) return getSmsReader().getCurrent();
      else                                                throw new AssertionError("Bad type: " + type);
    }

    public void close() {
      cursor.close();
    }
  }
}
