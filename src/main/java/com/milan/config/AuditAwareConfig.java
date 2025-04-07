package com.milan.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.ofNullable(1);
    }
}
