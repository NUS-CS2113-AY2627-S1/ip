#!/usr/bin/env bash
set -euo pipefail

test_data_directory="$(mktemp -d)"
trap 'rm -rf "$test_data_directory"' EXIT

java -Dyanny.data.file="$test_data_directory/yanny.txt" -cp out yanny.ui.Yanny
