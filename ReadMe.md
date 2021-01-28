# Regression tests for Illumin website  [www.illumin.com](http://www.illumin.com).

In the solution were used:
- Java 15 as the latest official version of Java.
- Test running - **[TestNG 7.3](https://github.com/cbeust/testng)**
- Web automation - **[Selenide 5.17.4](https://selenide.org/)**
- Yaml for configuration files - **[SnakeYAML](https://bitbucket.org/asomov/snakeyaml)**

The project is written in IntelliJ IDEA.
Satellite IDEA-files are already in the Git project.  
For running tests, please use created Run/Debug configuration **illumin\[test\]** in IDEA.  
For running directly in Gradle use command `.\gradlew regression` in the command line.  
The html report will be generated in `/build/reports/tests/test/index.html`.  
Tests are separated to four classes according to website functions.  
All parameters for tests are managed by  
[regression.yaml](src/regression.yaml) (for the full suite) and  
[defaults.yaml](src/defaults.yaml) (convenient for running separate tests in the IDE).
