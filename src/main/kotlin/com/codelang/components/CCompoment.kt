package com.codelang.components

import com.codelang.annotation.Compo
import com.codelang.annotation.Component

@Component(
    name = "lisi",
    version = "2.0.0",
    dependency = "com.aa.bb",
)
class CComponent


/**
 * 故意写一个与 Component 不同的注解，来测试排除解析
 */
@Compo(
    name = "list",
    version = "3.0.0",
    dependency = "com.aa.bb",
)
class CCompo
