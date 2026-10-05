#!/usr/bin/env bash
set -euo pipefail

test_data_directory="$(mktemp -d)"
trap 'rm -rf "$test_data_directory"' EXIT

assert_load_failure() {
    local record="$1"
    local data_file="$test_data_directory/nested/yanny.txt"
    mkdir -p "$(dirname "$data_file")"
    printf '%s\n' "$record" > "$data_file"

    local output
    output="$(printf 'list\n' | java -Dyanny.data.file="$data_file" -cp out yanny.ui.Yanny)"
    grep -F '| ERROR > TASK DATA COULD NOT BE LOADED. CHECK FILE FORMAT AND PERMISSIONS.' \
        <<<"$output" >/dev/null
    rm -f "$data_file"
}

assert_load_failure 'T | 2 | invalid status'
assert_load_failure 'T | 0'
assert_load_failure 'Q | 0 | unknown type'
assert_load_failure 'D | 0 | | missing description'
assert_load_failure 'E | 0 | event | missing end'

mkdir "$test_data_directory/not-a-file"
directory_output="$(printf 'list\n' \
    | java -Dyanny.data.file="$test_data_directory/not-a-file" -cp out yanny.ui.Yanny)"
grep -F '| ERROR > TASK DATA COULD NOT BE LOADED. CHECK FILE FORMAT AND PERMISSIONS.' \
    <<<"$directory_output" >/dev/null

printf 'blocking path' > "$test_data_directory/block"
write_output="$(printf 'todo cannot save\nlist\nbye\n' \
    | java -Dyanny.data.file="$test_data_directory/block/yanny.txt" -cp out yanny.ui.Yanny)"
grep -F '| ERROR > TASK DATA COULD NOT BE SAVED. CHECK FILE PERMISSIONS.' \
    <<<"$write_output" >/dev/null
grep -F '| OUTPUT > NO TASKS STORED' <<<"$write_output" >/dev/null

printf '%s\n' 'Persistence error handling test passed.'
