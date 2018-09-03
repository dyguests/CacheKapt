package com.fanhl.cachekapt.processor

import com.fanhl.cachekapt.annotation.Cache
import com.google.auto.service.AutoService
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

/**
 * 注解实现
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions(CacheProcessor.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class CacheProcessor : AbstractProcessor() {
    override fun process(set: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

        createGeneratedClass(roundEnv)

        return true
    }

    override fun getSupportedAnnotationTypes() = mutableSetOf(Cache::class.java.canonicalName)


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
