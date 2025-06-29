package com.youkydesign.sakeca.data.groceries;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class GroceriesDataSource_Factory implements Factory<GroceriesDataSource> {
  private final Provider<GroceryDao> groceryDaoProvider;

  public GroceriesDataSource_Factory(Provider<GroceryDao> groceryDaoProvider) {
    this.groceryDaoProvider = groceryDaoProvider;
  }

  @Override
  public GroceriesDataSource get() {
    return newInstance(groceryDaoProvider.get());
  }

  public static GroceriesDataSource_Factory create(Provider<GroceryDao> groceryDaoProvider) {
    return new GroceriesDataSource_Factory(groceryDaoProvider);
  }

  public static GroceriesDataSource newInstance(GroceryDao groceryDao) {
    return new GroceriesDataSource(groceryDao);
  }
}
