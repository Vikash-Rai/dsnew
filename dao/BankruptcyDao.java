package com.equabli.collectprism.dao;

import java.util.Map;

import com.equabli.collectprism.entity.Bankruptcy;
import com.equabli.domain.Response;
import com.equabli.domain.SearchCriteria;

public interface BankruptcyDao {

    Response<Map<String, Object>> insertOrUpdateBankruptcyDetails(Bankruptcy bankruptcy);

    Response<Map<String, Object>> getBankruptcyDetailById(Long bankruptcyId);

    Response<Map<String, Object>> getBankruptcyDetails(SearchCriteria<Bankruptcy> bankruptcySearch);
}