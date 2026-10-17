package com.vibespace.app.di;

import com.vibespace.app.data.db.VibeSpaceDatabase;
import com.vibespace.app.data.db.dao.TrackDao;
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
public final class DatabaseModule_ProvideTrackDaoFactory implements Factory<TrackDao> {
  private final Provider<VibeSpaceDatabase> dbProvider;

  public DatabaseModule_ProvideTrackDaoFactory(Provider<VibeSpaceDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TrackDao get() {
    return provideTrackDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTrackDaoFactory create(
      Provider<VibeSpaceDatabase> dbProvider) {
    return new DatabaseModule_ProvideTrackDaoFactory(dbProvider);
  }

  public static TrackDao provideTrackDao(VibeSpaceDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTrackDao(db));
  }
}
