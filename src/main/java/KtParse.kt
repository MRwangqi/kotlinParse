import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.codelang.annotation.Component
import kastree.ast.Node
import kastree.ast.Visitor
import kastree.ast.psi.Parser
import java.io.File

/**
 * https://github.com/cretz/kastree
 */
fun main(args: Array<String>) {

    val targetAnnotation = Component::class.java.simpleName

    val targetDirectory = File("src/main")
    val list = arrayListOf<File>()
    // todo dfs 遍历 file
    dfsFile(targetDirectory, list)

    val jsonArray = JSONArray()
    // 只解析 kt 文件
    list.filter { it.name.endsWith(".kt") }
        .forEach {
            println(it.absolutePath)
            val ktCode = it.readText()
            parseKotlinFile(jsonArray, ktCode, targetAnnotation)
        }

}

private fun dfsFile(file: File, list: ArrayList<File>) {
    if (file.isDirectory) {
        file.listFiles().forEach {
            dfsFile(it, list)
        }
    } else {
        list.add(file)
    }
}


private fun parseKotlinFile(jsonArray: JSONArray, ktCode: String, targetAnnotation: String) {
    val file = Parser.parseFile(ktCode, false)
    var packageName: String = ""
    Visitor.visit(file) continuation@{ v, _ ->
        // todo Package 会优先被解析
        if (v is Node.Package) {
            packageName = v.names.joinToString(".") + "."
        }

        if (v is Node.Decl.Structured) {
            val anns: Node.Modifier.AnnotationSet.Annotation
            try {
                anns = (v.mods[0] as Node.Modifier.AnnotationSet).anns[0]
            } catch (e: Exception) {
                // todo 说明该类不符合我们的解析，直接返回不处理
                return@continuation
            }

            val annotation = anns.names[0]
            // todo 非目标注解不处理
            if (annotation != targetAnnotation) {
                return@continuation
            }

            val jsonObject = JSONObject()
            jsonArray.add(jsonObject)
            jsonObject.put("className", packageName + v.name)

            anns.args.forEach { node ->
                val expr = node.expr

                println("expr--------->$expr")

                if (expr is Node.Expr.StringTmpl) {
                    val elems = expr.elems[0]
                    if (elems is Node.Expr.StringTmpl.Elem.Regular) {
                        println("key=" + node.name + " value=" + elems.str)
                        jsonObject.put(node.name, elems.str)
                    }
                } else if (expr is Node.Expr.CollLit) {
                    val list = expr.exprs.map {
                        val exprs = it as Node.Expr.StringTmpl
                        val elems = exprs.elems[0]
                        val rg = elems as Node.Expr.StringTmpl.Elem.Regular
                        rg.str
                    }.toList()
                    println("key=" + node.name + " value=" + list)
                    jsonObject.put(node.name, list)
                } else if (expr is Node.Expr.Brace) {
                    // 解析 java 文件时会走 Brace ，但解析 java 会有点问题，当注解中的 value 有 array 时，只会取数组的第一个值
//                    expr.block?.stmts?.map {
//                        it as?  Node.Stmt.Expr
//                    }?.map {
//                        it?.expr as? Node.Expr.StringTmpl
//                    }
                }
            }

            val file = File("component.json")
            if (file.exists()) {
                file.delete()
            }

            val root = JSONObject()
            root["components"] = jsonArray

            file.writeText(root.toJSONString())

            println("生成目标 json=" + file.absolutePath)
        }
    }
}

// node 示例
//Structured(mods=[AnnotationSet(target=null, anns=[Annotation(names=[com.codelang.annotation.Component], typeArgs=[], args=[ValueArg(name=name, asterisk=false, expr=StringTmpl(elems=[Regular(str=zhangsan)], raw=false)), ValueArg(name=version, asterisk=false, expr=StringTmpl(elems=[Regular(str=1.0.0)], raw=false)), ValueArg(name=dependency, asterisk=false, expr=StringTmpl(elems=[Regular(str=com.aa.bb)], raw=false)), ValueArg(name=verifiedContainer, asterisk=false, expr=CollLit(exprs=[StringTmpl(elems=[Regular(str=list)], raw=false), StringTmpl(elems=[Regular(str=homeContainer)], raw=false)]))])])] , form=CLASS, name=AppComponent, typeParams=[], primaryConstructor=null, parentAnns=[], parents=[], typeConstraints=[], members=[])
//Structured(mods=[AnnotationSet(target=null, anns=[Annotation(names=[com.codelang.annotation.Component], typeArgs=[], args=[ValueArg(name=name, asterisk=false, expr=StringTmpl(elems=[Regular(str=list)], raw=false)), ValueArg(name=version, asterisk=false, expr=StringTmpl(elems=[Regular(str=2.0.0)], raw=false)), ValueArg(name=dependency, asterisk=false, expr=StringTmpl(elems=[Regular(str=com.aa.bb)], raw=false))])])], form=CLASS, name=App2Component, typeParams=[], primaryConstructor=null, parentAnns=[], parents=[], typeConstraints=[], members=[])