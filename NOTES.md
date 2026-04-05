## Summary of Changes

- Removed artificial delay in task search API caused by Thread.sleep().
- Improved API response performance significantly.

## Issue Details

### Issue 1: Artificial delay in API response

- Problem:
  API response was slow and inconsistent due to an artificial delay.

- Root Cause:
  Thread.sleep() was used based on query length, making shorter queries slower.

- How I Found:
  Tested API using Postman and compared response times for different queries.

- Fix:
  Removed Thread.sleep() logic completely.

- Why:
  Blocking threads reduces performance and scalability. This delay had no valid purpose.

## What I did NOT change

- Did not refactor repository or SQL logic to keep changes minimal and focused.

## Biggest Risk Remaining

- Query logic may have potential filtering issues due to SQL condition structure.

## Tools Used

- Postman for API testing

### Issue 2: Incorrect SQL filtering logic due to AND/OR precedence

- Problem:
  The API returned incorrect results when filtering by search term and status.
  Tasks with wrong status or archived tasks were appearing in results.

- Root Cause:
  The SQL query did not group conditions properly. Due to SQL operator precedence (AND > OR),
  the query was evaluated incorrectly.

- How I Found:
  Tested API using Postman:
  GET /api/tasks?q=api&status=DONE

Observed that results included tasks with incorrect status (e.g., OPEN),
which should not happen.

- Fix:
  Added parentheses to correctly group conditions:

archived = FALSE AND (title LIKE term OR description LIKE term) AND status condition

- Why:
  Ensures correct filtering logic and prevents invalid data from being returned.

### Issue 3: API crash on invalid status input

- Problem:
  API crashed with 500 error when invalid status value was provided.

- Root Cause:
  Used TaskStatus.valueOf() without validation, causing IllegalArgumentException.

- How I Found:
  Tested API using invalid input:
  GET /api/tasks?status=INVALID

- Fix:
  Wrapped parsing logic in try-catch block and handled invalid input safely.

- Why:
  Prevents API crash and improves robustness.

### Issue 4: Pagination crash for invalid page values

- Problem:
  API crashed when page <= 0 due to invalid index calculation.

- Root Cause:
  Negative start index passed to subList().

- How I Found:
  Tested:
  GET /api/tasks?page=0

- Fix:
  Validated page and pageSize to ensure minimum values.

- Why:
  Prevents runtime exception and improves API robustness.
