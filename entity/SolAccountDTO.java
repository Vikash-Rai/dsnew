package com.equabli.collectprism.entity;

/**
 * Lean projection DTO for the SOL pre-fetch query (fetchSolAccounts).
 *
 * Contains ONLY the 5 values consumed by SolEnrichmentServiceImpl.enrich().
 * All other fields that existed in the original query have been removed:
 *
 *   REMOVED  clientName      — duplicate of ctx.clientShortName (Caffeine)
 *   REMOVED  partnerName     — never read by any handler
 *   REMOVED  debtCategoryId  — duplicate of ctx.productDebtCategoryId (Caffeine)
 *   REMOVED  cycleDay        — duplicate of ctx.cycleDay (Caffeine)
 *   REMOVED  solId           — fetched, never read by any handler
 *   REMOVED  client_id       — already on AccountEnrichment
 *   REMOVED  product_id      — already on AccountEnrichment
 *   REMOVED  partner_id      — already on AccountEnrichment
 *
 * NULL CONTRACT:
 *   getStateCode()      — null when account has no primary consumer, no address,
 *                         or address.state_code is null.
 *   getCountryStateId() — null when stateCode is null or not in country_state table.
 *   getSolMonth()       — null when no address exists (distinguishes "no data" from
 *                         a legitimate zero-month config which should never exist).
 *   getSolDay()         — null when not a primary address with a known state.
 *
 * Column aliases in fetchSolAccounts SQL must match these getter names exactly
 * (Spring Data projection is case-insensitive on the alias).
 */
public interface SolAccountDTO {

    /** data.account.account_id - always non-null; used as Map key in fetchSolDtoMap(). */
    Long getAccountId();

    /**
     * data.address.state_code of the primary consumer's best address.
     * Null when: no consumer, no address, or address has no state_code.
     */
    String getStateCode();

    /**
     * conf.country_state.country_state_id matched on state_code.
     * Null when stateCode is null or not found in the country_state table.
     */
    Long getCountryStateId();

    /**
     * SOL months for this account's debt category and state.
     *
     * Driven by CASE in SQL:
     *   addr row absent entirely         -> NULL
     *   Primary addr + known state_code  -> conf.statutes_of_limitation.sol_month
     *   Primary addr + null state_code   -> NULL
     *   Non-primary addr                 -> MIN(sol_month) across debt category
     */
    Integer getSolMonth();

    /**
     * Client-configured SOL day override from conf.client_statutes_of_limitation.
     *
     * Driven by CASE in SQL:
     *   Primary addr + known state_code  → csol.sol_day
     *   All other cases                  → NULL
     */
    Integer getSolDay();
}