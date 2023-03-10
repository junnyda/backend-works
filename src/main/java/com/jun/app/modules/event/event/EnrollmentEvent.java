package com.jun.app.modules.event.event;

import com.jun.app.modules.event.domain.entity.Enrollment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EnrollmentEvent {
    protected final Enrollment enrollment;
    protected final String message;
}

