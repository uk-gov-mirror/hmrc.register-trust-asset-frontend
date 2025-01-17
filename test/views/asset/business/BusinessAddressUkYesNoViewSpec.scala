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

package views.asset.business

import forms.YesNoFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.asset.buisness.BusinessAddressUkYesNoView

class BusinessAddressUkYesNoViewSpec extends YesNoViewBehaviours {

  private val messageKeyPrefix: String = "business.addressUkYesNo"
  override val form: Form[Boolean] = new YesNoFormProvider().withPrefix(messageKeyPrefix)
  private val index: Int = 0
  private val businessName: String = "Test"

  private val view: BusinessAddressUkYesNoView = viewFor[BusinessAddressUkYesNoView](Some(emptyUserAnswers))

  "BusinessAddressUkYesNo view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, fakeDraftId, index, businessName)(fakeRequest, messages)

    behave like dynamicTitlePage(applyView(form), messageKeyPrefix, businessName)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, Seq("Test"))

    behave like pageWithASubmitButton(applyView(form))
  }
}
