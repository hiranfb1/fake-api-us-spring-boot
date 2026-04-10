package com.javanauta.fake_api_us.infrastructure.configs.error;

import com.javanauta.fake_api_us.business.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class NotificacaoErroAspect {
    private final EmailService emailService;

    @Pointcut("@within(com.javanautas.fake_api_us.infrastructure.configs.error.NotificacaoErro) || @annotation(com.javanautas.fake_api_us.infrastructure.configs.error.NotificacaoErro)")
    public void notificacaoDeErroPointcut() {
    }

    @AfterThrowing(pointcut = "notificacaoDeErroPointcut()", throwing = "ex")
    public void notificacaoDeErro(final Exception ex) {
        emailService.enviarEmailExcecao(ex);
    }
}