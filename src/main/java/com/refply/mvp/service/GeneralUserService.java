package com.refply.mvp.service;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.refply.mvp.Repository.ReferelProgramRepo;
import com.refply.mvp.Repository.UserRepo;
import com.refply.mvp.entity.BusinessProfileEntity;
import com.refply.mvp.entity.ReferelProgramEntity;
import com.refply.mvp.entity.UserEntity;

import io.jsonwebtoken.lang.Collections;

@Service
public class GeneralUserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ReferelProgramRepo referelProgramRepo;

    public BusinessProfileEntity setProfileComplete(UserEntity user, BusinessProfileEntity profile) {

        UserEntity existingUser = userRepo.findByEmail(user.getEmail()).get();
        profile.setUser(existingUser);

        if (profile.getIsWalletEnebled() == null)
            profile.setIsWalletEnebled(false);
        if (profile.getWalletBalance() == null)
            profile.setWalletBalance(0.0);
        if (profile.getNoOfProgram() == null)
            profile.setNoOfProgram(0);
        if (profile.getNoOfRunningProram() == null)
            profile.setNoOfRunningProram(0);

        if (existingUser.getBusinesses() == null) {
            existingUser.setBusinesses(new java.util.ArrayList<>());
        }
        existingUser.getBusinesses().add(profile);
        existingUser.setIsProfileComplete(true);

        userRepo.save(existingUser);
        return profile;
    }

    public ReferelProgramEntity createNewProgram(ReferelProgramEntity data, UserEntity user, Long businessId) {
        UserEntity existingUser = userRepo.findByEmail(user.getEmail()).get();
        BusinessProfileEntity profile = getBusinessProfileByID(businessId, existingUser.getBusinesses());

        if (profile == null) {
            return null;
        }
        
        profile.setNoOfProgram((profile.getNoOfProgram() != null ? profile.getNoOfProgram() : 0) + 1);
        data.setUrl(generateUniqueReferCode());
        data.setBusinessProfile(profile);
        List<ReferelProgramEntity> programs = profile.getReferelPrograms() == null
                ? new ArrayList<>()
                : new ArrayList<>(profile.getReferelPrograms());
        programs.add(data);
        profile.setReferelPrograms(programs);
        userRepo.save(existingUser);
        return data;
    }

    public BusinessProfileEntity getBusinessProfileByID(Long id, List<BusinessProfileEntity> profile) {

        if (profile == null) {
            return null;
        }

        for (BusinessProfileEntity p : profile) {
            if (p.getId() != null && p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public List<BusinessProfileEntity> getBusinessProfiles(UserEntity user) {
        UserEntity existingUser = userRepo.findByEmail(user.getEmail()).get();
        if (existingUser.getBusinesses() == null) {
            return Collections.emptyList();
        }
        return existingUser.getBusinesses();
    }

    private String generateUniqueReferCode() {
        String referCode;
        do {
            referCode = UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        } while (referelProgramRepo.existsByUrl(referCode));

        return referCode;
    }

    public ReferelProgramEntity loadReferProgramByCode(String uniqCode, UserEntity user) {
        UserEntity existingUser = userRepo.findByEmail(user.getEmail()).get();

        ReferelProgramEntity program = referelProgramRepo.findByUrl(uniqCode).orElse(null);

        if (program == null) {
            return null;
        }

        BusinessProfileEntity programBusiness = program.getBusinessProfile();
        if (programBusiness == null
                || getBusinessProfileByID(programBusiness.getId(), existingUser.getBusinesses()) == null) {
            return null;
        }

        return program;
    }

    public ReferelProgramEntity getPublicProgramByCode(String uniqCode) {
        return referelProgramRepo.findByUrl(uniqCode).orElse(null);
    }

}
