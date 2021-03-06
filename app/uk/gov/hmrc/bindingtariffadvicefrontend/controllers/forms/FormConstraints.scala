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

package uk.gov.hmrc.bindingtariffadvicefrontend.controllers.forms

import org.apache.commons.validator.routines.EmailValidator
import play.api.data.validation.{Constraint, Invalid, Valid}

object FormConstraints {

  def validEmailAddress(key: String): Constraint[String] = Constraint {
    case str: String if EmailValidator.getInstance().isValid(str) => Valid
    case _ => Invalid(key)
  }

  def nonEmpty(key: String): Constraint[String] = Constraint {
    case str: String if str.trim.nonEmpty => Valid
    case _ => Invalid(key)
  }

  def defined[T](key: String): Constraint[Option[T]] = Constraint {
    case option: Option[T] if option.isDefined => Valid
    case _ => Invalid(key)
  }

}
