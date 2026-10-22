<#
.SYNOPSIS
Builds an executable King.jar using JDK 25, without packaging tests or local data.
.PARAMETER JdkPath
Optional JDK installation folder. Otherwise, javac and jar are resolved from PATH.
#>
param([string]$JdkPath)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot

if ($JdkPath) {
    $compiler = Join-Path $JdkPath 'bin/javac.exe'
    $archiver = Join-Path $JdkPath 'bin/jar.exe'
} else {
    $compiler = (Get-Command javac -ErrorAction Stop).Source
    $archiver = (Get-Command jar -ErrorAction Stop).Source
}

$compilerVersion = & $compiler --version
if ($LASTEXITCODE -ne 0 -or $compilerVersion -notmatch '^javac 25(\.|\s|$)') {
    throw 'JDK 25 is required. Set PATH or supply -JdkPath pointing to JDK 25.'
}
$archiverVersion = & $archiver --version
if ($LASTEXITCODE -ne 0 -or $archiverVersion -notmatch '^jar 25(\.|\s|$)') {
    throw 'The jar tool must also come from JDK 25.'
}

# A fresh class directory prevents stale classes from entering the distribution.
$buildDirectory = Join-Path $projectRoot ('target/jar-build-' + [guid]::NewGuid().ToString('N'))
$classesDirectory = Join-Path $buildDirectory 'classes'
New-Item -ItemType Directory -Path $classesDirectory -Force | Out-Null
$sourceDirectory = Join-Path $projectRoot 'src/main/java'
$javaSources = @(Get-ChildItem -LiteralPath $sourceDirectory -Recurse -Filter '*.java' |
    Sort-Object FullName | ForEach-Object { $_.FullName })
if ($javaSources.Count -eq 0) {
    throw 'No production Java sources were found.'
}

& $compiler --release 25 -encoding UTF-8 -d $classesDirectory $javaSources
if ($LASTEXITCODE -ne 0) {
    throw 'Compilation failed; no new JAR was created.'
}

$temporaryJar = Join-Path $buildDirectory 'King.jar'
& $archiver --create --file $temporaryJar --main-class king.King -C $classesDirectory .
if ($LASTEXITCODE -ne 0) {
    throw 'JAR creation failed.'
}

$outputJar = Join-Path $projectRoot 'target/King.jar'
Copy-Item -LiteralPath $temporaryJar -Destination $outputJar -Force
Write-Output "Created $outputJar"
