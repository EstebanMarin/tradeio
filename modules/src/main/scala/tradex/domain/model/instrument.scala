package tradex.domain
package model

import java.time.LocalDateTime

import cats.data._
import cats.implicits._

import squants.market._

import common._
import NewtypeRefinedOps._
import newtypes._
import enums._

object instrument {
  private[domain] final case class Instrument(
      isinCode: ISINCode,
      name: InstrumentName,
      instrumentType: InstrumentType,
      dateOfIssue: Option[LocalDateTime], // for non CCY
      dateOfMaturity: Option[LocalDateTime], // for Fixed Income
      lotSize: LotSize,
      unitPrice: Option[Money], // for Equity
      couponRate: Option[Money], // for Fixed Income
      couponFrequency: Option[BigDecimal] // for Fixed Income
  )

  object Instrument {
    private[model] def validateISINCode(isin: String): ValidationResult[ISINCode] = validate[ISINCode](isin).toValidated
    /*
    private[model] def validateISINCode(
        isin: ISINCode
    ): ValidationResult[ISINCode] =
      if (isin.value.isEmpty || isin.value.size != 12)
        s"ISIN code has to be 12 characters long: found $isin".invalidNec
      else isin.validNec
      */
  }
}
