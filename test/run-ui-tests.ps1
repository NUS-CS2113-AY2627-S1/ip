$ErrorActionPreference = 'Stop'
$separator = '    ____________________________________________________________'
function Response([string[]]$lines) { return $separator + "`n" + ($lines -join "`n") + "`n" + $separator + "`n`n" }
$unknown = "     OOPS!!! I'm sorry, but I don't know what that means :-("
$cases = @(
    @{ Name='Add a task'; Aim='Accept and list a valid todo'; Inputs=@('todo read book','list'); Lines=@(@("     Got it. I've added this task:",'       [T][ ] read book','     Now you have 1 tasks in the list.'),@('     Here are the tasks in your list:','     1.[T][ ] read book')) },
    @{ Name='Empty todos and unknown commands'; Aim='Reject incorrect inputs without adding tasks'; Inputs=@('todo',('todo' + ' ' * 3),'blah','','marking 1','unmark1','list'); Lines=@(@('     OOPS!!! The description of a todo cannot be empty.'),@('     OOPS!!! The description of a todo cannot be empty.'),@($unknown),@($unknown),@($unknown),@($unknown),@('     Here are the tasks in your list:')) },
    @{ Name='Valid tasks and task numbers'; Aim='Continue after invalid numbers and preserve task behavior'; Inputs=@('deadline homework /by Friday','event lunch /from noon /to evening','mark','mark abc','mark 0','mark 3','mark 9999999999999999','unmark -1','mark 1','unmark 1','list'); Lines=@(@("     Got it. I've added this task:",'       [D][ ] homework (by: Friday)','     Now you have 1 tasks in the list.'),@("     Got it. I've added this task:",'       [E][ ] lunch (from: noon to: evening)','     Now you have 2 tasks in the list.'),@('     Invalid task number.'),@('     Invalid task number.'),@('     Invalid task number.'),@('     Invalid task number.'),@('     Invalid task number.'),@('     Invalid task number.'),@("     Nice! I've marked this task as done:",'       [X] homework'),@("     OK, I've marked this task as not done yet:",'       [ ] homework'),@('     Here are the tasks in your list:','     1.[D][ ] homework (by: Friday)','     2.[E][ ] lunch (from: noon to: evening)')) }
)
. "$PSScriptRoot/mixed-ui-cases.ps1"
$plan = "# UI Test Plan`n`nRun with Java 25. Compare command responses exactly, including separators and blank lines. The unchanged startup banner and greeting are excluded from comparison; full actual sessions are recorded separately. Every session ends with bye and checks the farewell.`n"
$bye = "    ____________________________________________________________`n Bye. Hope to see you again soon!`n   ____________________________________________________________`n"
foreach ($case in $cases) {
    $case.Expected = (($case.Lines | ForEach-Object { Response $_ }) -join '') + $bye
    $case.Inputs += 'bye'
    $plan += "`n## $($case.Name)`n`n**Aim:** $($case.Aim)`n`n**Inputs:**`n`n``````text`n$($case.Inputs -join "`n")`n```````n`n**Expected output (after greeting):**`n`n``````text`n$($case.Expected)```````n"
}
$plan += Get-Content -Raw "$PSScriptRoot/storage-test-plan.md"
Set-Content -LiteralPath test/ui-test-plan.md -Value $plan
New-Item -ItemType Directory -Force -Path build/ui-test | Out-Null
javac -d build/ui-test (Get-ChildItem src/main/java/mybff/*.java).FullName
if ($LASTEXITCODE -ne 0) { throw 'Compilation failed' }
$record = "# UI Test Sessions`n`nJava 25.0.4`n"
foreach ($case in $cases) {
    $classes = (Resolve-Path build/ui-test).Path
    $sessionDirectory = Join-Path (Resolve-Path build/ui-test).Path ([guid]::NewGuid().ToString())
    New-Item -ItemType Directory -Path $sessionDirectory | Out-Null
    Push-Location $sessionDirectory
    try {
        $outputLines = $case.Inputs | java -cp $classes mybff.MyBff
    } finally {
        Pop-Location
    }
    $output = ($outputLines -join "`n") + "`n"
    $marker = "     What can I do for you?`n$separator`n`n"
    $offset = $output.IndexOf($marker)
    $actual = if ($offset -ge 0) { $output.Substring($offset + $marker.Length) } else { $output }
    $passed = $LASTEXITCODE -eq 0 -and $actual -ceq $case.Expected
    $record += "`n## $($case.Name): $passed`n`nInput:`n``````text`n$($case.Inputs -join "`n")`n```````nActual output:`n``````text`n$output```````n"
    Set-Content -LiteralPath test/ui-test-sessions.md -Value $record
    if (-not $passed) { Write-Output $record; Write-Output "EXPECTED: $($case.Expected)"; throw "Failed: $($case.Name)" }
    Write-Output "PASS: $($case.Name)"
}
