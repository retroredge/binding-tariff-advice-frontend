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

package uk.gov.hmrc.bindingtariffadvicefrontend.connector

import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.matching.EqualToJsonPattern
import org.apache.http.HttpStatus
import uk.gov.hmrc.bindingtariffadvicefrontend.model.{AdviceRequestEmail, AdviceRequestEmailParameters}

class EmailConnectorSpec extends ConnectorTest {

  private val connector = new EmailConnector(appConfig, standardHttpClient)

  "Connector 'Send'" should {
    "POST Email payload" in {
      stubFor(post(urlEqualTo("/hmrc/email"))
        .withRequestBody(new EqualToJsonPattern(fromResource("advice_request_email-request.json"), true, false))
        .willReturn(aResponse()
          .withStatus(HttpStatus.SC_ACCEPTED))
      )

      val email = AdviceRequestEmail(
        to = Seq("user@domain.com"),
        replyToAddress = "reply-to@domain.com",
        parameters = AdviceRequestEmailParameters("ref", "name", "email", "item-name", "item-description", "supporting-docs", "supporting-info")
      )

      await(connector.send(email)) shouldBe ((): Unit)

      verify(
        postRequestedFor(urlEqualTo("/hmrc/email"))
          .withoutHeader("X-Api-Token")
      )
    }
  }

}
