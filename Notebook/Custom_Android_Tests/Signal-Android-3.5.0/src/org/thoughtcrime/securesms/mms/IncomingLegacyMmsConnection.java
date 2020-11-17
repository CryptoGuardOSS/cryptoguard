/**
 * Copyright (C) 2015 Open Whisper Systems
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
package org.thoughtcrime.securesms.mms;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGetHC4;
import org.apache.http.client.methods.HttpUriRequest;

import java.io.IOException;
import java.util.Arrays;

import ws.com.google.android.mms.InvalidHeaderValueException;
import ws.com.google.android.mms.pdu.NotifyRespInd;
import ws.com.google.android.mms.pdu.PduComposer;
import ws.com.google.android.mms.pdu.PduHeaders;
import ws.com.google.android.mms.pdu.PduParser;
import ws.com.google.android.mms.pdu.RetrieveConf;

@SuppressWarnings("deprecation")
public class IncomingLegacyMmsConnection extends LegacyMmsConnection implements IncomingMmsConnection {
  private static final String TAG = IncomingLegacyMmsConnection.class.getSimpleName();

  public IncomingLegacyMmsConnection(Context context) throws ApnUnavailableException {
    super(context);
  }

  private HttpUriRequest constructRequest(Apn contentApn, boolean useProxy) throws IOException {
    HttpGetHC4 request = new HttpGetHC4(contentApn.getMmsc());
    for (Header header : getBaseHeaders()) {
      request.addHeader(header);
    }
    if (useProxy) {
      HttpHost proxy = new HttpHost(contentApn.getProxy(), contentApn.getPort());
      request.setConfig(RequestConfig.custom().setProxy(proxy).build());
    }
    return request;
  }

  @Override
  public @Nullable RetrieveConf retrieve(@NonNull String contentLocation,
                                         byte[] transactionId)
      throws MmsRadioException, ApnUnavailableException, IOException
  {
    MmsRadio radio = MmsRadio.getInstance(context);
    Apn contentApn = new Apn(contentLocation, apn.getProxy(), Integer.toString(apn.getPort()), apn.getUsername(), apn.getPassword());
    if (isCdmaDevice()) {
      Log.w(TAG, "Connecting directly...");
      try {
        return retrieve(contentApn, transactionId, false, false);
      } catch (IOException | ApnUnavailableException e) {
        Log.w(TAG, e);
      }
    }

    Log.w(TAG, "Changing radio to MMS mode..");
    radio.connect();

    try {
      Log.w(TAG, "Downloading in MMS mode with proxy...");

      try {
        return retrieve(contentApn, transactionId, true, true);
      } catch (IOException | ApnUnavailableException e) {
        Log.w(TAG, e);
      }

      Log.w(TAG, "Downloading in MMS mode without proxy...");

      return retrieve(contentApn, transactionId, true, false);

    } finally {
      radio.disconnect();
    }
  }

  public RetrieveConf retrieve(Apn contentApn, byte[] transactionId, boolean usingMmsRadio, boolean useProxyIfAvailable)
      throws IOException, ApnUnavailableException
  {
    byte[] pdu = null;

    final boolean useProxy   = useProxyIfAvailable && contentApn.hasProxy();
    final String  targetHost = useProxy
                             ? contentApn.getProxy()
                             : Uri.parse(contentApn.getMmsc()).getHost();
    if (checkRouteToHost(context, targetHost, usingMmsRadio)) {
      Log.w(TAG, "got successful route to host " + targetHost);
      pdu = execute(constructRequest(contentApn, useProxy));
    }

    if (pdu == null) {
      throw new IOException("Connection manager could not obtain route to host.");
    }

    RetrieveConf retrieved = (RetrieveConf)new PduParser(pdu).parse();

    if (retrieved == null) {
      Log.w(TAG, "Couldn't parse PDU, byte response: " + Arrays.toString(pdu));
      Log.w(TAG, "Couldn't parse PDU, ASCII:         " + new String(pdu));
      throw new IOException("Bad retrieved PDU");
    }

    sendRetrievedAcknowledgement(transactionId, usingMmsRadio, useProxy);
    return retrieved;
  }

  private void sendRetrievedAcknowledgement(byte[] transactionId,
                                            boolean usingRadio,
                                            boolean useProxy)
      throws ApnUnavailableException
  {
    try {
      NotifyRespInd notifyResponse = new NotifyRespInd(PduHeaders.CURRENT_MMS_VERSION,
                                                       transactionId,
                                                       PduHeaders.STATUS_RETRIEVED);

      OutgoingLegacyMmsConnection connection = new OutgoingLegacyMmsConnection(context);
      connection.sendNotificationReceived(new PduComposer(context, notifyResponse).make(), usingRadio, useProxy);
    } catch (InvalidHeaderValueException | IOException e) {
      Log.w(TAG, e);
    }
  }
}
