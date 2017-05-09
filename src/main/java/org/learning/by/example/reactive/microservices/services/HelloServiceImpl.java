package org.learning.by.example.reactive.microservices.services;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.learning.by.example.reactive.microservices.exceptions.InvalidParametersException;
import reactor.core.publisher.Mono;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.function.Function;

public class HelloServiceImpl implements HelloService{
    private static final String EMPTY = "";
    private static final String BAD_PARAMETERS = "bad parameters";

    @Override
    public Function<Mono<String>, Mono<String>> getGreetings() {
        return value -> value.flatMap(name -> {
            if (name.equals(EMPTY)) {
                return Mono.error(new InvalidParametersException(BAD_PARAMETERS));
            }
            return Mono.just(name);
        });
    }

    @Override
    public Function<Mono<String>, Mono<String>> decode() {
        return stringMono -> stringMono.flatMap(s -> Mono.just(new String(Base64.decode(s))));
                //.onErrorMap(Mono.error(new InvalidParametersException("No me gusta tu rollito")));
    }

    @Override
    public Function<Mono<String>, Mono<String>> compile() {

        return value -> value.flatMap(name -> {
            if (name.equals(EMPTY)) {
                return Mono.error(new InvalidParametersException(BAD_PARAMETERS));
            }
            System.out.println(name);

            try {
// Save source in .java file.

                final String JAVA_IO_TEMPDIR = System.getProperty("java.io.tmpdir");
                System.out.println(JAVA_IO_TEMPDIR);
                File root = new File(JAVA_IO_TEMPDIR + "/java"); // On Windows running on C:\, this is C:\java.
                File sourceFile = new File(root, "HelloWorld.java");
                sourceFile.getParentFile().mkdirs();
                Files.write(sourceFile.toPath(), name.getBytes(StandardCharsets.UTF_8));

// Compile source file.
                JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
                compiler.run(null, null, null, sourceFile.getPath());

// Load and instantiate compiled class.
                URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{root.toURI().toURL()});
                Class<?> cls = Class.forName("HelloWorld", true, classLoader); // Should print "hello".
                Object instance = cls.newInstance(); // Should print "world".
                System.out.println(instance); // Should print "test.Test@hashcode".
                final String result = (String) cls.getDeclaredMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { null });
                System.out.println(result);
                //Class.forName("HelloWorld").getDeclaredMethod("main", new Class[] { String[].class })
                //        .invoke(null, new Object[] { null });
            }catch(IOException e){
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            /*
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

            StringWriter writer = new StringWriter();
            PrintWriter out = new PrintWriter(writer);
            out.println(name);

            //out.println("public class HelloWorld {");
            //out.println("  public static void main(String args[]) {");
            //out.println("    System.out.println(\"This is in another java file\");");
            //out.println("  }");
            //out.println("}");

            out.close();
            JavaFileObject file = new JavaSourceFromString("HelloWorld", writer.toString());

            Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
            JavaCompiler.CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

            boolean success = task.call();
            for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
                System.out.println(diagnostic.getCode());
                System.out.println(diagnostic.getKind());
                System.out.println(diagnostic.getPosition());
                System.out.println(diagnostic.getStartPosition());
                System.out.println(diagnostic.getEndPosition());
                System.out.println(diagnostic.getSource());
                System.out.println(diagnostic.getMessage(null));

            }
            System.out.println("Success: " + success);

            if (success) {
                try {
                    Class.forName("HelloWorld").getDeclaredMethod("main", new Class[] { String[].class })
                            .invoke(null, new Object[] { null });
                } catch (ClassNotFoundException e) {
                    System.err.println("Class not found: " + e);
                } catch (NoSuchMethodException e) {
                    System.err.println("No such method: " + e);
                } catch (IllegalAccessException e) {
                    System.err.println("Illegal access: " + e);
                } catch (InvocationTargetException e) {
                    System.err.println("Invocation target: " + e);
                }
            }
            */

            return Mono.just(name);
        });
    }

    class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }

    @Override
    public Function<Mono<String>, Mono<String>> run() {
        return value -> value.flatMap(name -> {

            //Execute bytecode

            return Mono.just(name);
        });
    }
}
