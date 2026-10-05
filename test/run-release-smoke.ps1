#requires -Version 7.0

param(
    [Parameter(Mandatory = $true)]
    [string]$JarPath,

    [string]$JavaExecutable = 'java',

    [string]$TranscriptPath = (Join-Path $PSScriptRoot 'ui-test-session.txt')
)

$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$runnerPath = Join-Path $projectRoot '.codex/skills/test-ui/scripts/run_ui_tests.ps1'
$separator = '    ____________________________________________________________'
$originalLocation = Get-Location
$originalProcessDirectory = [Environment]::CurrentDirectory
$runId = [Guid]::NewGuid().ToString('N')
$testDirectory = Join-Path $projectRoot "_temp/release smoke $runId"
$recordsDirectory = Join-Path $projectRoot "_temp/release smoke records $runId"
$recordPaths = @()
$record = [System.Text.StringBuilder]::new()
$failure = $null
$status = 'FAIL'
$transcriptFile = [IO.Path]::GetFullPath($TranscriptPath)

function Get-Response {
    param([string[]]$Lines)
    return (@($separator) + $Lines + @($separator)) -join [Environment]::NewLine
}

function Get-ListResponse {
    param(
        [string[]]$Rows = @(),
        [switch]$IsSearch
    )
    $heading = if ($IsSearch) {
        '     Here are the matching tasks in your list:'
    } else {
        '     Here are the tasks in your list:'
    }
    $lines = @($heading) + @($Rows | ForEach-Object { '     ' + $_ })
    return Get-Response -Lines $lines
}

function Get-AddedResponse {
    param([string]$Task, [int]$Count)
    $taskWord = if ($Count -eq 1) { 'task' } else { 'tasks' }
    return Get-Response -Lines @(
        "     Got it. I've added this task:"
        "       $Task"
        "     Now you have $Count $taskWord in the list."
    )
}

function Get-ErrorResponse {
    param([string]$Message)
    return Get-Response -Lines @('     OOPS!!! ' + $Message)
}

try {
    [void]$record.AppendLine('=== Release JAR smoke test ===')
    $jarFile = (Resolve-Path -LiteralPath $JarPath).ProviderPath
    $jarName = Split-Path -Leaf $jarFile
    $javaPath = (Get-Command -Name $JavaExecutable -CommandType Application | Select-Object -First 1).Source
    $runtime = @(& $javaPath --version 2>&1 | ForEach-Object { $_.ToString() })
    if ($LASTEXITCODE -ne 0 -or $runtime.Count -eq 0 -or $runtime[0] -notmatch '\b25(?:\.|\s|$)') {
        throw 'The release smoke test requires Java 25. Check java --version or supply -JavaExecutable.'
    }

    [void]$record.AppendLine("JAR: $jarName")
    [void]$record.AppendLine('SHA-256: ' + (Get-FileHash -LiteralPath $jarFile -Algorithm SHA256).Hash.ToLowerInvariant())
    [void]$record.AppendLine('Runtime: ' + ($runtime -join ' | '))
    [void]$record.AppendLine('Operating system: ' + [Runtime.InteropServices.RuntimeInformation]::OSDescription)
    [void]$record.AppendLine("Launch: java -jar `"$jarName`"")
    [void]$record.AppendLine('Initial folder: A fresh folder containing only the supplied JAR; its name contains spaces.')

    New-Item -ItemType Directory -Path $testDirectory, $recordsDirectory -Force | Out-Null
    Copy-Item -LiteralPath $jarFile -Destination (Join-Path $testDirectory $jarName)
    Set-Location -LiteralPath $testDirectory
    # The existing runner uses ProcessStartInfo, which inherits this directory.
    [Environment]::CurrentDirectory = $testDirectory

    $initialRows = @(
        '1.[T][ ] borrow book'
        '2.[D][ ] return book (by: Oct 15 2026)'
        '3.[E][ ] project meeting (from: Mon 2pm to: 4pm)'
        '4.[T][ ] notebook'
        '5.[T][ ] Book launch'
        '6.[T][ ] café 読書'
        '7.[D][ ] leap day (by: Feb 29 2024)'
    )
    $savedRows = @(
        '1.[T][ ] borrow book'
        '2.[D][ ] return book (by: Oct 15 2026)'
        '3.[E][ ] project meeting (from: Mon 2pm to: 4pm)'
        '4.[T][ ] Book launch'
        '5.[T][X] café 読書'
        '6.[D][ ] leap day (by: Feb 29 2024)'
    )
    $bookRows = @(
        '1.[T][ ] borrow book'
        '2.[D][X] return book (by: Oct 15 2026)'
        '3.[T][ ] notebook'
    )
    $savedBookRows = @(
        '1.[T][ ] borrow book'
        '2.[D][ ] return book (by: Oct 15 2026)'
    )
    $byeResponse = Get-Response -Lines @('     Bye. Hope to see you again soon!')
    $cases = @(
        @{ Command = 'list'; Expected = (Get-ListResponse) }
        @{ Command = 'find book'; Expected = (Get-ListResponse -IsSearch) }
        @{ Command = 'todo borrow book'; Expected = (Get-AddedResponse '[T][ ] borrow book' 1) }
        @{ Command = 'deadline return book /by 2026-10-15'; Expected = (Get-AddedResponse '[D][ ] return book (by: Oct 15 2026)' 2) }
        @{ Command = 'event project meeting /from Mon 2pm /to 4pm'; Expected = (Get-AddedResponse '[E][ ] project meeting (from: Mon 2pm to: 4pm)' 3) }
        @{ Command = 'todo notebook'; Expected = (Get-AddedResponse '[T][ ] notebook' 4) }
        @{ Command = 'todo Book launch'; Expected = (Get-AddedResponse '[T][ ] Book launch' 5) }
        @{ Command = 'todo café 読書'; Expected = (Get-AddedResponse '[T][ ] café 読書' 6) }
        @{ Command = 'deadline leap day /by 2024-02-29'; Expected = (Get-AddedResponse '[D][ ] leap day (by: Feb 29 2024)' 7) }
        @{ Command = 'deadline invalid date /by 2026-02-30'; Expected = (Get-ErrorResponse 'Please provide a valid deadline date in yyyy-MM-dd format.') }
        @{ Command = 'list'; Expected = (Get-ListResponse -Rows $initialRows) }
        @{ Command = 'mark 2'; Expected = (Get-Response -Lines @("     Nice! I've marked this task as done:", '       [D][X] return book (by: Oct 15 2026)')) }
        @{ Command = 'find book'; Expected = (Get-ListResponse -Rows $bookRows -IsSearch) }
        @{ Command = 'find Book'; Expected = (Get-ListResponse -Rows @('1.[T][ ] Book launch') -IsSearch) }
        @{ Command = 'find return book'; Expected = (Get-ListResponse -Rows @('1.[D][X] return book (by: Oct 15 2026)') -IsSearch) }
        @{ Command = 'find 読書'; Expected = (Get-ListResponse -Rows @('1.[T][ ] café 読書') -IsSearch) }
        @{ Command = 'find Mon'; Expected = (Get-ListResponse -IsSearch) }
        @{ Command = 'find Oct'; Expected = (Get-ListResponse -IsSearch) }
        @{ Command = 'find missing'; Expected = (Get-ListResponse -IsSearch) }
        @{ Command = 'find'; Expected = (Get-ErrorResponse 'The keyword for find cannot be empty.') }
        @{ Command = 'unmark 2'; Expected = (Get-Response -Lines @("     OK, I've marked this task as not done yet:", '       [D][ ] return book (by: Oct 15 2026)')) }
        @{ Command = 'mark 6'; Expected = (Get-Response -Lines @("     Nice! I've marked this task as done:", '       [T][X] café 読書')) }
        @{ Command = 'mark abc'; Expected = (Get-ErrorResponse 'Please provide a valid task number.') }
        @{ Command = 'mark 99'; Expected = (Get-ErrorResponse 'That task number is not in the list.') }
        @{ Command = 'delete abc'; Expected = (Get-ErrorResponse 'Please provide a valid task number.') }
        @{ Command = 'delete 99'; Expected = (Get-ErrorResponse 'That task number is not in the list.') }
        @{ Command = 'delete 4'; Expected = (Get-Response -Lines @("     Noted. I've removed this task:", '       [T][ ] notebook', '     Now you have 6 tasks in the list.')) }
        @{ Command = 'list'; Expected = (Get-ListResponse -Rows $savedRows) }
        @{ Command = 'find book'; Expected = (Get-ListResponse -Rows $savedBookRows -IsSearch) }
        @{ Command = 'todo'; Expected = (Get-ErrorResponse 'The description of a todo cannot be empty.') }
        @{ Command = 'deadline missing date'; Expected = (Get-ErrorResponse 'Use: deadline DESCRIPTION /by yyyy-MM-dd') }
        @{ Command = 'event missing end /from Monday'; Expected = (Get-ErrorResponse 'Use: event DESCRIPTION /from START /to END') }
        @{ Command = 'unknown'; Expected = (Get-ErrorResponse "I'm sorry, but I don't know what that means :-(") }
        @{ Command = 'bye'; Expected = $byeResponse }
    )
    $firstRecord = Join-Path $recordsDirectory 'commands.txt'
    $recordPaths += $firstRecord
    $runnerArguments = @{
        Executable = $javaPath
        ArgumentList = @('-jar', $jarName)
        Commands = @($cases | ForEach-Object { $_.Command })
        ExpectedOutputs = @($cases | ForEach-Object { $_.Expected })
        EndMarker = $separator
        TranscriptPath = $firstRecord
    }
    & $runnerPath @runnerArguments | Where-Object { $_ -notlike 'Transcript:*' }
    if (-not (Test-Path -LiteralPath 'data/ted.txt' -PathType Leaf)) {
        throw 'The JAR did not create data/ted.txt in its launch folder.'
    }

    $restartCases = @(
        @{ Command = 'list'; Expected = (Get-ListResponse -Rows $savedRows) }
        @{ Command = 'find book'; Expected = (Get-ListResponse -Rows $savedBookRows -IsSearch) }
        @{ Command = 'find Book'; Expected = (Get-ListResponse -Rows @('1.[T][ ] Book launch') -IsSearch) }
        @{ Command = 'find 読書'; Expected = (Get-ListResponse -Rows @('1.[T][X] café 読書') -IsSearch) }
        @{ Command = 'bye'; Expected = $byeResponse }
    )
    $restartRecord = Join-Path $recordsDirectory 'restart.txt'
    $recordPaths += $restartRecord
    $runnerArguments.Commands = @($restartCases | ForEach-Object { $_.Command })
    $runnerArguments.ExpectedOutputs = @($restartCases | ForEach-Object { $_.Expected })
    $runnerArguments.TranscriptPath = $restartRecord
    & $runnerPath @runnerArguments | Where-Object { $_ -notlike 'Transcript:*' }
    $status = 'PASS'
    [void]$record.AppendLine('Commands checked: ' + ($cases.Count + $restartCases.Count))
} catch {
    $failure = $_
} finally {
    Set-Location -LiteralPath $originalLocation
    [Environment]::CurrentDirectory = $originalProcessDirectory
    [void]$record.AppendLine("Status: $status")
    foreach ($recordPath in $recordPaths) {
        if (Test-Path -LiteralPath $recordPath) {
            [void]$record.AppendLine()
            $sessionName = if ((Split-Path -Leaf $recordPath) -eq 'commands.txt') {
                '=== Commands and saving ==='
            } else {
                '=== Restart with saved tasks ==='
            }
            [void]$record.AppendLine($sessionName)
            [void]$record.AppendLine((Get-Content -LiteralPath $recordPath -Raw))
        }
    }
    if ($failure) {
        [void]$record.AppendLine('=== Failure: expected and actual output ===')
        [void]$record.AppendLine($failure.Exception.Message)
    }
    $recordText = $record.ToString().Replace($testDirectory, '[test folder]').Replace($projectRoot, '[project]')
    $transcriptDirectory = Split-Path -Parent $transcriptFile
    New-Item -ItemType Directory -Path $transcriptDirectory -Force | Out-Null
    Set-Content -LiteralPath $transcriptFile -Value ($recordText.TrimEnd()) -Encoding utf8
}

if ($failure) {
    throw $failure
}
Write-Output 'Release JAR smoke test passed: 39 commands, including persistence after restart.'
