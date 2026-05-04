package com.equabli.collectprism.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.equabli.collectprism.entity.ChargeOffReason;

@Repository
public interface ChargeOffReasonRepository extends JpaRepository<ChargeOffReason, Integer> {

	/**
     * Finds charge off reason by using the scrub type as a search criteria.
     * @param scrubType
     * @return Charge off reason whose scrub type is an exact match with the given scrub type.
     *          If no charge off reason is found, this method returns an empty list.
     */
	@Query("SELECT cor FROM ChargeOffReason cor WHERE cor.scrubType in (:scrubTypes)")
	public List<ChargeOffReason> chargeOffOtherAndMissing(List<Integer> scrubTypes);
}