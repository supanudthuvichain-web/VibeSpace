package com.vibespace.app.data.repository;

import android.content.Context;
import com.vibespace.app.data.db.dao.TrackDao;
import com.vibespace.app.data.mediastore.MediaStoreScanner;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class MusicRepository_Factory implements Factory<MusicRepository> {
  private final Provider<TrackDao> trackDaoProvider;

  private final Provider<MediaStoreScanner> mediaStoreScannerProvider;

  private final Provider<Context> contextProvider;

  public MusicRepository_Factory(Provider<TrackDao> trackDaoProvider,
      Provider<MediaStoreScanner> mediaStoreScannerProvider, Provider<Context> contextProvider) {
    this.trackDaoProvider = trackDaoProvider;
    this.mediaStoreScannerProvider = mediaStoreScannerProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public MusicRepository get() {
    return newInstance(trackDaoProvider.get(), mediaStoreScannerProvider.get(), contextProvider.get());
  }

  public static MusicRepository_Factory create(Provider<TrackDao> trackDaoProvider,
      Provider<MediaStoreScanner> mediaStoreScannerProvider, Provider<Context> contextProvider) {
    return new MusicRepository_Factory(trackDaoProvider, mediaStoreScannerProvider, contextProvider);
  }

  public static MusicRepository newInstance(TrackDao trackDao, MediaStoreScanner mediaStoreScanner,
      Context context) {
    return new MusicRepository(trackDao, mediaStoreScanner, context);
  }
}
