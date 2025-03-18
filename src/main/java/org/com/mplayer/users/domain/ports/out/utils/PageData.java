package org.com.mplayer.users.domain.ports.out.utils;

import java.util.List;


public record PageData<T>(
    int page,
    int size,
    int totalPages,
    long totalItems,
    boolean isLast,
    List<T> list
) {
}
