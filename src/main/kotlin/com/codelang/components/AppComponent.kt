package com.codelang.components

import com.codelang.annotation.Component

/**
 * 测试单文件里面有多个注解
 */

@Component(
    name = "zhangsan",
    version = "1.0.0",
    dependency = "com.aa.bb",
    verifiedContainer = ["list", "homeContainer"],
    verifiedProtocol = ["public"]
)
class AComponent

@Component(
    name = "list",
    version = "1.0.1",
    dependency = "com.aa.bb",
    verifiedProtocol = ["private"]
)
class BComponent





