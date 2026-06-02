package com.grymprojects.openbeta.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.grymprojects.openbeta.model.Consumer;
import com.grymprojects.openbeta.model.ConsumerBusinessMembership;
import com.grymprojects.openbeta.model.BusinessOnboarding;

@Repository
public interface ConsumerBusinessMembershipRepository extends JpaRepository<ConsumerBusinessMembership, Long> {

    boolean existsByConsumerAndBusiness(Consumer consumer, BusinessOnboarding business);
}
