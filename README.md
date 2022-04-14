本仓库主要做了一个静态解析 kt 文件的功能，将注解类中的内容全部解析出来，并生成最终的 json 文件。本仓库是作者需求调研时写的一个
demo，使用的是 [kastree](https://github.com/cretz/kastree) 来实现

运行 [KtParse](src/main/java/KtParse.kt) 会在根目录生成一个 [component.json](component.json) 文件

