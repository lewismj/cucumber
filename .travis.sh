cd ./cucumber-plugin; sbt ++$TRAVIS_SCALA_VERSION clean test
cd ./cucumber-plugin-example; sbt clean test
cd ./cucumber-runner; sbt clean test
cd ./cucumber-runner-example; sbt clean test
