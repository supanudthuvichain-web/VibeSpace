package com.vibespace.app.di;

import com.vibespace.app.data.db.VibeSpaceDatabase;
import com.vibespace.app.data.db.dao.PlaybackStateDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class DatabaseModule_ProvidePlaybackStateDaoFactory implements Factory<PlaybackStateDao> {
  private final Provider<VibeSpaceDatabase> dbProvider;

  public DatabaseModule_ProvidePlaybackStateDaoFactory(Provider<VibeSpaceDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public PlaybackStateDao get() {
    return providePlaybackStateDao(dbProvider.get());
  }

  public static DatabaseModule_ProvidePlaybackStateDaoFactory create(
      Provider<VibeSpaceDatabase> dbProvider) {
    return new DatabaseModule_ProvidePlaybackStateDaoFactory(dbProvider);
  }

  public static PlaybackStateDao providePlaybackStateDao(VibeSpaceDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePlaybackStateDao(db));
  }
}
