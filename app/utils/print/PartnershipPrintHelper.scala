/*
 * Copyright 2020 HM Revenue & Customs
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

import controllers.asset.partnership.routes._
import models.UserAnswers
import pages.asset.partnership._
import play.api.i18n.Messages
import utils.countryOptions.CountryOptions
import utils.{AnswerRowConverter, CheckAnswersFormatters}
import viewmodels.AnswerRow

import javax.inject.Inject

class PartnershipPrintHelper @Inject()(countryOptions: CountryOptions,
                                       checkAnswersFormatters: CheckAnswersFormatters) extends PrintHelper {

  override def headingKey(index: Int)(implicit messages: Messages): String = {
    messages("answerPage.section.partnershipAsset.subheading", index + 1)
  }

  override def answerRows(userAnswers: UserAnswers,
                          arg: String,
                          index: Int,
                          draftId: String)
                         (implicit messages: Messages): Seq[AnswerRow] = {

    val converter: AnswerRowConverter = new AnswerRowConverter(countryOptions, checkAnswersFormatters)(userAnswers, arg)

    Seq(
      converter.assetTypeQuestion(index, draftId),
      converter.stringQuestion(PartnershipDescriptionPage(index), "partnership.description", PartnershipDescriptionController.onPageLoad(index, draftId).url),
      converter.dateQuestion(PartnershipStartDatePage(index), "partnership.startDate", PartnershipStartDateController.onPageLoad(index, draftId).url)
    ).flatten
  }
}
