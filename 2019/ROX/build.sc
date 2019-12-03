import mill._, scalalib._

object advent extends ScalaModule {
  def scalaVersion = "2.13.1"
  override def scalacOptions = Seq("-Ydelambdafy:inline")
  override def forkArgs = Seq("-Xmx4g")
  object test extends Tests {
    override def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.0.8"
    )
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}
