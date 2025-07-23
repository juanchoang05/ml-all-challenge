# JaCoCo Configuration for MercadoLibre Core Platform

This project uses JaCoCo for code coverage analysis.

## Configuration

- **Minimum coverage threshold**: 80% for instructions and branches
- **Class-level threshold**: 70% for instructions
- **Excluded from coverage**:
  - Configuration classes
  - DTO classes  
  - Main application class
  - Config packages

## Maven Goals

### Generate coverage report
```bash
mvn clean test
```

### Generate coverage report with verification
```bash
mvn clean verify
```

### Generate coverage report only
```bash
mvn jacoco:report
```

### Check coverage thresholds
```bash
mvn jacoco:check
```

### Run tests with coverage for integration tests
```bash
mvn clean verify -P integration-test
```

## Reports Location

After running tests, coverage reports will be available at:
- **HTML Report**: `target/site/jacoco/index.html`
- **XML Report**: `target/site/jacoco/jacoco.xml`
- **CSV Report**: `target/site/jacoco/jacoco.csv`

## Coverage Thresholds

The project enforces the following coverage thresholds:
- **Bundle level**: 80% instruction and branch coverage
- **Class level**: 70% instruction coverage

## Excluded Classes

The following classes/packages are excluded from coverage:
- `**/config/**` - Configuration classes
- `**/dto/**` - Data Transfer Objects
- `**/MlCorePlatformApplication.class` - Main application class
- `**/*Config.class` - Configuration classes
- `**/*Configuration.class` - Configuration classes

## Integration with CI/CD

Coverage reports can be integrated with:
- SonarQube
- Codecov
- Coveralls
- GitHub Actions

## Viewing Reports

Open `target/site/jacoco/index.html` in your browser to view the interactive coverage report.
