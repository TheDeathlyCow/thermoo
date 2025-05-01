# Unit and Game Test Structure

Unit tests are defined as small tests which cover single 'units' of a code, typically a single method. These tests should be used for small utilities and things that do not involve interactions with the world or entities, where dependencies can be easily mocked out. The point is to verify the logic of the unit under test - including both the blue sky AND alternate/exceptional flow scenarios.

Game tests are necessary when making direct interactions with a world or an entity. Currently, Thermoo does not use automatic game tests. Instead, use `./gradlew runTestmodClient` to do manual testing with a test mod that implements Thermoo APIs for various systems.

# Unit tests naming convention

All unit tests shall be placed in the folder `src/test/`. They shall follow the same package structure and class name as the class they are testing, but with the word `Test` appended onto the end. For example, if the class `TemperatureUnit` in the package `com.github.thedeathlycow.thermoo.api.util` was being tested, then the unit test would be in the package `com.github.thedeathlycow.thermoo.api.util` (but in `src/test/` rather than `src/main/`) and the test class itself will be called `TemperatureUnitTest`.

The testing cases/methods for unit tests should describe what the test is doing and may use underscores. A good unit test would be of the form `{inputs}_{action}_{expected result}`. For example, `invalidTemperature_testPredicate_isFalse`.

Every unit test may be run from the command line using the following command:

```bash
./gradlew test
```

These tests are also run on build and in GitHub actions on push.

# Test coverage and TDD

Test coverage isn't a huge priority, as many systems in Thermoo rely on entity/world interactions that cannot be mocked out and unit-tested. However, if a package is unit-testable, then line coverage of around 90% is a good target for that package.

From Thermoo 4.3 onwards, the following TDD-style of development will be implemented:

1. Define the tests for the feature, but do not implement them. For unit tests, focus just on the name and the inputs (for parameterized tests) and have their implementations simply be `Assertions.fail()`. This is to bring a focus onto the test cases themselves and help consider building more testable stuff. This should be committed, BUT NOT PUSHED!
2. Provide the minimum implementation for the feature. The tests still won't pass (they are all set to fail, after all). This should again be committed, BUT NOT PUSHED!
3. Implement the tests for the feature now. Ensure that they are all working, and then commit and push.