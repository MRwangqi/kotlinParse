package com.codelang.components;

import com.codelang.annotation.Component;

/**
 * 测试 java 文件是否可被解析
 */
@Component(
        verifiedContainer = {"xiaoming", "huawei"},
        verifiedProtocol = {"private","public"},
        name = "java",
        version = "2.0.0",
        dependency = "com.aa.bb"
)
public class DComponent {
}
