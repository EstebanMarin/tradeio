package tradex.domain
package trading

import cats.Parallel
import cats.effect._
import cats.implicits._

import repository._
import repository.interpreter.memory._

object Algebras {
  def make[F[_]: Sync]: F[Algebras[F]] =
    for {
      accountRepositoryInterpreter <- AccountRepositoryInterpreter.make[F]
      executionRepositoryInterpreter <- ExecutionRepositoryInterpreter.make[F]
      instrumentRepositoryInterpreter <- InstrumentRepositoryInterpreter.make[F]
      orderRepositoryInterpreter <- OrderRepositoryInterpreter.make[F]
      tradeRepositoryInterpreter <- TradeRepositoryInterpreter.make[F]
    } yield new Algebras[F](
      accountRepositoryInterpreter,
      executionRepositoryInterpreter,
      instrumentRepositoryInterpreter,
      orderRepositoryInterpreter,
      tradeRepositoryInterpreter
    )
}

final class Algebras[F[_]](
    val accountRepository: AccountRepository[F],
    val executionRepository: ExecutionRepository[F],
    val instrumentRepository: InstrumentRepository[F],
    val orderRepository: OrderRepository[F],
    val tradeRepository: TradeRepository[F]
)
