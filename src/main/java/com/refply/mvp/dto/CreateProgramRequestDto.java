package com.refply.mvp.dto;

import org.springframework.stereotype.Component;

import com.refply.mvp.entity.ReferelProgramEntity;

@Component
public class CreateProgramRequestDto {
    private Long businessId;
    private ReferelProgramEntity program;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public ReferelProgramEntity getProgram() {
        return program;
    }

    public void setProgram(ReferelProgramEntity program) {
        this.program = program;
    }
}
