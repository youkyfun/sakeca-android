package com.youkydesign.sakeca.domain.groceries;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class GroceriesInteractor_Factory implements Factory<GroceriesInteractor> {
  private final Provider<IGroceriesRepository> repositoryProvider;

  public GroceriesInteractor_Factory(Provider<IGroceriesRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GroceriesInteractor get() {
    return newInstance(repositoryProvider.get());
  }

  public static GroceriesInteractor_Factory create(
      Provider<IGroceriesRepository> repositoryProvider) {
    return new GroceriesInteractor_Factory(repositoryProvider);
  }

  public static GroceriesInteractor newInstance(IGroceriesRepository repository) {
    return new GroceriesInteractor(repository);
  }
}
