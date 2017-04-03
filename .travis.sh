sbt clean
cd ./example; sbt clean test
cd ./plugin/cucumber-plugin; sbt ++$TRAVIS_SCALA_VERSION clean test
cd ./plugin/cucumber-plugin-example; sbt clean test
