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

package navigation

import config.FrontendAppConfig
import controllers.asset.business.routes._
import models.UserAnswers
import pages.Page
import pages.asset.business._
import play.api.mvc.Call
import uk.gov.hmrc.auth.core.AffinityGroup

import javax.inject.{Inject, Singleton}

@Singleton
class BusinessNavigator @Inject()(config: FrontendAppConfig) extends Navigator(config) {

  override protected def route(draftId: String): PartialFunction[Page, AffinityGroup => UserAnswers => Call] = {
    case BusinessNamePage(index) => _ => _ => BusinessDescriptionController.onPageLoad(index, draftId)
    case BusinessDescriptionPage(index) => _ => _ => BusinessAddressUkYesNoController.onPageLoad(index, draftId)
    case page @ BusinessAddressUkYesNoPage(index) => _ => ua => yesNoNav(
      ua = ua,
      fromPage = page,
      yesCall = BusinessUkAddressController.onPageLoad(index, draftId),
      noCall = BusinessInternationalAddressController.onPageLoad(index, draftId)
    )
    case BusinessUkAddressPage(index) => _ => _ => BusinessValueController.onPageLoad(index, draftId)
    case BusinessInternationalAddressPage(index) => _ => _ => BusinessValueController.onPageLoad(index, draftId)
    case BusinessValuePage(index) => _ => _ => BusinessAnswersController.onPageLoad(index, draftId)
  }

}
