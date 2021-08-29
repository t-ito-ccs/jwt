@echo off
 
REM PostgreSQLのbinディレクトリ
set PGPATH=C:\Program Files\PostgreSQL\10\bin\
set DB_NAME=postgres

REM スクリプト実行
"%PGPATH%psql" -h %HOST% -p %PORT% -U %USER_ID% -d %DB_NAME% -f 01_db_create.sql