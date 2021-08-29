@echo off

rem 接続先情報
set HOST=127.0.0.1
set PORT=5432
set USER_ID=******
set PGPASSWORD=******

rem -------------------------------------------
rem 01_initdb.bat の呼出し
rem -------------------------------------------
call 01_initdb.bat

rem -------------------------------------------
rem 02_initdata.bat の呼出し
rem -------------------------------------------
call 02_initdata.bat

echo normal end.
pause
