package com.vibespace.app.ui.player;

import com.vibespace.app.data.repository.QueueRepository;
import com.vibespace.app.data.repository.RecentlyPlayedRepository;
import com.vibespace.app.player.PlayerController;
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
public final class PlayerViewModel_Factory implements Factory<PlayerViewModel> {
  private final Provider<PlayerController> playerControllerProvider;

  private final Provider<QueueRepository> queueRepositoryProvider;

  private final Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider;

  public PlayerViewModel_Factory(Provider<PlayerController> playerControllerProvider,
      Provider<QueueRepository> queueRepositoryProvider,
      Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider) {
    this.playerControllerProvider = playerControllerProvider;
    this.queueRepositoryProvider = queueRepositoryProvider;
    this.recentlyPlayedRepositoryProvider = recentlyPlayedRepositoryProvider;
  }

  @Override
  public PlayerViewModel get() {
    return newInstance(playerControllerProvider.get(), queueRepositoryProvider.get(), recentlyPlayedRepositoryProvider.get());
  }

  public static PlayerViewModel_Factory create(Provider<PlayerController> playerControllerProvider,
      Provider<QueueRepository> queueRepositoryProvider,
      Provider<RecentlyPlayedRepository> recentlyPlayedRepositoryProvider) {
    return new PlayerViewModel_Factory(playerControllerProvider, queueRepositoryProvider, recentlyPlayedRepositoryProvider);
  }

  public static PlayerViewModel newInstance(PlayerController playerController,
      QueueRepository queueRepository, RecentlyPlayedRepository recentlyPlayedRepository) {
    return new PlayerViewModel(playerController, queueRepository, recentlyPlayedRepository);
  }
}
