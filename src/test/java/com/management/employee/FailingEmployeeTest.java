package com.management.employee;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ======================================================================================
 * JENKINS CI/CD DEMONSTRATION TEST: FailingEmployeeTest
 * ======================================================================================
 * Purpose:
 * This test class demonstrates Jenkins Pipeline Quality Gate enforcement.
 *
 * Instructions to test Jenkins Pipeline Failure:
 * 1. Remove the @Disabled annotation below or change the assertion to fail (e.g. assertEquals(1, 2)).
 * 2. Commit and push changes to Git.
 * 3. Trigger the Jenkins Pipeline.
 * 4. OBSERVATION:
 *    - Stage 4 ("Unit Test" -> `mvn test`) will fail immediately.
 *    - The pipeline execution will STOP IMMEDIATELY.
 *    - Subsequent stages (Stage 5 Package, Stage 6 Docker Build, Stage 7 Stop Container, Stage 8 Deploy)
 *      will NOT execute.
 *    - This guarantees broken code is NEVER containerized or deployed to production.
 * ======================================================================================
 */
class FailingEmployeeTest {

    @Test
    @Disabled("Disabled by default for production builds. Remove @Disabled to test Jenkins CI pipeline test failure handling.")
    @DisplayName("Intentionally Failing Test Case for CI/CD Quality Gate")
    void testJenkinsPipelineQualityGateFailure() {
        // Intentionally failing assertion to demonstrate Jenkins build halt
        int expectedValue = 100;
        int actualValue = 500;

        assertEquals(expectedValue, actualValue,
                "JENKINS PIPELINE FAILURE DEMO: Unit test failed! CI/CD Pipeline must halt immediately prior to Docker Build & Deployment.");
    }
}
