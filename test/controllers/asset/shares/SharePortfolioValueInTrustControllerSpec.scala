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
import controllers.IndexValidation
import forms.ValueFormProvider
import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import pages.asset.shares.SharePortfolioValueInTrustPage
import play.api.data.Form
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import play.api.test.Helpers.{route, _}
import views.html.asset.shares.SharePortfolioValueInTrustView

class SharePortfolioValueInTrustControllerSpec extends SpecBase with ModelGenerators with IndexValidation {

  val formProvider = new ValueFormProvider(frontendAppConfig)
  val form: Form[Long] = formProvider.withConfig(prefix = "shares.portfolioValueInTrust")
  val index: Int = 0
  val validAnswer: Long = 4000L

  lazy val sharePortfolioValueInTrustRoute: String = routes.SharePortfolioValueInTrustController.onPageLoad(index, fakeDraftId).url

  "SharePortfolioValueInTrust Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, sharePortfolioValueInTrustRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[SharePortfolioValueInTrustView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(SharePortfolioValueInTrustPage(index), validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, sharePortfolioValueInTrustRoute)

      val view = application.injector.instanceOf[SharePortfolioValueInTrustView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, sharePortfolioValueInTrustRoute)
          .withFormUrlEncodedBody(("value", validAnswer.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual fakeNavigator.desiredRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, sharePortfolioValueInTrustRoute)
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val view = application.injector.instanceOf[SharePortfolioValueInTrustView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, sharePortfolioValueInTrustRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, sharePortfolioValueInTrustRoute)
          .withFormUrlEncodedBody(("value", validAnswer.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

  }

  "for a GET" must {

    def getForIndex(index: Int) : FakeRequest[AnyContentAsEmpty.type] = {
      val route = routes.SharePortfolioValueInTrustController.onPageLoad(index, fakeDraftId).url

      FakeRequest(GET, route)
    }

    validateIndex(
      arbitrary[Long],
      SharePortfolioValueInTrustPage.apply,
      getForIndex
    )

  }

  "for a POST" must {
    def postForIndex(index: Int): FakeRequest[AnyContentAsFormUrlEncoded] = {

      val route =
        routes.SharePortfolioValueInTrustController.onPageLoad(index, fakeDraftId).url

      FakeRequest(POST, route)
        .withFormUrlEncodedBody(("value", validAnswer.toString))
    }

    validateIndex(
      arbitrary[Long],
      SharePortfolioValueInTrustPage.apply,
      postForIndex
    )
  }
}
