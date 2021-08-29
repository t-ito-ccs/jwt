@echo off
 
REM PostgreSQLのbinディレクトリ
set PGPATH=C:\Program Files\PostgreSQL\10\bin\
set DB_NAME=logindb

REM スクリプト実行
"%PGPATH%psql" -h %HOST% -p %PORT% -U %USER_ID% -d %DB_NAME% -f 02_data_init.sql