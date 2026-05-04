package com.equabli.collectprism.dao;

import com.equabli.collectprism.entity.Account;
import com.equabli.domain.Response;

import java.util.Map;

public interface AccountDao {
    Response<Map<String, Object>> updateAccountDetails(Account account);
}
