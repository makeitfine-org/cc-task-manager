---
description: Review current branch changes as a senior Java developer
allowed-tools: Read, Bash(git *)
---

Review the current branch changes as a senior Java developer.

PR diff:
!`git diff main...HEAD`

Check for:
- Missing tests for new public methods
- JPA entities exposed in controller responses (should use DTOs)
- Missing validation annotations on DTOs
- SQL injection risks in @Query annotations
- Hardcoded configuration values
- Missing error handling

Provide a structured review with MUST-FIX and NICE-TO-HAVE categories.
