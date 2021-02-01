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

package utils.print

import controllers.asset.noneeabusiness.routes._
import models.UserAnswers
import pages.asset.noneeabusiness._
import play.api.i18n.Messages
import utils.{AnswerRowConverter, CheckAnswersFormatters}
import viewmodels.AnswerRow

import javax.inject.Inject

class NonEeaBusinessPrintHelper @Inject()(checkAnswersFormatters: CheckAnswersFormatters) extends PrintHelper {

  override def headingKey(index: Int)(implicit messages: Messages): String = {
    messages("answerPage.section.nonEeaBusinessAsset.subheading", index + 1)
  }

  override def answerRows(userAnswers: UserAnswers,
                          arg: String,
                          index: Int,
                          draftId: String)
                         (implicit messages: Messages): Seq[AnswerRow] = {

    val converter: AnswerRowConverter = new AnswerRowConverter(checkAnswersFormatters)(userAnswers, arg)

    Seq(
      converter.assetTypeQuestion(index, draftId),
      converter.stringQuestion(NamePage(index), "nonEeaBusiness.name", NameController.onPageLoad(index, draftId).url),
      converter.yesNoQuestion(AddressUkYesNoPage(index), "nonEeaBusiness.addressUkYesNo", AddressUkYesNoController.onPageLoad(index, draftId).url),
      converter.addressQuestion(UkAddressPage(index), "nonEeaBusiness.ukAddress", UkAddressController.onPageLoad(index, draftId).url),
      converter.addressQuestion(InternationalAddressPage(index), "nonEeaBusiness.internationalAddress", InternationalAddressController.onPageLoad(index, draftId).url),
      converter.countryQuestion(GoverningCountryPage(index), "nonEeaBusiness.governingCountry", GoverningCountryController.onPageLoad(index, draftId).url),
      converter.dateQuestion(StartDatePage(index), "nonEeaBusiness.startDate", StartDateController.onPageLoad(index, draftId).url)
    ).flatten
  }
}
