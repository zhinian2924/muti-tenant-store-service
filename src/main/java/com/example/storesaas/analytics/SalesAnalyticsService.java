package com.example.storesaas.analytics;

import com.example.storesaas.analytics.vo.SalesOverviewVO;

import java.time.LocalDate;

public interface SalesAnalyticsService {
    SalesOverviewVO overview(SalesPeriod period, LocalDate date);
}
