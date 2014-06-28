import sbt._
import Keys._

object Grunt extends Build {
  var gruntProcess: Option[Process] = None

  val webPath = Path("./src/main/web").asFile

  def runBuild = {
    Process("bower install", webPath).!
    Process("grunt build", webPath).!
  }

  def runWatch = {
    if (Grunt.gruntProcess.isDefined) {
      Grunt.gruntProcess.get.destroy()
    }
    val gruntProcess = Process("grunt rebuild", webPath).run()
    Grunt.gruntProcess = Some(gruntProcess)
  }

}
