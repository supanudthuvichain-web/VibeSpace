package com.vibespace.app.ui.playlist;

import com.vibespace.app.data.repository.PlaylistRepository;
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
public final class PlaylistDetailViewModel_Factory implements Factory<PlaylistDetailViewModel> {
  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  public PlaylistDetailViewModel_Factory(Provider<PlaylistRepository> playlistRepositoryProvider) {
    this.playlistRepositoryProvider = playlistRepositoryProvider;
  }

  @Override
  public PlaylistDetailViewModel get() {
    return newInstance(playlistRepositoryProvider.get());
  }

  public static PlaylistDetailViewModel_Factory create(
      Provider<PlaylistRepository> playlistRepositoryProvider) {
    return new PlaylistDetailViewModel_Factory(playlistRepositoryProvider);
  }

  public static PlaylistDetailViewModel newInstance(PlaylistRepository playlistRepository) {
    return new PlaylistDetailViewModel(playlistRepository);
  }
}
