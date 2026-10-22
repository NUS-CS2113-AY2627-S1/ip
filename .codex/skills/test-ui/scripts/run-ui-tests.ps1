param(
    [string]$PlanPath = "test/ui-test-plan.md",
    [string]$WorkingDirectory = (Get-Location).Path
)

$ErrorActionPreference = "Stop"

function Normalize-Output([string]$Text) {
    if ($null -eq $Text) {
        return ""
    }

    return (($Text -replace "`r`n", "`n" -replace "`r", "`n").TrimEnd("`n"))
}

function Read-FencedBlock([string]$Section, [string]$Label) {
    $pattern = "(?ms)^###\s+$([regex]::Escape($Label))\s*\r?\n\s*```[^\r\n]*\r?\n(?<content>.*?)\r?\n```"
    $match = [regex]::Match($Section, $pattern)
    if (-not $match.Success) {
        throw "Missing fenced '$Label' block."
    }

    return $match.Groups["content"].Value
}

function Read-TestCases([string]$PlanText) {
    $matches = [regex]::Matches($PlanText, "(?ms)^##\s+Test case\s+(?<number>[^:]+):\s*(?<name>[^\r\n]+)\r?\n(?<body>.*?)(?=^##\s+Test case\s+|\z)")
    if ($matches.Count -eq 0) {
        throw "No test cases found. Add sections headed '## Test case N: name'."
    }

    $cases = @()
    foreach ($match in $matches) {
        $body = $match.Groups["body"].Value
        $aimMatch = [regex]::Match($body, "(?ms)^###\s+Aim\s*\r?\n(?<aim>.*?)(?=^###\s+|\z)")
        if (-not $aimMatch.Success) {
            throw "Test case $($match.Groups['number'].Value) is missing an Aim section."
        }

        $cases += [pscustomobject]@{
            Number = $match.Groups["number"].Value.Trim()
            Name = $match.Groups["name"].Value.Trim()
            Aim = $aimMatch.Groups["aim"].Value.Trim()
            Command = (Read-FencedBlock $body "Program command").Trim()
            Inputs = Read-FencedBlock $body "Inputs"
            Expected = Read-FencedBlock $body "Expected output"
        }
    }

    return $cases
}

function Split-Command([string]$Command) {
    if ($Command -match "[|;&<>]" -or $Command -match "\r?\n") {
        throw "Program command '$Command' contains shell operators or multiple lines. Use one executable and its arguments."
    }

    $match = [regex]::Match($Command.Trim(), '^(?:(?:"(?<exe>[^"]+)")|(?<exe>\S+))(?:\s+(?<args>.*))?$')
    if (-not $match.Success) {
        throw "Could not parse program command '$Command'."
    }

    return @($match.Groups["exe"].Value, $match.Groups["args"].Value)
}

function Invoke-Program($TestCase) {
    $parts = Split-Command $TestCase.Command
    $startInfo = [System.Diagnostics.ProcessStartInfo]::new()
    $startInfo.FileName = $parts[0]
    $startInfo.Arguments = $parts[1]
    $startInfo.WorkingDirectory = $WorkingDirectory
    $startInfo.UseShellExecute = $false
    $startInfo.CreateNoWindow = $true
    $startInfo.RedirectStandardInput = $true
    $startInfo.RedirectStandardOutput = $true
    $startInfo.RedirectStandardError = $true

    $process = [System.Diagnostics.Process]::new()
    $process.StartInfo = $startInfo
    try {
        if (-not $process.Start()) {
            throw "Could not start '$($TestCase.Command)'."
        }

        $input = Normalize-Output $TestCase.Inputs
        if ($input.Length -gt 0) {
            $process.StandardInput.Write($input)
            $process.StandardInput.Write("`n")
        }
        $process.StandardInput.Close()

        $stdout = $process.StandardOutput.ReadToEnd()
        $stderr = $process.StandardError.ReadToEnd()
        $process.WaitForExit()

        return [pscustomobject]@{
            ExitCode = $process.ExitCode
            Output = Normalize-Output $stdout
            ErrorOutput = Normalize-Output $stderr
        }
    }
    finally {
        $process.Dispose()
    }
}

if (-not [System.IO.Path]::IsPathRooted($PlanPath)) {
    $PlanPath = Join-Path $WorkingDirectory $PlanPath
}
if (-not (Test-Path -LiteralPath $PlanPath -PathType Leaf)) {
    throw "Test plan not found: $PlanPath"
}

$javaVersion = (& java -version 2>&1 | Out-String)
if ($javaVersion -notmatch 'version "25(?:\.|"|-)') {
    throw "Java 25 is required. Detected: $($javaVersion.Trim())"
}

$planText = Get-Content -Raw -LiteralPath $PlanPath
$testCases = Read-TestCases $planText
$sessionLines = @("=== UI test session ===")
$passed = 0

foreach ($testCase in $testCases) {
    $sessionLines += ""
    $sessionLines += "[$($testCase.Number): $($testCase.Name)]"
    $sessionLines += "Aim: $($testCase.Aim)"
    $sessionLines += "Command: $($testCase.Command)"
    $sessionLines += "Input:"
    $inputLines = (Normalize-Output $testCase.Inputs) -split "`n", -1
    foreach ($line in $inputLines) {
        if ($line.Length -gt 0) {
            $sessionLines += "> $line"
        }
    }

    $result = Invoke-Program $testCase
    $sessionLines += "Output:"
    if ($result.Output.Length -gt 0) {
        $sessionLines += $result.Output -split "`n", -1
    }
    if ($result.ErrorOutput.Length -gt 0) {
        $sessionLines += "[stderr]"
        $sessionLines += $result.ErrorOutput -split "`n", -1
    }

    $actual = $result.Output
    $expected = Normalize-Output $testCase.Expected
    $passedTest = $result.ExitCode -eq 0 -and $actual -eq $expected -and $result.ErrorOutput.Length -eq 0
    if (-not $passedTest) {
        $sessionLines += "Result: FAIL"
        $sessionLines -join "`n"
        Write-Output ""
        Write-Output "FAILED TEST CASE $($testCase.Number): $($testCase.Name)"
        Write-Output "Actual output:"
        Write-Output $actual
        if ($result.ErrorOutput.Length -gt 0) {
            Write-Output "Process error output:"
            Write-Output $result.ErrorOutput
        }
        Write-Output "Expected output:"
        Write-Output $expected
        Write-Output "Exit code: $($result.ExitCode)"
        exit 1
    }

    $sessionLines += "Result: PASS"
    $passed++
}

$sessionLines += ""
$sessionLines += "=== $passed/$($testCases.Count) test cases passed ==="
$sessionLines -join "`n"
