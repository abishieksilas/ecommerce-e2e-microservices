# Start Zipkin Server for local distributed tracing
# UI: http://localhost:9411

$ErrorActionPreference = "Stop"
$ZipkinDir = Join-Path $PSScriptRoot "zipkin"
$JarName = "zipkin-server-3.4.3-exec.jar"
$JarPath = Join-Path $ZipkinDir $JarName
$DownloadUrl = "https://repo1.maven.org/maven2/io/zipkin/zipkin-server/3.4.3/zipkin-server-3.4.3-exec.jar"

if (-not (Test-Path $ZipkinDir)) {
    New-Item -ItemType Directory -Path $ZipkinDir | Out-Null
}

if (-not (Test-Path $JarPath)) {
    Write-Host "Downloading Zipkin server..."
    Invoke-WebRequest -Uri $DownloadUrl -OutFile $JarPath
    Write-Host "Downloaded: $JarPath"
}

$java = $env:JAVA_HOME
if ($java) { $java = Join-Path $java "bin\java.exe" }
if (-not $java -or -not (Test-Path $java)) {
    $java = "java"
}

Write-Host "Starting Zipkin on http://localhost:9411 ..."
& $java -jar $JarPath
