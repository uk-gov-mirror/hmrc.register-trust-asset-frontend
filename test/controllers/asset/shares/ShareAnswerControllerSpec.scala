/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.asset.shares

import base.SpecBase
import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito.when
import pages.asset.shares._
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.print.SharesPrintHelper
import views.html.asset.shares.ShareAnswersView

class ShareAnswerControllerSpec extends SpecBase {

  private val index: Int = 0

  private lazy val shareAnswerRoute: String = routes.ShareAnswerController.onPageLoad(index, fakeDraftId).url

  "ShareAnswer Controller" must {

    "return OK and the correct view for a GET" when {

      "share company name" in {

        val name: String = "Company Name"

        val userAnswers = emptyUserAnswers
          .set(SharesInAPortfolioPage(index), false).success.value
          .set(ShareCompanyNamePage(index), name).success.value

        val expectedSections = Nil
        val mockPrintHelper: SharesPrintHelper = mock[SharesPrintHelper]
        when(mockPrintHelper.checkDetailsSection(any(), eqTo(name), any(), any())(any())).thenReturn(Nil)

        val application = applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(bind[SharesPrintHelper].toInstance(mockPrintHelper))
          .build()

        val request = FakeRequest(GET, shareAnswerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ShareAnswersView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(index, fakeDraftId, expectedSections)(fakeRequest, messages).toString

        application.stop()
      }

      "portfolio name" in {

        val name: String = "Portfolio Name"

        val userAnswers = emptyUserAnswers
          .set(SharesInAPortfolioPage(index), true).success.value
          .set(SharePortfolioNamePage(index), name).success.value

        val expectedSections = Nil
        val mockPrintHelper: SharesPrintHelper = mock[SharesPrintHelper]
        when(mockPrintHelper.checkDetailsSection(any(), eqTo(name), any(), any())(any())).thenReturn(Nil)

        val application = applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(bind[SharesPrintHelper].toInstance(mockPrintHelper))
          .build()

        val request = FakeRequest(GET, shareAnswerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ShareAnswersView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(index, fakeDraftId, expectedSections)(fakeRequest, messages).toString

        application.stop()
      }

      "no name" in {

        val userAnswers = emptyUserAnswers
          .set(SharesInAPortfolioPage(index), true).success.value

        val expectedSections = Nil
        val mockPrintHelper: SharesPrintHelper = mock[SharesPrintHelper]
        when(mockPrintHelper.checkDetailsSection(any(), eqTo("the asset"), any(), any())(any())).thenReturn(Nil)

        val application = applicationBuilder(userAnswers = Some(userAnswers))
          .overrides(bind[SharesPrintHelper].toInstance(mockPrintHelper))
          .build()

        val request = FakeRequest(GET, shareAnswerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[ShareAnswersView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(index, fakeDraftId, expectedSections)(fakeRequest, messages).toString

        application.stop()
      }
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, shareAnswerRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

  }
}
