# A-Jar: Executable distribution

## Run the release

1. Install Java 25 and check `java --version`.
2. Download `King.jar` from the fork's GitHub release assets.
3. Copy it into an empty folder and open a terminal in that folder.
4. Run:

```text
java -jar "King.jar"
```

King creates `data/king.txt` in that folder after the first task change and
restores it on subsequent runs. Keep the data folder when replacing the JAR
with a newer version. Use `bye` to exit. Double-clicking is not required.

## Build from source

From the repository root, with JDK 25 on PATH, run in PowerShell:

```powershell
.\scripts\build-jar.ps1
```

If a different JDK is on PATH, select the installed JDK 25 explicitly:

```powershell
.\scripts\build-jar.ps1 -JdkPath 'C:\Program Files\Java\jdk-25.0.4'
```

The script checks the tool versions, compiles production sources into a fresh
folder, and sets `Main-Class: king.King` in the manifest. The resulting
`target/King.jar` contains application classes only, with no test classes or
personal task data. All build output is covered by the existing `target/`
Git ignore rule. No external runtime libraries are required.

Run `.\scripts\test-jar.ps1` (with the same optional `-JdkPath`) to verify
the manifest, archive contents, and four launches in a fresh folder. The test
also uses a JAR filename containing spaces and brackets, verifies saving and
deletion across restarts, and leaves its isolated test files under `target/`.

## Publish

Commit source/build instructions only. Tag the completed increment `A-Jar`
and push the tag. Publish a GitHub release with an appropriate version tag
(the first release uses `v0.1`) and attach `target/King.jar` as an asset.
The release version and `A-Jar` should identify the same source commit.
Do not commit the generated JAR.

See the [SE-EDU JAR tutorial](https://se-education.org/guides/tutorials/jar.html)
for background on executable JAR files.
