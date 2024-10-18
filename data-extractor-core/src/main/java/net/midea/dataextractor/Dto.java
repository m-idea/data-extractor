package net.midea.dataextractor;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Dto {
    /**
     * discriminator value
     *
     * @return discriminatorValue
     */
    @AliasFor("discriminatorValue")
    String value() default "";

    @AliasFor("value")
    String discriminatorValue() default "";
}
