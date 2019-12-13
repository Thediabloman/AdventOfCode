import mill._, scalalib._, scalafmt._

object advent extends ScalaModule with ScalafmtModule {
  def scalaVersion = "2.13.1"
  override def scalacOptions = Seq("-Ydelambdafy:inline")
  override def forkArgs = Seq("-Xmx4g")
  override def ivyDeps = Agg(
    ivy"org.typelevel::cats-core:2.0.0"
  )
  object test extends Tests {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.0.8"
    )
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}
