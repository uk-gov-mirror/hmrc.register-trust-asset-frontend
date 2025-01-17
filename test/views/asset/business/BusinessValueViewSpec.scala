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

import forms.ValueFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.LongViewBehaviours
import views.html.asset.buisness.BusinessValueView

class BusinessValueViewSpec extends LongViewBehaviours {

  private val index: Int = 0
  private val messageKeyPrefix: String = "business.currentValue"
  private val businessName: String = "Test"

  override val form: Form[Long] = new ValueFormProvider(frontendAppConfig).withConfig(messageKeyPrefix)

  "BusinessValue view" must {

    val view = viewFor[BusinessValueView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, fakeDraftId, index, businessName)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like longPageWithDynamicTitle(form, applyView, messageKeyPrefix, businessName)
  }
}
