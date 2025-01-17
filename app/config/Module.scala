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

package config

import com.google.inject.AbstractModule
import controllers.actions._
import navigation._
import repositories.{DefaultRegistrationsRepository, RegistrationsRepository}
import config.annotations._

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[RegistrationsRepository]).to(classOf[DefaultRegistrationsRepository]).asEagerSingleton()
    bind(classOf[RegistrationDataRequiredAction]).to(classOf[RegistrationDataRequiredActionImpl]).asEagerSingleton()
    bind(classOf[DraftIdRetrievalActionProvider]).to(classOf[DraftIdDataRetrievalActionProviderImpl]).asEagerSingleton()

    bind(classOf[Navigator]).annotatedWith(classOf[Money]).to(classOf[MoneyNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[PropertyOrLand]).to(classOf[PropertyOrLandNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[Shares]).to(classOf[SharesNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[Business]).to(classOf[BusinessNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[Partnership]).to(classOf[PartnershipNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[Other]).to(classOf[OtherNavigator])
    bind(classOf[Navigator]).annotatedWith(classOf[NonEeaBusiness]).to(classOf[NonEeaBusinessNavigator])
  }
}
