package me.ultradev.ultrarpg.api.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {
    String label();
    boolean opOnly();
    int cooldown() default 0;
    String[] aliases() default {};
    String[] arguments() default {};
}
