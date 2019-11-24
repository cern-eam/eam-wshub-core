package ch.cern.eam.wshub.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InforField {
    String xpath();
    boolean enforceValidXpath() default true;
    boolean readOnly() default false;
    BooleanType booleanType() default BooleanType.TRUE_FALSE;
}