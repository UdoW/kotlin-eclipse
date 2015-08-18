/*******************************************************************************
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
package org.jetbrains.kotlin.ui.editors.completion

import org.eclipse.core.resources.IFile
import org.eclipse.jdt.core.search.SearchPattern
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor
import org.jetbrains.kotlin.core.builder.KotlinPsiManager
import org.jetbrains.kotlin.core.log.KotlinLogger
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.eclipse.ui.utils.EditorUtil
import org.jetbrains.kotlin.eclipse.ui.utils.LineEndUtil
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.JetFile
import org.jetbrains.kotlin.psi.JetSimpleNameExpression
import com.intellij.openapi.util.text.StringUtilRt
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

public object KotlinCompletionUtils {
    private val KOTLIN_DUMMY_IDENTIFIER = "KotlinRulezzz"
    
    public fun filterCompletionProposals(descriptors: List<DeclarationDescriptor>, prefix: String): Collection<DeclarationDescriptor> {
        return descriptors.filter { applicableNameFor(prefix, it.getName()) }
    }
    
    public fun applicableNameFor(prefix: String, name: Name): Boolean {
        return if (!name.isSpecial()) {
                val identifier = name.getIdentifier()
                identifier.startsWith(prefix) || 
                    identifier.toLowerCase().startsWith(prefix) || 
                    SearchPattern.camelCaseMatch(prefix, identifier)
            } else {
                false
            }
    }
    
    public fun getSimpleNameExpression(editor: JavaEditor, identOffset: Int): JetSimpleNameExpression? {
        val sourceCode = EditorUtil.getSourceCode(editor)
        val sourceCodeWithMarker = StringBuilder(sourceCode).insert(identOffset, KOTLIN_DUMMY_IDENTIFIER).toString()
        val jetFile: JetFile?
        val file = EditorUtil.getFile(editor)
        if (file != null) {
            jetFile = KotlinPsiManager.INSTANCE.parseText(StringUtilRt.convertLineSeparators(sourceCodeWithMarker), file)
        } else {
            KotlinLogger.logError("Failed to retrieve IFile from editor " + editor, null)
            return null
        }
        
        if (jetFile == null) return null
        
        val offsetWithourCR = LineEndUtil.convertCrToDocumentOffset(sourceCodeWithMarker, identOffset, EditorUtil.getDocument(editor))
        val psiElement = jetFile.findElementAt(offsetWithourCR)
        return PsiTreeUtil.getParentOfType(psiElement, javaClass<JetSimpleNameExpression>())
    }
    
    public fun replaceMarkerInIdentifier(identifier: String): String {
        return identifier.replaceFirst(KOTLIN_DUMMY_IDENTIFIER, "")
    }
}