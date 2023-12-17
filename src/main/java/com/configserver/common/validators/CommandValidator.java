package com.configserver.common.validators;

import an.awesome.pipelinr.Command;
import com.configserver.common.exceptions.ApplicationRuntimeException;
import com.configserver.common.exceptions.BadRequestException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

@Component
public class CommandValidator implements Command.Middleware{

    private final ObjectProvider<ICommandValidator> validators;

    CommandValidator(ObjectProvider<ICommandValidator> validators) {
        this.validators = validators;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C command, Next<R> next) {
        validators
                .stream()
                .filter(v -> v.matches(command))
                .findFirst()
                .ifPresent(v -> {
                    var result = v.validate(command);
                    if (!result.isNone()) {
                        try {
                            throw new BadRequestException(result);
                        } catch (BadRequestException e) {
                            throw new ApplicationRuntimeException(e);
                        }
                    }
                });
        return next.invoke();
    }
}
