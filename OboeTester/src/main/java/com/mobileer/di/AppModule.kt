package one.younite.data.di

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.mobileer.domain.helpers.SharedPrefMethods
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("OBOE_TESTER",Activity.MODE_PRIVATE)
    }


}