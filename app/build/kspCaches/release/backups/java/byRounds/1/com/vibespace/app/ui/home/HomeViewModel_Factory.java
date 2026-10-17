package com.vibespace.app.ui.home;

import com.vibespace.app.data.repository.MusicRepository;
import com.vibespace.app.data.repository.RecentlyPlayedRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<MusicRepository> musicRepositoryProvider;

  private final Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider;

  public HomeViewModel_Factory(Provider<MusicRepository> musicRepositoryProvider,
      Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider) {
    this.musicRepositoryProvider = musicRepositoryProvider;
    this.recentlyPlayedRepositoryProvider = recentlyPlayedRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(musicRepositoryProvider.get(), recentlyPlayedRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<MusicRepository> musicRepositoryProvider,
      Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider) {
    return new HomeViewModel_Factory(musicRepositoryProvider, recentlyPlayedRepositoryProvider);
  }

  public static HomeViewModel newInstance(MusicRepository musicRepository,
      RecentlyPlayedRepository recentlyPlayedRepository) {
    return new HomeViewModel(musicRepository, recentlyPlayedRepository);
  }
}
