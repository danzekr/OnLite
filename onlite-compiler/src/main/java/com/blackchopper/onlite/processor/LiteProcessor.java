package com.blackchopper.onlite.processor;

import com.blackchopper.onlite.LiteFile;
import com.blackchopper.onlite.annotation.Table;
import com.blackchopper.onlite.util.Logger;
import com.google.auto.service.AutoService;

import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : OnLite
 */
@AutoService(Processor.class)
public class LiteProcessor extends AbstractProcessor {
    protected Messager messager;
    protected Elements elementUtils;
    protected Map<String, LiteFile> mProxyMap = new LinkedHashMap<>();

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> supportType = new LinkedHashSet<>();
        supportType.add(Table.class.getCanonicalName());
        return supportType;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "process...");
        mProxyMap.clear();
        processLite(annotations, roundEnv);
        process();
        return true;
    }

    private void processLite(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> tables = roundEnv.getElementsAnnotatedWith(Table.class);
        for (Element element : tables) {
            Logger.v(element.asType().toString());
            String fullClassName = element.asType().toString();
            Table table = element.getAnnotation(Table.class);

            if (mProxyMap.get(fullClassName) == null) {
                mProxyMap.put(fullClassName, new LiteFile(elementUtils, (TypeElement) element,table.value()));
            }
        }
    }

    private void process() {
        for (String key : mProxyMap.keySet()) {
            LiteFile liteFile = mProxyMap.get(key);
            try {
                JavaFileObject jfo = processingEnv.getFiler().createSourceFile(
                        liteFile.getProxyClassFullName(),
                        liteFile.getTypeElement()
                );
                Writer writer = jfo.openWriter();
                writer.write(liteFile.generateJavaCode());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
