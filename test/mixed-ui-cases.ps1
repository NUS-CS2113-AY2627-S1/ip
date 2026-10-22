# Each step declares its expected response independently of the chatbot.
$emptyTodo = '     OOPS!!! The description of a todo cannot be empty.'
$listHeader = '     Here are the tasks in your list:'
$invalidNumber = '     Invalid task number.'
$mixedCases = @(
    @{
        Name = 'Find tasks by description'
        Aim = 'Match case-sensitive substrings across task types, preserve order and status, and handle empty searches'
        Steps = @(
            @{ Input='find book'; Output=@('     Here are the matching tasks in your list:') }
            @{ Input='todo read book'; Output=@("     Got it. I've added this task:", '       [T][ ] read book', '     Now you have 1 tasks in the list.') }
            @{ Input='todo unrelated'; Output=@("     Got it. I've added this task:", '       [T][ ] unrelated', '     Now you have 2 tasks in the list.') }
            @{ Input='deadline return book /by June 6th'; Output=@("     Got it. I've added this task:", '       [D][ ] return book (by: June 6th)', '     Now you have 3 tasks in the list.') }
            @{ Input='event book club /from noon /to evening'; Output=@("     Got it. I've added this task:", '       [E][ ] book club (from: noon to: evening)', '     Now you have 4 tasks in the list.') }
            @{ Input='mark 1'; Output=@("     Nice! I've marked this task as done:", '       [X] read book') }
            @{ Input="  find`t book  "; Output=@('     Here are the matching tasks in your list:', '     1.[T][X] read book', '     2.[D][ ] return book (by: June 6th)', '     3.[E][ ] book club (from: noon to: evening)') }
            @{ Input='find ook'; Output=@('     Here are the matching tasks in your list:', '     1.[T][X] read book', '     2.[D][ ] return book (by: June 6th)', '     3.[E][ ] book club (from: noon to: evening)') }
            @{ Input='find return book'; Output=@('     Here are the matching tasks in your list:', '     1.[D][ ] return book (by: June 6th)') }
            @{ Input='find Book'; Output=@('     Here are the matching tasks in your list:') }
            @{ Input='find June'; Output=@('     Here are the matching tasks in your list:') }
            @{ Input='find missing'; Output=@('     Here are the matching tasks in your list:') }
            @{ Input='find'; Output=@('     OOPS!!! The keyword for find cannot be empty.') }
            @{ Input="find `t "; Output=@('     OOPS!!! The keyword for find cannot be empty.') }
            @{ Input='finder book'; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] read book', '     2.[T][ ] unrelated', '     3.[D][ ] return book (by: June 6th)', '     4.[E][ ] book club (from: noon to: evening)') }
        )
    },
    @{
        Name = 'Errors between additions and completion changes'
        Aim = 'Check counts, ordering and completion flags immediately after rejected inputs'
        Steps = @(
            @{ Input='list'; Output=@($listHeader) }
            @{ Input='todo'; Output=@($emptyTodo) }
            @{ Input='todo first'; Output=@("     Got it. I've added this task:", '       [T][ ] first', '     Now you have 1 tasks in the list.') }
            @{ Input='blah'; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][ ] first') }
            @{ Input='mark 1'; Output=@("     Nice! I've marked this task as done:", '       [X] first') }
            @{ Input='todo'; Output=@($emptyTodo) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] first') }
            @{ Input='todo second'; Output=@("     Got it. I've added this task:", '       [T][ ] second', '     Now you have 2 tasks in the list.') }
            @{ Input='blah second'; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] first', '     2.[T][ ] second') }
            @{ Input='unmark 1'; Output=@("     OK, I've marked this task as not done yet:", '       [ ] first') }
            @{ Input=''; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][ ] first', '     2.[T][ ] second') }
        )
    },
    @{
        Name = 'Whitespace and command boundaries'
        Aim = 'Reject blank and misspelled commands while preserving descriptions and accepting whitespace around valid todos'
        Steps = @(
            @{ Input=" `t "; Output=@($unknown) }
            @{ Input="  todo`tread book  "; Output=@("     Got it. I've added this task:", '       [T][ ] read book', '     Now you have 1 tasks in the list.') }
            @{ Input="todo`t  "; Output=@($emptyTodo) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][ ] read book') }
            @{ Input='todoish read'; Output=@($unknown) }
            @{ Input='mark 1'; Output=@("     Nice! I've marked this task as done:", '       [X] read book') }
            @{ Input='TODO read'; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] read book') }
            @{ Input='todo bye /by Friday'; Output=@("     Got it. I've added this task:", '       [T][ ] bye /by Friday', '     Now you have 2 tasks in the list.') }
            @{ Input='bye now'; Output=@($unknown) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] read book', '     2.[T][ ] bye /by Friday') }
            @{ Input='list extra'; Output=@($unknown) }
            @{ Input='todo read book'; Output=@("     Got it. I've added this task:", '       [T][ ] read book', '     Now you have 3 tasks in the list.') }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] read book', '     2.[T][ ] bye /by Friday', '     3.[T][ ] read book') }
        )
    },
    @{
        Name = 'Invalid indexes between valid status updates'
        Aim = 'Verify missing, noninteger, out-of-range and overflowing indexes cannot change existing task flags'
        Steps = @(
            @{ Input='mark 1'; Output=@($invalidNumber) }
            @{ Input='todo first'; Output=@("     Got it. I've added this task:", '       [T][ ] first', '     Now you have 1 tasks in the list.') }
            @{ Input='unmark'; Output=@($invalidNumber) }
            @{ Input='todo second'; Output=@("     Got it. I've added this task:", '       [T][ ] second', '     Now you have 2 tasks in the list.') }
            @{ Input='mark 1 2'; Output=@($invalidNumber) }
            @{ Input='mark 2'; Output=@("     Nice! I've marked this task as done:", '       [X] second') }
            @{ Input='unmark 0'; Output=@($invalidNumber) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][ ] first', '     2.[T][X] second') }
            @{ Input='mark 3'; Output=@($invalidNumber) }
            @{ Input='mark 1'; Output=@("     Nice! I've marked this task as done:", '       [X] first') }
            @{ Input='unmark -2147483648'; Output=@($invalidNumber) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] first', '     2.[T][X] second') }
            @{ Input='unmark 2147483648'; Output=@($invalidNumber) }
            @{ Input='unmark 2'; Output=@("     OK, I've marked this task as not done yet:", '       [ ] second') }
            @{ Input='mark 1.5'; Output=@($invalidNumber) }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] first', '     2.[T][ ] second') }
            @{ Input='unmark abc'; Output=@($invalidNumber) }
            @{ Input='unmark 2'; Output=@("     OK, I've marked this task as not done yet:", '       [ ] second') }
            @{ Input='list'; Output=@($listHeader, '     1.[T][X] first', '     2.[T][ ] second') }
        )
    }
)
$mixedCases += @{
    Name = 'Delete tasks and reuse list positions'
    Aim = 'Delete middle, first and last tasks, preserve status and order, and add after deletion'
    Steps = @(
        @{ Input='todo first'; Output=@("     Got it. I've added this task:", '       [T][ ] first', '     Now you have 1 tasks in the list.') }
        @{ Input='event project meeting /from Aug 6th 2pm /to 4pm'; Output=@("     Got it. I've added this task:", '       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)', '     Now you have 2 tasks in the list.') }
        @{ Input='deadline last /by Friday'; Output=@("     Got it. I've added this task:", '       [D][ ] last (by: Friday)', '     Now you have 3 tasks in the list.') }
        @{ Input='mark 3'; Output=@("     Nice! I've marked this task as done:", '       [X] last') }
        @{ Input='delete 2'; Output=@("     Noted. I've removed this task:", '       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)', '     Now you have 2 tasks in the list.') }
        @{ Input='list'; Output=@($listHeader, '     1.[T][ ] first', '     2.[D][X] last (by: Friday)') }
        @{ Input='delete 1'; Output=@("     Noted. I've removed this task:", '       [T][ ] first', '     Now you have 1 tasks in the list.') }
        @{ Input="  delete`t1  "; Output=@("     Noted. I've removed this task:", '       [D][X] last (by: Friday)', '     Now you have 0 tasks in the list.') }
        @{ Input='list'; Output=@($listHeader) }
        @{ Input='delete 1'; Output=@($invalidNumber) }
        @{ Input='todo new'; Output=@("     Got it. I've added this task:", '       [T][ ] new', '     Now you have 1 tasks in the list.') }
        @{ Input='delete'; Output=@($invalidNumber) }
        @{ Input='delete abc'; Output=@($invalidNumber) }
        @{ Input='delete 0'; Output=@($invalidNumber) }
        @{ Input='delete -1'; Output=@($invalidNumber) }
        @{ Input='delete 2'; Output=@($invalidNumber) }
        @{ Input='delete 2147483648'; Output=@($invalidNumber) }
        @{ Input='delete -2147483648'; Output=@($invalidNumber) }
        @{ Input='delete 1 2'; Output=@($invalidNumber) }
        @{ Input='delete1'; Output=@($unknown) }
        @{ Input='list'; Output=@($listHeader, '     1.[T][ ] new') }
    )
}
$growthSteps = @(1..101 | ForEach-Object {
    @{ Input="todo task $_"; Output=@("     Got it. I've added this task:", "       [T][ ] task $_", "     Now you have $_ tasks in the list.") }
})
$growthSteps += @(
    @{ Input='delete 100'; Output=@("     Noted. I've removed this task:", '       [T][ ] task 100', '     Now you have 100 tasks in the list.') }
    @{ Input='mark 100'; Output=@("     Nice! I've marked this task as done:", '       [X] task 101') }
    @{ Input='todo replacement'; Output=@("     Got it. I've added this task:", '       [T][ ] replacement', '     Now you have 101 tasks in the list.') }
    @{ Input='list'; Output=@($listHeader) + @(1..99 | ForEach-Object { "     $_.[T][ ] task $_" }) + @('     100.[T][X] task 101', '     101.[T][ ] replacement') }
)
$mixedCases += @{
    Name = 'Collection grows beyond 100 tasks'
    Aim = 'Store more than 100 tasks and preserve indexes and order after deletion and addition'
    Steps = $growthSteps
}
foreach ($case in $mixedCases) {
    $cases += @{
        Name = $case.Name
        Aim = $case.Aim
        Inputs = @($case.Steps | ForEach-Object { $_.Input })
        Lines = @($case.Steps | ForEach-Object { ,$_.Output })
    }
}
