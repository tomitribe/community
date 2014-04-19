package org.superbiz.jpa.entitymanager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

@Target(value = {ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier // <1>  A qualifier must be annotated with +Qualifier+ annotation.
public @interface SeriesEntityManager {

}
