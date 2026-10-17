package com.vibespace.app.data.mediastore;

import android.content.ContentResolver;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
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
public final class MediaStoreScanner_Factory implements Factory<MediaStoreScanner> {
  private final Provider<ContentResolver> contentResolverProvider;

  public MediaStoreScanner_Factory(Provider<ContentResolver> contentResolverProvider) {
    this.contentResolverProvider = contentResolverProvider;
  }

  @Override
  public MediaStoreScanner get() {
    return newInstance(contentResolverProvider.get());
  }

  public static MediaStoreScanner_Factory create(
      Provider<ContentResolver> contentResolverProvider) {
    return new MediaStoreScanner_Factory(contentResolverProvider);
  }

  public static MediaStoreScanner newInstance(ContentResolver contentResolver) {
    return new MediaStoreScanner(contentResolver);
  }
}
