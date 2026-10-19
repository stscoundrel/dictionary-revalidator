package io.github.stscoundrel.revalidator.revalidators

fun validateRevalidationParameters(
    start: Int? = null,
    end: Int? = null,
    batchSize: Int? = null,
    retriesCount: Int? = null
) {
    require(start == null || start >= 0) { "start must be nonnegative" }
    require(end == null || end > (start ?: 0)) { "end must be greater than start" }
    require(batchSize == null || batchSize > 0) { "batch must be positive" }
    require(retriesCount == null || retriesCount >= 0) { "retries must be nonnegative" }
}
