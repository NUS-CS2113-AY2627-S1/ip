[CmdletBinding()]
param(
    [Parameter(Mandatory = $true)]
    [string] $PlanPath,

    [Parameter(Mandatory = $true)]
    [string] $RunExecutable,

    [string[]] $RunArgument = @(),

    [ValidateRange(1, 600)]
    [int] $TimeoutSeconds = 30
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function Normalize-Transcript {
    param(
        [AllowEmptyString()]
        [string] $Value
    )

    if ($null -eq $Value) {
        return ""
    }

    return (($Value -replace "`r`n", "`n") -replace "`r", "`n").TrimEnd([char] 10)
}

function Get-RequiredPlanBlock {
    param(
        [string] $Body,
        [string] $Label,
        [string] $CaseId
    )

    $pattern = '(?ms)^\s*-\s*' + [regex]::Escape($Label) `
        + '(?:\s*\([^\r\n]*\))?\s*:\s*\r?\n```[^\r\n]*\r?\n(?<value>.*?)\r?\n```'
    $match = [regex]::Match($Body, $pattern)

    if (-not $match.Success) {
        throw "${CaseId} is missing its ${Label} fenced block."
    }

    return $match.Groups["value"].Value
}

function Read-TestPlan {
    param(
        [string] $Path
    )

    $content = [System.IO.File]::ReadAllText((Resolve-Path -LiteralPath $Path).Path)
    $casePattern = "(?ms)^###\s+(?<id>TC-\S+)\s+(?<title>[^\r\n]+)\r?\n(?<body>.*?)(?=^###\s+|\z)"
    $matches = [regex]::Matches($content, $casePattern)

    if ($matches.Count -eq 0) {
        throw "No test cases were found in ${Path}."
    }

    $cases = foreach ($match in $matches) {
        $caseId = $match.Groups["id"].Value
        $title = $match.Groups["title"].Value.Trim() -replace "^[—:-]\s*", ""
        $body = $match.Groups["body"].Value
        $aimMatch = [regex]::Match($body, "(?m)^\s*-\s*Aim:\s*(?<value>.+?)\s*$")

        if (-not $aimMatch.Success) {
            throw "${caseId} is missing its Aim."
        }

        [pscustomobject] @{
            Id = $caseId
            Title = $title
            Aim = $aimMatch.Groups["value"].Value.Trim()
            Inputs = Get-RequiredPlanBlock -Body $body -Label "Inputs" -CaseId $caseId
            ExpectedOutput = Get-RequiredPlanBlock -Body $body -Label "Expected output" -CaseId $caseId
        }
    }

    return $cases
}

function Invoke-ConsoleSession {
    param(
        [string] $InputText
    )

    $startInfo = [System.Diagnostics.ProcessStartInfo]::new()
    $startInfo.FileName = $RunExecutable
    $startInfo.WorkingDirectory = (Get-Location).Path
    $startInfo.UseShellExecute = $false
    $startInfo.CreateNoWindow = $true
    $startInfo.RedirectStandardInput = $true
    $startInfo.RedirectStandardOutput = $true
    $startInfo.RedirectStandardError = $true

    foreach ($argument in $RunArgument) {
        [void] $startInfo.ArgumentList.Add($argument)
    }

    $process = [System.Diagnostics.Process]::new()
    $process.StartInfo = $startInfo

    if (-not $process.Start()) {
        throw "Could not start ${RunExecutable}."
    }

    $stdoutTask = $process.StandardOutput.ReadToEndAsync()
    $stderrTask = $process.StandardError.ReadToEndAsync()
    $process.StandardInput.Write($InputText)
    $process.StandardInput.Close()

    if (-not $process.WaitForExit($TimeoutSeconds * 1000)) {
        $process.Kill($true)
        throw "The process exceeded the ${TimeoutSeconds}-second timeout."
    }

    return [pscustomobject] @{
        ExitCode = $process.ExitCode
        Output = $stdoutTask.GetAwaiter().GetResult()
        Error = $stderrTask.GetAwaiter().GetResult()
    }
}

function Write-TextBlock {
    param(
        [AllowEmptyString()]
        [string] $Text
    )

    if ($null -ne $Text) {
        [Console]::Out.Write($Text)
    }

    if ([string]::IsNullOrEmpty($Text) -or ($Text -notmatch "(?:`r|`n)$")) {
        [Console]::Out.WriteLine()
    }
}

function Write-SessionRecord {
    param(
        [pscustomobject] $TestCase,
        [string] $InputText,
        [string] $OutputText
    )

    [Console]::Out.WriteLine("=== $($TestCase.Id): $($TestCase.Title) ===")
    [Console]::Out.WriteLine("Aim: $($TestCase.Aim)")
    [Console]::Out.WriteLine("--- Console input ---")
    Write-TextBlock -Text $InputText
    [Console]::Out.WriteLine("--- Console output ---")
    Write-TextBlock -Text $OutputText
}

$testCases = Read-TestPlan -Path $PlanPath
$passedCount = 0

foreach ($testCase in $testCases) {
    $actualOutput = ""
    $processError = ""
    $exitCode = -1

    try {
        $session = Invoke-ConsoleSession -InputText $testCase.Inputs
        $actualOutput = $session.Output
        $processError = $session.Error
        $exitCode = $session.ExitCode
    } catch {
        $processError = $_.Exception.Message
        $actualOutput = "<no complete stdout: $processError>"
    }

    Write-SessionRecord -TestCase $testCase -InputText $testCase.Inputs -OutputText $actualOutput

    $outputMatches = (Normalize-Transcript $actualOutput) -ceq (Normalize-Transcript $testCase.ExpectedOutput)
    $sessionPassed = $outputMatches -and ($exitCode -eq 0) -and [string]::IsNullOrEmpty($processError)

    if (-not $sessionPassed) {
        [Console]::Out.WriteLine("--- Expected output ---")
        Write-TextBlock -Text $testCase.ExpectedOutput
        [Console]::Out.WriteLine("--- Actual output ---")
        Write-TextBlock -Text $actualOutput
        [Console]::Out.WriteLine("Exit code: $exitCode")

        if (-not [string]::IsNullOrEmpty($processError)) {
            [Console]::Out.WriteLine("Stderr/process error:")
            Write-TextBlock -Text $processError
        }

        [Console]::Out.WriteLine("FAIL: $($testCase.Id). Test session terminated immediately.")
        exit 1
    }

    $passedCount++
    [Console]::Out.WriteLine("PASS: $($testCase.Id)")
}

[Console]::Out.WriteLine("All $passedCount UI test case(s) passed.")
