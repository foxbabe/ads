package com.sztouyun.advertisingsystem.web.security;

import org.springframework.security.core.GrantedAuthority;

public class UrlGrantedAuthority implements GrantedAuthority {
    private final String url;

    public UrlGrantedAuthority(String url) {
        this.url = url;
    }

    @Override
    public String getAuthority() {
        return url;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        UrlGrantedAuthority target = (UrlGrantedAuthority) o;
        return url.equals(target.getUrl());
    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }
}
