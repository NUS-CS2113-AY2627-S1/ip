param(
    [Parameter(Mandatory = $true)]
    [string]$Executable,

    [string[]]$ArgumentList = @(),

    [Parameter(Mandatory = $true)]
    [string[]]$Commands,

    [Parameter(Mandatory = $true)]
    [string[]]$ExpectedOutputs,

    [Parameter(Mandatory = $true)]
    [string]$EndMarker,

    [int]$MarkersPerCommand = 2,

    [string]$TranscriptPath = 'test/ui-test-session.txt'
)

if ($Commands.Count -ne $ExpectedOutputs.Count) {
    throw 'Commands and expected outputs must contain the same number of items.'
}

if ($MarkersPerCommand -lt 1) {
    throw 'MarkersPerCommand must be at least 1.'
}

$transcriptDirectory = Split-Path -Parent $TranscriptPath
if ($transcriptDirectory -and -not (Test-Path $transcriptDirectory)) {
    New-Item -ItemType Directory -Path $transcriptDirectory -Force | Out-Null
}

$startInfo = [System.Diagnostics.ProcessStartInfo]::new()
$startInfo.FileName = $Executable
$startInfo.UseShellExecute = $false
$startInfo.CreateNoWindow = $true
$startInfo.RedirectStandardInput = $true
$startInfo.RedirectStandardOutput = $true
$startInfo.RedirectStandardError = $true
$pipeEncoding = [System.Text.UTF8Encoding]::new($false)
$startInfo.StandardInputEncoding = $pipeEncoding
$startInfo.StandardOutputEncoding = $pipeEncoding
$startInfo.StandardErrorEncoding = $pipeEncoding
foreach ($argument in $ArgumentList) {
    [void]$startInfo.ArgumentList.Add($argument)
}

$process = [System.Diagnostics.Process]::new()
$process.StartInfo = $startInfo
$transcript = [System.Text.StringBuilder]::new()
$markerCount = 0

function Save-Transcript {
    param([string]$Path, [System.Text.StringBuilder]$Content)
    $Content.ToString() | Set-Content -Path $Path -Encoding utf8
}

try {
    if (-not $process.Start()) {
        throw "Could not start '$Executable'."
    }

    [void]$transcript.AppendLine('=== UI test session ===')
    for ($i = 0; $i -lt $Commands.Count; $i++) {
        $command = $Commands[$i]
        $expected = $ExpectedOutputs[$i]
        $targetMarkerCount = $markerCount + $MarkersPerCommand
        [void]$transcript.AppendLine('$ ' + $command)
        $process.StandardInput.WriteLine($command)
        $process.StandardInput.Flush()

        $response = [System.Text.StringBuilder]::new()
        while ($markerCount -lt $targetMarkerCount) {
            $readTask = $process.StandardOutput.ReadLineAsync()
            if (-not $readTask.Wait(5000)) {
                throw "Timed out waiting for the response to '$command'."
            }
            $line = $readTask.Result
            if ($null -eq $line) {
                break
            }
            [void]$response.AppendLine($line)
            [void]$transcript.AppendLine($line)
            if ($line -eq $EndMarker) {
                $markerCount++
            }
        }

        $responseText = $response.ToString()
        if (-not $responseText.Contains($expected)) {
            Save-Transcript $TranscriptPath $transcript
            $failureMessage = "UI test failed for command '$command'. Expected '$expected'. Actual response:"
            throw $failureMessage + [Environment]::NewLine + $responseText
        }

        [void]$transcript.AppendLine("PASS: $expected")
        [void]$transcript.AppendLine()
    }

    if (-not $process.HasExited) {
        $process.StandardInput.Close()
        [void]$process.WaitForExit(5000)
    }

    $errorText = $process.StandardError.ReadToEnd()
    if ($errorText) {
        [void]$transcript.AppendLine('=== stderr ===')
        [void]$transcript.AppendLine($errorText)
    }

    Save-Transcript $TranscriptPath $transcript
    Write-Output "UI tests passed: $($Commands.Count) commands."
    Write-Output "Transcript: $TranscriptPath"
} catch {
    if (-not $process.HasExited) {
        $process.Kill()
        [void]$process.WaitForExit()
    }
    Save-Transcript $TranscriptPath $transcript
    throw
} finally {
    $process.Dispose()
}
