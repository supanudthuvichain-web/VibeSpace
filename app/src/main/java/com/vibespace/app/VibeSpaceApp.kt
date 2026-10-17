package com.vibespace.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * VibeSpace Application class.
 * Annotated with @HiltAndroidApp to trigger Hilt's code generation
 * and serve as the application-level dependency container.
 */
@HiltAndroidApp
class VibeSpaceApp : Application()
