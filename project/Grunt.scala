import sbt._
import Keys._

object Grunt extends Build {
  var gruntProcess: Option[Process] = None

  val webPath = Path("./src/main/web").asFile

  def runBuild = {
    Process("grunt build", webPath).!
  }

  def runWatch = {
    if (Grunt.gruntProcess.isDefined) {
      Grunt.gruntProcess.get.destroy()
    }
    val gruntProcess = Process("grunt serve", webPath).run()
    Grunt.gruntProcess = Some(gruntProcess)
  }

}
