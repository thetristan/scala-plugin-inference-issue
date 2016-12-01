package example

import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Main extends App {
  type Result[A] = Future[Either[String, A]]

  def fooWorks(): Future[Either[String, String]] = Future(Right("foo"))
  def fooRedSquiggles(): Result[String] = Future(Right("foo"))

  val concat = for {
    name1 <- EitherT(fooWorks())        // Works as expected, no red squiggles
    name2 <- EitherT(fooRedSquiggles()) // Type mismatch, expected: NotInferedF[Either[NotInferedA, NotInferedB]], actual: Result.Result[String]
  } yield name1 + name2

  println(Await.result(concat.value, Duration.Inf).right.get) // prints foofoo
}