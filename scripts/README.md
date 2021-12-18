# Scripts
Scripts are made with Babashka command line utility : https://github.com/babashka/babashka#installation

## Installation
```shell
brew install borkdude/brew/babashka
```


## Examples
```shell
ls | bb -i '(filter #(-> % io/file .isDirectory) *input*)'
=> ("elasticsearch" "kibana" "logstash")
```
