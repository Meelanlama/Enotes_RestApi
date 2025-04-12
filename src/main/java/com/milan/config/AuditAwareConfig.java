package com.milan.config;

import com.milan.model.User;
import com.milan.util.CommonUtil;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareConfig implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {

        User loggedInUser = CommonUtil.getLoggedInUser();

        //get user id automatically
        return Optional.of(loggedInUser.getId());
    }
}
