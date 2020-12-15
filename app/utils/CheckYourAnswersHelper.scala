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

package utils

import javax.inject.Inject
import mapping.reads._
import models.{NormalMode, UserAnswers}
import pages.asset._
import pages.asset.business._
import pages.asset.money._
import pages.asset.other.{OtherAssetDescriptionPage, OtherAssetValuePage}
import pages.asset.partnership._
import pages.asset.property_or_land._
import pages.asset.shares._
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import utils.countryOptions.CountryOptions
import viewmodels.{AnswerRow, AnswerSection}
import utils.CheckAnswersFormatters.{assetName, _}

class CheckYourAnswersHelper @Inject()(countryOptions: CountryOptions)
                                      (userAnswers: UserAnswers,
                                       draftId: String,
                                       canEdit: Boolean)
                                      (implicit messages: Messages) {

  def money: Seq[AnswerSection] = {
    val answers = userAnswers.get(Assets).getOrElse(Nil).zipWithIndex.collect {
      case (x: MoneyAsset, index) => (x, index)
    }

    answers.flatMap {
      case o@(m, index) =>
        Seq(
          AnswerSection(
            Some(messages("answerPage.section.moneyAsset.subheading")),
            Seq(
              assetMoneyValue(index)
            ).flatten,
            None
          )
        )
    }
  }

  def shares: Seq[AnswerSection] = {
    val answers: Seq[(ShareAsset, Int)] = userAnswers.get(Assets).getOrElse(Nil).zipWithIndex.collect {
      case (x: ShareNonPortfolioAsset, index) => (x, index)
      case (x: SharePortfolioAsset, index) => (x, index)
    }

    answers.flatMap {
      case o@(m, index) =>
        m match {
          case _: ShareNonPortfolioAsset =>
            Seq(
              AnswerSection(
                Some(s"${messages("answerPage.section.shareAsset.subheading")} ${answers.indexOf(o) + 1}"),
                Seq(
                  sharesInAPortfolio(index),
                  shareCompanyName(index),
                  sharesOnStockExchange(index),
                  shareClass(index),
                  shareQuantityInTrust(index),
                  shareValueInTrust(index)
                ).flatten,
                None
              )
            )
          case _: SharePortfolioAsset =>
            Seq(
              AnswerSection(
                Some(s"${messages("answerPage.section.shareAsset.subheading")} ${answers.indexOf(o) + 1}"),
                Seq(
                  sharesInAPortfolio(index),
                  sharePortfolioName(index),
                  sharePortfolioOnStockExchange(index),
                  sharePortfolioQuantityInTrust(index),
                  sharePortfolioValueInTrust(index)
                ).flatten,
                None
              )
            )
          case _ => Nil
        }
    }

  }

  def propertyOrLand: Seq[AnswerSection] = {
    val answers: Seq[(PropertyOrLandAsset, Int)] = userAnswers.get(Assets).getOrElse(Nil).zipWithIndex.collect {
      case (x: PropertyOrLandAsset, index) => (x, index)
    }

    answers.flatMap {
      case o@(m, index) =>
        Seq(
          AnswerSection(
            Some(s"${messages("answerPage.section.propertyOrLandAsset.subheading")} ${answers.indexOf(o) + 1}"),
            Seq(
              propertyOrLandAddressYesNo(index),
              propertyOrLandDescription(index),
              propertyOrLandAddressUkYesNo(index),
              propertyOrLandUKAddress(index),
              propertyOrLandInternationalAddress(index),
              propertyOrLandTotalValue(index),
              trustOwnAllThePropertyOrLand(index),
              propertyLandValueTrust(index)
            ).flatten,
            None
          )
        )
      case _ => Nil
    }

  }

  def propertyOrLandAddressYesNo(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandAddressYesNoPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.addressYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandAddressYesNoController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyLandValueTrust(index: Int): Option[AnswerRow] = userAnswers.get(PropertyLandValueTrustPage(index)) map {
    x =>
      AnswerRow(
        "propertyLand.valueInTrust.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.property_or_land.routes.PropertyLandValueTrustController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyOrLandDescription(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandDescriptionPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.description.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandDescriptionController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyOrLandAddressUkYesNo(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandAddressUkYesNoPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.addressUkYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandAddressUkYesNoController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def trustOwnAllThePropertyOrLand(index: Int): Option[AnswerRow] = userAnswers.get(TrustOwnAllThePropertyOrLandPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.trustOwnAllYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.property_or_land.routes.TrustOwnAllThePropertyOrLandController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyOrLandUKAddress(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandUKAddressPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.ukAddress.checkYourAnswersLabel",
        ukAddress(x),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandUKAddressController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyOrLandInternationalAddress(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandInternationalAddressPage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.internationalAddress.checkYourAnswersLabel",
        internationalAddress(x, countryOptions),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandInternationalAddressController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def propertyOrLandTotalValue(index: Int): Option[AnswerRow] = userAnswers.get(PropertyOrLandTotalValuePage(index)) map {
    x =>
      AnswerRow(
        "propertyOrLand.totalValue.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.property_or_land.routes.PropertyOrLandTotalValueController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def shareCompanyName(index: Int): Option[AnswerRow] = userAnswers.get(ShareCompanyNamePage(index)) map {
    x =>
      AnswerRow(
        "shareCompanyName.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.shares.routes.ShareCompanyNameController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def sharesOnStockExchange(index: Int): Option[AnswerRow] = userAnswers.get(SharesOnStockExchangePage(index)) map {
    x =>
      AnswerRow(
        "sharesOnStockExchange.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.shares.routes.SharesOnStockExchangeController.onPageLoad(NormalMode, index, draftId).url),
        shareCompName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def sharesInAPortfolio(index: Int): Option[AnswerRow] = userAnswers.get(SharesInAPortfolioPage(index)) map {
    x =>
      AnswerRow(
        "sharesInAPortfolio.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.shares.routes.SharesInAPortfolioController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def shareValueInTrust(index: Int): Option[AnswerRow] = userAnswers.get(ShareValueInTrustPage(index)) map {
    x =>
      AnswerRow(
        "shareValueInTrust.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.shares.routes.ShareValueInTrustController.onPageLoad(NormalMode, index, draftId).url),
        shareCompName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def shareQuantityInTrust(index: Int): Option[AnswerRow] = userAnswers.get(ShareQuantityInTrustPage(index)) map {
    x =>
      AnswerRow(
        "shareQuantityInTrust.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.shares.routes.ShareQuantityInTrustController.onPageLoad(NormalMode, index, draftId).url),
        shareCompName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def sharePortfolioValueInTrust(index: Int): Option[AnswerRow] = userAnswers.get(SharePortfolioValueInTrustPage(index)) map {
    x =>
      AnswerRow(
        "sharePortfolioValueInTrust.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.shares.routes.SharePortfolioValueInTrustController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def sharePortfolioQuantityInTrust(index: Int): Option[AnswerRow] = userAnswers.get(SharePortfolioQuantityInTrustPage(index)) map {
    x =>
      AnswerRow(
        "sharePortfolioQuantityInTrust.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.shares.routes.SharePortfolioQuantityInTrustController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def sharePortfolioOnStockExchange(index: Int): Option[AnswerRow] = userAnswers.get(SharePortfolioOnStockExchangePage(index)) map {
    x =>
      AnswerRow(
        "sharePortfolioOnStockExchange.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.shares.routes.SharePortfolioOnStockExchangeController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def sharePortfolioName(index: Int): Option[AnswerRow] = userAnswers.get(SharePortfolioNamePage(index)) map {
    x =>
      AnswerRow(
        "sharePortfolioName.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.shares.routes.SharePortfolioNameController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def shareClass(index: Int): Option[AnswerRow] = userAnswers.get(ShareClassPage(index)) map {
    x =>
      AnswerRow(
        "shareClass.checkYourAnswersLabel",
        HtmlFormat.escape(messages(s"shareClass.$x")),
        Some(controllers.asset.shares.routes.ShareClassController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def assetMoneyValue(index: Int): Option[AnswerRow] = userAnswers.get(AssetMoneyValuePage(index)) map {
    x =>
      AnswerRow(
        "assetMoneyValue.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.money.routes.AssetMoneyValueController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def whatKindOfAsset(index: Int): Option[AnswerRow] = userAnswers.get(WhatKindOfAssetPage(index)) map {
    x =>
      AnswerRow(
        "whatKindOfAsset.checkYourAnswersLabel",
        HtmlFormat.escape(messages(s"whatKindOfAsset.$x")),
        Some(controllers.asset.routes.WhatKindOfAssetController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def otherAssetDescription(index: Int): Option[AnswerRow] = userAnswers.get(OtherAssetDescriptionPage(index)) map {
    x =>
      AnswerRow(
        "assets.other.description.checkYourAnswersLabel",
        escape(x),
        Some(controllers.asset.other.routes.OtherAssetDescriptionController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def otherAssetValue(index: Int, description: String): Option[AnswerRow] = userAnswers.get(OtherAssetValuePage(index)) map {
    x =>
      AnswerRow(
        "assets.other.value.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.other.routes.OtherAssetValueController.onPageLoad(NormalMode, index, draftId).url),
        description,
        canEdit = canEdit
      )
  }

  def partnershipStartDate(index: Int): Option[AnswerRow] = userAnswers.get(PartnershipStartDatePage(index)) map {
    x =>
      AnswerRow(
        "partnershipStartDate.checkYourAnswersLabel",
        HtmlFormat.escape(x.format(dateFormatter)),
        Some(controllers.asset.partnership.routes.PartnershipStartDateController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def partnershipDescription(index: Int): Option[AnswerRow] = userAnswers.get(PartnershipDescriptionPage(index)) map {
    x =>
      AnswerRow(
        "partnershipDescription.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.partnership.routes.PartnershipDescriptionController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def assetAddressUkYesNo(index: Int): Option[AnswerRow] = userAnswers.get(BusinessAddressUkYesNoPage(index)) map {
    x =>
      AnswerRow(
        "assetAddressUkYesNo.checkYourAnswersLabel",
        yesOrNo(x),
        Some(controllers.asset.business.routes.BusinessAddressUkYesNoController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def assetDescription(index: Int): Option[AnswerRow] = userAnswers.get(BusinessDescriptionPage(index)) map {
    x =>
      AnswerRow(
        "assetDescription.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.business.routes.BusinessDescriptionController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def assetNamePage(index: Int): Option[AnswerRow] = userAnswers.get(BusinessNamePage(index)) map {
    x =>
      AnswerRow(
        "assetName.checkYourAnswersLabel",
        HtmlFormat.escape(x),
        Some(controllers.asset.business.routes.BusinessNameController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }

  def assetInternationalAddress(index: Int): Option[AnswerRow] = userAnswers.get(BusinessInternationalAddressPage(index)) map {
    x =>
      AnswerRow(
        "assetInternationalAddress.checkYourAnswersLabel",
        internationalAddress(x, countryOptions),
        Some(controllers.asset.business.routes.BusinessInternationalAddressController.onPageLoad(NormalMode, index, draftId).url),
        assetName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def assetUkAddress(index: Int): Option[AnswerRow] = userAnswers.get(BusinessUkAddressPage(index)) map {
    x =>
      AnswerRow(
        "assetUkAddress.checkYourAnswersLabel",
        ukAddress(x),
        Some(controllers.asset.business.routes.BusinessUkAddressController.onPageLoad(NormalMode, index, draftId).url),
        assetName(index, userAnswers),
        canEdit = canEdit
      )
  }

  def currentValue(index: Int): Option[AnswerRow] = userAnswers.get(BusinessValuePage(index)) map {
    x =>
      AnswerRow(
        "currentValue.checkYourAnswersLabel",
        currency(x.toString),
        Some(controllers.asset.business.routes.BusinessValueController.onPageLoad(NormalMode, index, draftId).url),
        canEdit = canEdit
      )
  }
}