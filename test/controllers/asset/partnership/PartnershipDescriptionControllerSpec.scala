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

package controllers.asset.partnership

import base.SpecBase
import controllers.IndexValidation
import forms.DescriptionFormProvider
import org.scalacheck.Arbitrary.arbitrary
import pages.asset.partnership.PartnershipDescriptionPage
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded}
import play.api.test.FakeRequest
import play.api.test.Helpers.{route, _}
import views.html.asset.partnership.PartnershipDescriptionView

class PartnershipDescriptionControllerSpec extends SpecBase with IndexValidation {

  private val formProvider = new DescriptionFormProvider()
  private val form = formProvider.withConfig(56, "partnership.description")
  private val index = 0

  private lazy val partnershipDescriptionRoute = routes.PartnershipDescriptionController.onPageLoad(index, fakeDraftId).url

  "PartnershipDescription Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, partnershipDescriptionRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[PartnershipDescriptionView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, index, fakeDraftId)(fakeRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(PartnershipDescriptionPage(index), "answer").success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, partnershipDescriptionRoute)

      val view = application.injector.instanceOf[PartnershipDescriptionView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill("answer"), index, fakeDraftId)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, partnershipDescriptionRoute)
          .withFormUrlEncodedBody(("value", "answer"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual fakeNavigator.desiredRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, partnershipDescriptionRoute)
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val view = application.injector.instanceOf[PartnershipDescriptionView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, index, fakeDraftId)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, partnershipDescriptionRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, partnershipDescriptionRoute)
          .withFormUrlEncodedBody(("value", "answer"))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }

  "for a GET" must {

    def getForIndex(index: Int) : FakeRequest[AnyContentAsEmpty.type] = {
      val route = routes.PartnershipDescriptionController.onPageLoad(index, fakeDraftId).url

      FakeRequest(GET, route)
    }

    validateIndex(
      arbitrary[String],
      PartnershipDescriptionPage.apply,
      getForIndex
    )

  }

  "for a POST" must {
    def postForIndex(index: Int): FakeRequest[AnyContentAsFormUrlEncoded] = {

      val route =
        routes.PartnershipDescriptionController.onPageLoad(index, fakeDraftId).url

      FakeRequest(POST, route)
        .withFormUrlEncodedBody(("value", "true"))
    }

    validateIndex(
      arbitrary[String],
      PartnershipDescriptionPage.apply,
      postForIndex
    )
  }

}
