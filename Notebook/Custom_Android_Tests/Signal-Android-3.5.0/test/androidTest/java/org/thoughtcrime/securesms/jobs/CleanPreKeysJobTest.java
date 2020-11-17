package org.thoughtcrime.securesms.jobs;

import org.thoughtcrime.securesms.TextSecureTestCase;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.dependencies.AxolotlStorageModule;
import org.whispersystems.libaxolotl.ecc.Curve;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyStore;
import org.whispersystems.textsecure.api.TextSecureAccountManager;
import org.whispersystems.textsecure.api.push.SignedPreKeyEntity;
import org.whispersystems.textsecure.api.push.exceptions.PushNetworkException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import dagger.Module;
import dagger.ObjectGraph;
import dagger.Provides;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class CleanPreKeysJobTest extends TextSecureTestCase {

  public void testSignedPreKeyRotationNotRegistered() throws IOException, MasterSecretJob.RequirementNotMetException {
    TextSecureAccountManager accountManager    = mock(TextSecureAccountManager.class);
    SignedPreKeyStore        signedPreKeyStore = mock(SignedPreKeyStore.class);
    MasterSecret             masterSecret      = mock(MasterSecret.class);
    when(accountManager.getSignedPreKey()).thenReturn(null);

    CleanPreKeysJob cleanPreKeysJob = new CleanPreKeysJob(getContext());

    ObjectGraph objectGraph = ObjectGraph.create(new TestModule(accountManager, signedPreKeyStore));
    objectGraph.inject(cleanPreKeysJob);

    cleanPreKeysJob.onRun(masterSecret);

    verify(accountManager).getSignedPreKey();
    verifyNoMoreInteractions(signedPreKeyStore);
  }

  public void testSignedPreKeyEviction() throws Exception {
    SignedPreKeyStore        signedPreKeyStore         = mock(SignedPreKeyStore.class);
    TextSecureAccountManager accountManager            = mock(TextSecureAccountManager.class);
    SignedPreKeyEntity       currentSignedPreKeyEntity = mock(SignedPreKeyEntity.class);
    MasterSecret             masterSecret              = mock(MasterSecret.class);

    when(currentSignedPreKeyEntity.getKeyId()).thenReturn(3133);
    when(accountManager.getSignedPreKey()).thenReturn(currentSignedPreKeyEntity);

    final SignedPreKeyRecord currentRecord = new SignedPreKeyRecord(3133, System.currentTimeMillis(), Curve.generateKeyPair(), new byte[64]);

    List<SignedPreKeyRecord> records = new LinkedList<SignedPreKeyRecord>() {{
      add(new SignedPreKeyRecord(2, 11, Curve.generateKeyPair(), new byte[32]));
      add(new SignedPreKeyRecord(4, System.currentTimeMillis() - 100, Curve.generateKeyPair(), new byte[64]));
      add(currentRecord);
      add(new SignedPreKeyRecord(3, System.currentTimeMillis() - 90, Curve.generateKeyPair(), new byte[64]));
      add(new SignedPreKeyRecord(1, 10, Curve.generateKeyPair(), new byte[32]));
    }};

    when(signedPreKeyStore.loadSignedPreKeys()).thenReturn(records);
    when(signedPreKeyStore.loadSignedPreKey(eq(3133))).thenReturn(currentRecord);

    CleanPreKeysJob cleanPreKeysJob = new CleanPreKeysJob(getContext());

    ObjectGraph objectGraph = ObjectGraph.create(new TestModule(accountManager, signedPreKeyStore));
    objectGraph.inject(cleanPreKeysJob);

    cleanPreKeysJob.onRun(masterSecret);

    verify(signedPreKeyStore).removeSignedPreKey(eq(1));
    verify(signedPreKeyStore, times(1)).removeSignedPreKey(anyInt());
  }

  public void testSignedPreKeyNoEviction() throws Exception {
    SignedPreKeyStore        signedPreKeyStore         = mock(SignedPreKeyStore.class);
    TextSecureAccountManager accountManager            = mock(TextSecureAccountManager.class);
    SignedPreKeyEntity       currentSignedPreKeyEntity = mock(SignedPreKeyEntity.class);

    when(currentSignedPreKeyEntity.getKeyId()).thenReturn(3133);
    when(accountManager.getSignedPreKey()).thenReturn(currentSignedPreKeyEntity);

    final SignedPreKeyRecord currentRecord = new SignedPreKeyRecord(3133, System.currentTimeMillis(), Curve.generateKeyPair(), new byte[64]);

    List<SignedPreKeyRecord> records = new LinkedList<SignedPreKeyRecord>() {{
      add(currentRecord);
    }};

    when(signedPreKeyStore.loadSignedPreKeys()).thenReturn(records);
    when(signedPreKeyStore.loadSignedPreKey(eq(3133))).thenReturn(currentRecord);

    CleanPreKeysJob cleanPreKeysJob = new CleanPreKeysJob(getContext());

    ObjectGraph objectGraph = ObjectGraph.create(new TestModule(accountManager, signedPreKeyStore));
    objectGraph.inject(cleanPreKeysJob);

    verify(signedPreKeyStore, never()).removeSignedPreKey(anyInt());
  }

  public void testConnectionError() throws Exception {
    SignedPreKeyStore        signedPreKeyStore = mock(SignedPreKeyStore.class);
    TextSecureAccountManager accountManager    = mock(TextSecureAccountManager.class);
    MasterSecret             masterSecret      = mock(MasterSecret.class);

    when(accountManager.getSignedPreKey()).thenThrow(new PushNetworkException("Connectivity error!"));

    CleanPreKeysJob cleanPreKeysJob = new CleanPreKeysJob(getContext());

    ObjectGraph objectGraph = ObjectGraph.create(new TestModule(accountManager, signedPreKeyStore));
    objectGraph.inject(cleanPreKeysJob);

    try {
      cleanPreKeysJob.onRun(masterSecret);
      throw new AssertionError("should have failed!");
    } catch (IOException e) {
      assertTrue(cleanPreKeysJob.onShouldRetry(e));
    }
  }

  @Module(injects = {CleanPreKeysJob.class})
  public static class TestModule {
    private final TextSecureAccountManager accountManager;
    private final SignedPreKeyStore        signedPreKeyStore;

    private TestModule(TextSecureAccountManager accountManager, SignedPreKeyStore signedPreKeyStore) {
      this.accountManager    = accountManager;
      this.signedPreKeyStore = signedPreKeyStore;
    }

    @Provides TextSecureAccountManager provideTextSecureAccountManager() {
      return accountManager;
    }

    @Provides
    AxolotlStorageModule.SignedPreKeyStoreFactory provideSignedPreKeyStore() {
      return new AxolotlStorageModule.SignedPreKeyStoreFactory() {
        @Override
        public SignedPreKeyStore create() {
          return signedPreKeyStore;
        }
      };
    }
  }

}
