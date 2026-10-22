# Storage error sessions

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo replacement
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
bye
```
Output:
```text
     OOPS!!! Could not load data/mybff.txt. Your saved file has not been changed. Check the file and restart.
```

Input:
```text
todo rejected
list
bye
```
Output:
```text
    ____________________________________________________________
     __  ____   ______  ______ ______ 
     |  \/  \ \ / /  _ \|  ____|  ____|
     | \  / |\ V /| |_) | |__  | |__   
     | |\/| | | | |  _ <|  __| |  __|  
     | |  | | |.| | |_) | |    | |     
     |_|  |_| |_| |____/|_|    |_|     

     Hello! I'm MyBff.
     What can I do for you?
    ____________________________________________________________

    ____________________________________________________________
     OOPS!!! Could not save data/mybff.txt. No changes were made. Check the file and try again.
    ____________________________________________________________

    ____________________________________________________________
     Here are the tasks in your list:
    ____________________________________________________________

    ____________________________________________________________
 Bye. Hope to see you again soon!
   ____________________________________________________________
```

## Special-text storage case: timed out

Input:
```text
list
todo pipes | and \ paths
bye
```

Expected: list the completed `original` task, add `pipes | and \ paths`, then print the farewell.

Actual runner output:
```text
subprocess.TimeoutExpired: mybff.MyBff timed out after 10 seconds
```

The runner did not record the application's partial output for this timeout. Later cases were not run.
