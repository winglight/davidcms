name := "davidcms"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  filters,
  "commons-io" % "commons-io" % "2.0",
  "mysql" % "mysql-connector-java" % "5.1.21"
)

play.Project.playJavaSettings
