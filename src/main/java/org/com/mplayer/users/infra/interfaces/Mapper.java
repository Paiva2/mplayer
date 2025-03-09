package org.com.mplayer.users.infra.interfaces;

public interface Mapper<D, P> {
    D toDomain(P persistenceEntity);

    P toPersistence(D domainEntity);

    void map(Object source, Object target);
}
