cd ./cucumber-plugin; sbt ++$TRAVIS_SCALA_VERSION clean test
cd ../cucumber-plugin-example; sbt ++$TRAVIS_SCALA_VERSION clean test
cd ../cucumber-runner; sbt ++$TRAVIS_SCALA_VERSION clean test
cd ../cucumber-runner-example; sbt ++$TRAVIS_SCALA_VERSION clean test

