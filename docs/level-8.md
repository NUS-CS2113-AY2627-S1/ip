# Level 8: Deadline dates

Enter deadlines using ISO dates:

```text
deadline return book /by 2019-10-15
```

King displays `return book (by: Oct 15 2019)`. Deadlines use `LocalDate`,
so impossible dates such as `2019-02-29` are rejected. Leap days such as
`2024-02-29` are accepted. This increment supports dates without times.
Events still accept free-form start and end text.

Storage keeps the existing Base64 record format, encoding the ISO date
instead of the display text. Existing ISO deadlines load normally.
Legacy deadlines such as `Friday` cannot be converted to an unambiguous
date. Loading reports the affected line and leaves the file untouched.
Back up the file and correct the encoded deadline to an ISO date, or move
the old file aside and re-enter the tasks.

Validation uses the command and persistence regression suites on Java 25,
including invalid dates, leap days, formatted output, repeated reloads,
and preservation of invalid legacy records.
