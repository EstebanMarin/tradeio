package tradex.domain

import java.util.UUID
import java.time.LocalDateTime
import model.account._
import NewtypeRefinedOps._

import eu.timepit.refined.scalacheck.string._
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import squants.market.USD
import squants.market.JPY

object generators {
  val nonEmptyStringGen: Gen[String] =
    Gen
      .chooseNum(21, 40)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  def nesGen[A](f: String => A): Gen[A] =
    nonEmptyStringGen.map(f)

  def idGen[A](f: UUID => A): Gen[A] =
    Gen.uuid.map(f)

  val accountNoStringGen: Gen[String] =
    Gen
      .chooseNum(5, 12)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  val accountNoGen: Gen[AccountNo] = accountNoStringGen.map(str =>
    validate[AccountNo](str)
      .fold(errs => throw new Exception(errs.toString), identity)
  )

  val accountNameGen: Gen[AccountName] = arbitrary[NonEmptyString].map(AccountName(_))

  def openCloseDateGen: Gen[(LocalDateTime, LocalDateTime)] = for {
    o <- Gen.oneOf(List(LocalDateTime.now, LocalDateTime.now.plusDays(2)))
    c <- Gen.oneOf(List(LocalDateTime.now.plusDays(10), LocalDateTime.now.plusDays(20)))
  } yield (o, c)

  def tradingAccountGen: Gen[Account] = for {
    no <- accountNoGen
    nm <- accountNameGen
    oc <- openCloseDateGen
    tp <- Gen.const(AccountType.Trading)
    bc <- Gen.const(USD)
    tc <- Gen.oneOf(List(USD, JPY)).map(Some(_))
    sc <- Gen.const(None)
  } yield Account(no, nm, oc._1, Some(oc._2), tp, bc, tc, sc)

  def settlementAccountGen: Gen[Account] = for {
    no <- accountNoGen
    nm <- accountNameGen
    oc <- openCloseDateGen
    tp <- Gen.const(AccountType.Settlement)
    bc <- Gen.const(USD)
    tc <- Gen.const(None)
    sc <- Gen.oneOf(List(USD, JPY)).map(Some(_))
  } yield Account(no, nm, oc._1, Some(oc._2), tp, bc, tc, sc)

  def bothAccountGen: Gen[Account] = for {
    no <- accountNoGen
    nm <- accountNameGen
    oc <- openCloseDateGen
    tp <- Gen.const(AccountType.Both)
    bc <- Gen.const(USD)
    tc <- Gen.oneOf(List(USD, JPY)).map(Some(_))
    sc <- Gen.oneOf(List(USD, JPY)).map(Some(_))
  } yield Account(no, nm, oc._1, Some(oc._2), tp, bc, tc, sc)

  def accountGen: Gen[Account] = Gen.oneOf(tradingAccountGen, settlementAccountGen, bothAccountGen)
}
