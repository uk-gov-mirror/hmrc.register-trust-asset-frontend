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

package controllers.asset.other

import base.SpecBase
import forms.DescriptionFormProvider
import models.UserAnswers
import models.WhatKindOfAsset.Other
import pages.asset.WhatKindOfAssetPage
import pages.asset.other.OtherAssetDescriptionPage
import play.api.Application
import play.api.data.Form
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._
import views.html.asset.other.OtherAssetDescriptionView

import scala.concurrent.Future

class OtherAssetDescriptionControllerSpec extends SpecBase {

  val formProvider = new DescriptionFormProvider()
  val form: Form[String] = formProvider.withConfig(56, "other.description")
  val index = 0
  val validAnswer: String = "Description"

  lazy val descriptionRoute: String = routes.OtherAssetDescriptionController.onPageLoad(index, fakeDraftId).url

  val baseAnswers: UserAnswers = emptyUserAnswers
    .set(WhatKindOfAssetPage(index), Other).success.value

  "OtherAssetDescriptionController" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request = FakeRequest(GET, descriptionRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[OtherAssetDescriptionView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = baseAnswers
        .set(OtherAssetDescriptionPage(index), validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, descriptionRoute)

      val view = application.injector.instanceOf[OtherAssetDescriptionView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(validAnswer), fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to what kind of asset page if no kind of asset found" in {

      val application: Application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, descriptionRoute)

      val result: Future[Result] = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual
        controllers.asset.routes.WhatKindOfAssetController.onPageLoad(index, fakeDraftId).url

      application.stop()
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request =
        FakeRequest(POST, descriptionRoute)
          .withFormUrlEncodedBody(("value", validAnswer))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual fakeNavigator.desiredRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

      val request =
        FakeRequest(POST, descriptionRoute)
          .withFormUrlEncodedBody(("value", ""))

      val boundForm = form.bind(Map("value" -> ""))

      val view = application.injector.instanceOf[OtherAssetDescriptionView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, fakeDraftId, index)(fakeRequest, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, descriptionRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, descriptionRoute)
          .withFormUrlEncodedBody(("value", validAnswer))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
