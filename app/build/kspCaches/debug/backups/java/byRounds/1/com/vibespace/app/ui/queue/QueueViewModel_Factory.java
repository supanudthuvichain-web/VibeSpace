package com.vibespace.app.ui.queue;

import com.vibespace.app.data.repository.QueueRepository;
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
public final class QueueViewModel_Factory implements Factory<QueueViewModel> {
  private final Provider<QueueRepository> queueRepositoryProvider;

  public QueueViewModel_Factory(Provider<QueueRepository> queueRepositoryProvider) {
    this.queueRepositoryProvider = queueRepositoryProvider;
  }

  @Override
  public QueueViewModel get() {
    return newInstance(queueRepositoryProvider.get());
  }

  public static QueueViewModel_Factory create(Provider<QueueRepository> queueRepositoryProvider) {
    return new QueueViewModel_Factory(queueRepositoryProvider);
  }

  public static QueueViewModel newInstance(QueueRepository queueRepository) {
    return new QueueViewModel(queueRepository);
  }
}
