@echo off
REM Start Zipkin Server - UI at http://localhost:9411
powershell -ExecutionPolicy Bypass -File "%~dp0start-zipkin.ps1"
