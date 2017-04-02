$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("Multiplication.feature");
formatter.feature({
  "line": 2,
  "name": "Multiplication",
  "description": "In order to avoid making mistakes\r\nAs a dummy\r\nI want to multiply numbers",
  "id": "multiplication",
  "keyword": "Feature",
  "tags": [
    {
      "line": 1,
      "name": "@my-test"
    }
  ]
});
formatter.scenario({
  "line": 7,
  "name": "Multiply two variables",
  "description": "",
  "id": "multiplication;multiply-two-variables",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 8,
  "name": "a variable x with value 2",
  "keyword": "Given "
});
formatter.step({
  "line": 9,
  "name": "a variable y with value 3",
  "keyword": "And "
});
formatter.step({
  "line": 10,
  "name": "I multiply x * y",
  "keyword": "When "
});
formatter.step({
  "line": 11,
  "name": "I get 6",
  "keyword": "Then "
});
formatter.match({
  "arguments": [
    {
      "val": "2",
      "offset": 24
    }
  ],
  "location": "MultiplicationSteps.scala:44"
});
formatter.result({
  "duration": 96671367,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "3",
      "offset": 24
    }
  ],
  "location": "MultiplicationSteps.scala:48"
});
formatter.result({
  "duration": 146993,
  "status": "passed"
});
formatter.match({
  "location": "MultiplicationSteps.scala:52"
});
formatter.result({
  "duration": 72269,
  "status": "passed"
});
formatter.match({
  "arguments": [
    {
      "val": "6",
      "offset": 6
    }
  ],
  "location": "MultiplicationSteps.scala:56"
});
formatter.result({
  "duration": 227205,
  "status": "passed"
});
});