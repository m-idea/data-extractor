package net.midea.dataextractor.mvn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.palantir.javapoet.*;
import lombok.Data;
import net.midea.dataextractor.*;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import javax.lang.model.element.Modifier;
import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.Instant;

@Mojo(name = "generate-dto", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateDTOMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;
    @Parameter(required = true)
    private File descriptorFile;
    @Parameter(defaultValue = "${project.groupId}", required = true)
    private String targetPackage;
    @Parameter(defaultValue = "DTO", required = true)
    private String prefix;
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/dto/", required = true)
    private String targetDir;


    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (descriptorFile == null) {
            throw new MojoFailureException("descriptorFile cannot be null");
        }

        try (FileInputStream fis = new FileInputStream(descriptorFile);) {
            DtoDescriptor descriptor = objectMapper.readValue(fis, DtoDescriptor.class);
            for (DtoDescriptorRecord header : descriptor.getHeaders()) {
                generateSource(header);
            }
            for (DtoDescriptorRecord content : descriptor.getContent()) {
                generateSource(content);
            }
            for (DtoDescriptorRecord footer : descriptor.getTrailers()) {
                generateSource(footer);
            }
            project.addCompileSourceRoot(targetDir);
        } catch (IOException e) {
            getLog().error("Failed to read descriptor file", e);
            throw new MojoExecutionException("Failed to read descriptor file", e);
        }
    }

    private void generateSource(DtoDescriptorRecord record) throws IOException {

        getLog().info("generating dto for " + record.getName());
        File of = new File(targetDir);
        TypeSpec.Builder dtoType = TypeSpec.classBuilder(prefix + record.getName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Data.class)
                .addAnnotation(AnnotationSpec.builder(Dto.class)
                        .addMember("value", "$S", record.getName()).build());

        for (DtoDescriptorField field : record.getFields()) {
            FieldSpec fieldSpec = FieldSpec.builder(mapType(field.getType()), field.getName(), Modifier.PRIVATE)
                    .addModifiers(Modifier.PRIVATE)
                    .build();
            dtoType.addField(fieldSpec);
        }
        JavaFile javaFile = JavaFile.builder(targetPackage, dtoType.build())
                .build();

        javaFile.writeTo(of);
    }

    private Type mapType(DtoFieldType type) {
        return switch (type) {
            case DATE -> Instant.class;
            case BINARY -> byte[].class;
            case NUMBER -> BigDecimal.class;
            default -> String.class;
        };
    }
}
