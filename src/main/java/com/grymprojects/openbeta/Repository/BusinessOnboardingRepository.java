package com.grymprojects.openbeta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grymprojects.openbeta.model.BusinessOnboarding;

@Repository
public interface BusinessOnboardingRepository extends JpaRepository<BusinessOnboarding, Long> {

    boolean existsByBusinessEmail(String businessEmail);

    boolean existsByGstNumber(String gstNumber);
}
