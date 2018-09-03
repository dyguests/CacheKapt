package com.fanhl.cachekapt.processor

import com.fanhl.cachekapt.annotation.Cache
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.asTypeName
import java.io.File
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

/**
 * 注解实现
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(CacheProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class CacheProcessor : AbstractProcessor() {
    private val elementUtils by lazy { processingEnv.elementUtils }
    private val typeUtils by lazy { processingEnv.typeUtils }

    /**
     * 源码生成位置.
     */
    private val sourceLocation by lazy {
        val infoFile = processingEnv.filer.createSourceFile("package-info", null)
        val out = infoFile.openWriter()
        out.close()
        File(infoFile.name).parentFile
    }

    override fun process(set: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {
        //获取所有有@Cache的元素
        val matchedElements = roundEnv?.getElementsAnnotatedWith(Cache::class.java)
            ?.filter { it.kind == ElementKind.FIELD }
            ?.takeIf { it.isNotEmpty() } ?: return true

        // map: clazz -> list(element)
        val elementMap = HashMap<Element, MutableList<Element>?>()
        matchedElements.forEach { elemenet ->
            elementMap[elemenet.enclosingElement]?.add(elemenet) ?: also { elementMap[elemenet.enclosingElement] = mutableListOf(elemenet) }
        }

        elementMap.forEach(::generateClass)

//        createGeneratedClass(roundEnv)

        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(Cache::class.java.canonicalName)

    /**
     * 生成对应class的所有扩展
     */
    private fun generateClass(clazz: Element, fields: MutableList<Element>?) {
        val className = clazz.simpleName
        FileSpec.builder(elementUtils.getPackageOf(clazz).asType().toString(), "$className\$CacheExt")
            .apply {
                fields?.forEach { field -> generateProperty(this@apply, field) }
            }
            .build()
            .writeTo(sourceLocation)
    }

    /**
     * 生成class中对应property的扩展
     */
    private fun generateProperty(builder: FileSpec.Builder, field: Element) {
        val clazz = field.enclosingElement
        val className = clazz.simpleName
        val fieldName = field.simpleName
        builder.addProperty(
            PropertySpec.varBuilder("${fieldName}Cache", String::class)
                .addKdoc("Cache extension for $className.$fieldName\n")
                .receiver(clazz.asType().asTypeName())
                .getter(
                    FunSpec.getterBuilder()
                        .addStatement("val a=1")
                        .addComment("${clazz}")
                        .addComment("${clazz.asType().asTypeName()}")
                        .addStatement("return \"1\"")
                        .build()
                )
                .setter(
                    FunSpec.setterBuilder()
                        .addParameter(
                            //                                            ParameterSpec.get(field as VariableElement)
                            "value", String::class
                        )
                        .addStatement("val a=1")
                        .build()
                )
                .build()
        )
    }

    /**
     * 这只是用来测试的方法
     */
    @Deprecated("这个只是用来测试的")
    private fun createGeneratedClass(roundEnv: RoundEnvironment?) {
        //last
        val builder = StringBuilder()
            .append("package com.fanhl.androidkapt.processor.generated;\n\n")
            .append("public class GeneratedClass {\n\n") // open class
            .append("\tpublic String getMessage() {\n") // open method
            .append("\t\treturn \"")


        // for each javax.lang.model.element.Element annotated with the CustomAnnotation
        roundEnv?.getElementsAnnotatedWith(Cache::class.java)?.forEach { element ->
            val objectType = element.simpleName.toString()


            // this is appending to the return statement
            builder.append(objectType).append(" says hello!\\n")
        }


        builder.append("\"; \n ") // end return
            .append("\t}\n") // close method
            .append("}\n") // close class


        try { // write the file
            val source = processingEnv.filer.createSourceFile("com.fanhl.androidkapt.processor.generated.GeneratedClass")


            val writer = source.openWriter()
            writer.write(builder.toString())
            writer.flush()
            writer.close()
        } catch (e: IOException) {
            // Note: calling e.printStackTrace() will print IO errors
            // that occur from the file already existing after its first run, this is normal
        }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
