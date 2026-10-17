package com.vibespace.app.di;

import com.vibespace.app.data.db.VibeSpaceDatabase;
import com.vibespace.app.data.db.dao.RecentlyPlayedDao;
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
public final class DatabaseModule_ProvideRecentlyPlayedDaoFactory implements Factory<RecentlyPlayedDao> {
  private final Provider<VibeSpaceDatabase> dbProvider;

  public DatabaseModule_ProvideRecentlyPlayedDaoFactory(Provider<VibeSpaceDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RecentlyPlayedDao get() {
    return provideRecentlyPlayedDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRecentlyPlayedDaoFactory create(
      Provider<VibeSpaceDatabase> dbProvider) {
    return new DatabaseModule_ProvideRecentlyPlayedDaoFactory(dbProvider);
  }

  public static RecentlyPlayedDao provideRecentlyPlayedDao(VibeSpaceDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRecentlyPlayedDao(db));
  }
}
