/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.bindingtariffadvicefrontend.controllers

import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.bindingtariffadvicefrontend.config.AppConfig
import uk.gov.hmrc.bindingtariffadvicefrontend.controllers.action.RetrieveAnswersAction
import uk.gov.hmrc.bindingtariffadvicefrontend.controllers.request.AnswersRequest
import uk.gov.hmrc.bindingtariffadvicefrontend.service.AdviceService
import uk.gov.hmrc.bindingtariffadvicefrontend.views
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class CheckYourAnswersController @Inject()(retrieveAnswers: RetrieveAnswersAction,
                                           adviceService: AdviceService,
                                           override val messagesApi: MessagesApi,
                                           implicit val appConfig: AppConfig) extends FrontendController with I18nSupport {

  def get: Action[AnyContent] = retrieveAnswers.async { implicit request: AnswersRequest[AnyContent] =>
    Future.successful(Ok(views.html.check_your_answers()))
  }

  def post: Action[AnyContent] = retrieveAnswers.async { implicit request: AnswersRequest[AnyContent] =>
    for {
      _ <- adviceService.submit(request.advice)
    } yield Navigator.continue(routes.ConfirmationController.get())
  }

}
