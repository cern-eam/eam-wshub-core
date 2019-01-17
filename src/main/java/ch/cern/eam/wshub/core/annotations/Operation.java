package ch.cern.eam.wshub.core.annotations;

import ch.cern.eam.wshub.core.interceptors.LogDataReferenceType;
import ch.cern.eam.wshub.core.services.INFOR_OPERATION;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(java.lang.annotation.ElementType.METHOD)
public @interface Operation {
    INFOR_OPERATION logOperation();
    LogDataReferenceType logDataReference1() default LogDataReferenceType.NONE;
    String logDataReference1FieldName() default "";
    LogDataReferenceType logDataReference2() default LogDataReferenceType.NONE;
    String logDataReference2FieldName() default "";
}