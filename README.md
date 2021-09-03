# Name
SpringBoot,SpringSecurityを使用したJWTによるRestAPI認証処理サンプル

# Requiment

```
PostgreSQL:10.15-1
OpenJDK:16.0.1
Eclipse:pleiades-2020-12-java-win-64bit_20201101
```

# Structure

```
Spring Initializer

Project:Gradle Project
Java:16
Spring boot:2.4.2

Group:ccs.education
Artiface:jwt
Name:jwt
Description:Demo project for Spring Security
Package name:ccs.education.jwt
Packaging:Jar

Depedencies:
Spring Web
PostgreSQL Driver
Spring Security
Spring Data JPA
JWT 1.31.1
```

# Usage
設定の流れ

1.テーブルの作成用の設定

    1-1.\login\sql\run.batの接続先情報を環境に合わせて設定する

    1-2.\login\sql\run.batを実行する
    →自動でdbを作成するので[normal end.]が出ていればok

2.eclipseからworkspaceを開く

    2-1.C:\pleiades\eclipse\eclipse.exeを実行する
    →eclipseを起動する

    2-2.workspaceの指定が出るので、\Java_DevAdv2Auth_Login\workspace\を指定する
    →eclipseが起動すること

    ※workspaceの選択が起動時に出ない場合は、eclipseのメニューから
    ファイル→ワークスペースの切り替え→その他で選択を行うこと

3.gradleのインポートを行う

    3-1.ファイル→インポート→Gradle→既存のGradleプロジェクトを選択し次へを押下
    →プロジェクトのインポート画面が開くこと

    3-2.参照からC:\education\03.eclipse_workspaces\Java_DevAdv2Auth_Login\workspace\jwtを選択し完了を押下
    →プロジェクトがインポートされ、パッケージエクスプローラにloginが表示されること

    3-3.application.propertiesの接続先情報を環境に合わせて設定する
    →接続先情報があっていない場合、4-2で起動時に失敗する。

4.プロジェクトのビルド

    4-1.Gradleタスクからlogin→build→bootjarを右クリックしGradleタスクの実行を選択
    →演算命令がすべて青丸になっていること

    4-2.パッケージエクスプローラのlogin上で右クリックし、実行→Spring bootアプリケーションを選択
    コンソールでERRORが出ずにStarted LoginApplicationが出ること

5.初期画面の表示

    5-1.http://localhost:8080/をブラウザのアドレスに入力
    →「Top」が表示されること

# Note

DB,テーブルの構成
```
CREATE DATABASE logindb;
CREATE EXTENSION pgcrypto;
CREATE TABLE account (
id VARCHAR(20) NOT NULL
, password TEXT
, PRIMARY KEY(id)
, UNIQUE (id));

CREATE TABLE token (
id VARCHAR(20) NOT NULL
, refresh_token TEXT
, issue_date_time TIMESTAMPTZ
, PRIMARY KEY(id)
, UNIQUE (id));

```

URLの構成
```
トップ画面(Topと表示されるだけ、制御なし)
http:localhost:8080/

ログインREST API(Post={username,password})
http:localhost:8080/login

Hello Rest API(Get)
http:localhost:8080/api/hello

RefreshToken Rest API(Post={RequestBody:{refreshToken,accessToken}})
http:localhost:8080/api/refreshToken
```

curlでの確認
```
curl -i -X POST "http://localhost:8080/login" -d "username=***" -d "password=***"

curl -H GET "http://localhost:8080/api/hello" -H "Authorization: Bearer ***.***"
```

# Author
# Licence

# Reference
参考にさせて頂いたサイト

[SpringでJWT認証をリフレッシュトークン込みで実装してみた](https://qiita.com/otoiku/items/a8dfddebd56b1177d6df)

[Spring Security & JWT with Spring Boot 2.0で簡単なRest APIを実装する](https://qiita.com/rubytomato@github/items/eb595303430b35f4773d)