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
- ChatGPT for guidance and validation
