package io.github.fintechproject.mscartoes.domain;

public enum BandeiraCartao {
    MASTERCARD, VISA;

    public static BandeiraCartao fromString(String value) {
        return BandeiraCartao.valueOf(value.toUpperCase());
    }
}
