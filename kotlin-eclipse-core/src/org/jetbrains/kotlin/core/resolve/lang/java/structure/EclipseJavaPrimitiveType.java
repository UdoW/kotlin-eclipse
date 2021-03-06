/*******************************************************************************
 * Copyright 2000-2014 JetBrains s.r.o.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *******************************************************************************/
package org.jetbrains.kotlin.core.resolve.lang.java.structure;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.builtins.PrimitiveType;
import org.jetbrains.kotlin.load.java.structure.JavaPrimitiveType;
import org.jetbrains.kotlin.resolve.jvm.JvmPrimitiveType;

public class EclipseJavaPrimitiveType extends EclipseJavaType<ITypeBinding> implements JavaPrimitiveType {

    public EclipseJavaPrimitiveType(ITypeBinding typeBinding) {
        super(typeBinding);
    }

    @Override
    @Nullable
    public PrimitiveType getType() {
        String text = getBinding().getName();
        return "void".equals(text) ? null : JvmPrimitiveType.get(text).getPrimitiveType();
    }
}
