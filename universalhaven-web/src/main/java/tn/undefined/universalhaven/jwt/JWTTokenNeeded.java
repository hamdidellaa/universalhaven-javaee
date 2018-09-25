package tn.undefined.universalhaven.jwt;

import javax.ws.rs.NameBinding;

import tn.undefined.universalhaven.enumerations.UserRole;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface JWTTokenNeeded {
	UserRole role() default UserRole.CAMP_STAFF;
}
