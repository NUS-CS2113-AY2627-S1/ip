<#
.SYNOPSIS
Tests the built JAR in a fresh folder, including saving and loading across separate processes.
#>
param([string]$JdkPath)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$java = if ($JdkPath) { Join-Path $JdkPath 'bin/java.exe' } else { (Get-Command java).Source }
$javaVersion = & $java --version
if ($LASTEXITCODE -ne 0 -or ($javaVersion -join ' ') -notmatch '^(java|openjdk) 25(\.|\s|$)') {
    throw 'Run this test with Java 25.'
}

$sourceJar = Join-Path $projectRoot 'target/King.jar'
Add-Type -AssemblyName System.IO.Compression.FileSystem
$archive = [System.IO.Compression.ZipFile]::OpenRead($sourceJar)
try {
    if ($archive.Entries.FullName -match 'Test\.class$|\.java$|^data/') {
        throw 'The distribution contains tests, source files, or user data.'
    }
    $manifestReader = [System.IO.StreamReader]::new($archive.GetEntry('META-INF/MANIFEST.MF').Open())
    try {
        if (-not $manifestReader.ReadToEnd().Contains('Main-Class: king.King')) {
            throw 'The executable entry point is missing.'
        }
    } finally {
        $manifestReader.Dispose()
    }
} finally {
    $archive.Dispose()
}

$testDirectory = Join-Path $projectRoot ('target/jar-smoke-' + [guid]::NewGuid().ToString('N'))
$testDirectory = Join-Path $testDirectory 'Empty folder [test]'
New-Item -ItemType Directory -Path $testDirectory -Force | Out-Null
$testJar = Join-Path $testDirectory 'King [release].jar'
Copy-Item -LiteralPath $sourceJar -Destination $testJar
Push-Location -LiteralPath $testDirectory
try {
    $firstRun = @('todo', 'blah', 'todo read book', 'deadline return book /by Friday',
        'event meeting /from noon /to night', 'mark 1', 'bye') | & $java -jar 'King [release].jar'
    if ($LASTEXITCODE -ne 0) { throw 'First JAR launch failed.' }
    $firstOutput = $firstRun -join "`n"
    if (-not $firstOutput.Contains('a todo needs a description') -or
            -not $firstOutput.Contains("I do not recognize 'blah'")) {
        throw 'Input-error handling failed in the packaged application.'
    }
    $saveFile = Join-Path $testDirectory 'data/king.txt'
    if (-not (Test-Path -LiteralPath $saveFile)) { throw 'First run did not create the save file.' }

    $secondRun = @('list', 'delete 2', 'bye') | & $java -jar 'King [release].jar'
    if ($LASTEXITCODE -ne 0) { throw 'Second JAR launch failed.' }
    $secondOutput = $secondRun -join "`n"
    foreach ($expected in @('1. [T][X] read book', '2. [D][ ] return book (by: Friday)',
            '3. [E][ ] meeting (from: noon to: night)')) {
        if (-not $secondOutput.Contains($expected)) { throw "Restart lost a task: $expected" }
    }

    $thirdRun = @('list', 'delete 1', 'delete 1', 'bye') | & $java -jar 'King [release].jar'
    if ($LASTEXITCODE -ne 0) { throw 'Third JAR launch failed.' }
    $thirdOutput = $thirdRun -join "`n"
    if ($thirdOutput.Contains('return book') -or
            -not $thirdOutput.Contains('2. [E][ ] meeting (from: noon to: night)')) {
        throw 'Deletion did not survive restart.'
    }

    $fourthRun = @('list', 'bye') | & $java -jar 'King [release].jar'
    if ($LASTEXITCODE -ne 0 -or ($fourthRun -join "`n").Contains('1. [')) {
        throw 'The empty task list was not saved.'
    }
} finally {
    Pop-Location
}
Write-Output 'JAR manifest, contents, and four standalone launch/restart checks passed on Java 25.'
Write-Output "Test files are in $testDirectory"
