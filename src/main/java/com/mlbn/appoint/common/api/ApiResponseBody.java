package com.mlbn.appoint.common.api;

import java.util.Set;

public record ApiResponseBody(Object result, Set<String> errors) {
}
