package com.equabli.collectprism.dao;

import com.equabli.collectprism.entity.Adjustment;
import com.equabli.domain.Response;

import java.util.Map;

public interface AdjustmentDao {
    Response<Map<String,Object>> insertBalanceAdjustment(Adjustment adjustment);
}
