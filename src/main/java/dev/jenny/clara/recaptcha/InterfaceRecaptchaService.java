package dev.jenny.clara.recaptcha;

public interface InterfaceRecaptchaService {

    boolean verify(String token);

    void verifyOrThrow(String token);

}
