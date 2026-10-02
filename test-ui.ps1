$ErrorActionPreference = 'Stop'
$projectRoot = $PSScriptRoot
$javaVersion = & javac -version 2>&1
if ("$javaVersion" -notmatch '^javac 25\.') {
    throw 'Please put Java 25 on PATH before running tests.'
}
$testRun = Join-Path $projectRoot ('_temp/test-' + [guid]::NewGuid().ToString('N'))
$classes = Join-Path $testRun 'classes'
New-Item -ItemType Directory -Path $classes -Force | Out-Null
$sources = Get-ChildItem (Join-Path $projectRoot 'src') -Recurse -Filter '*.java'
& javac -encoding UTF-8 -d $classes $sources.FullName
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed.' }
Push-Location $testRun
try {
    & java -cp $classes StorageTest
    if ($LASTEXITCODE -ne 0) { throw 'Tests failed.' }
    Write-Output "Console transcript: $testRun/ui-transcript.txt"
} finally {
    Pop-Location
}
