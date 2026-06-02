package com.grymprojects.openbeta.util;

import java.net.URI;
import java.util.Locale;

public final class DomainNameUtils {

    private DomainNameUtils() {
    }

    public static String normalize(String webAddress) {
        if (webAddress == null || webAddress.isBlank()) {
            return null;
        }

        String value = webAddress.trim().toLowerCase(Locale.ROOT);
        String uriValue = value.contains("://") ? value : "https://" + value;

        try {
            String host = URI.create(uriValue).getHost();

            if (host != null && !host.isBlank()) {
                return removeWwwPrefix(host);
            }
        } catch (IllegalArgumentException exception) {
            // Fall through to simple cleanup for values that are not valid URIs.
        }

        String cleaned = value
                .replaceFirst("^https?://", "")
                .split("/")[0]
                .split(":")[0];

        return removeWwwPrefix(cleaned);
    }

    private static String removeWwwPrefix(String domainName) {
        return domainName.startsWith("www.") ? domainName.substring(4) : domainName;
    }
}
