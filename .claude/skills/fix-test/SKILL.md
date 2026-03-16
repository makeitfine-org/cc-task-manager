---
description: Diagnose and fix a failing test
argument-hint: [TestClassName]
allowed-tools: Read, Write, Bash(mvn *)
---

The following test is failing: $ARGUMENTS

Current PR diff for context:
!`git diff main...HEAD`

1. Read the test file
2. Read the source file it tests
3. Run the specific test with `mvn test -pl backend -Dtest=$ARGUMENTS`
4. Analyze the failure output
5. Fix the issue — prefer fixing the source code unless the test expectation is wrong
6. Re-run the test to confirm it passes
7. Run the full test suite to ensure no regressions
