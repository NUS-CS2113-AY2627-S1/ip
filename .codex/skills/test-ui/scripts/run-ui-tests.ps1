[CmdletBinding(DefaultParameterSetName = "Plan")]
param(
    [Parameter(ParameterSetName = "Plan")]
    [string]$PlanPath,

    [Parameter(Mandatory = $true, ParameterSetName = "AdHoc")]
    [string]$Name,

    [Parameter(Mandatory = $true, ParameterSetName = "AdHoc")]
    [string]$Aim,

    [Parameter(Mandatory = $true, ParameterSetName = "AdHoc")]
    [string[]]$Commands,

    [Parameter(Mandatory = $true, ParameterSetName = "AdHoc")]
    [string[]]$ExpectedOutputs
)

Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"

function ConvertTo-NormalizedText {
    param(
        [AllowNull()]
        [string]$Text
    )

    if ($null -eq $Text) {
        return ""
    }

    return $Text.Replace("`r`n", "`n").Replace("`r", "`n")
}

function Write-ExpectedBlocks {
    param(
        [string[]]$Blocks
    )

    for ($i = 0; $i -lt $Blocks.Count; $i++) {
        Write-Output ("[" + ($i + 1) + "]")
        Write-Output $Blocks[$i]
    }
}

function Get-JavaVersionText {
    param(
        [string]$ExecutablePath
    )

    $startInfo = New-Object System.Diagnostics.ProcessStartInfo
    $startInfo.FileName = $ExecutablePath
    $startInfo.Arguments = "-version"
    $startInfo.UseShellExecute = $false
    $startInfo.CreateNoWindow = $true
    $startInfo.RedirectStandardOutput = $true
    $startInfo.RedirectStandardError = $true

    $process = New-Object System.Diagnostics.Process
    $process.StartInfo = $startInfo
    [void]$process.Start()
    $standardOutput = $process.StandardOutput.ReadToEnd()
    $standardError = $process.StandardError.ReadToEnd()
    $process.WaitForExit()

    if ($process.ExitCode -ne 0) {
        throw "Version check failed for $ExecutablePath."
    }

    return ($standardOutput + $standardError).Trim()
}

$projectRoot = (Resolve-Path (
        Join-Path $PSScriptRoot "..\..\..\.."
    )).Path
$defaultPlanPath = Join-Path $projectRoot "test\ui-test-plan.md"

if ($PSCmdlet.ParameterSetName -eq "AdHoc") {
    $testCases = @(
        [PSCustomObject]@{
            name = $Name
            aim = $Aim
            commands = $Commands
            expectedOutputs = $ExpectedOutputs
        }
    )
} else {
    if ([string]::IsNullOrWhiteSpace($PlanPath)) {
        $PlanPath = $defaultPlanPath
    }

    $resolvedPlanPath = (Resolve-Path -LiteralPath $PlanPath).Path
    $planContent = Get-Content -Raw -LiteralPath $resolvedPlanPath
    $planPattern =
            '(?s)<!-- TEST-CASES-START -->\s*```json\s*(.*?)\s*```\s*<!-- TEST-CASES-END -->'
    $planMatch = [regex]::Match($planContent, $planPattern)

    if (-not $planMatch.Success) {
        throw "Could not find the JSON test-case block in $resolvedPlanPath."
    }

    $parsedTestCases = $planMatch.Groups[1].Value | ConvertFrom-Json
    $testCases = @(
        foreach ($parsedTestCase in $parsedTestCases) {
            $parsedTestCase
        }
    )
}

if ($testCases.Count -eq 0) {
    throw "No UI test cases were supplied."
}

$javacCommand = Get-Command javac -ErrorAction Stop
$javaCommand = Get-Command java -ErrorAction Stop
$javacVersion = Get-JavaVersionText $javacCommand.Source
$javaVersion = Get-JavaVersionText $javaCommand.Source

if ($javacVersion -notmatch '^javac 25(\.|$)') {
    throw "Java compiler 25 is required, but found: $javacVersion"
}

if ($javaVersion -notmatch 'version "25(\.|")') {
    throw "Java runtime 25 is required, but found: $javaVersion"
}

$tempBase = [IO.Path]::GetFullPath([IO.Path]::GetTempPath())
$tempPrefix = $tempBase
if (-not $tempPrefix.EndsWith(
        [IO.Path]::DirectorySeparatorChar.ToString()
    )) {
    $tempPrefix += [IO.Path]::DirectorySeparatorChar
}

$tempDirectory = $null
$scriptExitCode = 0

try {
    $tempDirectory = Join-Path $tempBase (
        "noah-ui-test-" + [Guid]::NewGuid().ToString("N")
    )
    $classDirectory = Join-Path $tempDirectory "classes"
    [IO.Directory]::CreateDirectory($classDirectory) | Out-Null

    $sourceDirectory = Join-Path $projectRoot "src\main\java"
    $sourceFiles = @(
        Get-ChildItem -LiteralPath $sourceDirectory -Filter "*.java" -File -Recurse |
            Sort-Object Name |
            ForEach-Object { $_.FullName }
    )

    if ($sourceFiles.Count -eq 0) {
        throw "No Java source files were found in $sourceDirectory."
    }

    $compileOutput = @(
        & $javacCommand.Source -d $classDirectory $sourceFiles 2>&1
    )
    if ($LASTEXITCODE -ne 0) {
        Write-Output "=== COMPILATION FAILED ==="
        $compileOutput | ForEach-Object { Write-Output $_ }
        throw "The UI test run stopped because compilation failed."
    }

    Write-Output "Java compiler: $javacVersion"
    Write-Output "Java runtime: $($javaVersion.Split([Environment]::NewLine)[0])"
    Write-Output ""

    $testCaseNumber = 0
    foreach ($testCase in $testCases) {
        $testCaseNumber++
        $caseCommands = @($testCase.commands)
        $caseExpectedOutputs = @($testCase.expectedOutputs)

        if ($caseCommands.Count -eq 0) {
            throw "Test '$($testCase.name)' has no commands."
        }

        if ($caseCommands.Count -ne $caseExpectedOutputs.Count) {
            throw (
                "Test '$($testCase.name)' must have one expected output " +
                "block per command."
            )
        }

        if ($caseCommands[-1] -ne "bye") {
            throw "Test '$($testCase.name)' must end with the bye command."
        }

        $inputText = (
            $caseCommands -join [Environment]::NewLine
        ) + [Environment]::NewLine
        $caseDirectory = Join-Path $tempDirectory (
            "case-" + $testCaseNumber
        )
        [IO.Directory]::CreateDirectory($caseDirectory) | Out-Null

        Push-Location -LiteralPath $caseDirectory
        try {
            $runOutput = @(
                $inputText |
                    & $javaCommand.Source -cp $classDirectory noah.Noah 2>&1
            )
            $runExitCode = $LASTEXITCODE
        } finally {
            Pop-Location
        }
        $actualOutput = (
            $runOutput | ForEach-Object { $_.ToString() }
        ) -join "`n"

        Write-Output "============================================================"
        Write-Output "TEST: $($testCase.name)"
        Write-Output "AIM:  $($testCase.aim)"
        Write-Output "--- Console input ---"
        foreach ($command in $caseCommands) {
            Write-Output ("> " + $command)
        }
        Write-Output "--- Console output ---"
        Write-Output $actualOutput

        if ($runExitCode -ne 0) {
            Write-Output "--- Expected output blocks ---"
            Write-ExpectedBlocks -Blocks $caseExpectedOutputs
            throw (
                "Test '$($testCase.name)' failed with process exit code " +
                "$runExitCode."
            )
        }

        $normalizedActual = ConvertTo-NormalizedText $actualOutput
        $searchStart = 0

        for ($i = 0; $i -lt $caseExpectedOutputs.Count; $i++) {
            $normalizedExpected = ConvertTo-NormalizedText (
                [string]$caseExpectedOutputs[$i]
            )

            if ($normalizedExpected.Length -eq 0) {
                throw (
                    "Test '$($testCase.name)' has an empty expected output " +
                    "for command '$($caseCommands[$i])'."
                )
            }

            $matchIndex = $normalizedActual.IndexOf(
                $normalizedExpected,
                $searchStart,
                [StringComparison]::Ordinal
            )

            if ($matchIndex -lt 0) {
                Write-Output "=== TEST FAILED ==="
                Write-Output "Command:  $($caseCommands[$i])"
                Write-Output "--- Expected output ---"
                Write-Output $normalizedExpected
                Write-Output "--- Actual full output ---"
                Write-Output $normalizedActual
                throw (
                    "Test '$($testCase.name)' stopped at its first " +
                    "mismatched command."
                )
            }

            $searchStart = $matchIndex + $normalizedExpected.Length
        }

        Write-Output "RESULT: PASS"
        Write-Output ""
    }

    Write-Output "All $($testCases.Count) UI test cases passed."
} catch {
    Write-Output ""
    Write-Output ("UI test run stopped: " + $_.Exception.Message)
    $scriptExitCode = 1
} finally {
    if ($null -ne $tempDirectory -and
            (Test-Path -LiteralPath $tempDirectory)) {
        $fullTempDirectory = [IO.Path]::GetFullPath($tempDirectory)
        $isWithinTemp = $fullTempDirectory.StartsWith(
            $tempPrefix,
            [StringComparison]::OrdinalIgnoreCase
        )
        $hasSafeName = [IO.Path]::GetFileName(
            $fullTempDirectory
        ).StartsWith(
            "noah-ui-test-",
            [StringComparison]::OrdinalIgnoreCase
        )

        if ($isWithinTemp -and $hasSafeName) {
            Remove-Item -LiteralPath $fullTempDirectory -Recurse -Force
        } else {
            Write-Warning (
                "Refused to remove unexpected temporary path: " +
                $fullTempDirectory
            )
        }
    }
}

exit $scriptExitCode
