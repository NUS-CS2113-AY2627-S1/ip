param(
    [string]$PlanPath = "test/ui-test-plan.md"
)

$ErrorActionPreference = "Stop"

function Normalize-Output([string]$text) {
    return $text.Replace("`r`n", "`n").Replace("`r", "`n").TrimEnd("`n")
}

if (-not (Test-Path $PlanPath)) {
    throw "UI test plan not found: $PlanPath"
}

$plan = Get-Content -Raw $PlanPath
$casePattern = '(?ms)^## (?<name>.+?)\r?\nAim:\s*(?<aim>.*?)\r?\nInput:\r?\n```text\r?\n(?<input>.*?)\r?\n```\r?\nExpected output:\r?\n```text\r?\n(?<expected>.*?)\r?\n```'
$testCases = [regex]::Matches($plan, $casePattern)
if ($testCases.Count -eq 0) {
    throw "No test cases matched the required format in $PlanPath"
}

$temporaryDirectory = Join-Path ([System.IO.Path]::GetTempPath()) ("yoda-ui-test-" + [guid]::NewGuid())
$classDirectory = Join-Path $temporaryDirectory "classes"
New-Item -ItemType Directory -Path $classDirectory -Force | Out-Null

try {
    $sourceFiles = Get-ChildItem "src/main/java" -Filter "*.java" | ForEach-Object FullName
    & javac --release 25 -d $classDirectory $sourceFiles
    if ($LASTEXITCODE -ne 0) {
        throw "Compilation failed."
    }

    foreach ($testCase in $testCases) {
        $name = $testCase.Groups["name"].Value.Trim()
        $aim = $testCase.Groups["aim"].Value.Trim()
        $input = $testCase.Groups["input"].Value
        $expected = $testCase.Groups["expected"].Value
        $caseDirectory = Join-Path $temporaryDirectory ([guid]::NewGuid().ToString())
        New-Item -ItemType Directory -Path $caseDirectory | Out-Null

        Push-Location $caseDirectory
        try {
            $actual = $input | & java -cp $classDirectory Yoda 2>&1 | Out-String
            if ($LASTEXITCODE -ne 0) {
                throw "Application exited with code $LASTEXITCODE."
            }
        } finally {
            Pop-Location
        }
        Write-Output "=== $name ==="
        Write-Output "Aim: $aim"
        Write-Output "Console input:"
        Write-Output $input
        Write-Output "Expected output:"
        Write-Output $expected
        Write-Output "Actual output:"
        Write-Output $actual

        if ((Normalize-Output $actual) -ne (Normalize-Output $expected)) {
            Write-Output "RESULT: FAIL"
            exit 1
        }
        Write-Output "RESULT: PASS"
    }
} finally {
    Remove-Item -LiteralPath $temporaryDirectory -Recurse -Force -ErrorAction SilentlyContinue
}
