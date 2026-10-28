package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CinexRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cinex Computers", appName)
  }

  @Test
  fun `repository contains valid Tezpur store details`() {
    val repo = CinexRepository()
    assertEquals("Cinex Computers", repo.storeDetails.name)
    assertEquals("Gautam Kumar Medhi", repo.storeDetails.proprietor)
    assertTrue(repo.storeDetails.address.contains("Santa Plaza"))
    assertTrue(repo.products.isNotEmpty())
    assertTrue(repo.services.isNotEmpty())
  }
}
