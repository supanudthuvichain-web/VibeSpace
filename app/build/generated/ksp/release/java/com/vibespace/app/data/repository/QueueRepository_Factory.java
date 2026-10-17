package com.vibespace.app.data.repository;

import com.vibespace.app.data.db.dao.PlaybackStateDao;
import com.vibespace.app.data.db.dao.QueueDao;
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
public final class QueueRepository_Factory implements Factory<QueueRepository> {
  private final Provider<QueueDao> queueDaoProvider;

  private final Provider<PlaybackStateDao> playbackStateDaoProvider;

  public QueueRepository_Factory(Provider<QueueDao> queueDaoProvider,
      Provider<PlaybackStateDao> playbackStateDaoProvider) {
    this.queueDaoProvider = queueDaoProvider;
    this.playbackStateDaoProvider = playbackStateDaoProvider;
  }

  @Override
  public QueueRepository get() {
    return newInstance(queueDaoProvider.get(), playbackStateDaoProvider.get());
  }

  public static QueueRepository_Factory create(Provider<QueueDao> queueDaoProvider,
      Provider<PlaybackStateDao> playbackStateDaoProvider) {
    return new QueueRepository_Factory(queueDaoProvider, playbackStateDaoProvider);
  }

  public static QueueRepository newInstance(QueueDao queueDao, PlaybackStateDao playbackStateDao) {
    return new QueueRepository(queueDao, playbackStateDao);
  }
}
