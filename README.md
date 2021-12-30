# About
1. OpenGLをしたい
2. C++でやった:[javaboy-github/opengl-by-glfw](github.com/javaboy-github/opengl-by-glfw)
3. セグメンテーション違反！？低水準難しい...
5. そうだ！最近し始めたRustでやってみよう:[javaboy-github/opengl-by-rust](github.com/javaboy-github/opengl-by-rust)
6. 使用人口が少なくて難しい...
7. そうだ！一番得意なJavaでやろう(今ココ)

みたいな感じでこのリポジトリを作りました。色々あって今はscalaを使ってます。

# Run
## Step 1
Javaをインストールしてください。
### unix/linux
sdkmanという有名なツールを使います

```
curl -s "https://get.sdkman.io" | bash 
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java
```
### その他
[公式](https://java.com/ja/download/)から落としてます。

## Step2
gitでリポジトリを落として、ディレクトリに入り、
gradleというツール(rustでいうcargo、jsでいうnpmみたいに依存パッケージのダウンロードやコンパイル等をしてくれる)を使って実行します。このツールは、リポジトリと一緒についてくるので、インストール不要です。
### unix/linux
```
./gradlew run
```
### windows
```
gradlew.bat run
```
