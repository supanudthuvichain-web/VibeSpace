package com.vibespace.app.data.repository;

import com.vibespace.app.data.db.dao.RecentlyPlayedDao;
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
public final class RecentlyPlayedRepository_Factory implements Factory<RecentlyPlayedRepository> {
  private final Provider<RecentlyPlayedDao> recentlyPlayedDaoProvider;

  public RecentlyPlayedRepository_Factory(Provider<RecentlyPlayedDao> recentlyPlayedDaoProvider) {
    this.recentlyPlayedDaoProvider = recentlyPlayedDaoProvider;
  }

  @Override
  public RecentlyPlayedRepository get() {
    return newInstance(recentlyPlayedDaoProvider.get());
  }

  public static RecentlyPlayedRepository_Factory create(
      Provider<RecentlyPlayedDao> recentlyPlayedDaoProvider) {
    return new RecentlyPlayedRepository_Factory(recentlyPlayedDaoProvider);
  }

  public static RecentlyPlayedRepository newInstance(RecentlyPlayedDao recentlyPlayedDao) {
    return new RecentlyPlayedRepository(recentlyPlayedDao);
  }
}
