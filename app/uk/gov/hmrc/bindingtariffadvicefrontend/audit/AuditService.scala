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

package uk.gov.hmrc.bindingtariffadvicefrontend.audit

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.bindingtariffadvicefrontend.audit.AdviceAuditPayload.format
import uk.gov.hmrc.bindingtariffadvicefrontend.audit.AuditPayloadType.BTIAdviceSubmission
import uk.gov.hmrc.bindingtariffadvicefrontend.model.{Advice, ContactDetails, GoodDetails}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.audit.DefaultAuditConnector

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class AuditService @Inject()(auditConnector: DefaultAuditConnector) {

  def auditBTIAdviceSubmission(a: Advice)(implicit hc: HeaderCarrier): Unit = {
    auditConnector.sendExplicitAudit(
      auditType = BTIAdviceSubmission,
      detail = AdviceAuditPayload.from(a)
    )
  }

}

case class AdviceAuditPayload
(
  reference: String,
  contactDetails: Option[ContactDetails],
  goodDetails: Option[GoodDetails],
  supportingDocuments: Seq[String],
  supportingInformation: Option[String]
)

object AdviceAuditPayload {
  implicit val format: OFormat[AdviceAuditPayload] = Json.format[AdviceAuditPayload]

  def from(advice: Advice): AdviceAuditPayload = {
    AdviceAuditPayload(
      reference = advice.reference.getOrElse(throw new RuntimeException("Missing reference")),
      contactDetails = advice.contactDetails,
      goodDetails = advice.goodDetails,
      supportingDocuments = advice.supportingDocuments.map(_.id),
      supportingInformation = advice.supportingInformation
    )
  }
}

object AuditPayloadType {
  val BTIAdviceSubmission = "bindingTariffAdviceSubmission"
}
