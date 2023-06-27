package TestPackage;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
@Documented
public @interface NotRunnerTime {
    String value();
}
