#!/usr/bin/env bash
set -euo pipefail

test_data_directory="$(mktemp -d)"
trap 'rm -rf "$test_data_directory"' EXIT
data_file="$test_data_directory/nested/yanny.txt"

printf 'todo read saved task\ndeadline return book /by 2019-10-15\ndeadline submit report /by 2/12/2019 1800\nmark 1\nmark 3\nbye\n' \
    | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny >/dev/null

grep -Fx 'D | 0 | return book | 2019-10-15' "$data_file" >/dev/null
grep -Fx 'D | 1 | submit report | 2019-12-02T18:00' "$data_file" >/dev/null

printf 'D | 0 | old task | Sunday\n' >>"$data_file"
printf 'D | 0 | imported date | 2/12/2019 1800\n' >>"$data_file"

loaded_output="$(printf 'list\nbye\n' \
    | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny)"

grep -F '| 1. [T][X] read saved task' <<<"$loaded_output" >/dev/null
grep -F '| 2. [D][ ] return book (by: Oct 15 2019)' <<<"$loaded_output" >/dev/null
grep -F '| 3. [D][X] submit report (by: Dec 02 2019 6:00 PM)' <<<"$loaded_output" >/dev/null
grep -F '| 4. [D][ ] old task (by: Sunday)' <<<"$loaded_output" >/dev/null
grep -F '| 5. [D][ ] imported date (by: Dec 02 2019 6:00 PM)' <<<"$loaded_output" >/dev/null

printf 'mark 4\nbye\n' | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny >/dev/null
grep -Fx 'D | 1 | old task | Sunday' "$data_file" >/dev/null
grep -Fx 'D | 0 | imported date | 2019-12-02T18:00' "$data_file" >/dev/null
printf '%s\n' 'Persistence read test passed.'
